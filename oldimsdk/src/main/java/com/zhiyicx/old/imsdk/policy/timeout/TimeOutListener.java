package com.zhiyicx.old.imsdk.policy.timeout;

import com.zhiyicx.old.imsdk.entity.MessageContainer;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.old.imsdk.policy
 * zhibo_android
 * email:335891510@qq.com
 */
public interface TimeOutListener {
    void timeOut(MessageContainer messageContainer);
}
