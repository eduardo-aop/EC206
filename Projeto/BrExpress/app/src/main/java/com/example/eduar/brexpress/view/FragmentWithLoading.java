package com.example.eduar.brexpress.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.eduar.brexpress.R;

/**
 * Created by eduar on 24/05/2018.
 */

public class FragmentWithLoading extends Fragment {

    private ProgressDialog mProgress = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startLoading(String text) {
        ((ActivityWithLoading) this.getActivity()).startLoading(text);
    }

    public void stopLoading() {
        ((ActivityWithLoading) this.getActivity()).stopLoading();
    }
}
