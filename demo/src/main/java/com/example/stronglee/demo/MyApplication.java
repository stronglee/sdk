package com.example.stronglee.demo;

import android.content.Context;

import com.android.common.sdk.app.BaseApplication;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by stronglee on 16/1/4.
 */
public class MyApplication extends BaseApplication {
    private static MyApplication mInstance;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("")
                .setLogLevel(LogLevel.FULL)
                .setMethodCount(3)
                .setMethodOffset(2)
                .hideThreadInfo();
    }

    public static synchronized DaoMaster getDaoMaster(Context context) {
        if (mDaoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "my_db", null);
            mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    public static synchronized DaoSession getDaoSession(Context context) {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                getDaoMaster(context);
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }
}
