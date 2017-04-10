package com.zhiyicx.common.utils.appprocess;

import android.content.Context;

import java.util.List;

/**
 * Created by wenmingvs on 2016/1/14.
 */
public class BackgroundUtil {

    /**
     *无意中看到乌云上有人提的一个漏洞，Linux系统内核会把process进程信息保存在/proc目录下，使用Shell命令去获取的他，再根据进程的属性判断是否为前台
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public static boolean getLinuxCoreInfoForIsForeground(Context context, String packageName) {

        List<AndroidAppProcess> processes = ProcessManager.getRunningForegroundApps(context);
        for (AndroidAppProcess appProcess : processes) {
            if (appProcess.getPackageName().equals(packageName) && appProcess.foreground) {
                return true;
            }
        }
        return false;

    }


}
