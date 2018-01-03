package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/12/14/14:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningListBean extends BaseListBean {


    /**
     * id : 2
     * group_id : 10
     * subject : "仨仨仨仨仨 "加入圈子
     * type : 1
     * amount : 77
     * user_id : 701
     * created_at : 2017-12-14 06:48:49
     * updated_at : 2017-12-14 06:48:49
     * user : {"id":701,"name":"唐小德","location":"中国 四川省 成都市 武侯区 益州大道北段","sex":0,"bio":"好好学习，天天向上！","created_at":"2017-09-25 09:47:29","updated_at":"2017-09-25 10:20:03","avatar":null,"bg":null,"verified":null,"extra":{"user_id":701,"likes_count":5,"comments_count":188,"followers_count":3,"followings_count":2,"updated_at":"2017-12-11 09:27:45","feeds_count":3,"questions_count":12,"answers_count":2,"checkin_count":1,"last_checkin_count":1,"live_zans_count":0,"live_zans_remain":0,"live_time":0}}
     */

    private Long id;
    private long group_id;
    private String subject;
    private int type;
    private int amount;
    private long user_id;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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
        dest.writeString(this.subject);
        dest.writeInt(this.type);
        dest.writeInt(this.amount);
        dest.writeLong(this.user_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
    }

    public CircleEarningListBean() {
    }

    protected CircleEarningListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.group_id = in.readLong();
        this.subject = in.readString();
        this.type = in.readInt();
        this.amount = in.readInt();
        this.user_id = in.readLong();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<CircleEarningListBean> CREATOR = new Creator<CircleEarningListBean>() {
        @Override
        public CircleEarningListBean createFromParcel(Parcel source) {
            return new CircleEarningListBean(source);
        }

        @Override
        public CircleEarningListBean[] newArray(int size) {
            return new CircleEarningListBean[size];
        }
    };
}
