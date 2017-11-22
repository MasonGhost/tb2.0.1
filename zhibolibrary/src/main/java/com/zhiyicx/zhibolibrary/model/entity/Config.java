package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class Config implements Serializable {

    public Integer sns_timespan;

    @Override
    public String toString() {
        return "Config{" +
                "sns_timespan=" + sns_timespan +
                ", exchange_type_list=" + exchange_type_list +
                '}';
    }

    public StarExchangeList exchange_type_list;
}
