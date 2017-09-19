package com.zhiyicx.zhibolibrary.app;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.zhibolibrary.di.component.ClientComponent;
import com.zhiyicx.zhibolibrary.di.component.DaggerClientComponent;
import com.zhiyicx.zhibolibrary.di.module.ClientModule;
import com.zhiyicx.zhibolibrary.di.module.ServiceModule;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.ShareContent;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.SensitivewordFilter;
import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.manage.listener.OnFilterWordsConfigCallback;

import java.util.Set;

/**
 * 本项目由
 * dagger2
 * +mvp
 * +retrofit
 * +rxjava
 * +eventbus
 * +butterknife组成
 */
public class ZhiboApplication extends Application {
    public static final String INTNET_ACTION_USERHOMEACTIVITY ="com.zhiyicx.zhibo.UserHomeActivity";
    public static final String INTENT_ACTION_GOLDEEXCHANGEACTIVITY="com.zhiyicx.zhibo.GoldExchangeActivity";
    private static ShareContent mShareContent;

    public static ShareContent getShareContent() {
        return mShareContent;
    }

    public static void setShareContent(ShareContent shareContent) {
        mShareContent = shareContent;
    }


    static private ZhiboApplication mApplication;
    static public ClientComponent mZhiboClientComponent;
    public static String auth_accesskey;
    public static String auth_secretkey;
    public static SensitivewordFilter filter;
    public static UserInfo userInfo;

    public static UserInfo getUserInfo() {
        if (userInfo == null)
            userInfo = DataHelper.getDeviceData(DataHelper.USER_INFO, ZhiboApplication.mApplication);
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        ZhiboApplication.userInfo = userInfo;
        DataHelper.saveDeviceData(DataHelper.USER_INFO, userInfo, ZhiboApplication.mApplication);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        /**
         * 推流初始化
         */
        ZBSmartLiveSDK.init(this, ZBLApi.ZHIBO_BASE_URL,ZBLApi.ZHIBO_BASE_VERSION);
    }


    /**
     * dagger构建
     *
     * @return
     */
    public static ClientComponent getZhiboClientComponent() {
        if (mZhiboClientComponent == null) {
            mZhiboClientComponent = DaggerClientComponent
                    .builder()
                    .clientModule(new ClientModule())
                    .serviceModule(new ServiceModule())
                    .build();
        }
        return mZhiboClientComponent;
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }

    /**
     * 初始化敏感词
     */
    public void initFilterWord() {

        ZBInitConfigManager.getFilterWords(getApplicationContext(), new OnFilterWordsConfigCallback() {
            @Override
            public void onSuccess(Set<String> data) {
                filter = new SensitivewordFilter(data);
            }

            @Override()
            public void onFail(String code, String message) {
                LogUtils.warnInfo("message = " + message);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

}
