package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/08/22/10:30
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class AnswerDraftBean extends BaseDraftBean implements Parcelable {

    private Long id;// 答案所属问题
    @Id
    private Long mark;
    private String subject;// 答案节选
    private String title;// 问题主题或者说标题
    private String body;// 答案详情 html 内容
    private String qustionTitle;// 问题标题
    private int anonymity;// 是否匿名 1 匿名 ，0 不匿名
    private String updated_at;
    private String created_at;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getQustionTitle() {
        return qustionTitle;
    }

    public void setQustionTitle(String qustionTitle) {
        this.qustionTitle = qustionTitle;
    }

    public AnswerDraftBean() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnswerDraftBean that = (AnswerDraftBean) o;

        return mark != null ? mark.equals(that.mark) : that.mark == null;
    }

    @Override
    public int hashCode() {
        return mark != null ? mark.hashCode() : 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.mark);
        dest.writeString(this.subject);
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeString(this.qustionTitle);
        dest.writeInt(this.anonymity);
        dest.writeString(this.updated_at);
        dest.writeString(this.created_at);
    }

    protected AnswerDraftBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mark = (Long) in.readValue(Long.class.getClassLoader());
        this.subject = in.readString();
        this.title = in.readString();
        this.body = in.readString();
        this.qustionTitle = in.readString();
        this.anonymity = in.readInt();
        this.updated_at = in.readString();
        this.created_at = in.readString();
    }

    @Generated(hash = 1302083162)
    public AnswerDraftBean(Long id, Long mark, String subject, String title, String body,
            String qustionTitle, int anonymity, String updated_at, String created_at) {
        this.id = id;
        this.mark = mark;
        this.subject = subject;
        this.title = title;
        this.body = body;
        this.qustionTitle = qustionTitle;
        this.anonymity = anonymity;
        this.updated_at = updated_at;
        this.created_at = created_at;
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
