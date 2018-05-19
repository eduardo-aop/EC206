package com.example.eduar.brexpress.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eduar.brexpress.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 05/05/2018.
 */

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ViewHolder> {

    private ProductImagesActivity mActivity;
    private List<Bitmap> mProductImages = new ArrayList<>();
    private List<Bitmap> mSelectedProductImages = new ArrayList<>();

    public ProductImagesAdapter(ProductImagesActivity activity, List<Bitmap> productImages) {
        this.mActivity = activity;
        this.mProductImages = productImages;
    }

    @Override
    public ProductImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product_images, parent, false);

        ProductImagesAdapter.ViewHolder vh = new ProductImagesAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ProductImagesAdapter.ViewHolder holder, int position) {
        final Bitmap bitmap = mProductImages.get(position);

        holder.mProductImageView.setImageBitmap(bitmap);

        holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addOrRemoveFromSelected(holder, bitmap);
                mActivity.isRemovingPhotos(!mSelectedProductImages.isEmpty());
                return true;
            }
        });

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSelectedProductImages.isEmpty()) {
                    addOrRemoveFromSelected(holder, bitmap);
                    mActivity.isRemovingPhotos(!mSelectedProductImages.isEmpty());
                }
            }
        });
    }

    public void addOrRemoveFromSelected(ViewHolder holder, Bitmap bitmap) {
        if (!mSelectedProductImages.contains(bitmap)) {
            mSelectedProductImages.add(bitmap);
        } else {
            mSelectedProductImages.remove(bitmap);
        }

        holder.mSelection.setVisibility(mSelectedProductImages.contains(bitmap) ? View.VISIBLE : View.GONE);
    }

    public void dataChanged(List<Bitmap> list) {
        mProductImages = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mProductImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        ImageView mProductImageView;
        FrameLayout mContainer;
        LinearLayout mSelection;

        private ViewHolder(View card) {
            super(card);

            mProductImageView = card.findViewById(R.id.product_image);
            mContainer = card.findViewById(R.id.container);
            mSelection = card.findViewById(R.id.selection);
        }
    }
}