package com.zhilijiqi.cordova.appupdate.task;

/**
 * Created by Feng on 2017/9/7.
 */

public interface Listener {

    void onProgress(int progress);

    void onSuccess(String filePath);

    void onFailed();

    void onPaused();

    void onCanceled();
}
