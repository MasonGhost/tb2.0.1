package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class ConfigPushJson implements Serializable {
    public String name;
    public String token;
    public String hextime;

    public ConfigPushJson(String name, String token, String hextime) {
        this.name = name;
        this.token = token;
        this.hextime = hextime;
    }


    @Override
    public String toString() {
        return "ZBConfigPushJson{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", hextime='" + hextime + '\'' +
                '}';
    }
}
