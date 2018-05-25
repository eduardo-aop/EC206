package com.example.eduar.brexpress.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.eduar.brexpress.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ProductListFragment productListFragment = new ProductListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, productListFragment)
                .commit();
    }
}
