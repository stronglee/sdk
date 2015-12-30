package com.android.common.sdk.network;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by stronglee on 15/12/30.
 */
public class NetworkManager {
    private OkHttpClient mClient;
    private static NetworkManager mInstance;

    private NetworkManager() {
        mClient = new OkHttpClient();
    }

    public static synchronized NetworkManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }
}
