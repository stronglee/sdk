package com.example.stronglee.demo;

import com.android.common.sdk.app.BaseApplication;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by stronglee on 16/1/4.
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("bupt")
                .setLogLevel(LogLevel.FULL)
                .setMethodCount(3)
                .setMethodOffset(2)
                .hideThreadInfo();
    }
}
