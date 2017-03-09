package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author LiuChao
 * @describe 动态评论的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicCommentBean implements Parcelable {
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;


    @Id(autoincrement = true)
    private Long _id;
    @Unique
    @SerializedName("id")
    private Long comment_id;// 评论的id
    private Long feed_mark;// 属于哪条动态
    @Unique
    private Long comment_mark;// 发评论的唯一标识
    private String created_at;// 评论创建的时间
    private String comment_content;// 评论内容
    private long feed_user_id; // 发动态人的 id
    private long user_id;// 谁发的这条评论
    @ToOne(joinProperty = "user_id")// DynamicCommentBean 的 user_id 作为外键
    private UserInfoBean commentUser;
    private long reply_to_user_id;// 评论要发给谁
    @ToOne(joinProperty = "reply_to_user_id")// DynamicCommentBean 的 user_id 作为外键
    private UserInfoBean replyUser;// 被评论的用户信息
    private int state = SEND_SUCCESS;// 动态发送状态 0 发送失败 1 正在发送 2 发送成功

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public Long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(Long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getFeed_user_id() {
        return feed_user_id;
    }

    public void setFeed_user_id(long feed_user_id) {
        this.feed_user_id = feed_user_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public UserInfoBean getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(UserInfoBean commentUser) {
        this.commentUser = commentUser;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    public UserInfoBean getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserInfoBean replyUser) {
        this.replyUser = replyUser;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeValue(this.comment_id);
        dest.writeValue(this.feed_mark);
        dest.writeValue(this.comment_mark);
        dest.writeString(this.created_at);
        dest.writeString(this.comment_content);
        dest.writeLong(this.feed_user_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.commentUser, flags);
        dest.writeLong(this.reply_to_user_id);
        dest.writeParcelable(this.replyUser, flags);
        dest.writeInt(this.state);
    }

    public DynamicCommentBean() {
    }

    protected DynamicCommentBean(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.comment_content = in.readString();
        this.feed_user_id = in.readLong();
        this.user_id = in.readLong();
        this.commentUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.reply_to_user_id = in.readLong();
        this.replyUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.state = in.readInt();
    }

    public static final Creator<DynamicCommentBean> CREATOR = new Creator<DynamicCommentBean>() {
        @Override
        public DynamicCommentBean createFromParcel(Parcel source) {
            return new DynamicCommentBean(source);
        }

        @Override
        public DynamicCommentBean[] newArray(int size) {
            return new DynamicCommentBean[size];
        }
    };
}
