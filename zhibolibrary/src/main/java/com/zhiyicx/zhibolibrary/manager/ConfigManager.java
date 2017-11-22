package com.zhiyicx.zhibolibrary.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerConfigManagerComponent;
import com.zhiyicx.zhibolibrary.di.module.ConfigManagerModule;
import com.zhiyicx.zhibolibrary.manager.soupport.IConfigManager;
import com.zhiyicx.zhibolibrary.model.ConfigManagerModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.manage.listener.OnVertifyTokenCallbackListener;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/10/13.
 * com.zhiyicx.zhibolibrary.manager
 * ThinkSNS_Discovery_Android
 * email:335891510@qq.com
 */

public class ConfigManager implements IConfigManager {
    private volatile static ConfigManager sConfigManager;
    public static String DEFAULT_SHARE_IMAGE_PATH = "image_share.png";
    private Context mContext;
    @Inject
    ConfigManagerModel mConfigManagerModel;

    private ConfigManager(Context context) {
        this.mContext = context.getApplicationContext();
        DaggerConfigManagerComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .configManagerModule(new ConfigManagerModule())
                .build()
                .inject(this);

        saveDefaultShareImage();

    }

    private void saveDefaultShareImage() {
        Resources res = mContext.getResources();
        BitmapDrawable d = (BitmapDrawable) res.getDrawable(R.mipmap.pic_photo_340);
        Bitmap img = d.getBitmap();
        DEFAULT_SHARE_IMAGE_PATH = mContext.getFilesDir() + File.separator + DEFAULT_SHARE_IMAGE_PATH;
        try {
            OutputStream os = new FileOutputStream(DEFAULT_SHARE_IMAGE_PATH);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "", e);
        } finally {
            img.recycle();
        }
    }

    public static ConfigManager getInstance(Context context) {

        if (sConfigManager == null) {
            synchronized (ConfigManager.class) {
                if (sConfigManager == null) {
                    sConfigManager = new ConfigManager(context);
                }
            }
        }
        return sConfigManager;
    }

    /**
     * 票据验证
     *
     * @param rootDomain 用户信息域名
     * @param ticket     票据
     */
    @Override
    public void init(String rootDomain, final String ticket, final OnCommonCallbackListener callback) {
        //配置获取用户信息需要的域名
        ZBLApi.CONFIG_BASE_DOMAIN = rootDomain + ZBLApi.CONFIG_EXTRAL_URL;
        ZBInitConfigManager.vertifyToken(mContext,
                ticket, new OnVertifyTokenCallbackListener() {
                    @Override
                    public void onSuccess(ZBApiConfig zbApiConfig) {
                        /**
                         * 获取配置信息
                         */
                        ZBLApi.sZBApiConfig = zbApiConfig;
                        if (DataHelper.getDeviceData(DataHelper.USER_TICKET, UiUtils.getContext()) != null && DataHelper.getDeviceData(DataHelper.USER_TICKET, UiUtils.getContext()).equals(ticket) && (PermissionData[]) DataHelper.getDeviceData(DataHelper.USER_PERMISSION, UiUtils.getContext()) != null) {
                            ZhiboApplication.setPermissionDatas((PermissionData[]) DataHelper.getDeviceData(DataHelper.USER_PERMISSION, UiUtils.getContext()));
                            getUserInfoByUsid(callback);
                        } else {
                            mConfigManagerModel.getPermissionData(ticket)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io()).subscribe(new Action1<BaseJson<PermissionData[]>>() {
                                @Override
                                public void call(BaseJson<PermissionData[]> baseJson) {
                                    if (baseJson.isSuccess()) {
                                        ZhiboApplication.setPermissionDatas(baseJson.data);
                                        DataHelper.saveDeviceData(DataHelper.USER_TICKET, ticket, UiUtils.getContext());
                                        getUserInfoByUsid(callback);
                                    } else if (callback != null) {
                                        callback.onFail(baseJson.code, baseJson.message);
                                    }
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    if (callback != null) {
                                        callback.onError(throwable);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (callback != null) {
                            callback.onError(throwable);
                        }
                    }

                    @Override
                    public void onFial(String code, String message) {
                        if (callback != null) {
                            callback.onFail(code, message);
                        }
                    }
                });

    }


    /**
     * 更新自己在直播模块儿中的用户信息
     */
    @Override
    public void updateMyInfo(final OnCommonCallbackListener callback) throws IllegalAccessException {
        if (ZBInitConfigManager.getUserAuth(UiUtils.getContext()).getUsid() == null || ZhiboApplication.getPermissionDatas() == null) {
            throw new IllegalAccessException("请确保调用ConfigManager.getInstance(context).init(rootDomain,ticket,callback)调用成功!");
        }
        getUserInfoByUsid(callback);
    }

    private void getUserInfoByUsid(final OnCommonCallbackListener callback) {
        mConfigManagerModel.getUsidInfo(ZBInitConfigManager.getUserAuth(UiUtils.getContext()).getUsid(), "").observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<UserInfo[]>>() {
                    @Override
                    public void call(BaseJson<UserInfo[]> baseJson) {
                        if (baseJson.isSuccess()) {
                            ZhiboApplication.setUserInfo(baseJson.data[0]);
                            if (callback != null) {
                                callback.onSuccess();
                            }
                        } else {
                            if (callback != null) {
                                callback.onFail(baseJson.code, baseJson.message);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onError(throwable);
                        }
                    }
                });
    }

}
