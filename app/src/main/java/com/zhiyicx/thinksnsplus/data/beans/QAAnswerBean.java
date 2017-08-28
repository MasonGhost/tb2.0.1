package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Author Jliuer
 * @Date 2017/08/15/16:19
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QAAnswerBean {

    /**
     * question_id : 1
     * user_id : 1
     * body : 哈哈，可以的。
     * anonymity : 1
     * invited : false
     * updated_at : 2017-08-01 06:03:21
     * created_at : 2017-08-01 06:03:21
     * id : 3
     */

    private int question_id;
    private int user_id;
    private String body;
    private int anonymity;
    private boolean invited;
    private String updated_at;
    private String created_at;
    private int id;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
