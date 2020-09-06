package com.wintermute.adventuresmaster.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.wintermute.adventuresmaster.R;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.services.light.RestGun;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Handles all requests made to {@link com.wintermute.adventuresmaster.view.settings.PhilipsHueConfig}
 *
 * @author wintermute
 */
public class PhilipsHueConfigViewModel extends ViewModel
{

    private String discoverResponse;
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
     * Sends request for getting the hue bridge device name.
     *
     * @param context of calling activity.
     * @param url of device to request its information.
     */
    public void getHueBridgeDeviceName(Context context, String url)
    {
        RestGun restGun = new RestGun(context);
        restGun.getHueBridgeName(url);
    }

    /**
     * Starts discovering hue bridge over network.
     *
     * @return response if any received otherwise null.
     */
    public boolean discoverBridge()
    {
        try
        {
            discoverResponse = new DiscoverTask().execute().get();
            return discoverResponse != null;
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Stores the hue bridge in database and connects the client.
     *
     * @param context of calling activity.
     * @return true if new registration is successful, false this device is already registered.
     */
    public boolean registerHueBridge(Context context)
    {
        try
        {
            RestGun restGun = new RestGun(context);
            String hueUrl = parseHttpBroadcastResponse(discoverResponse).getString("LOCATION").split("/")[2];
            hueUrl = "http://" + hueUrl.split(":80")[0] + "/api";
            hueBridge = new RegisterIfNonExisting(AppDatabase.getAppDatabase(context), restGun).execute(hueUrl).get();
            return hueBridge != null;
        } catch (JSONException | NullPointerException | InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks whether the register request was successful or not.
     *
     * @param response on registration request.
     * @return true if success, false if registration failed.
     */
    public boolean registerDevice(JSONArray response, Context context)
    {
        try
        {
            String user = response.getJSONObject(0).getJSONObject("success").getString("username");
            hueBridge.setUser(user);
            getHueBridgeDeviceName(context, hueBridge.getUrl());
            return true;
        } catch (JSONException e)
        {
            return false;
        }
    }

    /**
     * Extracts the device name from response and sets it on the {@link HueBridge} object.
     *
     * @param context of calling activity.
     * @param response from hue bridge containing device information.
     */
    public void setDeviceName(Context context, JSONObject response)
    {
        try
        {
            hueBridge.setDeviceName(response.getString("name"));
            checkIfShouldBeDefaultAndStore(context);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Switches default hue bridge and persists it.
     *
     * @param context of calling activity.
     * @param selectedItem new default device.
     */
    public void changeDefaultDevice(Context context, HueBridge selectedItem)
    {
        new ChangeDefaultDevice(AppDatabase.getAppDatabase(context)).execute(selectedItem);
    }

    private void checkIfShouldBeDefaultAndStore(Context context)
    {
        try
        {
            hueBridge.setDefaultDevice(
                new GetDefaultDevice(AppDatabase.getAppDatabase(context)).execute().get() == null);
            hueBridge.setId(new StoreHueBridge(AppDatabase.getAppDatabase(context)).execute(hueBridge).get());
        } catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject parseHttpBroadcastResponse(String target)
    {
        StringBuilder parsedResponse = new StringBuilder("{");
        Arrays.stream(target.split("\\r\\n")).forEach(e ->
        {
            if (!"".equals(e))
            {
                String[] keyValue = e.split(" ");
                String key = keyValue[0].replace(":", "");
                parsedResponse.append("\"").append(key).append("\":");

                if (keyValue.length == 2)
                {
                    parsedResponse.append("\"").append(keyValue[1]).append("\",");
                } else
                {
                    parsedResponse.append("\"\",");
                }
            } else
            {
                parsedResponse.deleteCharAt(parsedResponse.toString().length() - 1).append("}");
            }
        });
        try
        {
            return new JSONObject(parsedResponse.toString());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the bridge is already registered, otherwise registers new device.
     *
     * @author wintermute
     */
    private static class RegisterIfNonExisting extends AsyncTask<String, Void, HueBridge>
    {

        private AppDatabase db;
        private RestGun restGun;

        public RegisterIfNonExisting(AppDatabase db, RestGun instance)
        {
            this.db = db;
            this.restGun = instance;
        }

        @Override
        protected HueBridge doInBackground(String... url)
        {
            HueBridge result = db.hueBridgeDao().getByUrl(url[0]);
            if (result == null)
            {
                restGun.registerHue(url[0], "{\"devicetype\":\"" + R.string.app_name + "#" + Build.MODEL + "\"}");
                return new HueBridge(url[0]);
            }
            return null;
        }
    }

    /**
     * Gets the default hue bridge
     */
    private static class GetDefaultDevice extends AsyncTask<Void, Void, HueBridge>
    {
        private AppDatabase db;

        public GetDefaultDevice(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected HueBridge doInBackground(Void... voids)
        {
            return db.hueBridgeDao().getDefaultDevice();
        }
    }

    /**
     * Changes default hue bridge.
     */
    private static class ChangeDefaultDevice extends AsyncTask<HueBridge, Void, Void>
    {

        private AppDatabase db;

        public ChangeDefaultDevice(AppDatabase db)
        {
            this.db = db;
        }

        @Override
        protected Void doInBackground(HueBridge... hueBridges)
        {
            HueBridge defaultDevice = db.hueBridgeDao().getDefaultDevice();
            defaultDevice.setDefaultDevice(false);
            db.hueBridgeDao().update(defaultDevice);
            hueBridges[0].setDefaultDevice(true);
            db.hueBridgeDao().update(hueBridges[0]);
            return null;
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
            return db.hueBridgeDao().insert(hueBridges[0]);
        }
    }

    /**
     * Discovers hue bridge over broadcast.
     */
    private static class DiscoverTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids)
        {
            byte[] sent, received;
            String hueDiscoverAddress = "239.255.255.250";
            int hueDiscoverPort = 1900;
            String discoverPacket = "M-SEARCH * HTTP/1.1\r\nHost: " + hueDiscoverAddress + ":" + hueDiscoverPort
                + "\r\nMan: ssdp:discover\r\nMX: 10\r\nST: " + "ssdp:all\r\n\r\n";
            sent = discoverPacket.getBytes();

            try
            {
                DatagramPacket sendPacket =
                    new DatagramPacket(sent, sent.length, InetAddress.getByName(hueDiscoverAddress), hueDiscoverPort);
                DatagramSocket clientSocket = new DatagramSocket();
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket;
                clientSocket.setSoTimeout(10000);

                received = new byte[1024];
                receivePacket = new DatagramPacket(received, received.length);
                clientSocket.receive(receivePacket);
                return new String(receivePacket.getData());
            } catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
}
