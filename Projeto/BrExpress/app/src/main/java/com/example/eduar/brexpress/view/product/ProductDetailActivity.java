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

/**
 * Created by eduar on 04/05/2018.
 */

public class ProductDetailActivity extends ActivityWithLoading {

    private Product mProduct;

    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mQtdTextView;
    private TextView mDescriptionTextView;
    private Button mBuyButton;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initAllComponents();

        Bundle b = getIntent().getExtras();
        int id = b.getInt(Constants.PRODUCT_ID);

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
        mNameTextView = findViewById(R.id.name_text);
        mPriceTextView = findViewById(R.id.price_text);
        mDescriptionTextView = findViewById(R.id.description_text);
        mQtdTextView = findViewById(R.id.qtd_text);
        mImageView = findViewById(R.id.image);
        mBuyButton = findViewById(R.id.buy_button);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProduct.getQtd() <= 0) {
                    Toast.makeText(ProductDetailActivity.this, R.string.no_product_available, Toast.LENGTH_LONG).show();
                } else if (Utils.getUserId(ProductDetailActivity.this) == -1) {
                    Toast.makeText(ProductDetailActivity.this, R.string.do_login_first, Toast.LENGTH_LONG).show();
                } else {
                    startLoading(null);
                    ProductControl.getInstance().buyProduct(ProductDetailActivity.this,
                            Utils.getUserId(ProductDetailActivity.this),
                            mProduct.getId());
                }
            }
        });
    }

    public void productDetailLoaded(Product product) {
        this.stopLoading();
        mProduct = product;

        mNameTextView.setText(product.getName());
        mDescriptionTextView.setText(product.getDescription());
        mQtdTextView.setText(String.valueOf(product.getQtd()));
        String price = String.format(getResources().getString(R.string.price), product.getPriceWithDiscount());
        mPriceTextView.setText(price);

        new ImageDownloader().execute(product.getId(), this, "productImage");
    }

    public void productDetailLoadedError() {
        this.stopLoading();
        Toast.makeText(this, R.string.failed_to_load_products, Toast.LENGTH_LONG).show();
        this.finish();
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
                        case Constants.CONFIRMED_ACTION:
                            ProductDetailActivity.this.finish();
                            Toast.makeText(ProductDetailActivity.this, R.string.follow_history, Toast.LENGTH_LONG).show();
                            break;
                        case Constants.PRODUCT_BOUGHT_SUCCESS:
                            stopLoading();
                            Utils.createDialogWithNoNegativeButton(ProductDetailActivity.this, R.string.tanks, R.string.working_for_credit_card);
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
        registerReceiver(mReceiver, new IntentFilter(Constants.CONFIRMED_ACTION));
        registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_BOUGHT_SUCCESS));
        registerReceiver(mReceiver, new IntentFilter(Constants.PRODUCT_BOUGHT_ERROR));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
