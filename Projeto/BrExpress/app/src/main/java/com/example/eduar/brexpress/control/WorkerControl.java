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
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.service.GsonRequest;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.worker.RegisterWorkerActivity;
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

public class WorkerControl {

    private static WorkerControl mInstance = null;

    public static WorkerControl getInstance() {
        if (mInstance == null)
            mInstance = new WorkerControl();
        return mInstance;
    }

    private WorkerControl() {

    }

    public void getAllWorkers(final FragmentWithLoading fragment) {
        if (Utils.isNetworkAvailable(fragment.getContext())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getWorkers?id=" + Utils.getUserId(fragment.getContext());
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(WorkerControl.class.getName(), "Get ALL Works");
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Worker>>(){}.getType();
                            List<Worker> workerList = gson.fromJson(response.toString(), listType);
                            if (fragment instanceof WorkerListFragment) {
                                ((WorkerListFragment) fragment).allClientsLoaded(workerList);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(WorkerControl.class.getName(), "Get ALL does not Work");
                            if (fragment instanceof WorkerListFragment) {
                                ((WorkerListFragment) fragment).allClientsLoadedError();
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

    public void getWorkerById(final ActivityWithLoading activity, final int id) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "getWorkerById?id=" + id;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ProductControl.class.getName(), "Get By Id Works");
                            Gson gson = new Gson();
                            Worker worker = gson.fromJson(response.toString(), Worker.class);
                            if (activity instanceof RegisterWorkerActivity) {
                                ((RegisterWorkerActivity) activity).workerLoaded(worker);
                            }
                        }
                    },  new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ClientControl.class.getName(), "Get ALL does not Work");
                            if (activity instanceof RegisterWorkerActivity) {
                                ((RegisterWorkerActivity) activity).workerLoadedError();
                            }
                        }
                    });

                    MainApplication.getInstance(activity).addToRequestQueue(jsonObjectRequest);
                }
            }).start();
        } else {
            Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            if (activity instanceof ActivityWithLoading) {
                activity.stopLoading();
            }
        }
    }

    public void saveWorker(final ActivityWithLoading activity, final Worker worker) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "saveWorker";
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, Worker.class, null,
                            worker.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ClientControl.class.getName(), "Update Works");
                            Intent intent = new Intent(Constants.WORKER_SAVED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ClientControl.class.getName(), "Update does not Work");
                            Intent intent = new Intent(Constants.WORKER_SAVED_ERROR);
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

    public void updateWorker(final ActivityWithLoading activity, final Worker worker) {
        if (Utils.isNetworkAvailable(activity)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Constants.SERVER_URL + "updateWorker";
                    String j = worker.converToJson();
                    GsonRequest gsonRequest = new GsonRequest(Request.Method.PUT, url, Worker.class, null,
                            worker.converToJson(), new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(ClientControl.class.getName(), "Update Works");
                            Intent intent = new Intent(Constants.WORKER_UPDATED_SUCCESSFULLY);
                            activity.sendBroadcast(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(ClientControl.class.getName(), "Update does not Work");
                            Intent intent = new Intent(Constants.WORKER_UPDATED_ERROR);
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
