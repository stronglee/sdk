package com.example.stronglee.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.common.sdk.app.BaseFragment;
import com.example.stronglee.demo.R;

/**
 * Created by stronglee on 16/1/6.
 */
public class FragmentTwo extends BaseFragment {
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two,container,false);
        return view;
    }
}
