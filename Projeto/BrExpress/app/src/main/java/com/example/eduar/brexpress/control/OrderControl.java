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
import com.example.eduar.brexpress.model.Order;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.product.OrderListFragment;
import com.example.eduar.brexpress.view.worker.WorkerListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eduar on 11/06/2018.
 */

public class OrderControl {

    private static OrderControl mInstance = null;

    public static OrderControl getInstance() {
        if (mInstance == null)
            mInstance = new OrderControl();
        return mInstance;
    }

    private OrderControl() {

    }

    public void getAllOrders(final FragmentWithLoading fragment, final int clientId) {
        if (Utils.isNetworkAvailable(fragment.getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getOrders?id=" + clientId;
                    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(WorkerControl.class.getName(), "Load orders Works");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Order>>(){}.getType();
                            List<Order> orderList = gson.fromJson(response.toString(), listType);
                            if (fragment instanceof OrderListFragment) {
                                ((OrderListFragment) fragment).allOrdersLoaded(orderList);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(OrderControl.class.getName(), "Load orders does not Work");
                            if (fragment instanceof OrderListFragment) {
                                ((OrderListFragment) fragment).allOrdersLoadedError();
                            }
                        }
                    });
                    MainApplication.getInstance(fragment.getContext()).addToRequestQueue(jsonObjectRequest);
                }
            }).start();
        } else {
            Toast.makeText(fragment.getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            ((ActivityWithLoading)fragment.getActivity()).stopLoading();
        }
    }
}
