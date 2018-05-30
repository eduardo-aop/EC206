package com.example.eduar.brexpress.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 03/04/2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private ProductListFragment mFragment;
    private List<Product> mProducts;
    private List<Integer> mSelectedProducts;
    private boolean isEditing = false;
    private boolean isAdmin;

    public ProductListAdapter(ProductListFragment fragment, List<Product> products) {
        this.mFragment = fragment;
        this.mProducts = products;
        this.mSelectedProducts = new ArrayList<>();
        isAdmin = Utils.getUserType(mFragment.getContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Product p = mProducts.get(position);
        holder.nameTextView.setText(p.getName());
        String price = String.format(mFragment.getResources().getString(R.string.price), p.getPrice());
        holder.mPriceTextView.setText(price);
        holder.mProductImageView.setImageBitmap(null);
        if (p.getImage() != null) {
            holder.mProductImageView.setImageBitmap(p.getImage());
        }

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    containerStyle(holder, selectProduct(p.getId()));
                } else if (!isAdmin) {
                    Intent i = new Intent(mFragment.getActivity(), ProductDetailActivity.class);
                    mFragment.getActivity().startActivity(i);
                }
            }
        });

        if (!isAdmin) {
            holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    containerStyle(holder, selectProduct(p.getId()));
                    return true;
                }
            });
        }
        containerStyle(holder, mSelectedProducts.contains(p.getId()));
    }

    private void containerStyle(ViewHolder holder, boolean selected) {
        if (selected) {
            holder.mContainer.setBackgroundColor(mFragment.getActivity().getResources()
                    .getColor(android.R.color.holo_blue_dark));
            holder.mContainer.setAlpha(0.5f);
        } else {
            holder.mContainer.setBackgroundColor(mFragment.getActivity().getResources()
                    .getColor(android.R.color.transparent));
            holder.mContainer.setAlpha(1.0f);
        }
    }

    private boolean selectProduct(Integer id) {
        boolean selected;
        if (mSelectedProducts.contains(id)) {
            mSelectedProducts.remove(id);
            selected = false;
        } else {
            mSelectedProducts.add(id);
            selected = true;
        }

        isEditing = !mSelectedProducts.isEmpty();

        mFragment.getActivity().invalidateOptionsMenu();
        return selected;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void notifyDataChanged(List<Product> products) {
        mProducts = products;
        this.notifyDataSetChanged();
    }

    public void notifySpecificItemChanged(final int pos) {
        Handler mainHandler = new Handler(mFragment.getActivity().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                ProductListAdapter.this.notifyItemChanged(pos);
            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView mProductImageView;
        private TextView nameTextView;
        private TextView mPriceTextView;
        private LinearLayout mContainer;

        private ViewHolder(View card) {
            super(card);
            mContainer = card.findViewById(R.id.container);
            nameTextView = card.findViewById(R.id.name_text);
            mPriceTextView = card.findViewById(R.id.price_text);
            mProductImageView = card.findViewById(R.id.product_image);
        }
    }
}
