package com.zhiyicx.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author LiuChao
 * @describe 设备相关的工具类方法
 * @date 2017/1/19
 * @contact email:450127106@qq.com
 */

public class DeviceUtils {
    /**
     * 发送邮件
     *
     * @param context
     * @param subject 主题
     * @param content 内容
     * @param emails  邮件地址
     */
    public static void sendEmail(Context context, String subject,
                                 String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            // 模拟器
            // intent.setGift_code("text/plain");
            intent.setType("message/rfc822"); // 真机
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * 判断是否存在sd卡
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static int getStatuBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources()
                    .getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }


    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 卸载软件
     *
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 当前的包是否存在
     *
     * @param context
     * @param pckName
     * @return
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TDvice", e.getMessage());
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public static void copyTextToBoard(Context context, String string) {
        if (TextUtils.isEmpty(string))
            return;
        ClipboardManager clip = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(string);
    }

    public static void openApp(Context context, String packageName) {
        Intent mainIntent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        if (mainIntent == null) {
            mainIntent = new Intent(packageName);
        } else {
        }
        context.startActivity(mainIntent);
    }

    public static String getIMEI(Context context) {
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tel.getDeviceId();
    }

    public static String getPhoneType() {
        return Build.MODEL;
    }

    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static PackageInfo getPackageInfo(Context context, String pckName) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(pckName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    /**
     * 获取指定包名应用的版本号
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            name = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 安装应用
     *
     * @param context
     * @param file
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static Intent getInstallApkIntent(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param number
     */
    public static void openDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    public static void openDail(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openSendMsg(Context context) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openCamera(Context context) {
        Intent intent = new Intent(); // 调用照相机
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setFlags(0x34c40000);
        context.startActivity(intent);
    }

    /**
     * 是否是中文语言
     * @param context
     * @return
     */
    public static boolean isZhCN(Context context) {
        String lang = context.getResources()
                .getConfiguration().locale.getCountry();
        if (lang.equalsIgnoreCase("CN")) {
            return true;
        }
        return false;
    }

    public static void showSoftKeyboard(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(4);
    }

    public static void showSoftKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
    }

    public static void toogleSoftKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 是否是横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        boolean flag;
        if (context.getResources().getConfiguration().orientation == 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /**
     * 是否是竖屏
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        boolean flag = true;
        if (context.getResources().getConfiguration().orientation != 1)
            flag = false;
        return flag;
    }
}
