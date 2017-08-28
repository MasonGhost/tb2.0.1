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


    private Long id;
    @Id
    private Long mark;
    private String subject;// 问题主题或者说标题
    private String body;// 答案详情
    private int anonymity;// 是否匿名 1 匿名 ，0 不匿名
    private String updated_at;
    private String created_at;

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

    public AnswerDraftBean() {
    }


    @Generated(hash = 1234838596)
    public AnswerDraftBean(Long id, Long mark, String subject, String body, int anonymity,
            String updated_at, String created_at) {
        this.id = id;
        this.mark = mark;
        this.subject = subject;
        this.body = body;
        this.anonymity = anonymity;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }
}
