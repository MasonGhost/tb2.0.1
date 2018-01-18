package com.zhiyicx.thinksnsplus.data.beans;


import com.hyphenate.chat.EMConversation;
import com.zhiyicx.baseproject.cache.CacheBean;

import java.io.Serializable;

/**
 * @author Catherine
 * @describe 基于环信的bean
 * @date 2017/12/12
 * @contact email:648129313@qq.com
 */

public class MessageItemBeanV2 extends CacheBean implements Serializable {

    private static final long serialVersionUID = 4454853132357124036L;

    private UserInfoBean userInfo;
    private EMConversation conversation;
    private String emKey;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public EMConversation getConversation() {
        return conversation;
    }

    public void setConversation(EMConversation conversation) {
        this.conversation = conversation;
    }

    public String getEmKey() {
        return emKey;
    }

    public void setEmKey(String emKey) {
        this.emKey = emKey;
    }

    @Override
    public String toString() {
        return "MessageItemBeanV2{" +
                "userInfo=" + userInfo +
                ", conversation=" + conversation +
                ", emKey='" + emKey + '\'' +
                '}';
    }
}
