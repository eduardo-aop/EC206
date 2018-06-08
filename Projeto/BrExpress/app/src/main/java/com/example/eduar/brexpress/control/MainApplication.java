package com.example.eduar.brexpress.control;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by eduar on 04/04/2018.
 */

public class MainApplication {

    private static MainApplication mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private MainApplication(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MainApplication getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MainApplication(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
