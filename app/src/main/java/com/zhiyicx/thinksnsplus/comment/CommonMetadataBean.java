package com.zhiyicx.thinksnsplus.comment;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/04/26/16:34
 * @Email Jliuer@aliyun.com
 * @Description 评论数据库实体类
 */
@Entity
public class CommonMetadataBean implements Serializable{

    @Transient
    private static final long serialVersionUID = 1L;

    public static final int SEND_ING = 0;
    public static final int SEND_SUCCESS = 1;
    public static final int SEND_ERROR = 2;

    @Id(autoincrement = true)
    private Long _id;

    @SerializedName("id")
    private int comment_id;// 这条评论id

    private int comment_type;

    private int source_id;// 这个资源id

    private int comment_state = SEND_SUCCESS;

    @SerializedName("reply_to_user_id")
    private int to_user;

    @SerializedName("user_id")
    private int from_user;

    @SerializedName("comment_content")
    private String comment_content;

    private String comment_url;
    private String delete_url;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("comment_mark")
    @Unique
    private Long comment_mark;// 拼接的 用户id+时间戳

    @Generated(hash = 168821524)
    public CommonMetadataBean(Long _id, int comment_id, int comment_type,
                              int source_id, int comment_state, int to_user, int from_user,
                              String comment_content, String comment_url, String delete_url,
                              String created_at, String updated_at, Long comment_mark) {
        this._id = _id;
        this.comment_id = comment_id;
        this.comment_type = comment_type;
        this.source_id = source_id;
        this.comment_state = comment_state;
        this.to_user = to_user;
        this.from_user = from_user;
        this.comment_content = comment_content;
        this.comment_url = comment_url;
        this.delete_url = delete_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.comment_mark = comment_mark;
    }

    @Generated(hash = 556510155)
    public CommonMetadataBean() {
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getComment_type() {
        return comment_type;
    }

    public void setComment_type(int comment_type) {
        this.comment_type = comment_type;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getComment_state() {
        return comment_state;
    }

    public void setComment_state(int comment_state) {
        this.comment_state = comment_state;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public int getFrom_user() {
        return from_user;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_url() {
        return comment_url;
    }

    public void setComment_url(String comment_url) {
        this.comment_url = comment_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(Long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getDelete_url() {
        return this.delete_url;
    }

    public void setDelete_url(String delete_url) {
        this.delete_url = delete_url;
    }
}
