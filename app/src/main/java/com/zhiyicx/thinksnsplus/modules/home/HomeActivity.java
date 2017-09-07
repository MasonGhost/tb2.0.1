package com.zhiyicx.thinksnsplus.modules.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.BuildConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/22
 * @Contact master.jungle68@gmail.com
 */

public class HomeActivity extends TSActivity {
    public static final String BUNDLE_JPUSH_MESSAGE = "jpush_message";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            JpushMessageBean jpushMessageBean = bundle.getParcelable(BUNDLE_JPUSH_MESSAGE);
            if (jpushMessageBean != null&& jpushMessageBean.getType()!=null) {
                switch (jpushMessageBean.getType()) {
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_SYSTEM:
                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MINE);
                        break;
                    default:
                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MESSAGE);
                }

            }
        }
    }

    @Override
    protected void componentInject() {
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG, "getDeviceInfo = " + getDeviceInfo(getApplicationContext()));
        }
    }

    @Override
    protected Fragment getFragment() {
        return HomeFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.goHome(this);
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    fstream.close();
                    if (in != null) {
                        in.close();
                    }

                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
