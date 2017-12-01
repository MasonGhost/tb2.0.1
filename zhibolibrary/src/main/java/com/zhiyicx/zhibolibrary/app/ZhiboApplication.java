package com.zhiyicx.zhibolibrary.app;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.zhibolibrary.di.component.ClientComponent;
import com.zhiyicx.zhibolibrary.di.component.DaggerClientComponent;
import com.zhiyicx.zhibolibrary.di.module.ClientModule;
import com.zhiyicx.zhibolibrary.di.module.ServiceModule;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.PermissionData;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.SensitivewordFilter;
import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.manage.listener.OnFilterWordsConfigCallback;

import java.util.Set;

import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_CONTENT;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_TITLE;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_URL;

/**
 * 本项目由
 * dagger2
 * +mvp
 * +retrofit
 * +rxjava
 * +eventbus
 * +butterknife组成
 */
public class ZhiboApplication {
    public static final String INTENT_ACTION_UESRINFO = "com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterActivity";
    public static final String INTENT_ACTION_RECHARGE = "com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity";
    public static final String INTNET_ACTION_USERHOMEACTIVITY = "com.zhiyicx.zhibo.UserHomeActivity";
    public static final String INTENT_ACTION_GOLDEEXCHANGEACTIVITY = "com.zhiyicx.zhibo.GoldExchangeActivity";

    private static ShareContent mShareContent;

    public static ShareContent getShareContent() {
        return mShareContent;
    }

    public static void setShareContent(ShareContent shareContent) {
        mShareContent = shareContent;
    }

    static private Application mContext;
    static public ClientComponent mZhiboClientComponent;
    public static SensitivewordFilter filter;
    public static UserInfo userInfo;

    private static String sToken;

    public static String getToken() {
        return sToken;
    }

    public static void setToken(String token) {
        sToken = token;
    }

    public static UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = DataHelper.getDeviceData(DataHelper.USER_INFO, mContext);
        }
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        ZhiboApplication.userInfo = userInfo;
        DataHelper.saveDeviceData(DataHelper.USER_INFO, userInfo, mContext);
    }

    public static void init(Application application) {
        mContext = application;
        /**
         * 初始化分享数据
         */
        initShareData();
        /**
         * 推流初始化
         */
        ZBSmartLiveSDK.init(application, ZBLApi.ZHIBO_BASE_URL, ZBLApi.ZHIBO_BASE_VERSION);

    }

    /**
     * 初始化分享数据
     */
    private static void initShareData() {
        /**
         * 暂时使用本地
         */
        ShareContent shareContent = new ShareContent();
        ZhiboApplication.setShareContent(shareContent);
        shareContent.setTitle(SHARE_TITLE);
        shareContent.setContent(SHARE_CONTENT);
        shareContent.setUrl(ZBInitConfigManager.getZBCloundDomain() + SHARE_URL);
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
    public static Application getContext() {
        return mContext;
    }

    /**
     * 初始化敏感词
     */
    public static void initFilterWord() {

        ZBInitConfigManager.getFilterWords(mContext, new OnFilterWordsConfigCallback() {
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
