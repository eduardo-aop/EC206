package com.example.eduar.brexpress.view;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.utils.Utils;
import com.example.eduar.brexpress.view.product.OrderListFragment;
import com.example.eduar.brexpress.view.product.ProductListFragment;
import com.example.eduar.brexpress.view.support.SupportFragment;
import com.example.eduar.brexpress.view.user.ClientListFragment;
import com.example.eduar.brexpress.view.user.EditAccountFragment;
import com.example.eduar.brexpress.view.user.LoginActivity;
import com.example.eduar.brexpress.view.worker.WorkerListFragment;

public class MainActivity extends ActivityWithLoading {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mHeaderView;
    private LinearLayout mHeaderLayout;
    private TextView mUserNameTextView;
    private TextView mTextView;
    private TextView mFirstLetterTextView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;

    private boolean mIsAdmin = false;
    private int mLastSelectedAdminItem = R.id.product;
    private int mLastSelectedUserItem = R.id.home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        addNavigationDrawer();

        replaceFragment(new ProductListFragment());
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
                if (item.getItemId() != R.id.logout)
                    checkItem(item.getItemId());

                if (mIsAdmin) {
                    adminItemMenuClicked(item);
                } else {
                    clientItemMenuClicked(item);
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mHeaderView = mNavigationView.getHeaderView(0);
        mHeaderLayout = mHeaderView.findViewById(R.id.nav_header);
        mUserNameTextView = mHeaderView.findViewById(R.id.name_nav_header);
        mTextView = mHeaderView.findViewById(R.id.nav_header_textView);
        mFirstLetterTextView = mHeaderView.findViewById(R.id.first_letter_text);

        mHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getUserId(MainActivity.this) == -1) {
                    //Has user logged
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    //No user logged
                }
            }
        });
    }

    private void adminItemMenuClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product:
                replaceFragment(new ProductListFragment());
                break;
            case R.id.employee:
                replaceFragment(new WorkerListFragment());
                break;
            case R.id.user:
                replaceFragment(new ClientListFragment());
                break;
            case R.id.shipping:
                break;
            case R.id.logout:
                Utils.clearUserData(MainActivity.this);
                styleNavigationDrawerHeader();
                reloadActivity();
                break;
        }
    }

    private void clientItemMenuClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                replaceFragment(new ProductListFragment());
                break;
            case R.id.my_account:
                replaceFragment(new EditAccountFragment());
                break;
            case R.id.my_orders:
                replaceFragment(new OrderListFragment());
                break;
            case R.id.support:
                replaceFragment(new SupportFragment());
                break;
            case R.id.logout:
                Utils.clearUserData(MainActivity.this);
                styleNavigationDrawerHeader();
                reloadActivity();
                break;
        }
    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    private void replaceFragment(FragmentWithLoading fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsAdmin = Utils.getUserType(this);
        styleNavigationDrawerMenu();
        styleNavigationDrawerHeader();

        if (mIsAdmin) checkItem(mLastSelectedAdminItem);
        else checkItem(mLastSelectedUserItem);
    }

    private void styleNavigationDrawerMenu() {
        mNavigationView.getMenu().clear();
        if (mIsAdmin) {
            mNavigationView.inflateMenu(R.menu.admin_main_drawer);
        } else {
            mNavigationView.inflateMenu(R.menu.user_main_drawer);
        }
    }

    private void styleNavigationDrawerHeader() {
        String userName = Utils.getUserName(this);

        if (userName != null) {
            mUserNameTextView.setText(userName);
            mTextView.setVisibility(View.GONE);
            mFirstLetterTextView.setText(String.valueOf(userName.charAt(0)).toUpperCase());
        } else {
            mUserNameTextView.setText(getResources().getString(R.string.do_login));
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getResources().getString(R.string.do_login));
            mFirstLetterTextView.setText(getResources().getString(R.string.br));
        }
    }

    public void checkItem(int id) {
        int size = mNavigationView.getMenu().size();
        if (mIsAdmin) mLastSelectedAdminItem = id;
        else mLastSelectedUserItem = id;

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
