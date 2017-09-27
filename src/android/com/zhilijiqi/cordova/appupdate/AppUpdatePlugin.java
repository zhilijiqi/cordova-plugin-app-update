package com.zhilijiqi.cordova.appupdate;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.dfhfax.app.R;
import com.zhilijiqi.cordova.appupdate.service.DownLoadService;
import com.zhilijiqi.cordova.appupdate.task.CheckVersionTask;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import java.io.File;

/**
 * Created by Feng on 2017/9/7.
 */

public class AppUpdatePlugin extends CordovaPlugin {

    private CordovaInterface cordova;
    private Activity activity;
    private CordovaWebView webView;
    private String conf;

    private int largeIcon;
    private int smallIcon;


    private DownLoadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name){downloadBinder = null;}
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            downloadBinder = (DownLoadService.DownloadBinder)service;
        }
    };
    private DownLoadService.DownloadCallback downloadCallback = new DownLoadService.DownloadCallback(){
        @Override
        public void callback(String filePath){
            if(filePath == null){
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            getActivity().startActivity(intent);
        }
    };


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova,webView);
        this.cordova = cordova;
        this.activity = cordova.getActivity();
        this.webView = webView;

        conf = preferences.getString("AppUpdateUrl",null);
        Intent intent = new Intent(getActivity(), DownLoadService.class);
        activity.startService(intent);
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);//绑定服务*/

       /* if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }*/
        if(cordova.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            checkAppVersion(conf);
        }else{
            cordova.requestPermission(this, 1, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        String smallIconResource = preferences.getString("NotifySmallIcon",null);
        if (smallIconResource != null) {
            smallIcon = cordova.getActivity().getResources().getIdentifier(smallIconResource, "mipmap", cordova.getActivity().getClass().getPackage().getName());
            if (smallIcon == 0) {
                smallIcon = cordova.getActivity().getResources().getIdentifier(smallIconResource, "drawable", cordova.getActivity().getPackageName());
            }
        }

        String largeIconResource = preferences.getString("NotifyLargeIcon", null);
        if (largeIconResource != null) {
            largeIcon = cordova.getActivity().getResources().getIdentifier(largeIconResource, "mipmap", cordova.getActivity().getClass().getPackage().getName());
            if (largeIcon == 0) {
                largeIcon = cordova.getActivity().getResources().getIdentifier(largeIconResource, "drawable", cordova.getActivity().getPackageName());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    private void checkAppVersion(String conf){
        if(TextUtils.isEmpty(conf)){
            return;
        }
        new CheckVersionTask(this).execute(conf);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(), R.string.permission_granted_failed, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    public Activity getActivity(){
        return (Activity)this.activity;
    }

    public void unbindService(){
        getActivity().unbindService(connection);
    }

    public void stopService(){
        unbindService();
        Intent intent = new Intent(getActivity(), DownLoadService.class);
        getActivity().stopService(intent);
    }

    public void startDownload(String url){
        if(downloadBinder != null){
            downloadBinder.startDownload(url, smallIcon, largeIcon, downloadCallback);
        }else{
            Toast.makeText(getActivity(), R.string.service_not_start, Toast.LENGTH_SHORT).show();
        }
    }
}
