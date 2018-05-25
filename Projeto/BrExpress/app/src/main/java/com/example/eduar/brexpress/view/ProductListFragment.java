package com.example.eduar.brexpress.view;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ProductControl;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.ImageDownloader;
import com.example.eduar.brexpress.utils.Utils;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_products_list, container, false);

        mProductList = new ArrayList<>();

        loadProducts();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);

        addListeners();
        this.showLoading(null);

        mAdapter = new ProductListAdapter(this, mProductList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                Utils.dpToPx(this.getContext(), 10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void addListeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setEnabled(false);
                loadProducts();
            }
        });
    }

    private void loadProducts() {
        ProductControl productControl = ProductControl.getInstance(this);
        productControl.getAllProducts();
    }

    public void allProductsLoaded(List<Product> products) {
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setEnabled(true);
        this.stopLoading();
        mProductList = products;
        mAdapter.notifyDataChanged(mProductList);

        for (Product p : mProductList) {
            if (p.getImage() == null) {
                new ImageDownloader().execute(p.getId(), this);
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
}