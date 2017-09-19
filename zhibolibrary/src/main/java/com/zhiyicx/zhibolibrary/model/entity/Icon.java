package com.zhiyicx.zhibolibrary.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jess on 16/4/12.
 */
public class Icon implements Serializable {
    @SerializedName("0")
    public String origin;

    public String getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "ZBApiIcon{" +
                "origin='" + origin + '\'' +
                '}';
    }
}
