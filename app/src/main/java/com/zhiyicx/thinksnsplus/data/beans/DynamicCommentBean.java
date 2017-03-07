package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author LiuChao
 * @describe 动态评论的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicCommentBean implements Parcelable {
    @Id
    private Long comment_id;// 评论的id
    private Long feed_mark;// 属于哪条动态
    private String created_at;// 评论创建的时间
    private String comment_content;// 评论内容
    private long feed_user_id; // 发动态人的 id
    private long user_id;// 谁发的这条评论
    @ToOne(joinProperty = "user_id")// DynamicCommentBean 的 user_id 作为外键
    private UserInfoBean commentUser;
    private long reply_to_user_id;// 评论要发给谁
    @ToOne(joinProperty = "reply_to_user_id")// DynamicCommentBean 的 user_id 作为外键
    private UserInfoBean replyUser;// 被评论的用户信息


    public long getFeed_user_id() {
        return feed_user_id;
    }

    public void setFeed_user_id(long feed_user_id) {
        this.feed_user_id = feed_user_id;
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
    @Keep
    public UserInfoBean getReplyUser() {
        return replyUser;
    }

    @Keep
    public void setReplyUser(UserInfoBean replyUser) {
        this.replyUser = replyUser;
    }


    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    @Keep
    public UserInfoBean getCommentUser() {
        return commentUser;
    }
    @Keep
    public void setCommentUser(UserInfoBean commentUser) {
        this.commentUser = commentUser;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    public DynamicCommentBean() {
    }

    @Override
    public String toString() {
        return "DynamicCommentBean{" +
                "comment_id=" + comment_id +
                ", feed_mark=" + feed_mark +
                ", created_at=" + created_at +
                ", comment_content='" + comment_content + '\'' +
                ", feed_user_id=" + feed_user_id +
                ", user_id=" + user_id +
                ", commentUser=" + commentUser +
                ", reply_to_user_id=" + reply_to_user_id +
                ", replyUser=" + replyUser +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.comment_id);
        dest.writeValue(this.feed_mark);
        dest.writeString(this.created_at);
        dest.writeString(this.comment_content);
        dest.writeLong(this.feed_user_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.commentUser, flags);
        dest.writeLong(this.reply_to_user_id);
        dest.writeParcelable(this.replyUser, flags);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 938647494)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicCommentBeanDao() : null;
    }

    protected DynamicCommentBean(Parcel in) {
        this.comment_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.comment_content = in.readString();
        this.feed_user_id = in.readLong();
        this.user_id = in.readLong();
        this.commentUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.reply_to_user_id = in.readLong();
        this.replyUser = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Generated(hash = 1706591772)
    public DynamicCommentBean(Long comment_id, Long feed_mark, String created_at,
            String comment_content, long feed_user_id, long user_id, long reply_to_user_id) {
        this.comment_id = comment_id;
        this.feed_mark = feed_mark;
        this.created_at = created_at;
        this.comment_content = comment_content;
        this.feed_user_id = feed_user_id;
        this.user_id = user_id;
        this.reply_to_user_id = reply_to_user_id;
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
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1852910231)
    private transient DynamicCommentBeanDao myDao;
    @Generated(hash = 734177030)
    private transient Long commentUser__resolvedKey;
    @Generated(hash = 1789712289)
    private transient Long replyUser__resolvedKey;

}
