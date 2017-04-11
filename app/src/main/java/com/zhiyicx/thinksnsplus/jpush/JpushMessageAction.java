package com.zhiyicx.thinksnsplus.jpush;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public enum JpushMessageAction {

    JPUSH_MESSAGE_ACTION_COMMENT("comment"),
    JPUSH_MESSAGE_ACTION_DIGG("digg");

    public String value;

    JpushMessageAction(String value) {
        this.value = value;
    }

}
