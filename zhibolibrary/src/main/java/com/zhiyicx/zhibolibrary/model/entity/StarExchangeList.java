package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by jess on 16/4/25.
 */
public class StarExchangeList implements Serializable{
    public StarExchange[] zan_list;
    public AndroidPayList pay_list;
    public StarExchange[] cash_list;

    public class StarExchange implements Serializable{
        public int zan;
        public int gold;
        public int money;
        public String product_id;
    }

    public class AndroidPayList implements Serializable{
        public StarExchange[] ios;
        public StarExchange[] android;


    }}
