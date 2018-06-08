package com.example.eduar.brexpress.control;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Client;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eduar on 06/06/2018.
 */

public class ClientControl {

    private static ClientControl mInstance = null;

    public static ClientControl getInstance() {
        if (mInstance == null)
            mInstance = new ClientControl();
        return mInstance;
    }

    private ClientControl() {

    }

    public void saveClient(final ActivityWithLoading activity, final Client client) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "saveClient";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, Client.class, null,
                            client.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ClientControl.class.getName(), "Save Works");
                            Intent intent = new Intent(Constants.PRODUCT_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ClientControl.class.getName(), "Save does not Work");
                            Intent intent = new Intent(Constants.PRODUCT_SAVED_ERROR);
                            activity.sendBroadcast(intent);
                        }
                    });
                    MainApplication.getInstance(activity).addToRequestQueue(gsonRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            activity.stopLoading();
        }
    }

    public void doLogin(final ActivityWithLoading activity, final String email, final String pwd) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", email);
                        jsonObject.put("pwd", pwd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = Constants.SERVER_URL + "doLogin";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, Client.class, null,
                            jsonObject.toString(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ClientControl.class.getName(), "Login Works");
                            Intent intent = new Intent(Constants.LOGIN_SUCCESS);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ClientControl.class.getName(), "Login does not Work");
                            Intent intent = new Intent(Constants.LOGIN_ERROR);
                            activity.sendBroadcast(intent);
                        }
                    });
                    MainApplication.getInstance(activity).addToRequestQueue(gsonRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            activity.stopLoading();
        }
    }
}
