package com.wintermute.adventuresmaster.services.light;

import android.content.Context;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Rest Request handler.
 *
 * @author wintermute
 */
public class RequestHandler
{
    private static RequestHandler INSTANCE;
    private RequestQueue requestQueue;
    private static Context context;

    private Cache fileCache = new DiskBasedCache(new File(""), 1024 * 1024); // 1MB cap
    private Network network = new BasicNetwork(new HurlStack());

    private RequestHandler(Context context)
    {
        RequestHandler.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * @param context of calling activity.
     * @return instance.
     */
    public static synchronized RequestHandler getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RequestHandler(context);
        }
        return INSTANCE;
    }

    /**
     * @return current request queue.
     */
    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds request to queue to execute.
     *
     * @param req to send.
     * @param <T> type of request.
     */
    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
