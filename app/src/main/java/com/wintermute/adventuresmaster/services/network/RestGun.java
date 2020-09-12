package com.wintermute.adventuresmaster.services.network;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wintermute.adventuresmaster.database.entity.settings.HueBridge;
import com.wintermute.adventuresmaster.database.entity.settings.HueBulb;
import com.wintermute.adventuresmaster.database.entity.tools.gm.Light;
import com.wintermute.adventuresmaster.helper.rest.JsonObjectReqArrayRsp;
import com.wintermute.adventuresmaster.services.light.ColorHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Prepares the request queue.
 *
 * @author wintermute
 */
public class RestGun
{
    public interface OnSuccess
    {
        void onResponse(JSONArray response);

        void onResponse(JSONObject response);

        //TODO: implement OnFail() writing logFile.
    }

    private OnSuccess onSuccess;
    private Context context;
    private String privilegedUrl;
    private RequestHandler reqHandler;
    private List<HueBulb> bulbs;

    /**
     * Creates instance.
     *
     * @param context of calling activity.
     */
    public RestGun(Context context)
    {
        this.context = context;
        reqHandler = RequestHandler.getInstance(context);
        PhilisHueConnector hueConnector = PhilisHueConnector.getInstance();
        HueBridge hueBridge = hueConnector.getHueBridge(context);
        if (hueBridge != null)
        {
            privilegedUrl = hueBridge.getUrl() + "/" + hueBridge.getUser();
            bulbs = hueConnector.getPairedBulbs(context, hueBridge.getId());
        }
    }

    /**
     * Change philips hue color and brightness configured for called scene.
     *
     * @param light configuration for scene.
     */
    public void adjustLightForScene(Light light){
        changeColor(ColorHelper.extractHueColorCoordinates(light.getColor()));
        changeBrightness(light.getBrightness());
    }

    /**
     * Adds a request to the que to change brightness of hue lights.
     *
     * @param color coordinates to set on philips hue bulbs.
     */
    public void changeColor(double[] color)
    {
        bulbs.forEach(b ->
        {
            String targetUrl = privilegedUrl + "/lights" + "/" + b.getId() + "/state";
            sendRequest(Request.Method.PUT, targetUrl, getChangeColorReqBody(color));
        });
    }

    /**
     * Adds a request to the que to change brightness of hue lights.
     *
     * @param brightness to set on bulbs.
     */
    public void changeBrightness(int brightness)
    {
        sendRequest(Request.Method.PUT, privilegedUrl, getChangeBrightnessReqBody(brightness));
    }

    public void requestBulbs(String url)
    {
        sendRequest(Request.Method.GET, url, null);
    }

    /**
     * @param url of target device.
     */
    public void getHueBridgeName(String url)
    {
        sendRequest(Request.Method.GET, url + "/config", null);
    }

    /**
     * Register device by hue bridge.
     *
     * @param url of hue bridge.
     * @param body containing string expected by hue bridge.
     */
    public void registerHue(String url, String body)
    {
        sendCustomReq(Request.Method.POST, url, createRequestBody(body));
    }

    private JSONObject getChangeColorReqBody(double[] color)
    {
        return createRequestBody(
            "{ \"on\":true, \"xy\": [ " + color[0] + ", " + color[1] + " ], \"transitiontime\":" + " 1 }");
    }

    private JSONObject getChangeBrightnessReqBody(int brightness)
    {
        return createRequestBody("{ \"on\":true, \"bri\": " + brightness + ", \"transitiontime\": 1 }");
    }

    private void sendRequest(int method, String url, JSONObject reqBody)
    {
        JsonObjectRequest req = new JsonObjectRequest(method, url, reqBody, response ->
        {
            setOnResponse((OnSuccess) context);
            if (onSuccess != null)
            {
                onSuccess.onResponse(response);
            }
        }, error -> Log.e("ERROR", error.toString()));
        reqHandler.addToRequestQueue(req);
    }

    private void sendCustomReq(int method, String url, JSONObject reqBody)
    {
        JsonObjectReqArrayRsp req = new JsonObjectReqArrayRsp(method, url, reqBody, response ->
        {
            setOnResponse((OnSuccess) context);
            if (onSuccess != null)
            {
                onSuccess.onResponse(response);
            }
        }, error -> Log.e("ERROR", error.toString()));
        reqHandler.addToRequestQueue(req);
    }

    private JSONObject createRequestBody(String reqBody)
    {
        try
        {
            return new JSONObject(reqBody);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private void setOnResponse(OnSuccess onSuccess)
    {
        this.onSuccess = onSuccess;
    }
}
