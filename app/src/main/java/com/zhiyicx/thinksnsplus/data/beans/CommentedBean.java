package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class CommentedBean {

    /**
     * id : 1
     * component : feed
     * comment_table : feed_comments
     * source_table : feeds
     * source_id : 17
     * comment_id : 45
     * user_id : 1
     * to_user_id : 1
     * reply_to_user_id : 0
     * created_at : 2017-04-11 02:49:02
     * updated_at : 2017-04-11 02:49:02
     */
    @Id
    private Long id;
    private String component;
    private String comment_table;
    private String source_table;
    private int source_id;
    private int comment_id;
    private int user_id;
    private int to_user_id;
    private int reply_to_user_id;
    private String created_at;
    private String updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComment_table() {
        return comment_table;
    }

    public void setComment_table(String comment_table) {
        this.comment_table = comment_table;
    }

    public String getSource_table() {
        return source_table;
    }

    public void setSource_table(String source_table) {
        this.source_table = source_table;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public int getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(int reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
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

    @Override
    public String toString() {
        return "CommentedBean{" +
                "id=" + id +
                ", component='" + component + '\'' +
                ", comment_table='" + comment_table + '\'' +
                ", source_table='" + source_table + '\'' +
                ", source_id=" + source_id +
                ", comment_id=" + comment_id +
                ", user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", reply_to_user_id=" + reply_to_user_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
