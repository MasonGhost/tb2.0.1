package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Jliuer
 * @Date 2017/11/28/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleInfo extends BaseListBean {


    /**
     * id : 3
     * name : 白岩说
     * user_id : 18
     * category_id : 2
     * location : chengdu
     * longitude : 100.23
     * latitude : 180.22
     * geo_hash : 1122tym
     * allow_feed : 0
     * mode : public
     * money : 0
     * summary : 第二个圈子
     * notice :
     * users_count : 0
     * posts_count : 0
     * audit : 1
     * created_at : 2017-11-28 02:46:28
     * updated_at : 2017-11-28 02:46:28
     * joined : {"id":2,"group_id":3,"user_id":18,"audit":0,"role":"founder","disabled":0,"created_at":"2017-11-29 17:08:16","updated_at":"2017-11-29 17:08:17"}
     */

    private Long id;
    private String name;
    private int user_id;
    private int category_id;
    private String location;
    private String longitude;
    private String latitude;
    private String geo_hash;
    private int allow_feed;
    private String mode;
    private int money;
    private String summary;
    private String notice;
    private int users_count;
    private int posts_count;
    private int audit;
    private String created_at;
    private String updated_at;
    private JoinedBean joined;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGeo_hash() {
        return geo_hash;
    }

    public void setGeo_hash(String geo_hash) {
        this.geo_hash = geo_hash;
    }

    public int getAllow_feed() {
        return allow_feed;
    }

    public void setAllow_feed(int allow_feed) {
        this.allow_feed = allow_feed;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public int getPosts_count() {
        return posts_count;
    }

    public void setPosts_count(int posts_count) {
        this.posts_count = posts_count;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
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

    public JoinedBean getJoined() {
        return joined;
    }

    public void setJoined(JoinedBean joined) {
        this.joined = joined;
    }

    public static class JoinedBean {
        /**
         * id : 2
         * group_id : 3
         * user_id : 18
         * audit : 0
         * role : founder
         * disabled : 0
         * created_at : 2017-11-29 17:08:16
         * updated_at : 2017-11-29 17:08:17
         */

        private int id;
        private int group_id;
        private int user_id;
        private int audit;
        private String role;
        private int disabled;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getAudit() {
            return audit;
        }

        public void setAudit(int audit) {
            this.audit = audit;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
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
}
