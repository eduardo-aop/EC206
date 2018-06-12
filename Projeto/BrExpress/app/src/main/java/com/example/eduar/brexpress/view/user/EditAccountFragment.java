package com.example.eduar.brexpress.view.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.control.ClientControl;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.FragmentWithLoading;

import java.util.regex.Pattern;

/**
 * Created by eduar on 06/06/2018.
 */

public class EditAccountFragment extends FragmentWithLoading {

    private ScrollView mScrollView;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mAddressEditText;
    private EditText mCpfEditText;
    private EditText mPwdEditText;
    private EditText mConfirmPwdEditText;
    private EditText mOldPwdEditText;

    private Button mCreateAccButton;
    private Button mBackButton;

    private BroadcastReceiver mReceiver;

    private int mUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);

        mUserId = Utils.getUserId(this.getContext());

        initAllComponents(view);
        broadcastReceiver();
        registerBroadcasts();

        loadUser();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        this.getActivity().getMenuInflater().inflate(R.menu.menu_edit_user, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUser() {
        this.startLoading(null);
        ClientControl.getInstance().getClientById(this, mUserId);
    }

    public void userLoaded(User user) {
        mNameEditText.setText(user.getName());
        mEmailEditText.setText(user.getEmail());
        mAddressEditText.setText(user.getAddress());
        mCpfEditText.setText(user.getCpf());
    }

    public void userLoadedError() {

    }

    private void setMockObject() {
        mNameEditText.setText("Eduardo");
        mEmailEditText.setText("edu@ina.br");
        mAddressEditText.setText("Casarao");
        mCpfEditText.setText("12345678998");
        mPwdEditText.setText("123456");
        mConfirmPwdEditText.setText("123456");
    }

    private void initAllComponents(View v) {
        mScrollView = v.findViewById(R.id.scroll_view);
        mNameEditText = v.findViewById(R.id.name);
        mEmailEditText = v.findViewById(R.id.email);
        mAddressEditText = v.findViewById(R.id.address);
        mCpfEditText = v.findViewById(R.id.cpf);
        mPwdEditText = v.findViewById(R.id.pwd);
        mConfirmPwdEditText = v.findViewById(R.id.confirm_pwd);
        mOldPwdEditText = v.findViewById(R.id.old_pwd);
    }

    /**
     * Check if has some empty field
     * @return
     */
    private boolean checkEmptyFields() {
        if (mNameEditText.getText().toString().isEmpty()
                || mEmailEditText.getText().toString().isEmpty()
                || mAddressEditText.getText().toString().isEmpty()
                || mCpfEditText.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_LONG).show();
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

        String oldPwd = mPwdEditText.getText().toString().trim();
        String pwd = mPwdEditText.getText().toString().trim();
        String confirmPwd = mConfirmPwdEditText.getText().toString().trim();

        if (!oldPwd.isEmpty() || !pwd.isEmpty() || !confirmPwd.isEmpty()) {
            if (oldPwd.length() < 5) {
                mOldPwdEditText.setError(getResources().getString(R.string.min_pwd_length));
                focusOnView(mOldPwdEditText);
                return false;
            }

            if (pwd.length() < 5) {
                mPwdEditText.setError(getResources().getString(R.string.min_pwd_length));
                focusOnView(mPwdEditText);
                return false;
            }

            if (!pwd.equals(confirmPwd)) {
                mPwdEditText.setError(getResources().getString(R.string.not_match_pwd));
                focusOnView(mPwdEditText);
                return false;
            }
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
            user.setOldPwd(mOldPwdEditText.getText().toString().trim());

            ClientControl.getInstance().updateClient(this, user);
        }
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.CLIENT_UPDATED_SUCCESSFULLY:
                            Toast.makeText(EditAccountFragment.this.getContext(),
                                    R.string.client_updated_success, Toast.LENGTH_LONG).show();
                            EditAccountFragment.this.stopLoading();
                            break;
                        case Constants.CLIENT_UPDATED_ERROR:
                            Toast.makeText(EditAccountFragment.this.getContext(),
                                    R.string.client_updated_error, Toast.LENGTH_LONG).show();
                            EditAccountFragment.this.stopLoading();
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
        getContext().registerReceiver(mReceiver, new IntentFilter(Constants.CLIENT_UPDATED_ERROR));
        getContext().registerReceiver(mReceiver, new IntentFilter(Constants.CLIENT_UPDATED_SUCCESSFULLY));
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mReceiver);
    }
}
