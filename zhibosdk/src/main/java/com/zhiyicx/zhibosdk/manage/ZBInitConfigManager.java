package com.zhiyicx.zhibosdk.manage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.di.component.DaggerZBInitConfigManagerComponent;
import com.zhiyicx.zhibosdk.di.module.SplashModule;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.manage.listener.OnFilterWordsConfigCallback;
import com.zhiyicx.zhibosdk.manage.listener.OnGiftConfigCallback;
import com.zhiyicx.zhibosdk.manage.listener.OnVertifyTokenCallbackListener;
import com.zhiyicx.zhibosdk.model.SplashModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.ZBContants;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBCheckConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;
import com.zhiyicx.zhibosdk.service.ZBUpdateService;
import com.zhiyicx.zhibosdk.utils.CommonUtils;
import com.zhiyicx.zhibosdk.utils.FileUtils;
import com.zhiyicx.zhibosdk.utils.LogUtils;
import com.zhiyicx.zhibosdk.utils.ZBDataHelper;

import java.net.URLEncoder;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/7/8.
 * com.zhiyicx.zhibosdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBInitConfigManager {
    private static final String TAG = "ZBInitConfigManager";
    public static final String CONFIG_NAME_GIFT = "gift_list";
    public static final String CONFIG_NAME_FILTER_WORD = "filter_word_list";
    private static final String DATACACHE_USERAUTH = "userAuth";
    @Inject
    SplashModel mSplashModel;

    @Inject
    Context mContext;


    public static ZBCheckConfig mConfig = new ZBCheckConfig();
    private String mDomain;

    public ZBInitConfigManager() {
        DaggerZBInitConfigManagerComponent
                .builder()
                .clientComponent(ZBSmartLiveSDK.getClientComponent())
                .splashModule(new SplashModule())
                .build()
                .inject(this);
        setAppId(mContext);
    }

    /**
     * 从配置文件中获取appid和token
     *
     * @param context
     */
    private static void setAppId(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            mConfig.setAppId(appInfo.metaData.getString("ZHIBO_APPID"));
            mConfig.setToken(appInfo.metaData.getString("ZHIBO_APPTOKEN"));

        } catch (Exception e) {
            throw new IllegalArgumentException("appid,token错误");
        }
    }

    public void initConfig(Context context) {
        //没有本地配置文件说明第一次登陆、或者一个月没有使用过
        ZBApiConfig config = getApiConfig(context);//从本地取出配置文件

        if (config == null || (int) (System.currentTimeMillis() / 1000) - ZBDataHelper.getIntergerSF(context, ZBDataHelper.LAST_USE_TIME) >= 30 * 24 * 60 * 60) {
            getDomain(context, null);//请求服务器域名和配置文件
        }
        else {//有本地配置
            mSplashModel.setConfig(config);//存储配置到内存中使用
            context.getApplicationContext().startService(new Intent(context, ZBUpdateService.class));

        }
        ZBDataHelper.SetIntergerSF(context, ZBDataHelper.LAST_USE_TIME, (int) (System.currentTimeMillis() / 1000));

    }

    /**
     * 获取域名
     */
    public void getDomain(final Context context, final OnCommonCallbackListener l) {
        final int currentTime = (int) (System.currentTimeMillis() / 1000);
        final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
        final String token = CommonUtils.MD5encode(currentTime + hexTime);//当前时间戳+16进制时间戳，MD5
        mSplashModel.getNewDomain(ZBApi.ZHIBO_DOMAIN_VERSION, token, hexTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<ZBBaseJson<String>, Observable<ZBBaseJson<ZBApiConfig>>>() {

                    @Override
                    public Observable<ZBBaseJson<ZBApiConfig>> call(ZBBaseJson<String> json) {
                        if (json.code.equals(ZBApi.REQUEST_SUCESS)) {//请求成功
                            mDomain = json.data;//更新域名
                            ZBApi.USENOW_DOMAIN = mDomain;
                            System.out.println("mDomain = " + mDomain);
                            return mSplashModel.getConfig(hexTime, token, "");
                        }
                        else {//请求失败
                            LogUtils.errroInfo("erroCode:" + json.code + "," + json.message);
                            if(l!=null)
                                l.onFail(json.code, json.message);
                            return null;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBBaseJson<ZBApiConfig>>() {
                    @Override
                    public void call(ZBBaseJson<ZBApiConfig> json) {
                        if (json == null) return;
                        if (json.code.equals(ZBApi.REQUEST_SUCESS)) {
                            System.out.println("mDomain 2 = " + mDomain);
                            json.data.zhibo_domain = mDomain;//设置域名
                            ZBDataHelper.saveDeviceData(context, ZBDataHelper.CONFIG_NAME, json.data);//保存到本地
                            mSplashModel.setConfig(json.data);//保存到内存
                            downloadFilterWords(json.data.filter_word_conf.filter_word_version, context, hexTime, token, null);
                            if (l != null)
                                l.onSuccess();
                        }
                        else {//请求失败
                            LogUtils.errroInfo("erroCode:" + json.code + "," + json.message);
                            if (l != null)
                                l.onFail(json.code, json.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        //提示用户
                        LogUtils.errroInfo("初始化失败，exception error");
                        if (l != null)
                            l.onError(throwable);
                    }
                });
    }

    private void downloadFilterWords(String filter_word_version, Context context, String hexTime, String token, final OnFilterWordsConfigCallback callback) {
        //下载敏感词
        mSplashModel.downloadFilterWord(context, hexTime, token, new String(Base64.encode(URLEncoder.encode(CONFIG_NAME_FILTER_WORD).getBytes(), Base64.DEFAULT)), filter_word_version, callback);
    }

    private void getVertifyToken(final String ticket, final Context context, final OnVertifyTokenCallbackListener l) {
        if (getConfig(context) == null) {
            getDomain(context.getApplicationContext(), new OnCommonCallbackListener() {
                @Override
                public void onSuccess() {
                    requestNetVertifyToken(ticket, context, l);
                }

                @Override
                public void onError(Throwable throwable) {
                    if (l != null)
                        l.onError(throwable);
                }

                @Override
                public void onFail(String code, String message) {
                    if (l != null)
                        l.onFial(code, message);
                }
            });
        }
        else
            requestNetVertifyToken(ticket, context, l);

    }

    private void requestNetVertifyToken(final String ticket, final Context context, final OnVertifyTokenCallbackListener l) {
        mSplashModel.vertifyToken(ticket)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ZBBaseJson<ZBUserAuth>>() {
                    @Override
                    public void call(ZBBaseJson<ZBUserAuth> json) {
                        if (json.code.equals(ZBApi.REQUEST_SUCESS)) {
                            vertifySuccess(json.data);
                            json.data.setTicket(ticket);
                            ZBDataHelper.saveDeviceData(context, DATACACHE_USERAUTH, json.data);
                            if (l != null)
                                l.onSuccess(getApiConfig(context));
                        }
                        else {
                            if (l != null)
                                l.onFial(json.code, json.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (l != null)
                            l.onError(throwable);

                    }
                });
    }

    private static void vertifySuccess(ZBUserAuth userAuth) {

        ZBContants.setmUserAuth(userAuth);
        ImLogin();
    }

    /**
     * 登录Im服务
     */
    private static void ImLogin() {
        IMConfig imConfig = new IMConfig();
        imConfig.setImUid(ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret())).getIm().im_uid);
        imConfig.setToken(ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret())).getIm().im_pwd);
        if (ZBApi.WEBIM != null && ZBApi.WEBIM.size() > 0) {
            int i = CommonUtils.getRandom(0, ZBApi.WEBIM.size() - 1);
            imConfig.setWeb_socket_authority(ZBApi.WEBIM.get(i).getExtranet());
        }
        ZBIMClient.getInstance().login(imConfig);

    }


    /**
     * 校验用户信息是否合法
     */
    public static void vertifyToken(Context context, String ticket) {

        doGetVertifyToken(context, ticket, null);
    }

    /**
     * 校验用户信息是否合法
     */
    public static void vertifyToken(Context context, String ticket, OnVertifyTokenCallbackListener l) {

        doGetVertifyToken(context, ticket, l);
    }


    private static ZBApiConfig getApiConfig(Context context) {

        if (ZBApi.ZBAPICONFIG != null)
            return ZBApi.ZBAPICONFIG;
        else
            ZBApi.ZBAPICONFIG = ZBDataHelper.getDeviceData(context, ZBDataHelper.CONFIG_NAME);
        return ZBApi.ZBAPICONFIG;//保存到本地;
    }


    private static void doGetVertifyToken(Context context, String ticket, OnVertifyTokenCallbackListener l) {
        ZBUserAuth userAuth = ZBDataHelper.getDeviceData(context, DATACACHE_USERAUTH);
        if (userAuth != null && userAuth.getTicket() != null && userAuth.getTicket().equals(ticket)) {
            vertifySuccess(userAuth);
            if (getConfig(context) == null) {
                new ZBInitConfigManager()
                        .initConfig(context.getApplicationContext());
                if (l != null)
                    l.onFial("", "sdk初始化失败");
            }
            else {
                if (l != null)
                    l.onSuccess(ZBApi.ZBAPICONFIG);
            }

            return;
        }
        ZBInitConfigManager zbInitConfigManager = new ZBInitConfigManager();
        zbInitConfigManager.getVertifyToken(ticket, context, l);
    }

    /**
     * 获取验证用户信息
     *
     * @param context
     * @return
     */
    public static ZBUserAuth getUserAuth(Context context) {
        return ZBDataHelper.getDeviceData(context, DATACACHE_USERAUTH);
    }

    /**
     * 获取配置域名
     *
     * @return
     */
    public static String getZBCloundDomain() {
        return ZBApi.USENOW_DOMAIN;
    }

    /**
     * 获取礼物配置信息
     *
     * @param callback
     */
    public static void getGiftConfig(OnGiftConfigCallback callback) {
        ZBInitConfigManager zbInitConfigManager = new ZBInitConfigManager();
        zbInitConfigManager.getGiftConfigList(callback);
    }

    private void getGiftConfigList(final OnGiftConfigCallback callback) {
        final int currentTime = (int) (System.currentTimeMillis() / 1000);
        final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
        String token = CommonUtils.MD5encode(currentTime + hexTime);//当前时间戳+16进制时间戳，MD5
        mSplashModel.getConfig(hexTime, token, new String(Base64.encode(URLEncoder.encode(CONFIG_NAME_GIFT).getBytes(), Base64.DEFAULT))).subscribe(new Action1<ZBBaseJson<ZBApiConfig>>() {
            @Override
            public void call(ZBBaseJson<ZBApiConfig> zbApiConfigZBBaseJson) {
                if (zbApiConfigZBBaseJson.code.equals(ZBApi.REQUEST_SUCESS)) {
                    if (callback != null)
                        callback.onSuccess(zbApiConfigZBBaseJson.data.gift_list);
                }
                else {
                    if (callback != null)
                        callback.onFail(zbApiConfigZBBaseJson.code, zbApiConfigZBBaseJson.message);
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (callback != null)
                    callback.onError(throwable);
            }
        });


    }

    /**
     * 获取过滤配置
     *
     * @param context
     * @param callback
     */
    public static void getFilterWords(Context context, final OnFilterWordsConfigCallback callback) {

        if (!TextUtils.isEmpty(ZBDataHelper.getStringSF(context.getApplicationContext(), ZBDataHelper.FILTER_WORD_VERSION))) {//成功获取到敏感词
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<Integer, String>() {
                        @Override
                        public String call(Integer integer) {
                            return FileUtils.readFile(ZBDataHelper.FILTER_WORD_UNZIP);
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String result) {
                            LogUtils.debugInfo(TAG, result);
                            Set<String> zbFilterWords = new Gson().fromJson(result, new TypeToken<Set<String>>() {
                            }.getType());
                            if (callback != null)
                                callback.onSuccess(zbFilterWords);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            if (callback != null)
                                callback.onError(throwable);
                        }
                    });

        }
        else {
            if (ZBApi.FILTERWORD != null && ZBApi.FILTERWORD.filter_word_version != null) {//sdk初始化时获取敏感词失败
                ZBInitConfigManager zbInitConfigManager = new ZBInitConfigManager();
                final int currentTime = (int) (System.currentTimeMillis() / 1000);
                final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
                final String token = CommonUtils.MD5encode(currentTime + hexTime);//当前时间戳+16进制时间戳，MD5
                zbInitConfigManager.downloadFilterWords(ZBApi.FILTERWORD.filter_word_version, context.getApplicationContext(), hexTime, token, callback);
            }
            else
                throw new IllegalAccessError("sdk初始化失败");
        }

    }

    /**
     * 获取配置信息，不包含礼物配置和敏感词词库
     */
    public static ZBApiConfig getConfig(Context context) {
        return getApiConfig(context);
    }

}
