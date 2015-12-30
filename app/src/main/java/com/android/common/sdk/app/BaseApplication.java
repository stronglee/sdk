package com.android.common.sdk.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by stronglee on 15/12/30.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initStetho();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private  void initStetho(){
        Stetho.initialize(Stetho
                .newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this)).build());
    }
}
