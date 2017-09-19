package com.zhiyicx.zhibosdk.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/4/7.
 */
public class ZBApiFilter implements Serializable{
    public FileterInfo[] live;
    public FileterInfo[] video;


    public class FileterInfo implements Serializable{
//                type":"between",//区域筛选
//                "key":"time",//回传字段,回传时将区间用英文逗号分割
//                "name":"date",//显示(类型)风格
//                "desc":"时间",//筛选条件的中文描述
//                "value":[
//                "MM-dd HH:MM:SS",//时间的解析格式
//                ]
        public String type;//区域筛选
        public String key;//回传字段,回传时将区间用英文逗号分割
        public String name;//显示(类型)风格
        public String desc;//筛选条件的中文描述
        public String[] value;
    }


}
