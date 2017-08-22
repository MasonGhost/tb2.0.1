package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/08/22/10:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class AnswerDraftBean extends BaseDraftBean implements Parcelable {

    @Id
    private Long id;
    @Unique
    private Long mark;
    private String subject;// 问题主题或者说标题
    private String body;// 答案详情
    private int anonymity;// 是否匿名 1 匿名 ，0 不匿名


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.mark);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeInt(this.anonymity);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMark() {
        return this.mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAnonymity() {
        return this.anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public AnswerDraftBean() {
    }

    protected AnswerDraftBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mark = (Long) in.readValue(Long.class.getClassLoader());
        this.subject = in.readString();
        this.body = in.readString();
        this.anonymity = in.readInt();
    }

    @Generated(hash = 1300082588)
    public AnswerDraftBean(Long id, Long mark, String subject, String body, int anonymity) {
        this.id = id;
        this.mark = mark;
        this.subject = subject;
        this.body = body;
        this.anonymity = anonymity;
    }

    public static final Creator<AnswerDraftBean> CREATOR = new Creator<AnswerDraftBean>() {
        @Override
        public AnswerDraftBean createFromParcel(Parcel source) {
            return new AnswerDraftBean(source);
        }

        @Override
        public AnswerDraftBean[] newArray(int size) {
            return new AnswerDraftBean[size];
        }
    };
}
