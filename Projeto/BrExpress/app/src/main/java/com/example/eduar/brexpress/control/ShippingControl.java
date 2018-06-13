package com.example.eduar.brexpress.control;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Shipping;
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.shipping.RegisterShippingActivity;
import com.example.eduar.brexpress.view.shipping.ShippingListFragment;
import com.example.eduar.brexpress.view.worker.RegisterWorkerActivity;
import com.example.eduar.brexpress.view.worker.WorkerListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eduar on 11/06/2018.
 */

public class ShippingControl {

    private static ShippingControl mInstance = null;

    public static ShippingControl getInstance() {
        if (mInstance == null)
            mInstance = new ShippingControl();
        return mInstance;
    }

    private ShippingControl() {

    }

    public void getAllShipping(final FragmentWithLoading fragment) {
        if (Utils.isNetworkAvailable(fragment.getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getAllShipping";
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(ShippingControl.class.getName(), "Get ALL Works");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Shipping>>(){}.getType();
                            List<Shipping> shippingList = gson.fromJson(response.toString(), listType);
                            if (fragment instanceof ShippingListFragment) {
                                ((ShippingListFragment) fragment).allShippingLoaded(shippingList);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ShippingControl.class.getName(), "Get ALL does not Work");
                            if (fragment instanceof ShippingListFragment) {
                                ((ShippingListFragment) fragment).allShippingLoadedError();
                            }
                        }
                    });

                    MainApplication.getInstance(fragment.getContext()).addToRequestQueue(jsonArrayRequest);
                }
            }).start();
        } else {
            Toast.makeText(fragment.getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            fragment.stopLoading();
        }
    }

    public void getShippingById(final ActivityWithLoading activity, final int id) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getShippingById?id=" + id;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ShippingControl.class.getName(), "Get By Id Works");
                            Gson gson = new Gson();
                            Shipping shipping = gson.fromJson(response.toString(), Shipping.class);
                            if (activity instanceof RegisterShippingActivity) {
                                ((RegisterShippingActivity) activity).shippingLoaded(shipping);
                            }
                        }
                    },  new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ShippingControl.class.getName(), "Get ALL does not Work");
                            if (activity instanceof RegisterShippingActivity) {
                                ((RegisterShippingActivity) activity).shippingLoadedError();
                            }
                        }
                    });

                    MainApplication.getInstance(activity).addToRequestQueue(jsonObjectRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            activity.stopLoading();
        }
    }

    public void saveShipping(final ActivityWithLoading activity, final Shipping shipping) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "saveShipping";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, Shipping.class, null,
                            shipping.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ShippingControl.class.getName(), "Save Works");
                            Intent intent = new Intent(Constants.SHIPPING_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ShippingControl.class.getName(), "Save does not Work");
                            Intent intent = new Intent(Constants.SHIPPING_SAVED_ERROR);
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

    public void updateShipping(final ActivityWithLoading activity, final Shipping shipping) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "updateShipping";

                    GsonRequest gsonRequest = new GsonRequest(Request.Method.PUT, url, Shipping.class, null,
                            shipping.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ShippingControl.class.getName(), "Update Works");
                            Intent intent = new Intent(Constants.SHIPPING_UPDATED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ShippingControl.class.getName(), "Update does not Work");
                            Intent intent = new Intent(Constants.SHIPPING_UPDATED_ERROR);
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

    public void deleteShipping(final ActivityWithLoading activity, final List<Integer> ids) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "deleteShipping";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ids", ids);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ProductControl.class.getName(), "Delete ALL Works");
                            Intent intent = new Intent(Constants.SHIPPING_DELETED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ProductControl.class.getName(), "Delete ALL does not Work");
                            Intent intent = new Intent(Constants.SHIPPING_DELETED_ERROR);
                            activity.sendBroadcast(intent);
                        }
                    });
                    MainApplication.getInstance(activity).addToRequestQueue(jsonObjectRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            activity.stopLoading();
        }
    }
}
