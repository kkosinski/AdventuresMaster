package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;
import com.wintermute.adventuresmaster.services.network.RestGun;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages user input for {@link com.wintermute.adventuresmaster.view.settings.PhilipsHueBulbsSettings}.
 *
 * @author wintermute
 */
public class HueBulbViewModel extends ViewModel
{
    private List<HueBulb> changeSet;

    /**
     * Sends request to discover bulbs available to paired philips hue bridge.
     *
     * @param context of calling activity.
     * @param bridge paired to users device.
     */
    public void requestAvailableBulbs(Context context, HueBridge bridge)
    {
        RestGun restGun = new RestGun(context);
        restGun.requestBulbs(bridge.getUrl() + "/" + bridge.getUser() + "/lights");
    }

    /**
     * Extracts bulbs information from response of philips hue bridge.
     *
     * @param response of requested available philips hue bulbs.
     * @return list of {@link HueBulb} available {@link HueBridge}
     */
    public List<HueBulb> getBulbs(JSONObject response, long bridgeId)
    {
        List<HueBulb> result = new ArrayList<>();
        Iterator<String> keys = response.keys();
        while (keys.hasNext())
        {
            String bulb = keys.next();
            try
            {
                JSONObject bulbDesc = (JSONObject) response.get(bulb);
                result.add(
                    new HueBulb(Long.parseLong(bulb), bridgeId, bulbDesc.getString("name"), bulbDesc.getString("type")));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Persists {@link HueBulb} in database.
     *
     * @param context of calling activity.
     */
    public void updatePairedBulbs(Context context)
    {
        new UpdateBulbsTask(AppDatabase.getAppDatabase(context)).execute(changeSet);
    }

    /**
     * @param context of calling activity.
     * @param bridgeId to get bulbs ordered to this bridge.
     * @return list of paired {@link HueBulb} with users device over this app.
     */
    public LiveData<List<HueBulb>> getPairedBulbs(Context context, long bridgeId)
    {
        return AppDatabase.getAppDatabase(context).hueBulbDao().getPairedBulbsDynamically(bridgeId);
    }

    /**
     * Decides whether the bulb should be stored in database or removed.
     *
     * @param hueBulb selected bulb.
     */
    public void classifyBulb(HueBulb hueBulb)
    {
        changeSet = changeSet == null ? new ArrayList<>() : changeSet;
        changeSet.add(hueBulb);
    }

    /**
     * Manages insert tasks of {@link HueBulb} into database.
     */
    private static class UpdateBulbsTask extends AsyncTask<List<HueBulb>, Void, Void>
    {

        private AppDatabase db;

        public UpdateBulbsTask(AppDatabase db)
        {
            this.db = db;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<HueBulb>... bulbs)
        {
            bulbs[0].forEach(b ->
            {
                if (b.isSelected())
                {
                    db.hueBulbDao().insert(b);
                } else
                {
                    db.hueBulbDao().delete(b);
                }
            });
            return null;
        }
    }
}
