package com.zhiyicx.baseproject.em.manager.eventbus;

import com.hyphenate.chat.EMMessage;

/**
 * @author Jliuer
 * @Date 18/02/01 13:55
 * @Email Jliuer@aliyun.com
 * @Description 自定义申请与通知 EventBus post 事件，传递事件与通知信息
 */
public class TSEMApplyForEvent {


    /**
     * 保存申请与通知的消息对象
     */
    private EMMessage message;


    public TSEMApplyForEvent() {

    }

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

}
