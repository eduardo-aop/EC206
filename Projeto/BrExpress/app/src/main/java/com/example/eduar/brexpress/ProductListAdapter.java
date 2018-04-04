package com.example.eduar.brexpress;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.nameTextView.setText(p.getName());
        holder.priceTextView.setText(p.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView productImageView;
        public TextView nameTextView;
        public TextView priceTextView;

        public ViewHolder(View card) {
            super(card);
            nameTextView = (TextView) card.findViewById(R.id.name_text);
            priceTextView = (TextView) card.findViewById(R.id.price_text);
            productImageView = (ImageView) card.findViewById(R.id.product_image);
        }
    }
}
