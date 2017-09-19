package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/7/14.
 * com.zhiyicx.zhibosdk.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBUserAuth implements Serializable {


    private String ticket;
    private ZBApiIm im;
    private String ak;
    private String usid;

    public ZBUserAuth() {
    }

    public ZBApiIm getIm() {
        return im;
    }

    public void setIm(ZBApiIm im) {
        this.im = im;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
