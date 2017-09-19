package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public class ZBEndStreamJson implements Serializable {
    public String code;
    public String message;
    public InCome data;
    public boolean isException;

    public class InCome implements Serializable {
        //        'gold':本次直播获得的金币
//        'fans_count':,本次直播获得的粉丝数
//        'zan_count':本次直播获得的赞
        public int gold;
        public int fans_count;
        public int view_count;
        public int zan_count;

        public InCome() {
        }
    }

    public ZBEndStreamJson(InCome data) {
        this.data = data;
    }

    public ZBEndStreamJson() {
        data=new InCome();
    }
}
