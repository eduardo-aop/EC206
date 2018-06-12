package com.example.eduar.brexpress.control;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.user.ClientListFragment;
import com.example.eduar.brexpress.view.worker.WorkerListFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

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
                    String url = Constants.SERVER_URL + "getWorkers";
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
}
