package com.wintermute.adventuresmaster.services.network;

import android.content.Context;
import android.os.AsyncTask;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Service providing philips hue access data and its paired objects.
 *
 * @author wintermute
 */
public class PhilisHueConnector
{
    private static PhilisHueConnector INSTANCE;

    private PhilisHueConnector()
    {
    }

    /**
     * @return instance.
     */
    public static synchronized PhilisHueConnector getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new PhilisHueConnector();
        }
        return INSTANCE;
    }

    /**
     * @param context of calling activity
     * @return true if default device is present, otherwise false.
     *
     * TODO: check if the bridge device is also reachable.
     */
    public boolean lightBridgeIsPresent(Context context){
        return getHueBridge(context) != null;
    }

    /**
     * @return philips hue bridge to connect.
     */
    public HueBridge getHueBridge(Context context)
    {
        try
        {
            return new HueBridgeProvider(AppDatabase.getAppDatabase(context)).execute().get();
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context of calling activity.
     * @param bridgeId which is currently connected.
     * @return list of bulbs that should be controlled by the app.
     */
    public List<HueBulb> getPairedBulbs(Context context, long bridgeId)
    {
        try
        {
            return new HueBulbsProvider(AppDatabase.getAppDatabase(context)).execute(bridgeId).get();
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a list of philips hue bulbs connected to device communicating with the app.
     */
    private static class HueBulbsProvider extends AsyncTask<Long, Void, List<HueBulb>>
    {
        private final AppDatabase db;

        HueBulbsProvider(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected List<HueBulb> doInBackground(Long... longs)
        {
            return db.hueBulbDao().getPairedBulbs(longs[0]);
        }
    }

    /**
     * Returns default philips hue bridge.
     */
    private static class HueBridgeProvider extends AsyncTask<Void, Void, HueBridge>
    {
        private final AppDatabase db;

        HueBridgeProvider(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected HueBridge doInBackground(Void... voids)
        {
            return db.hueBridgeDao().getDefaultDevice();
        }
    }
}
