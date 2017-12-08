package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Jliuer
 * @Date 2017/12/08/17:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopPostCommentListBean extends BaseListBean {

    public static final int TOP_REVIEW = 0;
    public static final int TOP_SUCCESS = 1;
    public static final int TOP_REFUSE = 2;
    /**
     * id : 6
     * channel : comment
     * raw : 9
     * target : 5497
     * user_id : 950
     * target_user : 18
     * amount : 10
     * day : 1
     * expires_at : null
     * status : 0
     * created_at : 2017-12-08 08:42:30
     * updated_at : 2017-12-08 08:42:30
     * comment : {"id":5497,"user_id":950,"target_user":18,"reply_user":0,"created_at":"2017-12-08 08:42:21","updated_at":"2017-12-08 08:42:21","commentable_type":"group-posts","commentable_id":9,"body":"突突突"}
     * user : {"id":950,"name":"时崎狂三藏","location":null,"sex":1,"bio":"q","created_at":"2017-10-27 13:44:16","updated_at":"2017-11-10 05:53:11","avatar":null,"bg":null,"verified":null,"extra":{"user_id":950,"likes_count":3,"comments_count":3,"followers_count":1,"followings_count":1,"updated_at":"2017-12-08 08:42:21","feeds_count":2,"questions_count":1,"answers_count":1,"checkin_count":0,"last_checkin_count":0,"live_zans_count":0,"live_zans_remain":0,"live_time":0}}
     * post : {"id":9,"group_id":2,"user_id":18,"title":"first","body":"司喜屋寿司永远是朋友        <hr><hr><hr><hr><b><i>苏坡pyro<strike>身在其中<\/strike><\/i><\/b><blockquote><b><i><strike>T恤娱乐无极限<\/strike><\/i><\/b><\/blockquote><h1><b><i><strike>有空咯了<\/strike><\/i><\/b><\/h1><hr><hr><a href=\"hfr\" class=\"editor-link\">厉害了<\/a><br><hr><br>@![image](5120)<br>","summary":"司喜屋寿司永远是朋友苏坡pyro身在其中T恤娱乐无极限有空咯了厉害了","likes_count":1,"comments_count":3,"views_count":0,"created_at":"2017-12-06 09:59:03","updated_at":"2017-12-08 08:42:21"}
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
    private CommentedBean comment;
    private UserInfoBean user;
    private CirclePostListBean post;

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public CommentedBean getComment() {
        return comment;
    }

    public void setComment(CommentedBean comment) {
        this.comment = comment;
    }

    public UserInfoBean getCommentUser() {
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
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.post, flags);
        dest.writeInt(this.state);
    }

    public TopPostCommentListBean() {
    }

    protected TopPostCommentListBean(Parcel in) {
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
        this.comment = in.readParcelable(CommentedBean.class.getClassLoader());
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.post = in.readParcelable(CirclePostListBean.class.getClassLoader());
        this.state = in.readInt();
    }

    public static final Creator<TopPostCommentListBean> CREATOR = new Creator<TopPostCommentListBean>() {
        @Override
        public TopPostCommentListBean createFromParcel(Parcel source) {
            return new TopPostCommentListBean(source);
        }

        @Override
        public TopPostCommentListBean[] newArray(int size) {
            return new TopPostCommentListBean[size];
        }
    };
}
