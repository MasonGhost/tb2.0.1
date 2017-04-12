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
public class DigedBean {
    /**
     * id : 4
     * component : feed
     * digg_table : feed_diggs
     * digg_id : 5
     * source_table : feeds
     * source_id : 17
     * user_id : 1
     * to_user_id : 1
     * created_at : 2017-04-11 02:41:42
     * updated_at : 2017-04-11 02:41:42
     */
    @Id
    private Long id;
    private String component;
    private String digg_table;
    private int digg_id;
    private String source_table;
    private int source_id;
    private int user_id;
    private int to_user_id;
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

    public String getDigg_table() {
        return digg_table;
    }

    public void setDigg_table(String digg_table) {
        this.digg_table = digg_table;
    }

    public int getDigg_id() {
        return digg_id;
    }

    public void setDigg_id(int digg_id) {
        this.digg_id = digg_id;
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
        return "DigedBean{" +
                "id=" + id +
                ", component='" + component + '\'' +
                ", digg_table='" + digg_table + '\'' +
                ", digg_id=" + digg_id +
                ", source_table='" + source_table + '\'' +
                ", source_id=" + source_id +
                ", user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
