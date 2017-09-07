package com.zhilijiqi.cordova.appupdate.task;

/**
 * Created by admin on 2017/9/7.
 */

public interface Listener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
