package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMembers extends BaseListBean {

    public static final String FOUNDER = "founder";
    public static final String ADMINISTRATOR = "administrator";
    public static final String MEMBER = "member";
    public static final String BLACKLIST = "blacklist";

    /**
     * id : 4
     * group_id : 3
     * user_id : 2
     * audit : 0
     * role : founder
     * disabled : 0
     * created_at :
     * updated_at :
     * user : {"id":2,"name":"大牛","bio":"策四","sex":2,"location":null,"created_at":"2017-10-23
     * 01:17:34","updated_at":"2017-12-01 03:46:14","avatar":"http://thinksns-plus
     * .dev/api/v2/users/2/avatar","bg":null,"verified":{"type":"user",
     * "icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png",
     * "description":"认证测试"},"extra":{"user_id":2,"likes_count":10,"comments_count":40,
     * "followers_count":3,"followings_count":5,"updated_at":"2017-11-16 08:24:44",
     * "feeds_count":25,"questions_count":16,"answers_count":16,"checkin_count":7,
     * "last_checkin_count":1}}
     */

    private Long id;
    private long group_id;
    private long user_id;
    private int audit;
    private String role;
    private int disabled;
    private String created_at;
    private String updated_at;
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
        if (disabled == 1) {
            role = CircleMembers.BLACKLIST;
        }
        return role;
    }

    public void setRole(String role) {
        if (CircleMembers.BLACKLIST.equals(role)) {
            disabled = 1;
        } else {
            disabled = 0;
        }
        this.role = role;
    }

    public int getDisabled() {
        if (disabled == 1) {
            role = CircleMembers.BLACKLIST;
        }
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
        dest.writeParcelable(this.user, flags);
    }

    public CircleMembers() {
    }

    protected CircleMembers(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.group_id = in.readLong();
        this.user_id = in.readLong();
        this.audit = in.readInt();
        this.role = in.readString();
        this.disabled = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<CircleMembers> CREATOR = new Creator<CircleMembers>() {
        @Override
        public CircleMembers createFromParcel(Parcel source) {
            return new CircleMembers(source);
        }

        @Override
        public CircleMembers[] newArray(int size) {
            return new CircleMembers[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CircleMembers members = (CircleMembers) o;

        if (group_id != members.group_id) {
            return false;
        }
        if (user_id != members.user_id) {
            return false;
        }
        return id != null ? id.equals(members.id) : members.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (group_id ^ (group_id >>> 32));
        result = 31 * result + (int) (user_id ^ (user_id >>> 32));
        return result;
    }
}
