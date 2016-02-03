package com.example.stronglee.demo;

import android.os.Bundle;
import android.view.View;

import com.android.common.sdk.app.BaseActivity;
import com.example.stronglee.demo.fragment.FragmentOne;
import com.example.stronglee.demo.fragment.FragmentTwo;

/**
 * Created by stronglee on 16/1/6.
 */
public class ContentActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        findViewById(R.id.pre_button).setOnClickListener(this);
        findViewById(R.id.next_button).setOnClickListener(this);
        pushFragmentToBackStack(FragmentOne.class,null);
        pushFragmentToBackStack(FragmentTwo.class,null);
    }

    @Override
    protected String getCloseWarning() {
        return "";
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.content_id;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pre_button:
                preFragment();
                break;
            case R.id.next_button:
                nextFragment();
                break;
            default:
                break;
        }
    }

    private void preFragment(){
        goToFragment(FragmentOne.class,null);
    }

    private void nextFragment(){
      goToFragment(FragmentTwo.class,null);
    }

}
