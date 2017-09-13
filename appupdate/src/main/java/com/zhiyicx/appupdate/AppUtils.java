package com.zhiyicx.appupdate;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/9/13
 * @Contact master.jungle68@gmail.com
 */
public class AppUtils {

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }
}

