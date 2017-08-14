package com.zhiyicx.thinksnsplus.data.beans.qa;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Describe this bean just for topic in qa
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class QATopicBean extends BaseListBean{

    /**
     * id : 1
     * name : PHP
     * description : 我是PHP
     * questions_count : 0
     * follows_count : 0
     * has_follow : false
     * avatar : null
     */

    private int id;
    private String name;
    private String description;
    private int questions_count;
    private int follows_count;
    private boolean has_follow;
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestions_count() {
        return questions_count;
    }

    public void setQuestions_count(int questions_count) {
        this.questions_count = questions_count;
    }

    public int getFollows_count() {
        return follows_count;
    }

    public void setFollows_count(int follows_count) {
        this.follows_count = follows_count;
    }

    public boolean isHas_follow() {
        return has_follow;
    }

    public void setHas_follow(boolean has_follow) {
        this.has_follow = has_follow;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.questions_count);
        dest.writeInt(this.follows_count);
        dest.writeByte(this.has_follow ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar);
    }

    public QATopicBean() {
    }

    protected QATopicBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.questions_count = in.readInt();
        this.follows_count = in.readInt();
        this.has_follow = in.readByte() != 0;
        this.avatar = in.readString();
    }

    public static final Creator<QATopicBean> CREATOR = new Creator<QATopicBean>() {
        @Override
        public QATopicBean createFromParcel(Parcel source) {
            return new QATopicBean(source);
        }

        @Override
        public QATopicBean[] newArray(int size) {
            return new QATopicBean[size];
        }
    };
}
