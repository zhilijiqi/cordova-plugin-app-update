package com.zhilijiqi.cordova.appupdate;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.zhilijiqi.cordova.appupdate.service.DownLoadService;
import com.zhilijiqi.cordova.appupdate.task.CheckVersionTask;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

/**
 * Created by admin on 2017/9/7.
 */

public class AppUpdatePlugin extends CordovaPlugin {

    private CordovaInterface cordova;
    private Context context;
    private CordovaWebView webView;


    private DownLoadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name){}
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            downloadBinder = (DownLoadService.DownloadBinder)service;
            //downloadBinder.startDownload("https://www.dfhfax.com/app/android-dev_20170907.apk");
        }
    };


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova,webView);
        this.cordova = cordova;
        this.context = cordova.getActivity();
        this.webView = webView;

        String confUrl = preferences.getString("AppUpdateUrl",null);
        checkAppVersion(confUrl);
        /*Intent intent = new Intent(context, DownLoadService.class);
        context.startService(intent);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);//绑定服务*/
    }

    private void checkAppVersion(String confUrl){
        if(TextUtils.isEmpty(confUrl)){
            return;
        }
        CheckVersionTask task = new CheckVersionTask(context);
        task.execute(confUrl);
    }

    @Override
    public void onStart() {
        super.onStart();
        //downloadBinder.startDownload("https://www.dfhfax.com/app/android-dev_20170907.apk");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //context.unbindService(connection);
    }
}
