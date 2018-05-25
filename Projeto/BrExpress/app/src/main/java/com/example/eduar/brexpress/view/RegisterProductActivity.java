package com.example.eduar.brexpress.view;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ProductControl;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;

import java.text.NumberFormat;

/**
 * Created by eduar on 04/05/2018.
 */

public class RegisterProductActivity extends ActivityWithLoading {

    public final int REQUEST_CODE_ASK_FOR_CAMERA = 1;

    private ImageView mSelectPhotoImageView = null;
    private ImageView mProductImageImageView = null;
    private EditText mProductNameEditText = null;
    private EditText mProductPriceEditText = null;
    private EditText mProductDiscountEditText = null;
    private EditText mProductQtdEditText = null;
    private EditText mProductDescriptionEditText = null;

    private String mCurrentPriceText = "";
    private boolean mSelectedPicture = false;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_product);

        hasCameraPermission();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initAllComponents();
        addComponentsListeners();
        broadcastReceiver();
        registerBroadcasts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if (validateField()) {
                RegisterProductActivity.this.showLoading(null);
                Utils.convertImageToBase64(this,
                        ((BitmapDrawable)mProductImageImageView.getDrawable()).getBitmap());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize all UI components
     */
    private void initAllComponents() {
        mSelectPhotoImageView = findViewById(R.id.select_photos);
        mProductImageImageView = findViewById(R.id.selected_product_image);
        mProductNameEditText = findViewById(R.id.product_name);
        mProductPriceEditText = findViewById(R.id.product_price);
        mProductDiscountEditText = findViewById(R.id.product_discount);
        mProductQtdEditText = findViewById(R.id.product_qtd);
        mProductDescriptionEditText = findViewById(R.id.product_description);
        mSelectedPicture = false;
    }

    /**
     * Add listeners to components
     */
    private void addComponentsListeners() {
        mSelectPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (hasCameraPermission()) {
                selectProductPhotos();
            }
            }
        });

        mProductPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(mCurrentPriceText)){
                    mProductPriceEditText.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    mCurrentPriceText = formatted;
                    mProductPriceEditText.setText(formatted);
                    mProductPriceEditText.setSelection(formatted.length());

                    mProductPriceEditText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Check if has some empty field
     * @return
     */
    private boolean checkEmptyFields() {
        if (mProductNameEditText.getText().toString().isEmpty()
                || mProductPriceEditText.getText().toString().isEmpty()
                || mProductDiscountEditText.getText().toString().isEmpty()
                || mProductQtdEditText.getText().toString().isEmpty()
                || mProductDescriptionEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Validate all fields
     * @return if all fields are correct
     */
    private boolean validateField() {
        if (!checkEmptyFields()) {
            return false;
        }
        if (mProductNameEditText.getText().toString().length() < 5) {
            mProductNameEditText.setError(getResources().getString(R.string.min_product_name_length));
            return false;
        }
        if (mProductQtdEditText.getText().toString().length() > 3) {
            mProductQtdEditText.setError(getResources().getString(R.string.max_product_qtd_available));
            return false;
        }
        if (mProductDiscountEditText.getText().toString().length() > 2) {
            mProductDiscountEditText.setError(getResources().getString(R.string.max_product_discount_available));
            return false;
        }
        if (!mSelectedPicture) {
            Toast.makeText(this, R.string.selected_a_picture, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Check camera permission is allowed
     * @return
     */
    private boolean hasCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_ASK_FOR_CAMERA);
            return false;
        }
        return true;
    }

    /**
     * Select picture by camera or gallery
     */
    private void selectProductPhotos() {
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
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    mProductImageImageView.setImageBitmap(bitmap);
                    mSelectedPicture = true;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        try {
                            Bitmap bitmap = Utils.convertBitmapToUri(this, uri);
                            mProductImageImageView.setImageBitmap(bitmap);
                            mSelectedPicture = true;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
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
                    selectProductPhotos();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.PRODUCT_SAVED_SUCCESSFULLY:
                            Toast.makeText(RegisterProductActivity.this,
                                    R.string.product_saved_success, Toast.LENGTH_LONG).show();
                            RegisterProductActivity.this.stopLoading();
                            break;
                        case Constants.PRODUCT_SAVED_ERROR:
                            Toast.makeText(RegisterProductActivity.this,
                                    R.string.product_saved_error, Toast.LENGTH_LONG).show();
                            RegisterProductActivity.this.stopLoading();
                            break;
                        case Constants.CONVERT_IMAGE_TO_BASE64_RECEIVER:
                            Product product = new Product();
                            product.setName(mProductNameEditText.getText().toString());
                            product.setDiscount(Float.valueOf(mProductDiscountEditText.getText().toString()));
                            product.setDescription(mProductDescriptionEditText.getText().toString());
                            product.setImageBase64(intent.getExtras().getString(Constants.BASE64_IMAGE));
                            String price = mProductPriceEditText.getText().toString();
                            String cleanPriceString = price.replaceAll("[R$]", "");
                            cleanPriceString = cleanPriceString.replaceAll(",",".");

                            product.setPrice(Float.valueOf(cleanPriceString));

                            ProductControl.getInstance(RegisterProductActivity.this).saveProduct(product);
                            break;
                        default:
                            break;

                    }
                }
            }
        };
    }

    /**
     * Registering all broadcast from this class
     */
    private void registerBroadcasts() {
        registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_SAVED_SUCCESSFULLY));
        registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_SAVED_ERROR));
        registerReceiver(mReceiver, new IntentFilter(Constants.CONVERT_IMAGE_TO_BASE64_RECEIVER));
        registerReceiver(mReceiver, new IntentFilter(Constants.BASE64_IMAGE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
