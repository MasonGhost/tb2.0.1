package com.zhiyicx.baseproject.em.manager.eventbus;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2018/02/01/12:45
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TSEMMultipleMessagesEvent {
    /**
     * 变化的消息对象
     */
    private List<EMMessage> messages;

    /**
     * 消息出现错误时的错误码
     */
    private int errorCode;

    /**
     * 消息出现错误时的错误信息
     */
    private String errorMessage;

    public List<EMMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<EMMessage> messages) {
        this.messages = messages;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
