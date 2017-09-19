package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * 应用验证
 * Created by jungle on 16/7/12.
 * com.zhiyicx.zhibosdk.modle
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBCheckConfig implements Serializable {
    private String appId;
    private String token;

    public ZBCheckConfig() {
    }

    public ZBCheckConfig(String appId, String token) {
        this.appId = appId;
        this.token = token;
    }

    public String getAppId() {
        if (appId == null) return "";
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {

        if (token == null) return "";
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
