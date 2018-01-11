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

    public static final int TOP_REVIEW = 0;
    public static final int TOP_SUCCESS = 1;
    public static final int TOP_REFUSE = 2;


    /**
     * id : 5
     * group_id : 56
     * user_id : 18
     * member_id : 225
     * status : 0
     * auditer : null
     * audit_at : null
     * created_at : 2017-12-22 06:21:24
     * updated_at : 2017-12-22 06:21:24
     * user : {"id":18,"name":"仨仨仨仨仨","location":null,"sex":1,"bio":"没有","created_at":"2017-04-10 02:10:22","updated_at":"2017-11-10 09:54:09","avatar":null,"bg":null,"verified":{"type":"user","icon":null,"description":"玄奘普法"},"extra":{"user_id":18,"likes_count":52,"comments_count":84,"followers_count":24,"followings_count":18,"updated_at":"2017-12-20 15:03:57","feeds_count":17,"questions_count":10,"answers_count":14,"checkin_count":2,"last_checkin_count":1,"live_zans_count":0,"live_zans_remain":0,"live_time":0}}
     * audit_user : null
     * member_info : {"id":225,"group_id":56,"user_id":18,"audit":0,"role":"member","disabled":0,"created_at":"2017-12-22 06:21:24","updated_at":"2017-12-22 06:21:24"}
     * group : {"id":56,"name":"CC收费","user_id":36,"category_id":4,"location":null,"longitude":null,"latitude":null,"geo_hash":null,"allow_feed":0,"mode":"paid","money":null,"summary":"哈哈哈哈，我是阿尔丹","notice":null,"permissions":"member,administrator,founder","users_count":1,"posts_count":0,"audit":1,"created_at":"2017-12-21 07:30:51","updated_at":"2017-12-22 06:24:32","deleted_at":null,"avatar":"http://test-plus.zhibocloud.cn/storage/group/avatars/000/000/000/56.jpeg"}
     */

    private Long id;
    private long group_id;
    private long user_id;
    private long member_id;
    private int status;
    private String auditer;
    private String audit_at;
    private String created_at;
    private String updated_at;
    private UserInfoBean user;
    private UserInfoBean audit_user;
    private CircleMembers member_info;
    private CircleInfo group;

    @Override
    public Long getMaxId() {
        return id;
    }

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

    public long getMember_id() {
        return member_id;
    }

    public void setMember_id(long member_id) {
        this.member_id = member_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuditer() {
        return auditer;
    }

    public void setAuditer(String auditer) {
        this.auditer = auditer;
    }

    public String getAudit_at() {
        return audit_at;
    }

    public void setAudit_at(String audit_at) {
        this.audit_at = audit_at;
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

    public UserInfoBean getAudit_user() {
        return audit_user;
    }

    public void setAudit_user(UserInfoBean audit_user) {
        this.audit_user = audit_user;
    }

    public CircleMembers getMember_info() {
        return member_info;
    }

    public void setMember_info(CircleMembers member_info) {
        this.member_info = member_info;
    }

    public CircleInfo getGroup() {
        return group;
    }

    public void setGroup(CircleInfo group) {
        this.group = group;
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
        dest.writeLong(this.member_id);
        dest.writeInt(this.status);
        dest.writeString(this.auditer);
        dest.writeString(this.audit_at);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.audit_user, flags);
        dest.writeParcelable(this.member_info, flags);
        dest.writeParcelable(this.group, flags);
    }

    public TopCircleJoinReQuestBean() {
    }

    protected TopCircleJoinReQuestBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.group_id = in.readLong();
        this.user_id = in.readLong();
        this.member_id = in.readLong();
        this.status = in.readInt();
        this.auditer = in.readString();
        this.audit_at = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.audit_user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.member_info = in.readParcelable(CircleMembers.class.getClassLoader());
        this.group = in.readParcelable(CircleInfo.class.getClassLoader());
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
