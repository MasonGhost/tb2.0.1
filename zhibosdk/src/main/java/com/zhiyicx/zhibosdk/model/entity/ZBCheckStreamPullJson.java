package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/3/24.
 */
public class ZBCheckStreamPullJson {
    public String code;
    public String message;
    public BackInfo data;

    public class BackInfo {
        public Share share;
        public String stream;
        public String code;
        public String message;
        public String time;
    }
    public class Share implements Serializable{

    }
}
