package com.example.eduar.brexpress.view.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ClientControl;
import com.example.eduar.brexpress.control.WorkerControl;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.product.RegisterProductActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 04/06/2018.
 */

public class WorkerListFragment extends FragmentWithLoading {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private WorkerListAdapter mAdapter;
    private List<Worker> mWorkerList;
    private FloatingActionButton mFab;

    private boolean mIsAdmin = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_list, container, false);
        mWorkerList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mSwipeRefresh = v.findViewById(R.id.swipe_refresh);
        mFab = v.findViewById(R.id.fab);

        addListeners();

        mAdapter = new WorkerListAdapter(this, mWorkerList);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

//        broadcastReceiver();
//        registerBroadcasts();

        mIsAdmin = Utils.getUserType(this.getContext());

        if (mIsAdmin) {
            mFab.setVisibility(View.VISIBLE);
        } else {
            mFab.setVisibility(View.GONE);
        }
        loadWorkers();
        this.showLoading(null);
    }

    private void addListeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setEnabled(false);
                loadWorkers();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RegisterProductActivity.class);
                getActivity().startActivity(i);
            }
        });
    }

    private void loadWorkers() {
        WorkerControl.getInstance().getAllWorkers(this);
    }

    public void allClientsLoaded(List<Worker> workers) {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        mWorkerList = workers;
        mAdapter.notifyDataChanged(mWorkerList);
    }

    public void allClientsLoadedError() {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        Toast.makeText(this.getContext(), R.string.failed_to_load_users, Toast.LENGTH_LONG).show();
    }
}
