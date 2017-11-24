package com.zhiyicx.zhibosdk.model.imp;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.zhibosdk.manage.listener.OnFilterWordsConfigCallback;
import com.zhiyicx.zhibosdk.model.SplashModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.service.ZBCommonService;
import com.zhiyicx.zhibosdk.model.api.service.ZBServiceManager;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;
import com.zhiyicx.zhibosdk.utils.FileUtils;
import com.zhiyicx.zhibosdk.utils.ZBDataHelper;

import java.util.Set;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public class SplashModelImpl implements SplashModel {
    private ZBCommonService mCommonService;

    public SplashModelImpl(ZBServiceManager manager) {
        this.mCommonService = manager.getCommonService();//初始化服务

    }


    @Override
    public Observable<ZBBaseJson<ZBApiConfig>> getConfig(String hextime,
                                                         String token,
                                                         String name) {
        return mCommonService.getConfig(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API, ZBApi.API_GET_CONFIG, hextime, token, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void downloadFilterWord(final Context context, String hextime, String token, String name, final String filter_word_version, final
    OnFilterWordsConfigCallback callback) {
        mCommonService.downLoadFilterWord(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API,ZBApi.API_GET_CONFIG, hextime, token, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
                        String result = null;
                        boolean isDowload = FileUtils.writeResponseBodyToDisk(responseBody, ZBDataHelper.FILTER_WORD);
                        if (isDowload) {
                            FileUtils.unZip(ZBDataHelper.FILTER_WORD, ZBDataHelper.FILTER_WORD_UNZIP);
                            ZBDataHelper.SetStringSF(context, ZBDataHelper.FILTER_WORD_VERSION, filter_word_version);//保存敏感词版本号
                            if (callback != null) {
                                result = FileUtils.readFile(ZBDataHelper.FILTER_WORD_UNZIP);
                            }
                        } else {
                            if (callback != null)
                                callback.onFail("-1", "获取敏感词失败！");
                        }
                        return result;
                    }

                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        if (callback != null) {
                            callback.onSuccess((Set<String>) new Gson().fromJson(result, new TypeToken<Set<String>>() {
                            }.getType()));
                        }
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


    /**
     * 将配置文件到内存中
     */
    @Override

    public void setConfig(ZBApiConfig config) {
        if (config != null)
            ZBApi.ZBAPICONFIG = config;

        if (config.zhibo_domain != null) {
            System.out.println("config.zhibo_domain = " + config.zhibo_domain);
            ZBApi.USENOW_DOMAIN = config.zhibo_domain;
        }

        if (config.filter_list != null) {
            ZBApi.FILTER_LIST = config.filter_list;
        }

        if (config.gift_list != null) {
            ZBApi.GIFT_LIST = config.gift_list;

        }
        if (config.webim != null)
            ZBApi.WEBIM = config.webim;
        if (config.filter_list != null)
            ZBApi.FILTERWORD = config.filter_word_conf;
    }

    @Override
    public Observable<ZBBaseJson<ZBUserAuth>> vertifyToken(String ticket) {
        return mCommonService.getUserAuthByTicket(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API,ZBApi.API_GET_TICKET, ticket);
    }

    /**
     * 获取应用验证域名
     *
     * @param appId
     * @param token
     * @param hexTime
     * @return
     */
    @Override
    public Observable<ZBBaseJson<String>> getDomain(String appId, String token, String hexTime) {
        return mCommonService.getApi(ZBApi.ZHIBO_DOMAIN + ZBApi.API_GET_API, appId, token, hexTime);
    }

    /**
     * 获取应用验证域名
     *
     * @param api_version
     * @param token
     * @param hexTime
     * @return
     */
    @Override
    public Observable<ZBBaseJson<String>> getNewDomain(String api_version, String token, String hexTime) {
        return mCommonService.getDomain(ZBApi.ZHIBO_DOMAIN+ ZBApi.BASE_API, ZBApi.API_GET_API_DOMAIN, hexTime, token, api_version);
    }

    /**
     * 查看api版本号
     *
     * @param hextime
     * @param token
     * @return
     */
    @Override
    public Observable<ZBBaseJson<String>> getApiVersion(String hextime, String token) {
        return mCommonService.getApiVersion(ZBApi.USENOW_DOMAIN + ZBApi.BASE_API,ZBApi.API_GET_APIVERSION, hextime, token);
    }
}
