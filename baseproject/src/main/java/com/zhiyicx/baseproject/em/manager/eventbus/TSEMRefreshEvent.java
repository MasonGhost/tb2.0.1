package com.zhiyicx.baseproject.em.manager.eventbus;

import com.hyphenate.chat.EMMessage;

/**
 * @author Jliuer
 * @Date 18/02/01 13:55
 * @Email Jliuer@aliyun.com
 * @Description 刷新事件
 */
public class TSEMRefreshEvent {

    /**
     * 用户退出 群 刷新消息
     */
    public static final int TYPE_USER_EXIT = 0x01;


    private String stringExtra;

    private EMMessage message;

    private int position;

    /**
     * 刷新方式类型
     */
    private int type;

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

    public String getStringExtra() {
        return stringExtra;
    }

    public void setStringExtra(String stringExtra) {
        this.stringExtra = stringExtra;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
