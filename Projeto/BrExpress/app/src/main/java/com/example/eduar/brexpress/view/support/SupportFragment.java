package com.example.eduar.brexpress.view.support;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eduar.brexpress.R;
import com.example.eduar.brexpress.view.FragmentWithLoading;

/**
 * Created by eduar on 12/06/2018.
 */

public class SupportFragment extends FragmentWithLoading {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_list, container, false);
        return v;
    }
}
