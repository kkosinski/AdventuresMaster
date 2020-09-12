package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;
import com.wintermute.adventuresmaster.services.network.RestGun;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages user input for {@link com.wintermute.adventuresmaster.view.settings.PhilipsHueBulbsSettings}.
 *
 * @author wintermute
 */
public class HueBulbViewModel extends ViewModel
{
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
    public List<HueBulb> getBulbs(JSONObject response)
    {
        List<HueBulb> result = new ArrayList<>();

        JSONArray names = response.names();

        for (int i = 0; i < names.length(); i++)
        {
            try
            {
                HueBulb bulb = new HueBulb();
                String currentItemName = (String) names.get(i);
                bulb.setId(Long.parseLong(currentItemName));
                JSONObject bulbInfo = (JSONObject) response.get(currentItemName);
                bulb.setName(bulbInfo.getString("name"));
                bulb.setType(bulbInfo.getString("type"));
                bulb.setSelected(false);
                result.add(bulb);
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
     * @param bulbsToPair requested list of bulbs to persist.
     */
    public void storeBulbs(Context context, List<HueBulb> bulbsToPair)
    {
        new InsertTask(AppDatabase.getAppDatabase(context)).execute(bulbsToPair);
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
     * Manages insert tasks of {@link HueBulb} into database.
     */
    private static class InsertTask extends AsyncTask<List<HueBulb>, Void, Void>
    {

        private AppDatabase db;

        public InsertTask(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected Void doInBackground(List<HueBulb>... bulbs)
        {
            bulbs[0].stream().forEach(b -> db.hueBulbDao().insert(b));
            return null;
        }
    }
}
