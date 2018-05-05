package com.example.eduar.brexpress.view;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by eduar on 04/05/2018.
 */

public class RegisterProductActivity extends ActivityWithLoading {

    public final int REQUEST_CODE_ASK_FOR_CAMERA = 1;

    ImageView mSelectPhotoImageView = null;
    ImageView mProductImageImageView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_product);

        mSelectPhotoImageView = findViewById(R.id.select_photos);
        mProductImageImageView = findViewById(R.id.selected_product_image);

        hasCameraPermission();

        mSelectPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraPermission()) {
                    selectProductPhoto();
                }
            }
        });
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

    private void selectProductPhoto() {
        //Build galleryIntent
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mProductImageImageView.setImageBitmap(photo);
            } else {
                try {
                    if (data.getData() != null) {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        mProductImageImageView.setImageBitmap(selectedImage);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_FOR_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //Allow to open camera
                    selectProductPhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
