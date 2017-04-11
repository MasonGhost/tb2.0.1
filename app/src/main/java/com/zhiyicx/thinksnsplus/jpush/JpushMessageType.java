package com.zhiyicx.thinksnsplus.jpush;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public enum JpushMessageType {

    JPUSH_MESSAGE_TYPE_FEED("feed"),
    JPUSH_MESSAGE_TYPE_IM("im"),
    JPUSH_MESSAGE_TYPE_CHANNEL("channel"),
    JPUSH_MESSAGE_TYPE_MUSIC("music"),
    JPUSH_MESSAGE_TYPE_NEWS("news");

    public final String value;

    JpushMessageType(String value) {
        this.value = value;
    }

}
