package com.example.eduar.brexpress.view;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduar.brexpress.R;

/**
 * Created by eduar on 04/04/2018.
 */

public class ActivityWithLoading extends AppCompatActivity {

    private ProgressDialog mProgress = null;
    private LinearLayout mProgressContainer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showLoading(String text) {
        dismissLoading();

        View view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        TextView message = view.findViewById(R.id.message);
        message.setText("");

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(null);
        mProgress.setCancelable(false);
        mProgress.show();
        mProgress.setContentView(view);
    }

    public void dismissLoading() {
        if (mProgress != null) {
            mProgress.dismiss();
        }

        mProgress = null;
    }
}
