package com.wintermute.adventuresmaster.services.light;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wintermute.adventuresmaster.helper.rest.JsonObjectReqArrayRsp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String url;
    private RequestHandler reqHandler;

    /**
     * Creates instance.
     *
     * @param context of calling activity.
     */
    public RestGun(Context context)
    {
        this.context = context;
        reqHandler = RequestHandler.getInstance(context);
        url = ""; //TODO: get url from database
    }

    /**
     * Adds a request to the que to change brightness of hue lights.
     *
     * @param color to set on bulbs.
     */
    public void changeColor(double[] color)
    {
        sendRequest(Request.Method.PUT, url, getChangeColorReqBody(color));
    }

    /**
     * Adds a request to the que to change brightness of hue lights.
     *
     * @param brightness to set on bulbs.
     */
    public void changeBrightness(int brightness)
    {
        sendRequest(Request.Method.PUT, url, getChangeBrightnessReqBody(brightness));
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
