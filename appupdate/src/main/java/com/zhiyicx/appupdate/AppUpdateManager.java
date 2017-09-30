package com.zhiyicx.appupdate;

import android.content.Context;
import android.content.Intent;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/9/13
 * @Contact master.jungle68@gmail.com
 */
public class AppUpdateManager {
    private volatile static AppUpdateManager ourInstance;
    private final VersionParams.Builder builder;
    private Context mContext;

    public static AppUpdateManager getInstance(Context context, String requstUrl) {
        if (ourInstance == null) {
            synchronized (AppUpdateManager.class) {
                if (ourInstance == null) {
                    ourInstance = new AppUpdateManager(context.getApplicationContext(), requstUrl);
                }
            }
        }
        return ourInstance;
    }

    private AppUpdateManager(Context context, String requstUrl) {
        mContext = context.getApplicationContext();
        builder = new VersionParams.Builder()
//                .setHttpHeaders(headers)
//                .setRequestMethod(requestMethod)
//                .setRequestParams(httpParams)
                .setRequestUrl(requstUrl)
                .setForceRedownload(true)
//                .setDownloadAPKPath(getApplicationContext().getFilesDir()+"/")
                .setService(UpdateService.class);
        CustomVersionDialogActivity.customVersionDialogIndex = 2;
        builder.setCustomDownloadActivityClass(CustomVersionDialogActivity.class);
        context.stopService(new Intent(context, UpdateService.class));
    }

    public void startVersionCheck() {
        AllenChecker.startVersionCheck(mContext, builder.build());
    }
}
