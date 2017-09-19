package com.zhiyicx.zhibosdk.model;

import android.content.Context;

import com.zhiyicx.zhibosdk.manage.listener.OnFilterWordsConfigCallback;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;

import rx.Observable;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public interface SplashModel {
    /**
     * 验证用户合法性
     * @param ticket
     */
    Observable<ZBBaseJson<ZBUserAuth>> vertifyToken(String ticket);
    /**
     * 获取域名
     *
     * @param appId
     * @param token
     * @param basetime
     * @return
     */
    Observable<ZBBaseJson<String>> getDomain(String appId,
                                             String token,
                                             String basetime);
    Observable<ZBBaseJson<String>> getNewDomain(String api_version, String token, String hexTime);
    /**
     * 获取API版本号
     *
     * @param hextime
     * @param token
     * @return
     */
    Observable<ZBBaseJson<String>> getApiVersion(String hextime
            , String token);



    /**
     * 获得配置信息
     *
     * @return
     */
    Observable<ZBBaseJson<ZBApiConfig>> getConfig(String hextime,
                                                  String token,
                                                  String name);


    /**
     * 获得配置信息
     *
     * @return
     */
    void downloadFilterWord(Context context,String hextime,
                            String token,
                            String name, String filter_word_version, OnFilterWordsConfigCallback callback);


    /**
     * 将配置文件保存到内存中
     * @param ZBApiConfig
     */
    void setConfig(ZBApiConfig ZBApiConfig);
}
