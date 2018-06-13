package com.example.eduar.brexpress.view.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ProductControl;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.ImageDownloader;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.FragmentWithLoading;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class ProductListFragment extends FragmentWithLoading {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private ProductListAdapter mAdapter;
    private List<Product> mProductList;
    private FloatingActionButton mFab;
    private boolean mIsAdmin = false;
    private BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products_list, container, false);

        mProductList = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
        mFab = view.findViewById(R.id.fab);

        addListeners();

        mAdapter = new ProductListAdapter(this, mProductList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                Utils.dpToPx(this.getContext(), 10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        broadcastReceiver();
        registerBroadcasts();

        mIsAdmin = Utils.getUserType(this.getContext());
        setHasOptionsMenu(mIsAdmin);

        if (mIsAdmin) {
            mFab.setVisibility(View.VISIBLE);
        } else {
            mFab.setVisibility(View.GONE);
        }
        loadProducts();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mAdapter.isEditing()) {
            this.getActivity().getMenuInflater().inflate(R.menu.menu_delete_product, menu);
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
            case R.id.action_delete_product:
                Utils.createDialog(this.getContext(), R.string.remove, R.string.confirm_delete_product);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addListeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setEnabled(false);
                loadProducts();
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

    private void loadProducts() {
        this.startLoading(null);
        ProductControl productControl = ProductControl.getInstance();
        productControl.getAllProducts(this);
    }

    public void allProductsLoaded(List<Product> products) {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        mProductList = products;
        mAdapter.notifyDataChanged(mProductList);

        for (Product p : mProductList) {
            if (p.getImage() == null) {
                new ImageDownloader().execute(p.getId(), this, "productImage");
            }
        }
    }

    public void allProductsLoadedError() {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        Toast.makeText(this.getContext(), R.string.failed_to_load_products, Toast.LENGTH_LONG).show();
    }

    public void imageDownloaded(InputStream inputStream, int id) {
        for (int i = 0; i < mProductList.size(); i++) {
            if (mProductList.get(i).getId() == id) {
                mProductList.get(i).setImage(BitmapFactory.decodeStream(inputStream));
                mAdapter.notifySpecificItemChanged(i);
                break;
            }
        }
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.CONFIRMED_ACTION:
                            startLoading(null);
                            ProductControl.getInstance().deleteProducts((ActivityWithLoading) getActivity(), mAdapter.getSelectedProducts());
                            break;
                        case Constants.PRODUCT_DELETED_SUCCESS:
                            ProductListFragment.this.stopLoading();
                            Toast.makeText(getActivity(), R.string.products_removed_success, Toast.LENGTH_LONG).show();
                            loadProducts();
                            mAdapter.setSelectedProducts(new ArrayList<Integer>());
                            getActivity().invalidateOptionsMenu();
                            break;
                        case Constants.PRODUCT_DELETED_ERROR:
                            ProductListFragment.this.stopLoading();
                            Toast.makeText(getActivity(), R.string.products_removed_error, Toast.LENGTH_LONG).show();
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
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_DELETED_SUCCESS));
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_DELETED_ERROR));
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }
}
