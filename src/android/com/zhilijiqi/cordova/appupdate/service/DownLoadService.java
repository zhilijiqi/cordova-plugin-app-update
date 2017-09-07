package com.zhilijiqi.cordova.appupdate.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.dfhfax.app.MainActivity;
import com.dfhfax.app.R;
import com.zhilijiqi.cordova.appupdate.task.Listener;
import com.zhilijiqi.cordova.appupdate.task.AppDownloadTask;

import java.io.File;

/**
 * Created by admin on 2017/9/7.
 */

public class DownLoadService extends Service {

    private AppDownloadTask downloadTask;

    private String downloadUrl;

    private Listener listener = new Listener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1 , getNotificaation("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            //下载成功是将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotificaation("Download Success", -1));
            Toast.makeText(DownLoadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotificaation("Download Failed", -1));
            Toast.makeText(DownLoadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownLoadService.this, "Download Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownLoadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();

        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class DownloadBinder extends Binder {

        public void startDownload(String url){
            if(downloadTask == null){
                downloadUrl = url;
                downloadTask = new AppDownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotificaation("Downloading...",0));
                Toast.makeText(DownLoadService.this, "Downloading...",Toast.LENGTH_SHORT).show();;
            }
        }
        public void pauseDownLoad(){
            if(downloadTask != null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if(downloadTask != null){
                downloadTask.cancelDownload();
            }else{
                if(downloadUrl != null){
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory,fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownLoadService.this,"Canceled",Toast.LENGTH_SHORT).show();

                }
            }

        }
    }
    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotificaation(String title,int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress > 0){
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
