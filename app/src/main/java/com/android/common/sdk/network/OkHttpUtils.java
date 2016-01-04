package com.android.common.sdk.network;

import android.os.Handler;
import android.os.Looper;

import com.android.common.sdk.network.builder.GetBuilder;
import com.android.common.sdk.network.builder.PostFileBuilder;
import com.android.common.sdk.network.builder.PostFormBuilder;
import com.android.common.sdk.network.builder.PostStringBuilder;
import com.android.common.sdk.network.callback.Callback;
import com.android.common.sdk.network.request.RequestCall;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class OkHttpUtils {
    public static final String TAG = "OkHttpUtils";
    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private boolean debug;
    private String tag;

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        if (true) {
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        if (debug) {
            mOkHttpClient.networkInterceptors().add(new StethoInterceptor());
        }
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }


    public OkHttpUtils debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 对okHttp的get请求的封装
     *
     * @return GetBuilder
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    /**
     * okHttp的post请求的封装
     *
     * @return PostFormBuilder
     */
    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    /**
     * post string
     *
     * @return PostStringBuilder
     */
    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    /**
     * post file的封装
     *
     * @return PostFileBuilder
     */
    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }


    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailResultCallback(request, e, finalCallback);
            }

            @Override
            public void onResponse(final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(requestCall.getRequest(), new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    // work thread
                    Object o = finalCallback.parseNetworkResponse(response);
                    // ui thread
                    sendSuccessResultCallback(o, finalCallback);
                } catch (IOException e) {
                    sendFailResultCallback(response.request(), e, finalCallback);
                }

            }
        });
    }


    public void sendFailResultCallback(final Request request, final Exception e, final Callback callback) {
        if (callback == null) {
            return;
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) {
            return;
        }
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    /**
     * 取消尚未执行的网络请求
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        mOkHttpClient.cancel(tag);
    }

    /**
     * 为https配置证书
     *
     * @param certificates
     */
    public void setCertificates(InputStream... certificates) {
        HttpsUtils.setCertificates(getOkHttpClient(), certificates, null, null);
    }


}

