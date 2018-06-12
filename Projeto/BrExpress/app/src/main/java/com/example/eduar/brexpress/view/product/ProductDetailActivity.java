package com.example.eduar.brexpress.view.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ProductControl;
import com.example.eduar.brexpress.model.Product;
import com.example.eduar.brexpress.service.ImageDownloader;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by eduar on 04/05/2018.
 */

public class ProductDetailActivity extends ActivityWithLoading {

    private Product mProduct;

    private ImageView mImageView;
    private TextView mName;
    private TextView mPrice;
    private TextView mQtd;
    private TextView mDescription;
    private Button mBuyButton;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initAllComponents();

        Bundle b = getIntent().getExtras();
        int id = b.getInt("productId");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.startLoading(null);
        ProductControl.getInstance().getProductById(this, id);
    }

    @Override
    protected void onResume() {
        super.onResume();

        broadcastReceiver();
        registerBroadcasts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAllComponents() {
        mName = findViewById(R.id.name_text);
        mPrice = findViewById(R.id.price_text);
        mDescription = findViewById(R.id.description_text);
        mQtd = findViewById(R.id.qtd_text);
        mImageView = findViewById(R.id.image);
        mBuyButton = findViewById(R.id.buy_button);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoading(null);
                ProductControl.getInstance().buyProduct(ProductDetailActivity.this,
                        Utils.getUserId(ProductDetailActivity.this),
                        mProduct.getId());

//                final int GET_NEW_CARD = 2;
//
//                Intent intent = new Intent(ProductDetailActivity.this, Card);
//                startActivityForResult(intent,GET_NEW_CARD);
            }
        });
    }

    public void productDetailLoaded(Product product) {
        this.stopLoading();
        mProduct = product;

        mName.setText(product.getName());
        mDescription.setText(product.getDescription());
        mQtd.setText(String.valueOf(product.getQtd()));
        mName.setText(product.getName());

        new ImageDownloader().execute(product.getId(), this, "productImage");
    }

    public void productDetailLoadedError() {
        this.stopLoading();
        Toast.makeText(this, R.string.failed_to_load_products, Toast.LENGTH_LONG).show();
    }

    public void imageDownloaded(InputStream inputStream, int id) {
        mProduct.setImage(BitmapFactory.decodeStream(inputStream));
        Handler mainHandler = new Handler(getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(mProduct.getImage());
            }
        };
        mainHandler.post(myRunnable);
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.PRODUCT_BOUGHT_SUCCESS:
                            stopLoading();
                            Toast.makeText(ProductDetailActivity.this, R.string.products_bought_success, Toast.LENGTH_LONG).show();

                            break;
                        case Constants.PRODUCT_BOUGHT_ERROR:
                            stopLoading();
                            Toast.makeText(ProductDetailActivity.this, R.string.products_bought_error, Toast.LENGTH_LONG).show();

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
        registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_BOUGHT_SUCCESS));
        registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_BOUGHT_ERROR));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
