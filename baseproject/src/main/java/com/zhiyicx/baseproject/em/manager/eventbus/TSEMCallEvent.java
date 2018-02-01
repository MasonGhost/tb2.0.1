package com.zhiyicx.baseproject.em.manager.eventbus;


import com.hyphenate.chat.EMCallStateChangeListener;

/**
 * @author Jliuer
 * @Date 18/02/01 9:53
 * @Email Jliuer@aliyun.com
 * @Description 通话事件类
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class TSEMCallEvent {

    /**
     * 通话错误信息
     */
    private EMCallStateChangeListener.CallError mCallError;

    /**
     * 通话状态
     */
    private EMCallStateChangeListener.CallState mCallState;


    public EMCallStateChangeListener.CallError getCallError() {
        return mCallError;
    }

    public void setCallError(EMCallStateChangeListener.CallError callError) {
        this.mCallError = callError;
    }

    public EMCallStateChangeListener.CallState getCallState() {
        return mCallState;
    }

    public void setCallState(EMCallStateChangeListener.CallState callState) {
        this.mCallState = callState;
    }


}
