package com.example.eduar.brexpress.service;

import android.os.AsyncTask;

import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.product.ProductDetailActivity;
import com.example.eduar.brexpress.view.product.ProductListFragment;
import com.example.eduar.brexpress.view.product.RegisterProductActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by eduar on 23/05/2018.
 */

public class ImageDownloader extends AsyncTask<Object, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Object... params) {
        int imageDownloadingId = (int) params[0];
        String url = Constants.SERVER_URL + "downloadImage?id=" + Integer.valueOf(String.valueOf(imageDownloadingId));

        InputStream inputStream = null;
        try {
            inputStream = new java.net.URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (params[1] instanceof ProductListFragment) {
            ((ProductListFragment) params[1]).imageDownloaded(inputStream, imageDownloadingId);
        } else if (params[1] instanceof ProductDetailActivity) {
            ((ProductDetailActivity) params[1]).imageDownloaded(inputStream, imageDownloadingId);
        } else if (params[1] instanceof RegisterProductActivity) {
            ((RegisterProductActivity) params[1]).imageDownloaded(inputStream, imageDownloadingId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
