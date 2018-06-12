package com.example.eduar.brexpress.view.product;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.example.eduar.brexpress.control.OrderControl;
import com.example.eduar.brexpress.control.WorkerControl;
import com.example.eduar.brexpress.model.Order;
import com.example.eduar.brexpress.service.ImageDownloader;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.FragmentWithLoading;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 04/06/2018.
 */

public class OrderListFragment extends FragmentWithLoading {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private OrderListAdapter mAdapter;
    private List<Order> mOrderList;
    private FloatingActionButton mFab;

    private boolean mIsAdmin = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_list, container, false);
        mOrderList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mSwipeRefresh = v.findViewById(R.id.swipe_refresh);
        mFab = v.findViewById(R.id.fab);

        addListeners();

        mAdapter = new OrderListAdapter(this, mOrderList);

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
        loadOrders();
        this.showLoading(null);
    }

    private void addListeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setEnabled(false);
                loadOrders();
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

    private void loadOrders() {
        OrderControl.getInstance().getAllOrders(this, Utils.getUserId(this.getContext()));
    }

    public void allOrdersLoaded(List<Order> orders) {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        mOrderList = orders;
        mAdapter.notifyDataChanged(mOrderList);

        for (Order o : mOrderList) {
            if (o.getImage() == null) {
                new ImageDownloader().execute(o.getId(), this, "productImage");
            }
        }
    }

    public void allOrdersLoadedError() {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        Toast.makeText(this.getContext(), R.string.failed_to_load_orders, Toast.LENGTH_LONG).show();
    }

    public void imageDownloaded(InputStream inputStream, int id) {
        for (int i = 0; i < mOrderList.size(); i++) {
            if (mOrderList.get(i).getId() == id) {
                mOrderList.get(i).setImage(BitmapFactory.decodeStream(inputStream));
                mAdapter.notifySpecificItemChanged(i);
            }
        }
    }
}
