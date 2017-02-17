package com.zhiyicx.thinksnsplus.config;


import retrofit2.http.DELETE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public enum BackgroundTaskRequestMethodConfig {
    POST(0),
    GET(1),
    GET_IM_INFO(2),
    GET_USER_INFO(3),
    DELETE(4);
   public final int id;

    BackgroundTaskRequestMethodConfig(int id) {
        this.id = id;
    }

}
