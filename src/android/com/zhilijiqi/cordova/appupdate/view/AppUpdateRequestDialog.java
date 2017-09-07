package com.zhilijiqi.cordova.appupdate.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.zhilijiqi.cordova.appupdate.config.VersionXml;

public class AppUpdateRequestDialog {

    private final Context context;
    private final String title;
    private final String message;
    private final String downloadUrl;
    private final boolean cancelable;

    /**
     * Constructor.
     *
     * @param context  application context
     */
    public AppUpdateRequestDialog(Context context, VersionXml version) {
        this.context = context;
        this.message = version.getMessage();
        this.downloadUrl = version.getUrl();
        this.title = version.getName();
        this.cancelable = !version.getForce();
    }

    /**
     * Show dialog to the user.
     */
    public void show() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(message);
        if(TextUtils.isEmpty(title)) {
            dialogBuilder.setTitle("版本更新");
        }else{
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(downloadUrl));
                context.startActivity(intent);
            }
        });
        if(cancelable) {
            dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        //dialogBuilder.show();
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

}
