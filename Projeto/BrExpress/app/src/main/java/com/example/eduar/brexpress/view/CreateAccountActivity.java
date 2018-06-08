package com.example.eduar.brexpress.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ClientControl;
import com.example.eduar.brexpress.model.Client;
import com.example.eduar.brexpress.utils.Constants;

import java.util.regex.Pattern;

/**
 * Created by eduar on 06/06/2018.
 */

public class CreateAccountActivity extends ActivityWithLoading {

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mAddressEditText;
    private EditText mCpfEditText;
    private EditText mPwdEditText;
    private EditText mConfirmPwdEditText;

    private Button mCreateAccButton;
    private Button mBackButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);

        initAllComponents();
    }

    private void initAllComponents() {
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
        if (checkEmptyFields()) {
            return false;
        }

        if (mNameEditText.getText().toString().trim().length() < 5) {
            mNameEditText.setError(getResources().getString(R.string.min_product_name_length));
            return false;
        }
        Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
        String email = mEmailEditText.getText().toString().trim();
        if (!pattern.matcher(email).matches()) {
            mEmailEditText.setError(getResources().getString(R.string.invalid_email));
            return false;
        }

        if (mCpfEditText.getText().toString().trim().length() == 11) {
            mCpfEditText.setError(getResources().getString(R.string.invalid_cpf));
            return false;
        }

        if (!mPwdEditText.getText().toString().trim().equals(mConfirmPwdEditText.getText().toString().trim())) {
            mPwdEditText.setError(getResources().getString(R.string.not_match_pwd));
            return false;
        }

        if (mPwdEditText.getText().toString().trim().length() < 5) {
            mPwdEditText.setError(getResources().getString(R.string.min_pwd_length));
            return false;
        }

        if (mAddressEditText.getText().toString().trim().length() < 5) {
            mAddressEditText.setError(getResources().getString(R.string.min_address_length));
            return false;
        }
        return true;
    }

    private void sendData() {
        if (validateFields()) {
            this.startLoading(null);
            Client client = new Client();
            client.setName(mNameEditText.getText().toString().trim());
            client.setAddress(mAddressEditText.getText().toString().trim());
            client.setEmail(mEmailEditText.getText().toString().trim());
            client.setCpf(mCpfEditText.getText().toString().trim());
            client.setPwd(mPwdEditText.getText().toString().trim());

            ClientControl.getInstance().saveClient(this, client);
        }
    }
}
