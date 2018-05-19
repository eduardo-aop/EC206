package com.example.eduar.brexpress.view;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 05/05/2018.
 */

public class ProductImagesActivity extends ActivityWithLoading {

    public final int REQUEST_CODE_ASK_FOR_CAMERA = 1;

    private RecyclerView mRecyclerView;
    private ProductImagesAdapter mAdapter;
    private List<Bitmap> mProductImages = new ArrayList<>();
    private boolean isRemovingImages = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_images);

        hasCameraPermission();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_view);

        mAdapter = new ProductImagesAdapter(this, mProductImages);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,
                Utils.dpToPx(this, 10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isRemovingImages) {
            getMenuInflater().inflate(R.menu.menu_product_images, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    public void isRemovingPhotos(boolean isRemoving) {
        isRemovingImages = isRemoving;
        this.invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            //TODO: save configs here
            return true;
        } else if (id == R.id.action_photos) {
            if (hasCameraPermission()) {
                selectProductPhotos();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean hasCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_ASK_FOR_CAMERA);
            return false;
        }
        return true;
    }

    private void selectProductPhotos() {
        //Build galleryIntent
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/*");

        //Create chooser
        Intent chooser = Intent.createChooser(galleryIntent, getResources().getString(R.string.product_picture));

        if (Utils.checkIfHasCamera(this)) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            //I have to re-create the chooser here or the Samsung will not show the 'camera' icons.
            //I have to add the cameraIntent first.
            //chooser = Intent.createChooser(cameraIntent, getResources().getString(R.string.product_picture));
            //Intent[] extraIntents = {galleryIntent};

            Intent[] extraIntents = {cameraIntent};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        }

        startActivityForResult(chooser, REQUEST_CODE_ASK_FOR_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ASK_FOR_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("data")) {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                if (!mProductImages.contains(bitmap)) {
                    mProductImages.add(bitmap);
                }
            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        Bitmap bitmap = Utils.convertBitmapToUri(this, uri);
                        if (!mProductImages.contains(bitmap)) {
                            mProductImages.add(bitmap);
                        }
                    }
                }
            }
            mAdapter.dataChanged(mProductImages);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_FOR_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Allow to open camera
                    selectProductPhotos();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
