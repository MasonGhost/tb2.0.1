package com.zhiyicx.thinksnsplus.config;


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
    DELETE(4),
    SEND_DYNAMIC(5),
    SEND_COMMENT(6),
    SEND_INFO_COMMENT(7),
    PATCH(8),
    SEND_DYNAMIC_V2(9),
    TOLL_DYNAMIC_COMMENT_V2(10),
    POST_V2(11),
    SEND_GROUP_DYNAMIC(12),
    SEND_GROUP_DYNAMIC_COMMENT(13),
    DELETE_V2(14);

    public final int id;

    BackgroundTaskRequestMethodConfig(int id) {
        this.id = id;
    }

}
