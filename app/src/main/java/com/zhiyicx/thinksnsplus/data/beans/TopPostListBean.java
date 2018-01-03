package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/12/22/13:05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopPostListBean extends BaseListBean {


    /**
     * id : 13
     * channel : post
     * raw : 0
     * target : 12
     * user_id : 226
     * target_user : 18
     * amount : 0
     * day : 2
     * expires_at : 2017-12-22 01:52:02
     * status : 1
     * created_at : 2017-12-20 01:52:02
     * updated_at : 2017-12-20 01:52:02
     * user : {"id":226,"name":"蓓蓓","location":"中国，四川省，成都市","sex":0,"bio":"嘿嘿嘿嘿","created_at":"2017-07-17 08:13:46","updated_at":"2017-11-23 08:33:48","avatar":"http://test-plus.zhibocloud.cn/api/v2/users/226/avatar","bg":null,"verified":{"type":"user","icon":null,"description":"测试工程师"},"extra":{"user_id":226,"likes_count":52,"comments_count":133,"followers_count":19,"followings_count":4,"updated_at":"2017-12-21 11:36:24","feeds_count":37,"questions_count":71,"answers_count":54,"checkin_count":9,"last_checkin_count":1,"live_zans_count":0,"live_zans_remain":0,"live_time":0}}
     * post : {"id":12,"group_id":3,"user_id":226,"title":"灯灯灯灯邓","body":"参差荇菜","summary":"参差荇菜","likes_count":0,"comments_count":2,"views_count":3,"created_at":"2017-12-11 03:54:11","updated_at":"2017-12-21 10:44:23","deleted_at":null}
     */

    private Long id;
    private String channel;
    private int raw;
    private long target;
    private long user_id;
    private long target_user;
    private int amount;
    private int day;
    private String expires_at;
    private int status;
    private String created_at;
    private String updated_at;
    private UserInfoBean user;
    private CirclePostListBean post;

    public UserInfoBean getCommentUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getTarget_user() {
        return target_user;
    }

    public void setTarget_user(long target_user) {
        this.target_user = target_user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public CirclePostListBean getPost() {
        return post;
    }

    public void setPost(CirclePostListBean post) {
        this.post = post;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.channel);
        dest.writeInt(this.raw);
        dest.writeLong(this.target);
        dest.writeLong(this.user_id);
        dest.writeLong(this.target_user);
        dest.writeInt(this.amount);
        dest.writeInt(this.day);
        dest.writeString(this.expires_at);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.post, flags);
    }

    public TopPostListBean() {
    }

    protected TopPostListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.channel = in.readString();
        this.raw = in.readInt();
        this.target = in.readLong();
        this.user_id = in.readLong();
        this.target_user = in.readLong();
        this.amount = in.readInt();
        this.day = in.readInt();
        this.expires_at = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.post = in.readParcelable(CirclePostListBean.class.getClassLoader());
    }

    public static final Creator<TopPostListBean> CREATOR = new Creator<TopPostListBean>() {
        @Override
        public TopPostListBean createFromParcel(Parcel source) {
            return new TopPostListBean(source);
        }

        @Override
        public TopPostListBean[] newArray(int size) {
            return new TopPostListBean[size];
        }
    };
}
