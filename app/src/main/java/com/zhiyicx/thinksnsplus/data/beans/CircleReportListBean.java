package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleReportListBean extends BaseListBean {

    public static final int TOP_REVIEW = 0;
    public static final int TOP_SUCCESS = 1;
    public static final int TOP_REFUSE = 2;
    /**
     * id : 1
     * user_id : 1
     * target_id : 1
     * group_id : 1
     * resource_id : 119
     * content : 哈哈
     * type : post
     * status : 1
     * cause :
     * handler : 2
     * created_at : 2017-11-30 10:21:52
     * updated_at : 2017-12-01 06:11:24
     * resource :
     * user : {"id":1,"name":"admin","bio":"","sex":2,"location":"四川省 巴中市 南江县","created_at":"2017-10-23 01:17:34","updated_at":"2017-11-15 07:36:17","avatar":"http://thinksns-plus.dev/api/v2/users/1/avatar","bg":null,"verified":{"type":"user","icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png","description":"1111"},"extra":{"user_id":1,"likes_count":5,"comments_count":13,"followers_count":0,"followings_count":6,"updated_at":"2017-11-30 03:04:06","feeds_count":33,"questions_count":2,"answers_count":0,"checkin_count":7,"last_checkin_count":1}}
     * target : {"id":1,"name":"admin","bio":null,"sex":2,"location":"四川省 巴中市 南江县","created_at":"2017-10-23 01:17:34","updated_at":"2017-11-15 07:36:17","avatar":"http://thinksns-plus.dev/api/v2/users/1/avatar","bg":null,"verified":{"type":"user","icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png","description":"1111"},"extra":{"user_id":1,"likes_count":5,"comments_count":13,"followers_count":0,"followings_count":6,"updated_at":"2017-11-30 03:04:06","feeds_count":33,"questions_count":2,"answers_count":0,"checkin_count":7,"last_checkin_count":1}}
     */

    private Long id;
    private long user_id;
    private long target_id;
    private long group_id;
    private long resource_id;
    private String content;
    private String type;
    private int status;
    private String cause;
    private int handler;
    private String created_at;
    private String updated_at;
    private Object resource;
    private UserInfoBean user;
    private UserInfoBean target;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getTarget_id() {
        return target_id;
    }

    public void setTarget_id(long target_id) {
        this.target_id = target_id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getResource_id() {
        return resource_id;
    }

    public void setResource_id(long resource_id) {
        this.resource_id = resource_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getHandler() {
        return handler;
    }

    public void setHandler(int handler) {
        this.handler = handler;
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

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public UserInfoBean getTarget() {
        return target;
    }

    public void setTarget(UserInfoBean target) {
        this.target = target;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.user_id);
        dest.writeLong(this.target_id);
        dest.writeLong(this.group_id);
        dest.writeLong(this.resource_id);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeInt(this.status);
        dest.writeString(this.cause);
        dest.writeInt(this.handler);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.target, flags);
    }

    public CircleReportListBean() {
    }

    protected CircleReportListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readLong();
        this.target_id = in.readLong();
        this.group_id = in.readLong();
        this.resource_id = in.readLong();
        this.content = in.readString();
        this.type = in.readString();
        this.status = in.readInt();
        this.cause = in.readString();
        this.handler = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.resource = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.target = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<CircleReportListBean> CREATOR = new Creator<CircleReportListBean>() {
        @Override
        public CircleReportListBean createFromParcel(Parcel source) {
            return new CircleReportListBean(source);
        }

        @Override
        public CircleReportListBean[] newArray(int size) {
            return new CircleReportListBean[size];
        }
    };
}
