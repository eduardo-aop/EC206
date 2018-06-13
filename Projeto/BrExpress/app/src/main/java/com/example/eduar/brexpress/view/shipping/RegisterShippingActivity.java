package com.example.eduar.brexpress.view.shipping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ShippingControl;
import com.example.eduar.brexpress.model.Shipping;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;

import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * Created by eduar on 06/06/2018.
 */

public class RegisterShippingActivity extends ActivityWithLoading {

    private ScrollView mScrollView;
    private EditText mNameEditText;
    private EditText mCompanyEditText;
    private EditText mPaymentEditText;
    private Toolbar mToolbar;

    private String mCurrentPriceText = "";

    private BroadcastReceiver mReceiver;

    private Integer mShippingId = null;
    private Shipping mEditingShipping = null;
    private boolean mIsEditing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_shipping);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            mIsEditing = b.getBoolean(Constants.IS_EDITING);
            mShippingId = b.getInt(Constants.SHIPPING_ID);

            if (mIsEditing) {
                loadShipping();
            }
        }

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initAllComponents();
        addComponentListener();
        broadcastReceiver();
        registerBroadcasts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_user, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_save:
                if (validateFields()) {
                    this.startLoading(null);
                    sendData();
                }
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadShipping() {
        this.startLoading(null);
        ShippingControl.getInstance().getShippingById(this, mShippingId);
    }

    public void shippingLoaded(Shipping shipping) {
        this.stopLoading();
        mEditingShipping = shipping;
        mNameEditText.setText(shipping.getName());
        mCompanyEditText.setText(shipping.getCompany());
        mPaymentEditText.setText(String.format("%.2f", shipping.getPayment()));
    }

    public void shippingLoadedError() {
        this.stopLoading();
        Toast.makeText(this, R.string.failed_to_load_orders, Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void setMockObject() {
        mNameEditText.setText("Eduardo");
        mCompanyEditText.setText("edu@ina.br");
    }

    private void initAllComponents() {
        mScrollView = findViewById(R.id.scroll_view);
        mNameEditText = findViewById(R.id.name_text);
        mCompanyEditText = findViewById(R.id.company_text);
        mPaymentEditText = findViewById(R.id.payment_text);
    }

    private void addComponentListener() {
        mPaymentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(mCurrentPriceText)){
                    mPaymentEditText.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    mCurrentPriceText = formatted;
                    mPaymentEditText.setText(formatted);
                    mPaymentEditText.setSelection(formatted.length());

                    mPaymentEditText.addTextChangedListener(this);
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
        if (mNameEditText.getText().toString().isEmpty()
                || mCompanyEditText.getText().toString().isEmpty()
                || mPaymentEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateFields() {
        if (!checkEmptyFields()) {
            return false;
        }

        if (mNameEditText.getText().toString().trim().length() < 5) {
            mNameEditText.setError(getResources().getString(R.string.min_product_name_length));
            focusOnView(mNameEditText);
            return false;
        }

        if (mCompanyEditText.getText().toString().trim().length() < 5) {
            mCompanyEditText.setError(getResources().getString(R.string.invalid_email));
            focusOnView(mCompanyEditText);
            return false;
        }

        if (mPaymentEditText.getText().toString().trim().length() <= 0) {
            mPaymentEditText.setError(getResources().getString(R.string.invalid_cpf));
            focusOnView(mPaymentEditText);
            return false;
        }

        return true;
    }

    private void focusOnView(final EditText editText){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, editText.getTop());
                editText.setFocusable(true);
            }
        });
    }

    private void sendData() {
        if (validateFields()) {
            this.startLoading(null);

            Shipping shipping = mEditingShipping != null ? mEditingShipping : new Shipping();

            shipping.setName(mNameEditText.getText().toString().trim());
            shipping.setCompany(mCompanyEditText.getText().toString().trim());

            String cleanPriceString = Utils.clearPrice(mPaymentEditText);

            shipping.setPayment(Float.valueOf(cleanPriceString));

            if (!mIsEditing) {
                ShippingControl.getInstance().saveShipping(this, shipping);
            } else {
                ShippingControl.getInstance().updateShipping(this, shipping);
            }
        }
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.SHIPPING_SAVED_SUCCESSFULLY:
                            Toast.makeText(RegisterShippingActivity.this,
                                    R.string.shipping_saved_success, Toast.LENGTH_LONG).show();
                            RegisterShippingActivity.this.stopLoading();
                            RegisterShippingActivity.this.finish();
                            break;
                        case Constants.SHIPPING_SAVED_ERROR:
                            Toast.makeText(RegisterShippingActivity.this,
                                    R.string.shipping_saved_error, Toast.LENGTH_LONG).show();
                            RegisterShippingActivity.this.stopLoading();
                            break;
                        case Constants.SHIPPING_UPDATED_SUCCESSFULLY:
                            Toast.makeText(RegisterShippingActivity.this,
                                    R.string.shipping_updated_success, Toast.LENGTH_LONG).show();
                            RegisterShippingActivity.this.stopLoading();
                            RegisterShippingActivity.this.finish();
                            break;
                        case Constants.SHIPPING_UPDATED_ERROR:
                            Toast.makeText(RegisterShippingActivity.this,
                                    R.string.shipping_updated_error, Toast.LENGTH_LONG).show();
                            RegisterShippingActivity.this.stopLoading();
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
        registerReceiver(mReceiver, new IntentFilter(Constants.SHIPPING_SAVED_SUCCESSFULLY));
        registerReceiver(mReceiver, new IntentFilter(Constants.SHIPPING_SAVED_ERROR));
        registerReceiver(mReceiver, new IntentFilter(Constants.SHIPPING_UPDATED_SUCCESSFULLY));
        registerReceiver(mReceiver, new IntentFilter(Constants.SHIPPING_UPDATED_ERROR));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
