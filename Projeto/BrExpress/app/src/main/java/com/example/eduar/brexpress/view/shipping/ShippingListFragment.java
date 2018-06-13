package com.example.eduar.brexpress.view.shipping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ShippingControl;
import com.example.eduar.brexpress.model.Shipping;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 04/06/2018.
 */

public class ShippingListFragment extends FragmentWithLoading {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private ShippingListAdapter mAdapter;
    private List<Shipping> mShippingList;
    private FloatingActionButton mFab;

    private boolean mIsAdmin = false;
    private BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shipping_list, container, false);
        mShippingList = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mSwipeRefresh = v.findViewById(R.id.swipe_refresh);
        mFab = v.findViewById(R.id.fab);

        addListeners();
        setHasOptionsMenu(true);

        mAdapter = new ShippingListAdapter(this, mShippingList);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        broadcastReceiver();
        registerBroadcasts();

        mIsAdmin = Utils.getUserType(this.getContext());

        if (mIsAdmin) {
            mFab.setVisibility(View.VISIBLE);
        } else {
            mFab.setVisibility(View.GONE);
        }
        loadAllShipping();
        this.startLoading(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mAdapter.isEditing()) {
            this.getActivity().getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_delete:
                Utils.createDialog(this.getContext(), R.string.remove, R.string.confirm_delete_shipping);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addListeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setEnabled(false);
                loadAllShipping();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RegisterShippingActivity.class);
                getActivity().startActivity(i);
            }
        });
    }

    private void loadAllShipping() {
        ShippingControl.getInstance().getAllShipping(this);
    }

    public void allShippingLoaded(List<Shipping> shippingList) {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        mShippingList = shippingList;
        mAdapter.notifyDataChanged(mShippingList);
    }

    public void allShippingLoadedError() {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        Toast.makeText(this.getContext(), R.string.failed_to_load_all_shipping, Toast.LENGTH_LONG).show();
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.CONFIRMED_ACTION:
                            startLoading(null);
                            ShippingControl.getInstance().deleteShipping((ActivityWithLoading) getActivity(), mAdapter.getSelectedShipping());
                            break;
                        case Constants.SHIPPING_DELETED_SUCCESSFULLY:
                            ShippingListFragment.this.stopLoading();
                            Toast.makeText(getActivity(), R.string.shipping_removed_success, Toast.LENGTH_LONG).show();
                            loadAllShipping();
                            mAdapter.setSelectedWorkers(new ArrayList<Integer>());
                            getActivity().invalidateOptionsMenu();
                            break;
                        case Constants.SHIPPING_DELETED_ERROR:
                            ShippingListFragment.this.stopLoading();
                            Toast.makeText(getActivity(), R.string.shipping_removed_error, Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        };
    }
    /**
     * Registering all broadcast from this class
     */
    private void registerBroadcasts() {
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constants.CONFIRMED_ACTION));
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constants.SHIPPING_DELETED_SUCCESSFULLY));
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constants.SHIPPING_DELETED_ERROR));
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }
}
