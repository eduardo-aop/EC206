package com.example.eduar.brexpress.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private List<Uri> mProductImages = new ArrayList<>();

    public ProductImagesAdapter(ProductImagesActivity activity, List<Uri> productImages) {
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
    public void onBindViewHolder(ProductImagesAdapter.ViewHolder holder, int position) {
        Uri uri = mProductImages.get(position);

        try {
            final InputStream imageStream = mActivity.getContentResolver().openInputStream(uri);
            final Bitmap image = BitmapFactory.decodeStream(imageStream);

            holder.mProductImageView.setImageBitmap(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dataChanged(List<Uri> list) {
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

        private ViewHolder(View card) {
            super(card);

            mProductImageView = card.findViewById(R.id.product_image);
        }
    }
}