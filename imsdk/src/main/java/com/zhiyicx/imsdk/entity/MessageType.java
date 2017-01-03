package com.zhiyicx.imsdk.entity;

/**
 * 当用户被主播禁言后，0-127的消息是会被屏蔽掉
 * Created by jungle on 16/8/1.
 * com.zhiyicx.imsdk.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class MessageType {
    /**
     * IM文本消息提示消息
     */
    public static final int MESSAGE_TYPE_TIP = -1;
    /**
     * IM文本消息
     */
    public static final int MESSAGE_TYPE_TEXT = 0;

    /**
     * 图片类型消息
     */
    public static final int MESSAGE_TYPE_IMAGE = 1;

    /**
     * 声音类型消息
     */
    public static final int MESSAGE_TYPE_AUDIO = 2;
    /**
     * 视频类型消息
     */
    public static final int MESSAGE_TYPE_VIDEO = 3;
    /**
     * 位置类型消息
     */
    public static final int MESSAGE_TYPE_LOCATION = 4;
    /**
     * 通知类型消息
     */
    public static final int MESSAGE_TYPE_NOTIFICATION = 5;
    /**
     * 文件类型消息
     */
    public static final int MESSAGE_TYPE_FILE = 6;

    /**
     * I用户自定义消息，被禁言时会被屏蔽
     */
    public static final int MESSAGE_TYPE_CUSTOM_DISABLE = 100;

    /**
     * I用户自定义消息，被禁言时不会被屏蔽
     */
    public static final int MESSAGE_TYPE_CUSTOM_ENAABLE = 210;

    /**
     * 50000 -> 聊天室相关
     * 60000 -> 私聊相关的
     * 70000 -> 群聊
     * 给SDK的用户自定义消息时,必须设置 CUSTOM_ID = 0000 - 9999
     */
    /**
     * IM礼物消息
     */
    public static final int MESSAGE_CUSTOM_ID_GIFT = 50200;
    /**
     * 点赞的消息
     */
    public static final int MESSAGE_CUSTOM_ID_ZAN = 50100;
    public static final int MESSAGE_CUSTOM_ID_ZAN_BLUE = 200;
    public static final int MESSAGE_CUSTOM_ID_ZAN_GREEN = 201;
    public static final int MESSAGE_CUSTOM_ID_ZAN_ORANGE = 202;
    public static final int MESSAGE_CUSTOM_ID_ZAN_PURPLE = 203;
    public static final int MESSAGE_CUSTOM_ID_ZAN_RED = 204;
    public static final int MESSAGE_CUSTOM_ID_ZAN_YELLOW = 205;


    /**
     * 有人进入聊天室
     */
    public static final int MESSAGE_CUSTOM_ID_JOIN_CHATROOM = 50300;
    /**
     * 有人离开聊天室
     */
    public static final int MESSAGE_CUSTOM_ID_LEAVE_CHATROOM = 50400;

    /**
     * IM关注
     */
    public static final int MESSAGE_CUSTOM_ID_FLLOW = 50500;

    /**
     * 聊天室人数 浏览量 统计
     */
    public static final int MESSAGE_CUSTOM_ID_DATACOUNT = 50600;

    /**
     * 主播结束
     */
    public static final int MESSAGE_CUSTOM_ID_CONVERSATION_END = 50700;
    /**
     * 系统消息
     */
    public static final int MESSAGE_CUSTOM_ID_SYSTEM_TIP = 50800;
    /**
     * default 文本消息
     */

    public static final String MESSAGE_TYPE_IMAGE_TXT = "图片";
    public static final String MESSAGE_TYPE_AUDIO_TXT = "语音";
    public static final String MESSAGE_TYPE_VIDEO_TXT = "视频";
    public static final String MESSAGE_TYPE_FILE_TXT = "文件";
    public static final String MESSAGE_TYPE_LOCATION_TXT = "位置";
    public static final String MESSAGE_TYPE_NOTIFY_TXT = "通知";
    public static final String MESSAGE_TYPE_CUSTOM_TXT = "自定义";

//    public static final String MESSAGE_TYPE_ZAN_TXT = "赞";
//    public static final String MESSAGE_TYPE_GIFT_TXT = "礼物";
//    public static final String MESSAGE_TYPE_FLLOW_TXT = "关注";

}
