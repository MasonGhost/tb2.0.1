package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Jliuer
 * @Date 2017/11/28/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleInfo extends BaseListBean {

    /**
     * name : 白岩说
     * location : chengdu
     * longitude : 100.23
     * latitude : 180.22
     * geo_hash : 1122tym
     * summary : 第二个圈子
     * user_id : 18
     * allow_feed : 0
     * mode : public
     * money : 0
     * audit : 0
     * category_id : 2
     * updated_at : 2017-11-28 02:46:28
     * created_at : 2017-11-28 02:46:28
     * id : 3
     */

    private String name;
    private String location;
    private String longitude;
    private String latitude;
    private String geo_hash;
    private String summary;
    private long user_id;
    private int allow_feed;
    private String mode;
    private int money;
    private int audit;
    private long category_id;
    private String updated_at;
    private String created_at;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.geo_hash);
        dest.writeString(this.summary);
        dest.writeLong(this.user_id);
        dest.writeInt(this.allow_feed);
        dest.writeString(this.mode);
        dest.writeInt(this.money);
        dest.writeInt(this.audit);
        dest.writeLong(this.category_id);
        dest.writeString(this.updated_at);
        dest.writeString(this.created_at);
        dest.writeValue(this.id);
    }

    public CircleInfo() {
    }

    protected CircleInfo(Parcel in) {
        super(in);
        this.name = in.readString();
        this.location = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.geo_hash = in.readString();
        this.summary = in.readString();
        this.user_id = in.readLong();
        this.allow_feed = in.readInt();
        this.mode = in.readString();
        this.money = in.readInt();
        this.audit = in.readInt();
        this.category_id = in.readLong();
        this.updated_at = in.readString();
        this.created_at = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<CircleInfo> CREATOR = new Creator<CircleInfo>() {
        @Override
        public CircleInfo createFromParcel(Parcel source) {
            return new CircleInfo(source);
        }

        @Override
        public CircleInfo[] newArray(int size) {
            return new CircleInfo[size];
        }
    };
}
