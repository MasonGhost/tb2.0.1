package com.hyphenate.easeui.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author Catherine
 * @describe 聊天专用的用户信息
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatUserInfoBean implements Parcelable{

    // 定义四种性别状态
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int SECRET = 0;

    private Long user_id;
    private String name;
    private String avatar;
    private ChatVerifiedBean verified;
    private int sex;            // 1 2 3  1男 2女 3其他

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ChatVerifiedBean getVerified() {
        return verified;
    }

    public void setVerified(ChatVerifiedBean verified) {
        this.verified = verified;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public ChatUserInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeParcelable(this.verified, flags);
    }

    protected ChatUserInfoBean(Parcel in) {
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.avatar = in.readString();
        this.verified = in.readParcelable(ChatVerifiedBean.class.getClassLoader());
    }

    public static final Creator<ChatUserInfoBean> CREATOR = new Creator<ChatUserInfoBean>() {
        @Override
        public ChatUserInfoBean createFromParcel(Parcel source) {
            return new ChatUserInfoBean(source);
        }

        @Override
        public ChatUserInfoBean[] newArray(int size) {
            return new ChatUserInfoBean[size];
        }
    };
}
