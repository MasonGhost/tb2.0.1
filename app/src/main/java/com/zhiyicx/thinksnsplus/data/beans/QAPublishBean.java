package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author Jliuer
 * @Date 2017/08/10/16:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QAPublishBean implements Parcelable {

    private String subject;// 问题主题或者说标题
    private String topics;// 绑定的话题
    private String invitations;// 问题邀请回答的人
    private String body;// 问题描述
    private int anonymity;// 是否匿名 1 匿名 ，0 不匿名
    private int automaticity;// 邀请悬赏自动入账，只邀请一个人的情况下，允许悬赏金额自动入账到被邀请回答者钱包中。1 自动入账 ，0 不自动入账
    private int look;// 是否开启围观，当问题有采纳或者邀请人已回答，则对外部观众自动开启围观。设置围观必须设置悬赏金额。1 开启围观 ，0 不开启围观
    private double amount;// 问题价值，悬赏金额

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject.isEmpty()){
            return;
        }
        this.subject = subject+"?";
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getInvitations() {
        return invitations;
    }

    public void setInvitations(String invitations) {
        this.invitations = invitations;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public int getAutomaticity() {
        return automaticity;
    }

    public void setAutomaticity(int automaticity) {
        this.automaticity = automaticity;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subject);
        dest.writeString(this.topics);
        dest.writeString(this.invitations);
        dest.writeString(this.body);
        dest.writeInt(this.anonymity);
        dest.writeInt(this.automaticity);
        dest.writeInt(this.look);
        dest.writeDouble(this.amount);
    }

    public QAPublishBean() {
    }

    protected QAPublishBean(Parcel in) {
        this.subject = in.readString();
        this.topics = in.readString();
        this.invitations = in.readString();
        this.body = in.readString();
        this.anonymity = in.readInt();
        this.automaticity = in.readInt();
        this.look = in.readInt();
        this.amount = in.readDouble();
    }

    public static final Creator<QAPublishBean> CREATOR = new Creator<QAPublishBean>() {
        @Override
        public QAPublishBean createFromParcel(Parcel source) {
            return new QAPublishBean(source);
        }

        @Override
        public QAPublishBean[] newArray(int size) {
            return new QAPublishBean[size];
        }
    };
}
