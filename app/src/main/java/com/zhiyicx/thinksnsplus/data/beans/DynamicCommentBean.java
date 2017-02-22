package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author LiuChao
 * @describe 动态评论的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicCommentBean implements Parcelable {
    @Id
    private int comment_id;// 评论的id
    private long feed_id;// 属于哪条动态
    private long create_at;// 评论创建的时间
    private String comment_content;// 评论内容
    private long user_id;// 谁发的这条屁股论
    private long reply_to_user_id;// 评论要发给谁


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.comment_id);
        dest.writeLong(this.feed_id);
        dest.writeLong(this.create_at);
        dest.writeString(this.comment_content);
        dest.writeLong(this.user_id);
        dest.writeLong(this.reply_to_user_id);
    }

    public DynamicCommentBean() {
    }

    protected DynamicCommentBean(Parcel in) {
        this.comment_id = in.readInt();
        this.feed_id = in.readLong();
        this.create_at = in.readLong();
        this.comment_content = in.readString();
        this.user_id = in.readLong();
        this.reply_to_user_id = in.readLong();
    }

    public static final Parcelable.Creator<DynamicCommentBean> CREATOR = new Parcelable.Creator<DynamicCommentBean>() {
        @Override
        public DynamicCommentBean createFromParcel(Parcel source) {
            return new DynamicCommentBean(source);
        }

        @Override
        public DynamicCommentBean[] newArray(int size) {
            return new DynamicCommentBean[size];
        }
    };

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public long getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(long feed_id) {
        this.feed_id = feed_id;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }
}
