package com.android.common.sdk.update;

public interface DownLoadListener {

    void onCancel();

    void onDone(boolean canceled, int error);

    void onPercentUpdate(int percent);
}
