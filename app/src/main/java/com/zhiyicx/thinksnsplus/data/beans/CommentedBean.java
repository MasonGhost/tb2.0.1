package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * @Describe  {@see  https://github.com/zhiyicx/plus-component-feed/blob/master/documents/%E6%88%91%E6%94%B6%E5%88%B0%E7%9A%84%E8%AF%84%E8%AE%BA%E5%88%97%E8%A1%A8.md}
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class CommentedBean extends BaseListBean{

    /**
     * id : 1
     * component : feed
     * comment_table : feed_comments
     * source_table : feeds
     * source_id : 17
     * comment_id : 45
     * user_id : 1
     * to_user_id : 1
     * reply_to_user_id : 0
     * created_at : 2017-04-11 02:49:02
     * updated_at : 2017-04-11 02:49:02
     */
    @Id
    private Long id; // 数据体 id
    private String component; // 数据所属扩展包名 目前可能的参数有 feed music news
    private String comment_table; // 评论所属数据表 目前可能的参数有 feed_comments music_comments news_comments
    private String source_table; // 关联评论 id
    private long source_id; // 关联资源 id
    private long comment_id; // 关联评论 id
    private long user_id; // 评论者 id
    @ToOne(joinProperty ="user_id")
    private UserInfoBean commentUserInfo;
    private long to_user_id; // 资源作者 id
    @ToOne(joinProperty ="to_user_id")
    private UserInfoBean sourceUserInfo;
    private long reply_to_user_id;  // 被回复着 id
    @ToOne(joinProperty ="reply_to_user_id")
    private UserInfoBean replyUserInfo;
    private String created_at;
    private String updated_at;

    private int source_cover; // 封面 id
    private String source_content; // 资源描述
    private String comment_content; // 评论类容


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComment_table() {
        return comment_table;
    }

    public void setComment_table(String comment_table) {
        this.comment_table = comment_table;
    }

    public String getSource_table() {
        return source_table;
    }

    public void setSource_table(String source_table) {
        this.source_table = source_table;
    }

    public long getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(int reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
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

    public int getSource_cover() {
        return source_cover;
    }

    public void setSource_cover(int source_cover) {
        this.source_cover = source_cover;
    }

    public String getSource_content() {
        return source_content;
    }

    public void setSource_content(String source_content) {
        this.source_content = source_content;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public UserInfoBean getCommentUserInfo() {
        return commentUserInfo;
    }

    public void setCommentUserInfo(UserInfoBean commentUserInfo) {
        this.commentUserInfo = commentUserInfo;
    }

    public UserInfoBean getSourceUserInfo() {
        return sourceUserInfo;
    }

    public void setSourceUserInfo(UserInfoBean sourceUserInfo) {
        this.sourceUserInfo = sourceUserInfo;
    }

    public UserInfoBean getReplyUserInfo() {
        return replyUserInfo;
    }

    public void setReplyUserInfo(UserInfoBean replyUserInfo) {
        this.replyUserInfo = replyUserInfo;
    }

    @Override
    public String toString() {
        return "CommentedBean{" +
                "id=" + id +
                ", component='" + component + '\'' +
                ", comment_table='" + comment_table + '\'' +
                ", source_table='" + source_table + '\'' +
                ", source_id=" + source_id +
                ", comment_id=" + comment_id +
                ", user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", reply_to_user_id=" + reply_to_user_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.component);
        dest.writeString(this.comment_table);
        dest.writeString(this.source_table);
        dest.writeLong(this.source_id);
        dest.writeLong(this.comment_id);
        dest.writeLong(this.user_id);
        dest.writeLong(this.to_user_id);
        dest.writeLong(this.reply_to_user_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.source_cover);
        dest.writeString(this.source_content);
        dest.writeString(this.comment_content);
    }

    public CommentedBean() {
    }

    protected CommentedBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.component = in.readString();
        this.comment_table = in.readString();
        this.source_table = in.readString();
        this.source_id = in.readInt();
        this.comment_id = in.readInt();
        this.user_id = in.readInt();
        this.to_user_id = in.readInt();
        this.reply_to_user_id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.source_cover = in.readInt();
        this.source_content = in.readString();
        this.comment_content = in.readString();
    }

    public static final Creator<CommentedBean> CREATOR = new Creator<CommentedBean>() {
        @Override
        public CommentedBean createFromParcel(Parcel source) {
            return new CommentedBean(source);
        }

        @Override
        public CommentedBean[] newArray(int size) {
            return new CommentedBean[size];
        }
    };

    @Override
    public Long getMaxId() {
        return this.id;
    }
}
