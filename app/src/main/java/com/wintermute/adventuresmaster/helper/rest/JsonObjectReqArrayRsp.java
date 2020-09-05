package com.wintermute.adventuresmaster.helper.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * This rest request takes JSONObject as request and responds with JSONArray. This is necessary due to some philips hue
 * inconsistent responses.
 *
 * @author wintermute
 */
public class JsonObjectReqArrayRsp extends JsonRequest<JSONArray>
{
    public JsonObjectReqArrayRsp(int method, String url, JSONObject req, Response.Listener<JSONArray> listener,
                                 Response.ErrorListener errorListener)
    {
        super(method, url, (req == null) ? null : req.toString(), listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            String jsonArray;
            if (jsonString.startsWith("{"))
            {
                jsonArray = "[" + jsonString + "]";
                return Response.success(new JSONArray(jsonArray), HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
