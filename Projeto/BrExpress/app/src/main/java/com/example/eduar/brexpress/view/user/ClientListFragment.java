package com.example.eduar.brexpress.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ClientControl;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.service.ImageDownloader;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.product.GridSpacingItemDecoration;
import com.example.eduar.brexpress.view.product.ProductListAdapter;
import com.example.eduar.brexpress.view.product.RegisterProductActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 04/06/2018.
 */

public class ClientListFragment extends FragmentWithLoading {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private ClientListAdapter mAdapter;
    private List<User> mUserList;
    private FloatingActionButton mFab;

    private boolean mIsAdmin = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_list, container, false);
        mUserList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mSwipeRefresh = v.findViewById(R.id.swipe_refresh);
        mFab = v.findViewById(R.id.fab);

        addListeners();

        mAdapter = new ClientListAdapter(this, mUserList);

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
        loadClients();
        this.showLoading(null);
    }

    private void addListeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setEnabled(false);
                loadClients();
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

    private void loadClients() {
        ClientControl.getInstance().getAllClients(this);
    }

    public void allClientsLoaded(List<User> users) {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        mUserList = users;
        mAdapter.notifyDataChanged(mUserList);
    }

    public void allClientsLoadedError() {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        Toast.makeText(this.getContext(), R.string.failed_to_load_users, Toast.LENGTH_LONG).show();
    }
}
