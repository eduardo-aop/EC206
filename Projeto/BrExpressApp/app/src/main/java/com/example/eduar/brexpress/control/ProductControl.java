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
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.product.ProductDetailActivity;
import com.example.eduar.brexpress.view.product.ProductListFragment;
import com.example.eduar.brexpress.view.product.RegisterProductActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

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
                            Log.d(ProductControl.class.getName(), "Get ALL Works");
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
                            Log.d(ProductControl.class.getName(), "Get ALL does not Work");
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

    public void getProductById(final ActivityWithLoading activity, final int id) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getProduct?id=" + id;
                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ProductControl.class.getName(), "Get By Id Works");
                            Gson gson = new Gson();
                            Product product = gson.fromJson(response.toString(), Product.class);
                            if (activity instanceof ProductDetailActivity) {
                                ((ProductDetailActivity) activity).productDetailLoaded(product);
                            } else if (activity instanceof RegisterProductActivity) {
                                ((RegisterProductActivity) activity).productDetailLoaded(product);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ProductControl.class.getName(), "Get By Id does not Work");
                            if (activity instanceof ProductDetailActivity) {
                                ((ProductDetailActivity) activity).productDetailLoadedError();
                            } else if (activity instanceof RegisterProductActivity) {
                                ((RegisterProductActivity) activity).productDetailLoadedError();
                            }
                        }
                    });

                    MainApplication.getInstance(activity).addToRequestQueue(jsonArrayRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            activity.stopLoading();
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
                            Log.d(ProductControl.class.getName(), "Save Works");
                            Intent intent = new Intent(Constants.PRODUCT_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ProductControl.class.getName(), "Save does not Work");
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

    public void updateProduct(final ActivityWithLoading activity, final Product product) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "updateProduct";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.PUT, url, Product.class, null,
                            product.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ProductControl.class.getName(), "Update Works");
                            Intent intent = new Intent(Constants.PRODUCT_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ProductControl.class.getName(), "Update does not Work");
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