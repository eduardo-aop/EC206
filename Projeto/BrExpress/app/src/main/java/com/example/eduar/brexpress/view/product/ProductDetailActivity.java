package com.example.eduar.brexpress.view.product;

import android.content.Intent;
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
import com.example.eduar.brexpress.view.ActivityWithLoading;

import java.io.InputStream;

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
                Intent i = new Intent(ProductDetailActivity.this, CreditCardActivity.class);
                startActivity(i);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
