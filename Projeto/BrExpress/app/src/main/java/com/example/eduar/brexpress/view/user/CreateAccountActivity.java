package com.example.eduar.brexpress.view.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ClientControl;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.view.ActivityWithLoading;

import java.util.regex.Pattern;

/**
 * Created by eduar on 06/06/2018.
 */

public class CreateAccountActivity extends ActivityWithLoading {

    private ScrollView mScrollView;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mAddressEditText;
    private EditText mCpfEditText;
    private EditText mPwdEditText;
    private EditText mConfirmPwdEditText;

    private Button mCreateAccButton;
    private Button mBackButton;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);

        initAllComponents();
        broadcastReceiver();
        registerBroadcasts();
        //setMockObject();
    }

    private void setMockObject() {
        mNameEditText.setText("Eduardo");
        mEmailEditText.setText("edu@ina.br");
        mAddressEditText.setText("Casarao");
        mCpfEditText.setText("12345678998");
        mPwdEditText.setText("123456");
        mConfirmPwdEditText.setText("123456");
    }

    private void initAllComponents() {
        mScrollView = findViewById(R.id.scroll_view);
        mNameEditText = findViewById(R.id.name);
        mEmailEditText = findViewById(R.id.email);
        mAddressEditText = findViewById(R.id.address);
        mCpfEditText = findViewById(R.id.cpf);
        mPwdEditText = findViewById(R.id.pwd);
        mConfirmPwdEditText = findViewById(R.id.confirm_pwd);

        mCreateAccButton = findViewById(R.id.create_account);
        mCreateAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountActivity.this.finish();
            }
        });
    }

    /**
     * Check if has some empty field
     * @return
     */
    private boolean checkEmptyFields() {
        if (mNameEditText.getText().toString().isEmpty()
                || mEmailEditText.getText().toString().isEmpty()
                || mAddressEditText.getText().toString().isEmpty()
                || mCpfEditText.getText().toString().isEmpty()
                || mPwdEditText.getText().toString().isEmpty()
                || mConfirmPwdEditText.getText().toString().isEmpty()) {
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
        Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
        String email = mEmailEditText.getText().toString().trim();
        if (!pattern.matcher(email).matches()) {
            mEmailEditText.setError(getResources().getString(R.string.invalid_email));
            focusOnView(mEmailEditText);
            return false;
        }

        if (mAddressEditText.getText().toString().trim().length() < 5) {
            mAddressEditText.setError(getResources().getString(R.string.min_address_length));
            focusOnView(mAddressEditText);
            return false;
        }

        if (mCpfEditText.getText().toString().trim().length() != 11) {
            mCpfEditText.setError(getResources().getString(R.string.invalid_cpf));
            focusOnView(mCpfEditText);
            return false;
        }

        if (!mPwdEditText.getText().toString().trim().equals(mConfirmPwdEditText.getText().toString().trim())) {
            mPwdEditText.setError(getResources().getString(R.string.not_match_pwd));
            focusOnView(mPwdEditText);
            return false;
        }

        if (mPwdEditText.getText().toString().trim().length() < 5) {
            mPwdEditText.setError(getResources().getString(R.string.min_pwd_length));
            focusOnView(mPwdEditText);
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
            User user = new User();
            user.setName(mNameEditText.getText().toString().trim());
            user.setAddress(mAddressEditText.getText().toString().trim());
            user.setEmail(mEmailEditText.getText().toString().trim());
            user.setCpf(mCpfEditText.getText().toString().trim());
            user.setPwd(mPwdEditText.getText().toString().trim());

            ClientControl.getInstance().saveClient(this, user);
        }
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.CLIENT_SAVED_SUCCESSFULLY:
                            Toast.makeText(CreateAccountActivity.this,
                                    R.string.client_saved_success, Toast.LENGTH_LONG).show();
                            CreateAccountActivity.this.stopLoading();
                            CreateAccountActivity.this.finish();
                            break;
                        case Constants.CLIENT_SAVED_ERROR:
                            Toast.makeText(CreateAccountActivity.this,
                                    R.string.client_saved_error, Toast.LENGTH_LONG).show();
                            CreateAccountActivity.this.stopLoading();
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
        registerReceiver(mReceiver, new IntentFilter(Constants.CLIENT_SAVED_SUCCESSFULLY));
        registerReceiver(mReceiver, new IntentFilter(Constants.CLIENT_SAVED_ERROR));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
