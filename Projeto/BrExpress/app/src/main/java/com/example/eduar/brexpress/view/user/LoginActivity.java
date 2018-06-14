package com.example.eduar.brexpress.view.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ClientControl;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.view.ActivityWithLoading;
import com.example.eduar.brexpress.view.MainActivity;

import java.util.regex.Pattern;

/**
 * Created by eduar on 25/05/2018.
 */

public class LoginActivity extends ActivityWithLoading {

    private EditText mEmailEditText;
    private EditText mPwdEditText;
    private Button mLoginButton;
    private Button mBackButton;
    private Button mNewAccButton;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initAllComponents();
        broadcastReceiver();
        registerBroadcasts();
    }

    private void initAllComponents() {
        mEmailEditText = findViewById(R.id.email);
        mPwdEditText = findViewById(R.id.pwd);
        mLoginButton = findViewById(R.id.login_button);
        mBackButton = findViewById(R.id.back_button);
        mNewAccButton = findViewById(R.id.create_acc_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    LoginActivity.this.startLoading(null);
                    ClientControl.getInstance().doLogin(LoginActivity.this,
                            mEmailEditText.getText().toString().trim(),
                            mPwdEditText.getText().toString().trim());
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMainActivity();
            }
        });
        
        mNewAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Check if has some empty field
     * @return
     */
    private boolean checkEmptyFields() {
        if (mEmailEditText.getText().toString().isEmpty()
                || mPwdEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateFields() {
        if (!checkEmptyFields()) {
            return false;
        }

        Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
        String email = mEmailEditText.getText().toString().trim();
        if (!pattern.matcher(email).matches()) {
            mEmailEditText.setError(getResources().getString(R.string.invalid_email));
            return false;
        }

        if (mPwdEditText.getText().toString().trim().length() < 5) {
            mPwdEditText.setError(getResources().getString(R.string.min_pwd_length));
            return false;
        }

        return true;
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.LOGIN_SUCCESS:
                            LoginActivity.this.stopLoading();

                            callMainActivity();
                            break;
                        case Constants.LOGIN_ERROR:
                            Toast.makeText(LoginActivity.this,
                                    R.string.login_invalid, Toast.LENGTH_LONG).show();
                            LoginActivity.this.stopLoading();
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
        registerReceiver(mReceiver, new IntentFilter(Constants.LOGIN_SUCCESS));
        registerReceiver(mReceiver, new IntentFilter(Constants.LOGIN_ERROR));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        callMainActivity();
    }

    private void callMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        LoginActivity.this.finish();
    }
}
