package com.example.eduar.brexpress.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Product;

import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private ProductListActivity mActivity;
    private List<Product> mProducts;

    public ProductListAdapter(ProductListActivity activity, List<Product> products) {
        this.mActivity = activity;
        this.mProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product p = mProducts.get(position);
        holder.mNameTextView.setText(p.getName());
        holder.mPriceTextView.setText(p.getPrice().toString());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView mProductImageView;
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private LinearLayout mContainer;

        private ViewHolder(View card) {
            super(card);
            mContainer = card.findViewById(R.id.container);
            mNameTextView = card.findViewById(R.id.name_text);
            mPriceTextView = card.findViewById(R.id.price_text);
            mProductImageView = card.findViewById(R.id.product_image);
        }
    }
}
