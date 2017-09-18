package com.zhiyicx.thinksnsplus.config;

/**
 * @Describe 推送消息的类型，基本定义  {@see  https://github.com/zhiyicx/thinksns-plus/blob/master/documents/api/v1/overview.md}
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */

public class JpushMessageTypeConfig {
    /**
     * type 推送模块类型
     */
    public static final String JPUSH_MESSAGE_TYPE_FEED_CONTENT = "feed:comment"; //  动态模块
    public static final String JPUSH_MESSAGE_TYPE_IM = "im"; //  即时聊天模块
//    public static final String JPUSH_MESSAGE_TYPE_CHANNEL = "channel"; //  频道模块
//    public static final String JPUSH_MESSAGE_TYPE_MUSIC = "music"; //  音乐模块
//    public static final String JPUSH_MESSAGE_TYPE_NEWS = "news"; //  资讯模块
//    public static final String JPUSH_MESSAGE_TYPE_USER = "user"; //  用户模块
    public static final String JPUSH_MESSAGE_TYPE_SYSTEM = "system"; //  系统通知模块

}
