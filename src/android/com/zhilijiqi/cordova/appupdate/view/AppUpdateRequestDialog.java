package com.zhilijiqi.cordova.appupdate.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;

import com.dfhfax.app.R;
import com.zhilijiqi.cordova.appupdate.config.VersionXml;
import com.zhilijiqi.cordova.appupdate.task.CheckVersionTask;

public class AppUpdateRequestDialog {

    private CheckVersionTask task;
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
    public AppUpdateRequestDialog(CheckVersionTask task, Context context, VersionXml version) {
        this.task = task;
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
            dialogBuilder.setTitle(R.string.version_update_title);
        }else{
            dialogBuilder.setTitle(title);
        }
        dialogBuilder.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(downloadUrl));
                context.startActivity(intent);*/
                task.startDownload(downloadUrl);
            }
        });
        if(cancelable) {
            dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    task.cancleDownload();
                }
            });
        }
        //dialogBuilder.show();
        AlertDialog dialog = dialogBuilder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.show();
    }

}
