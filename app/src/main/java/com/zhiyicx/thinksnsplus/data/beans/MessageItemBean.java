package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.imsdk.entity.Conversation;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public class MessageItemBean extends BaseListBean implements Parcelable {

    private UserInfoBean userInfo;
    private Conversation conversation; // 最后一条消息
    private int unReadMessageNums; // 未读消息数

    public UserInfoBean getUserInfo() {
        return userInfo==null?new UserInfoBean():userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public int getUnReadMessageNums() {
        return unReadMessageNums;
    }

    public void setUnReadMessageNums(int unReadMessageNums) {
        this.unReadMessageNums = unReadMessageNums;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userInfo, flags);
        dest.writeSerializable(this.conversation);
        dest.writeInt(this.unReadMessageNums);
    }

    public MessageItemBean() {
    }

    protected MessageItemBean(Parcel in) {
        this.userInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.conversation = (Conversation) in.readSerializable();
        this.unReadMessageNums = in.readInt();
    }

    public static final Creator<MessageItemBean> CREATOR = new Creator<MessageItemBean>() {
        @Override
        public MessageItemBean createFromParcel(Parcel source) {
            return new MessageItemBean(source);
        }

        @Override
        public MessageItemBean[] newArray(int size) {
            return new MessageItemBean[size];
        }
    };

    @Override
    public String toString() {
        return "MessageItemBean{" +
                "userInfo=" + userInfo +
                ", conversation=" + conversation +
                ", unReadMessageNums=" + unReadMessageNums +
                '}';
    }
}