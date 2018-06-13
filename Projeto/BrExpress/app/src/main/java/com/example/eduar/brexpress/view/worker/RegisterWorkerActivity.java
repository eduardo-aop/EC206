package com.example.eduar.brexpress.view.worker;

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
import com.example.eduar.brexpress.control.WorkerControl;
import com.example.eduar.brexpress.model.User;
import com.example.eduar.brexpress.model.Worker;
import com.example.eduar.brexpress.utils.Constants;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.ActivityWithLoading;

import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * Created by eduar on 06/06/2018.
 */

public class RegisterWorkerActivity extends ActivityWithLoading {

    private ScrollView mScrollView;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mAddressEditText;
    private EditText mCpfEditText;
    private EditText mSalaryEditText;
    private EditText mSectorEditText;
    private EditText mFunctionEditText;
    private Toolbar mToolbar;

    private String mCurrentPriceText = "";

    private BroadcastReceiver mReceiver;

    private Integer mWorkerId = null;
    private Worker mEditingWorker = null;
    private boolean mIsEditing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_worker);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            mIsEditing = b.getBoolean(Constants.IS_EDITING);
            mWorkerId = b.getInt(Constants.WORKER_ID);

            if (mIsEditing) {
                this.startLoading(null);
                WorkerControl.getInstance().getWorkerById(this, mWorkerId);
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

    public void workerLoaded(Worker worker) {
        this.stopLoading();
        mEditingWorker = worker;
        mNameEditText.setText(worker.getName());
        mEmailEditText.setText(worker.getEmail());
        mAddressEditText.setText(worker.getAddress());
        mCpfEditText.setText(worker.getCpf());
        mSalaryEditText.setText(String.format("%.2f", worker.getSalary()));
        mSectorEditText.setText(worker.getSector());
        mFunctionEditText.setText(worker.getFunction());
    }

    public void workerLoadedError() {
        this.stopLoading();
        Toast.makeText(this, R.string.failed_to_load_orders, Toast.LENGTH_LONG).show();
    }

    private void setMockObject() {
        mNameEditText.setText("Eduardo");
        mEmailEditText.setText("edu@ina.br");
        mAddressEditText.setText("Casarao");
        mCpfEditText.setText("12345678998");
    }

    private void initAllComponents() {
        mScrollView = findViewById(R.id.scroll_view);
        mNameEditText = findViewById(R.id.name_text);
        mEmailEditText = findViewById(R.id.email_text);
        mAddressEditText = findViewById(R.id.address_text);
        mCpfEditText = findViewById(R.id.cpf_text);
        mSalaryEditText = findViewById(R.id.salary_text);
        mSectorEditText = findViewById(R.id.sector_text);
        mFunctionEditText = findViewById(R.id.function_text);
    }

    private void addComponentListener() {
        mSalaryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(mCurrentPriceText)){
                    mSalaryEditText.removeTextChangedListener(this);

                    String cleanString = charSequence.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    mCurrentPriceText = formatted;
                    mSalaryEditText.setText(formatted);
                    mSalaryEditText.setSelection(formatted.length());

                    mSalaryEditText.addTextChangedListener(this);
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
                || mEmailEditText.getText().toString().isEmpty()
                || mAddressEditText.getText().toString().isEmpty()
                || mCpfEditText.getText().toString().isEmpty()
                || mSalaryEditText.getText().toString().isEmpty()
                || mSectorEditText.getText().toString().isEmpty()
                || mFunctionEditText.getText().toString().isEmpty()) {
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

        if (mSalaryEditText.getText().toString().trim().length() <= 0) {
            mSalaryEditText.setError(getResources().getString(R.string.invalid_cpf));
            focusOnView(mSalaryEditText);
            return false;
        }

        if (mSectorEditText.getText().toString().trim().length() < 5) {
            mSectorEditText.setError(getResources().getString(R.string.invalid_cpf));
            focusOnView(mSectorEditText);
            return false;
        }

        if (mFunctionEditText.getText().toString().trim().length() < 5) {
            mFunctionEditText.setError(getResources().getString(R.string.invalid_cpf));
            focusOnView(mFunctionEditText);
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

            Worker worker = mEditingWorker != null ? mEditingWorker : new Worker();

            worker.setName(mNameEditText.getText().toString().trim());
            worker.setAddress(mAddressEditText.getText().toString().trim());
            worker.setEmail(mEmailEditText.getText().toString().trim());
            worker.setCpf(mCpfEditText.getText().toString().trim());

            String cleanPriceString = Utils.clearPrice(mSalaryEditText);

            worker.setSalary(Float.valueOf(cleanPriceString));
            worker.setSector(mSectorEditText.getText().toString().trim());
            worker.setFunction(mFunctionEditText.getText().toString().trim());

            if (!mIsEditing) {
                WorkerControl.getInstance().saveWorker(this, worker);
            } else {
                WorkerControl.getInstance().updateWorker(this, worker);
            }
        }
    }

    private void broadcastReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case Constants.WORKER_SAVED_SUCCESSFULLY:
                            Toast.makeText(RegisterWorkerActivity.this,
                                    R.string.client_saved_success, Toast.LENGTH_LONG).show();
                            RegisterWorkerActivity.this.stopLoading();
                            RegisterWorkerActivity.this.finish();
                            break;
                        case Constants.WORKER_SAVED_ERROR:
                            Toast.makeText(RegisterWorkerActivity.this,
                                    R.string.client_saved_error, Toast.LENGTH_LONG).show();
                            RegisterWorkerActivity.this.stopLoading();
                            break;
                        case Constants.WORKER_UPDATED_SUCCESSFULLY:
                            Toast.makeText(RegisterWorkerActivity.this,
                                    R.string.client_updated_success, Toast.LENGTH_LONG).show();
                            RegisterWorkerActivity.this.stopLoading();
                            RegisterWorkerActivity.this.finish();
                            break;
                        case Constants.WORKER_UPDATED_ERROR:
                            Toast.makeText(RegisterWorkerActivity.this,
                                    R.string.client_updated_error, Toast.LENGTH_LONG).show();
                            RegisterWorkerActivity.this.stopLoading();
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
        registerReceiver(mReceiver, new IntentFilter(Constants.WORKER_SAVED_SUCCESSFULLY));
        registerReceiver(mReceiver, new IntentFilter(Constants.WORKER_SAVED_ERROR));
        registerReceiver(mReceiver, new IntentFilter(Constants.WORKER_UPDATED_SUCCESSFULLY));
        registerReceiver(mReceiver, new IntentFilter(Constants.WORKER_UPDATED_ERROR));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
