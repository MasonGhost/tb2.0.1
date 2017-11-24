package com.zhiyicx.zhibosdk.service;

import android.util.Base64;
import android.util.Log;

import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.di.component.DaggerUpdateComponent;
import com.zhiyicx.zhibosdk.di.module.SplashModule;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.model.SplashModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.utils.CommonUtils;
import com.zhiyicx.zhibosdk.utils.ZBDataHelper;

import java.net.URLEncoder;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/5/6.
 */
public class ZBUpdateService extends BaseService {
    @Inject
    SplashModel mSplashModel;
    @Inject
    ZBServiceManager mServiceManager;
    private Subscription mApiSubscription;
    private Subscription mConfigSubscription;
    private boolean upadteApiIssucesse;
    private boolean updateConfigSuccess;


    @Override
    public void init() {
        DaggerUpdateComponent
                .builder()
                .clientComponent(ZBSmartLiveSDK.getClientComponent())
                .splashModule(new SplashModule())
                .build()
                .inject(this);
//        updateApi();//更新api  暂时弃用
        upadteApiIssucesse = true;//弃用更新api 故先设置为true
        updateConfig();//更新配置

    }


    /**
     * 更新api域名
     */
    private void updateApi() {
        final int currentTime = (int) (System.currentTimeMillis() / 1000);
        final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
        mApiSubscription = mSplashModel.getNewDomain(ZBApi.ZHIBO_DOMAIN_VERSION, CommonUtils.MD5encode(currentTime + ZBInitConfigManager.mConfig.getToken()), hexTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<ZBBaseJson<String>>() {
                    @Override
                    public void call(ZBBaseJson<String> json) {
                        if (json.code.equals(ZBApi.REQUEST_SUCESS)) {
                            if (!ZBApi.USENOW_DOMAIN.equals(json.data)) {//如果与之前的域名不相同则保存到本地
                                ZBApiConfig config = ZBDataHelper.getDeviceData(getApplicationContext(), ZBDataHelper.CONFIG_NAME);
                                config.zhibo_domain = json.data;
                                ZBDataHelper.saveDeviceData(getApplicationContext(), ZBDataHelper.CONFIG_NAME, config);//保存到本地
                                Log.w(TAG, "update api success!");
                                upadteApiIssucesse = true;
                                if (updateConfigSuccess)
                                    stopSelf();
                            }
                            Log.w(TAG, "api not change");
                        }
                        else {
                            Log.w(TAG, "update api failure!" + json.toString());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.w(TAG, "update api failure!");
                    }
                });
    }

    /**
     * 更新配置文件
     */
    private void updateConfig() {
        final int currentTime = (int) (System.currentTimeMillis() / 1000);
        final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
        final String token = CommonUtils.MD5encode(currentTime + hexTime);//当前时间戳+16进制时间戳，MD5
        mConfigSubscription = mSplashModel.getApiVersion(hexTime, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<ZBBaseJson<String>, Observable<ZBBaseJson<ZBApiConfig>>>() {
                    @Override
                    public Observable<ZBBaseJson<ZBApiConfig>> call(ZBBaseJson<String> json) {
                        if (json.code.equals(ZBApi.REQUEST_SUCESS)) {
                            String version = ZBDataHelper.getStringSF(getApplicationContext(), ZBDataHelper.API_VERSION);//从本地拿出版本号
                            if (version == null || !version.equals(json.data)) {//为空或者,版本号不相同则更新本地版本号
                                ZBDataHelper.SetStringSF(getApplicationContext(), ZBDataHelper.API_VERSION, json.data);
                                Log.w(TAG, "api version is different");
                                return mSplashModel.getConfig(hexTime, token, "");//版本号不一致,请求服务器拉去最新的配置文件
                            }
                            Log.w(TAG, "api version is same");
                            return null;
                        }
                        else {
                            Log.w(TAG, "get api version failure!");
                            return null;
                        }

                    }
                }).subscribe(new Action1<ZBBaseJson<ZBApiConfig>>() {
                    @Override
                    public void call(ZBBaseJson<ZBApiConfig> json) {
                        if (json.code.equals(ZBApi.REQUEST_SUCESS)) {
                            ZBApiConfig config = ZBDataHelper.getDeviceData(getApplicationContext(), ZBDataHelper.CONFIG_NAME);

                            json.data.zhibo_domain = config.zhibo_domain;
                            ZBDataHelper.saveDeviceData(getApplicationContext(), ZBDataHelper.CONFIG_NAME, json.data);//保存到本地
                            String filter_wrod_version = ZBDataHelper.getStringSF(getApplicationContext(), ZBDataHelper.FILTER_WORD_VERSION);
                            if (filter_wrod_version == null || filter_wrod_version.equals(json.data.filter_word_conf.filter_word_version)) {//更新敏感词版本
                                mSplashModel.downloadFilterWord(getApplicationContext(), hexTime, token, new String(Base64.encode(URLEncoder.encode(ZBInitConfigManager.CONFIG_NAME_FILTER_WORD).getBytes(), Base64.DEFAULT)), json.data.filter_word_conf.filter_word_version, null);
                            }
                            Log.w(TAG, "update config success!");
                            updateConfigSuccess = true;
                            if (upadteApiIssucesse)
                                stopSelf();//结束Service
                        }
                        else {
                            Log.w(TAG, "update config failure!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.w(TAG, "update config failure!");
                        throwable.printStackTrace();
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mApiSubscription);
        unSubscribe(mConfigSubscription);
    }
}
