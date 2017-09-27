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
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.dfhfax.app.MainActivity;
import com.dfhfax.app.R;
import com.zhilijiqi.cordova.appupdate.task.Listener;
import com.zhilijiqi.cordova.appupdate.task.AppDownloadTask;

import java.io.File;

/**
 * Created by Feng on 2017/9/7.
 */

public class DownLoadService extends Service {

    private AppDownloadTask downloadTask;
    private DownloadCallback downloadCallback;

    private final static int NOTIFY_ID = 1;
    private String downloadUrl;
    private int largeIcon;
    private int smallIcon;

    private Listener listener = new Listener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(NOTIFY_ID, getNotification(R.string.downloading , progress));
        }

        @Override
        public void onSuccess(String filePath) {
            downloadTask = null;
            //下载成功是将前台服务通知关闭，并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(NOTIFY_ID, getNotification(R.string.download_success, -1));
            Toast.makeText(DownLoadService.this, R.string.download_success, Toast.LENGTH_SHORT).show();
            if(downloadCallback != null) {
                downloadCallback.callback(filePath);
            }
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            //下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(NOTIFY_ID, getNotification(R.string.download_failed, -1));
            Toast.makeText(DownLoadService.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownLoadService.this, R.string.download_paused, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownLoadService.this, R.string.download_canceled, Toast.LENGTH_SHORT).show();

        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder {

        public void startDownload(String url){
            startDownload(url, 0, 0, null);
        }
        public void startDownload(String url,int smallIcon,int largeIcon, DownloadCallback callback){
            if(downloadTask == null){
                downloadUrl = url;
                downloadTask = new AppDownloadTask(listener);
                downloadTask.execute(downloadUrl);
                downloadCallback = callback;
                DownLoadService.this.smallIcon = smallIcon;
                DownLoadService.this.largeIcon = largeIcon;
                startForeground(NOTIFY_ID,getNotification(R.string.downloading ,0));
                Toast.makeText(DownLoadService.this, R.string.downloading ,Toast.LENGTH_SHORT).show();;
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
                    getNotificationManager().cancel(NOTIFY_ID);
                    stopForeground(true);
                    Toast.makeText(DownLoadService.this,R.string.download_canceled,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public interface DownloadCallback {
        public void callback(String filePath);
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }


    private Notification getNotification(int resId, int progress){
        return getNotification(this.getResources().getText(resId).toString() ,progress);
    }

    private Notification getNotification(String title, int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if(smallIcon > 0){
            builder.setSmallIcon(smallIcon);
        }
        if(largeIcon > 0){
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),largeIcon));
        }

        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress > 0){
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

    /*private Intent getInstallIntent(String downloadFilePath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(downloadFilePath)), "application/vnd.android.package-archive");
        return intent;
    }*/
}
