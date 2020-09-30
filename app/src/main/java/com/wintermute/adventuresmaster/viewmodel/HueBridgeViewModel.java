package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.services.network.PhilisHueConnector;
import com.wintermute.adventuresmaster.services.network.RestGun;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Handles all requests made to {@link com.wintermute.adventuresmaster.view.settings.PhilipsHueConfig}
 *
 * @author wintermute
 */
public class HueBridgeViewModel extends ViewModel
{

    private HueBridge hueBridge;

    /**
     * @param context of calling activity.
     * @return list of registered hue bridges, stored in database.
     */
    public LiveData<List<HueBridge>> getRegisteredDevices(Context context)
    {
        return AppDatabase.getAppDatabase(context).hueBridgeDao().getAll();
    }

    /**
     * Requests discovering hue bridge over network.
     *
     * @return response if any received otherwise null.
     */
    public boolean discoverBridge()
    {
        hueBridge = PhilisHueConnector.getInstance().getDiscoveredPhilipsHueBridge();
        return hueBridge != null;
    }

    /**
     * Stores the hue bridge in database and connects the client.
     *
     * @param context of calling activity.
     */
    public void requestDeviceRegistration(Context context)
    {
        new RestGun(context).createPhilipsHueUser(hueBridge.getUrl());
    }

    /**
     * Checks whether the register request was successful or not.
     */
    public HueBridge registerDevice(String user, Context context)
    {
        try
        {
            hueBridge.setUser(user);
            hueBridge.setId(new StoreHueBridge(AppDatabase.getAppDatabase(context)).execute(hueBridge).get());
            return hueBridge;
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context of calling activity.
     * @return true if already paired, otherwise false.
     */
    public boolean deviceAlreadyRegistered(Context context)
    {
        try
        {
            return new CheckIfAlreadyPaired(AppDatabase.getAppDatabase(context)).execute(hueBridge).get();
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if philips hue bridge is already paired.
     */
    private static class CheckIfAlreadyPaired extends AsyncTask<HueBridge, Void, Boolean>
    {

        private AppDatabase db;

        public CheckIfAlreadyPaired(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected Boolean doInBackground(HueBridge... hueBridgeUser)
        {
            return db.hueBridgeDao().getByDeviceId(hueBridgeUser[0].getDeviceId()) != null;
        }
    }

    /**
     * Stores hue bridge in database.
     */
    private static class StoreHueBridge extends AsyncTask<HueBridge, Void, Long>
    {

        private AppDatabase db;

        public StoreHueBridge(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected Long doInBackground(HueBridge... hueBridges)
        {
            //TODO: test whether if check necessary.
            HueBridge registeredDevice = db.hueBridgeDao().getByDeviceId(hueBridges[0].getUser());
            if (registeredDevice != null) {
                return -1L;
            }
            return db.hueBridgeDao().insert(hueBridges[0]);
        }
    }
}
