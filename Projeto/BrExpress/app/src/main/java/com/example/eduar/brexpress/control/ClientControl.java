package com.example.eduar.brexpress.control;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;

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

    public void saveClient(final ActivityWithLoading activity, final User user) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "saveClient";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, User.class, null,
                            user.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ClientControl.class.getName(), "Save Works");
                            Intent intent = new Intent(Constants.CLIENT_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ClientControl.class.getName(), "Save does not Work");
                            Intent intent = new Intent(Constants.CLIENT_SAVED_ERROR);
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
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(url, jsonObject,
                            new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ClientControl.class.getName(), "Login Works");
                            try {
                                Utils.saveUser(activity, response.getInt("id"),
                                        response.getString("name"),
                                        response.getBoolean("type"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    MainApplication.getInstance(activity).addToRequestQueue(jsonRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            activity.stopLoading();
        }
    }
}
