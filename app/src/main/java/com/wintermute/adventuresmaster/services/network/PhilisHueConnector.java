package com.wintermute.adventuresmaster.services.network;

import android.content.Context;
import android.os.AsyncTask;
import com.wintermute.adventuresmaster.database.app.AppDatabase;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;
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
     * @deprecated this call checks requests bridge as default device. Default device is not supported anymore.
     * TODO: check if the bridge device is also reachable.
     */
    public boolean lightBridgeIsPresent(Context context)
    {
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
     * @return available philips hue bridge.
     */
    public HueBridge getDiscoveredPhilipsHueBridge()
    {
        try
        {
            JSONObject response = parseHttpBroadcastResponse(new DiscoverTask().execute().get());
            String url = "http://" + response.getString("LOCATION").split("/")[2].replace(":80", "/api");
            String deviceId = response.getString("hue-bridgeid");
            //TODO: request user for device name in the activity.
            String deviceName = "tmpName";
            return new HueBridge(url, deviceId, deviceName);
        } catch (JSONException | NullPointerException | InterruptedException | ExecutionException e)
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

    /**
     * Discovers hue bridge over broadcast.
     * <p>
     * TODO: prompt for pushing philips hue bridge button while discover task.
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
