package com.android.common.sdk.app;

import android.app.Application;

import com.android.common.sdk.network.OkHttpUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.stetho.Stetho;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

/**
 * Created by stronglee on 15/12/30.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initStetho();
        initGlideForOkHttp();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initStetho() {
        Stetho.initialize(Stetho
                .newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this)).build());
    }

    private void initGlideForOkHttp() {
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
}
