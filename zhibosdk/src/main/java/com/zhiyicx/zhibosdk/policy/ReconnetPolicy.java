package com.zhiyicx.zhibosdk.policy;

/**
 * Created by jess on 16/5/25.
 */
public interface ReconnetPolicy {
    //通知已断开连接
    void shutDown();
    //停止
    void stop();

    void setCallBack(ReconnectPolicyCallback callBack);

    void reconnectSuccess();

    interface ReconnectPolicyCallback {
        boolean onReConnect();
        void reConnentFailure();
        void reconnectStart();
        void reconnectEnd();
    }


}
