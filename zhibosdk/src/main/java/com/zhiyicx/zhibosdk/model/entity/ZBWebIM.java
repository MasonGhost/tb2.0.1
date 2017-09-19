package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/8/26.
 * com.zhiyicx.zhibosdk.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBWebIM implements Serializable {

    /**
     * extranet : ws://10.165.6.22:9901/
     * intranet : ws://218.244.149.144:9901/
     */
    private String extranet;
    private String intranet;

    public String getExtranet() {
        return extranet;
    }

    public void setExtranet(String extranet) {
        this.extranet = extranet;
    }

    public String getIntranet() {
        return intranet;
    }

    public void setIntranet(String intranet) {
        this.intranet = intranet;
    }
}
