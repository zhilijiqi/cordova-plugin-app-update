package com.zhilijiqi.cordova.appupdate.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhilijiqi.cordova.appupdate.config.GetConfigFromUrl;
import com.zhilijiqi.cordova.appupdate.config.VersionXml;
import com.zhilijiqi.cordova.appupdate.storage.SharedPref;
import com.zhilijiqi.cordova.appupdate.view.AppUpdateRequestDialog;

/**
 * Created by Feng on 2017/9/7.
 */

public class CheckVersionTask extends AsyncTask<String, Integer, VersionXml>{

    private Context content;
    /**是否提示更新*/
    private static boolean showUpdate = true;
    private final static String IGNORE_UPDATE_NAME = "ignore_update_name";

    public CheckVersionTask(Context content){
        this.content = content;
    }
    @Override
    protected VersionXml doInBackground(String... params) {
        /**检查是否提示更新*/
        if(showUpdate){
            showUpdate = false;
        }else{
            return null;
        }
        if(params == null || params.length == 0 || TextUtils.isEmpty(params[0])){
            return null;
        }
        String url = params[0];
        String confData = GetConfigFromUrl.sendGetRequest(url);
        VersionXml versionXml = new VersionXml(confData);
        if(isShowVersionUpdate(versionXml)){
            return versionXml;
        }
        return null;
    }


    @Override
    protected void onPostExecute(VersionXml version) {
        if(version != null){
            AppUpdateRequestDialog dialog = new AppUpdateRequestDialog(this.content, version);
            dialog.show();
        }else{

        }
    }

    /**
     * 是否显示版本更新
     * @param versionXml
     * @return
     */
    public Boolean isShowVersionUpdate(final VersionXml versionXml){
        boolean result = false;
        int versionCode = applicationVersionCode(this.content);
        //是否提醒过
        SharedPref sharedPref = new SharedPref(this.content,IGNORE_UPDATE_NAME);
        int pVersion = sharedPref.getInt("version",versionCode);

        if(pVersion < versionXml.getVersion()){
            sharedPref.write(new SharedPref.SharedPrefCallable<Boolean>(){
                @Override
                public Boolean call(SharedPreferences.Editor editor) {
                    editor.putInt("version",versionXml.getVersion());
                    return null;
                }
            });
            result = true;
        }
        
        if(versionCode < versionXml.getVersion() && versionXml.getForce()){
            result = true;
        }

        return result;
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
