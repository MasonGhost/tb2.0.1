package com.zhiyicx.zhibolibrary.manager.soupport;

import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;

/**
 * Created by jungle on 16/10/13.
 * com.zhiyicx.zhibolibrary.manager
 * ThinkSNS_Discovery_Android
 * email:335891510@qq.com
 */

public interface IConfigManager {

    /**
     * 票据验证
     * @param rootDomain
     * @param ticket
     * @param callback
     */
    void init(String rootDomain, final String ticket, final OnCommonCallbackListener callback);

    /**
     * 更新自己在直播模块儿中的用户信息
     * @param callback
     * @throws IllegalAccessException
     */
    void updateMyInfo(final OnCommonCallbackListener callback) throws IllegalAccessException;

}
