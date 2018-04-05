package com.example.eduar.brexpress.control;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.GsonRequest;

/**
 * Created by eduar on 04/04/2018.
 */

public class ProductControl {
    private static ProductControl mInstance = null;

    private static Context mContext = null;

    public static ProductControl getInstance(Context context) {
        mContext = context;

        if (mInstance == null)
            mInstance = new ProductControl();
        return mInstance;
    }

    public void getAllProducts() {
        String url = "http://get.all.product";
        GsonRequest gsonRequest = new GsonRequest(url, Product.class, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MainApplication.getInstance(mContext).addToRequestQueue(gsonRequest);
    }
}
