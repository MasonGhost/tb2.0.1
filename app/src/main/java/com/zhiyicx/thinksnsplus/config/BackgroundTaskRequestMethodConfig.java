package com.zhiyicx.thinksnsplus.config;


/**
 * @Describe
 * @author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public enum BackgroundTaskRequestMethodConfig {
    POST(0),
    GET(1),
    GET_IM_INFO(2),
    GET_USER_INFO(3),
    DELETE(4),
    SEND_DYNAMIC_COMMENT(6), // 其他
    SEND_INFO_COMMENT(7),// 资讯
    PATCH(8),
    SEND_DYNAMIC_V2(9),
    TOLL_DYNAMIC_COMMENT_V2(10),
    POST_V2(11),
    SEND_GROUP_DYNAMIC(12),
    SEND_GROUP_DYNAMIC_COMMENT(13),
    SEND_CIRCLE_POST_COMMENT(19),
    DELETE_V2(14),
    PUT(15),
    SEND_CERTIFICATION(16), // 发布认证申请
    SEND_ANSWER_COMMENT(17),// 评论问题答案
    SEND_QUESTION_COMMENT(18),// 评论问题
    GET_CHAT_GROUP_INFO(19);// 获取群聊信息


    public final int id;

    BackgroundTaskRequestMethodConfig(int id) {
        this.id = id;
    }

}
