package com.example.eduar.brexpress.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.example.eduar.brexpress.R;

/**
 * Created by eduar on 25/05/2018.
 */

public class LoginActivity extends ActivityWithLoading {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }
}
