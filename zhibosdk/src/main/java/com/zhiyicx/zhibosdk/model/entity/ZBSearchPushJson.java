package com.zhiyicx.zhibosdk.model.entity;

/**
 * Created by zhiyicx on 2016/4/7.
 */
public class ZBSearchPushJson {
    public String auth_accesskey;
    public String auth_secretkey;
    public String keyword;
    public String type;
    public int p;

    public ZBSearchPushJson(String auth_accessKey, String auth_secretKey, String keyword, String type, int p) {
        this.auth_accesskey = auth_accessKey;
        this.auth_secretkey = auth_secretKey;
        this.keyword = keyword;
        this.type = type;
        this.p = p;
    }
}
