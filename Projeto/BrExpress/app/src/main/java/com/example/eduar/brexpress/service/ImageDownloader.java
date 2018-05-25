package com.example.eduar.brexpress.service;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.view.FragmentWithLoading;
import com.example.eduar.brexpress.view.ProductListFragment;

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
        FragmentWithLoading fragment = (FragmentWithLoading) params[1];

        InputStream inputStream = null;
        try {
            inputStream = new java.net.URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fragment instanceof ProductListFragment) {
            ((ProductListFragment) fragment).imageDownloaded(inputStream, imageDownloadingId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
