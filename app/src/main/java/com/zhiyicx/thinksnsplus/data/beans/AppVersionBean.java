package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/9/13
 * @Contact master.jungle68@gmail.com
 */
public class AppVersionBean {

    /**
     * id : 1  更新记录自增id，可以此判断版本顺序
     * type : android 客户端的版本类型
     * version : v1.0.0  后台填写的版本
     * description : ### 安卓初始版本上线  markdown 格式的更新说明
     * link : http://127.0..0.1/ad.apk  更新包链接 ，ios 版本可能为AppStore的跳转链接
     * is_forced : 0  是否强制更新，0-非强制更新 1-强制更新
     * created_at : 2017-09-12 17:29:33  版本更新记录创建时间
     * updated_at : 2017-09-12 17:29:34
     */

    private int id;
    private String type;
    private String version;
    private String description;
    private String link;
    private int is_forced;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIs_forced() {
        return is_forced;
    }

    public void setIs_forced(int is_forced) {
        this.is_forced = is_forced;
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
}
