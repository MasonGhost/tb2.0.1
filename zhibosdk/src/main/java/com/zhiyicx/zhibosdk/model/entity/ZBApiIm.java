package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * Created by jess on 16/5/21.
 */
public class ZBApiIm implements Serializable{
    public Integer im_uid;
    public String im_pwd;

    @Override
    public String toString() {
        return "ZBApiIm{" +
                "im_uid=" + im_uid +
                ", im_pwd='" + im_pwd + '\'' +
                '}';
    }
}
