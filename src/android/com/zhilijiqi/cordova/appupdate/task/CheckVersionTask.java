package com.zhilijiqi.cordova.appupdate.task;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhilijiqi.cordova.appupdate.config.GetConfigFromUrl;
import com.zhilijiqi.cordova.appupdate.config.VersionXml;
import com.zhilijiqi.cordova.appupdate.view.AppUpdateRequestDialog;

/**
 * Created by admin on 2017/9/7.
 */

public class CheckVersionTask extends AsyncTask<String, Integer, VersionXml>{

    private Context content;

    public CheckVersionTask(Context content){
        this.content = content;
    }
    @Override
    protected VersionXml doInBackground(String... params) {
        if(params ==null || params.length==0){
            return null;
        }
        String url = params[0];
        if(TextUtils.isEmpty(url)){
            return null;
        }
        String confData = GetConfigFromUrl.sendGetRequest(url);
        VersionXml versionXml = new VersionXml(confData);
        int versionCode = applicationVersionCode(this.content);

        if(versionCode >= versionXml.getVersion()){
            return null;
        }
        return versionXml;
    }


    @Override
    protected void onPostExecute(VersionXml version) {
        if(version != null){
            AppUpdateRequestDialog dialog = new AppUpdateRequestDialog(this.content, version);
            dialog.show();
        }else{

        }
    }

    public int applicationVersionCode(final Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
