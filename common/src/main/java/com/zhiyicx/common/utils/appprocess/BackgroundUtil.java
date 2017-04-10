package com.zhiyicx.common.utils.appprocess;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.List;

/**
 * Created by wenmingvs on 2016/1/14.
 */
public class BackgroundUtil {


    /**
     * 判断是否有用权限
     *
     * @param context 上下文参数
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean HavaPermissionForTest(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    /**
     * 通过Android自带的无障碍功能，监控窗口焦点的变化，进而拿到当前焦点窗口对应的包名
     * 必须：
     * 1. 创建ACCESSIBILITY SERVICE INFO 属性文件
     * 2. 注册 DETECTION SERVICE 到 ANDROIDMANIFEST.XML
     *
     * @param context
     * @param packageName
     * @return
     */
//    public static boolean getFromAccessibilityService(Context context, String packageName) {
//        if (DetectService.isAccessibilitySettingsOn(context) == true) {
//            DetectService detectService = DetectService.getInstance();
//            String foreground = detectService.getForegroundPackage();
//            Log.d("wenming", "**方法五** 当前窗口焦点对应的包名为： =" + foreground);
//            return packageName.equals(foreground);
//        } else {
//            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//            Toast.makeText(context, R.string.accessbiliityNo, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }

    /**
     *无意中看到乌云上有人提的一个漏洞，Linux系统内核会把process进程信息保存在/proc目录下，使用Shell命令去获取的他，再根据进程的属性判断是否为前台
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public static boolean getLinuxCoreInfo(Context context, String packageName) {

        List<AndroidAppProcess> processes = ProcessManager.getRunningForegroundApps(context);
        for (AndroidAppProcess appProcess : processes) {
            if (appProcess.getPackageName().equals(packageName) && appProcess.foreground) {
                return true;
            }
        }
        return false;

    }


}
