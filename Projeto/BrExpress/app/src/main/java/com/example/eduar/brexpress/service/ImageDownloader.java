package com.example.eduar.brexpress.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.view.ProductListActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

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
        Context context = (Context) params[1];

        InputStream inputStream = null;
        try {
            inputStream = new java.net.URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (context instanceof ProductListActivity) {
            ((ProductListActivity) context).imageDownloaded(inputStream, imageDownloadingId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
