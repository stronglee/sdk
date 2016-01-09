package com.example.stronglee.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.common.sdk.update.UpdateController;

public class UpdateApplication extends AppCompatActivity {
    private String down_url = "http://p.gdown.baidu.com/e9d23a3c521fb3ee4dd4e14dc8d7931078082a4e34558184c110250620aa7d652f15c84854312a36cfbf4bdbd22eb1915966c17ce5866ef0ecebad4330265e1d9e3aec16fac0aaa86cb1434831f70f4794020dd331d5ee0bd3aa0b830a974eb4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_applicaton);
    }

    public void startDownload(View view) {
        UpdateController.getInstance().init(this, R.mipmap.ic_launcher);
        UpdateController.getInstance().beginDownLoad(down_url, true);

    }
}
