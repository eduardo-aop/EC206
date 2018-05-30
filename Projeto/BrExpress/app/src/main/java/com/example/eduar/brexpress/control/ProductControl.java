package com.example.eduar.brexpress.control;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.ProductListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eduar on 04/04/2018.
 */

public class ProductControl {
    private static ProductControl mInstance = null;

    public static ProductControl getInstance() {
        if (mInstance == null)
            mInstance = new ProductControl();
        return mInstance;
    }

    private ProductControl() {

    }

    public void getAllProducts(final FragmentWithLoading fragment) {
        if (Utils.isNetworkAvailable(fragment.getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getProducts";
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(ProductControl.class.getName(), "It Works");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Product>>(){}.getType();
                            List<Product> productList = gson.fromJson(response.toString(), listType);
                            if (fragment instanceof ProductListFragment) {
                                ((ProductListFragment) fragment).allProductsLoaded(productList);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ProductControl.class.getName(), "It does not Work");
                            if (fragment instanceof ProductListFragment) {
                                ((ProductListFragment) fragment).allProductsLoadedError();
                            }
                        }
                    });

                    MainApplication.getInstance(fragment.getContext()).addToRequestQueue(jsonArrayRequest);
                }
            }).start();
        } else {
            Toast.makeText(fragment.getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            if (fragment.getActivity() instanceof ActivityWithLoading) {
                ((ActivityWithLoading) fragment.getActivity()).stopLoading();
            }
        }
    }

    public void saveProduct(final ActivityWithLoading activity, final Product product) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "saveProduct";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, Product.class, null,
                            product.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ProductControl.class.getName(), "It Works");
                            Intent intent = new Intent(Constants.PRODUCT_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ProductControl.class.getName(), "It does not Work");
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
}