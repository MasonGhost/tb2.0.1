package com.zhiyicx.thinksnsplus.config;

/**
 * @Describe 推送消息的类型，基本定义
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */

public class JpushMessageTypeConfig {
    /**
     * type 推送模块类型
     */
    public static final String JPUSH_MESSAGE_TYPE_FEED = "feed"; //  动态模块
    public static final String JPUSH_MESSAGE_TYPE_IM = "im"; //  即时聊天模块
    public static final String JPUSH_MESSAGE_TYPE_CHANNEL = "channel"; //  频道模块
    public static final String JPUSH_MESSAGE_TYPE_MUSIC = "music"; //  音乐模块
    public static final String JPUSH_MESSAGE_TYPE_NEWS = "news"; //  资讯模块

    /**
     * action 推送操作类型
     */
    public static final String JPUSH_MESSAGE_ACTION_COMMENT = "comment"; //  评论操作
    public static final String JPUSH_MESSAGE_ACTION_DIGG= "digg"; //  点赞操作
}
