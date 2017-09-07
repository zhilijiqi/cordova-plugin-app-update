package com.zhilijiqi.cordova.appupdate.task;

import android.os.AsyncTask;

/**
 * Created by admin on 2017/9/7.
 */

public class AppDownloadTask extends AsyncTask<String , Integer, Integer> {

    private Listener listerner;

    public AppDownloadTask(Listener listener){
        this.listerner = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        return null;
    }

    public void pauseDownload(){

    }

    public void cancelDownload(){}
}
