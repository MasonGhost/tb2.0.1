package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.imsdk.entity.Message;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public class ChatItemBean extends CacheBean implements Parcelable {
    private UserInfoBean userInfo;
    private Message lastMessage; // 最后一条消息

    public UserInfoBean getUserInfo() {
        if (userInfo == null) {
            return new UserInfoBean();
        }
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userInfo, flags);
        dest.writeSerializable(this.lastMessage);
    }

    public ChatItemBean() {
    }

    protected ChatItemBean(Parcel in) {
        this.userInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.lastMessage = (Message) in.readSerializable();
    }

    public static final Creator<ChatItemBean> CREATOR = new Creator<ChatItemBean>() {
        @Override
        public ChatItemBean createFromParcel(Parcel source) {
            return new ChatItemBean(source);
        }

        @Override
        public ChatItemBean[] newArray(int size) {
            return new ChatItemBean[size];
        }
    };

    @Override
    public String toString() {
        return "ChatItemBean{" +
                "userInfo=" + userInfo +
                ", lastMessage=" + lastMessage +
                '}';
    }
}