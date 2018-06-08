package com.example.eduar.brexpress.view;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.product.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mHeaderView;
    private LinearLayout mHeaderLayout;
    private TextView mUserNameTextView;
    private ImageView mUserImageview;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        addNavigationDrawer();

        ProductListFragment productListFragment = new ProductListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, productListFragment)
                .commit();
    }

    private void addNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, 0, 0) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                checkItem(item.getItemId());
                switch (item.getItemId()) {
                    case R.id.product:
                        break;
                    case R.id.employee:
                        break;
                    case R.id.client:
                        break;
                    case R.id.shipping:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mHeaderView = mNavigationView.getHeaderView(0);
        mHeaderLayout = mHeaderView.findViewById(R.id.nav_header);
        mUserNameTextView = mHeaderView.findViewById(R.id.name_nav_header);
        mUserImageview = mHeaderView.findViewById(R.id.nav_header_imageView);

        mHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getUserId(MainActivity.this) == -1) {
                    //Has user logged
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    //No user logged
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = Utils.getUserName(this);

        if (userName != null) {
            mUserNameTextView.setText(userName);
        }
    }

    public void checkItem(int id) {
        int size = mNavigationView.getMenu().size();

        for (int i = 0; i < size; i++) {
            MenuItem item = mNavigationView.getMenu().getItem(i);
            if (item.getItemId() == id) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
