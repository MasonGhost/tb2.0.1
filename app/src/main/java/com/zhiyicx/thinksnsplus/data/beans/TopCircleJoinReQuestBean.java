package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/12/13/9:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopCircleJoinReQuestBean extends BaseListBean {


    /**
     * id : 34
     * group_id : 3
     * user_id : 3
     * audit : 0
     * role : member
     * disabled : 0
     * created_at : 2017-12-11 03:42:33
     * updated_at : 2017-12-11 03:45:34
     * group : {"id":3,"name":"篮球圈子","user_id":2,"category_id":1,"location":"","longitude":"","latitude":"","geo_hash":"","allow_feed":0,"mode":"paid","permissions":"member,administrator,founder","money":100,"summary":"篮球圈子","notice":"篮球圈子","users_count":1,"posts_count":0,"audit":1,"created_at":"2017-11-28 09:45:17","updated_at":"2017-11-28 09:45:17","deleted_at":"","avatar":""}
     * user : {"id":3,"name":"zhangsan1","bio":"就是测试测试","sex":1,"location":"","created_at":"2017-10-23 01:17:34","updated_at":"2017-10-25 03:03:46","avatar":"http://thinksns-plus.dev/api/v2/users/3/avatar","bg":"","verified":{"type":"user","icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png","description":"我是个人认证"},"extra":null}
     */

    private Long id;
    private long group_id;
    private long user_id;
    private int audit;
    private String role;
    private int disabled;
    private String created_at;
    private String updated_at;
    private CircleInfo group;
    private UserInfoBean user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
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

    public CircleInfo getGroup() {
        return group;
    }

    public void setGroup(CircleInfo group) {
        this.group = group;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.group_id);
        dest.writeLong(this.user_id);
        dest.writeInt(this.audit);
        dest.writeString(this.role);
        dest.writeInt(this.disabled);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.group, flags);
        dest.writeParcelable(this.user, flags);
    }

    public TopCircleJoinReQuestBean() {
    }

    protected TopCircleJoinReQuestBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.group_id = in.readLong();
        this.user_id = in.readLong();
        this.audit = in.readInt();
        this.role = in.readString();
        this.disabled = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.group = in.readParcelable(CircleInfo.class.getClassLoader());
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<TopCircleJoinReQuestBean> CREATOR = new Creator<TopCircleJoinReQuestBean>() {
        @Override
        public TopCircleJoinReQuestBean createFromParcel(Parcel source) {
            return new TopCircleJoinReQuestBean(source);
        }

        @Override
        public TopCircleJoinReQuestBean[] newArray(int size) {
            return new TopCircleJoinReQuestBean[size];
        }
    };
}
