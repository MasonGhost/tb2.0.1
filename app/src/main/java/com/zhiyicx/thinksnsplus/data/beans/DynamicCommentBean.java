package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * @author LiuChao
 * @describe 动态评论的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicCommentBean extends BaseListBean implements Parcelable,Serializable {
    private static final long serialVersionUID = 536871008;
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;


    @Id(autoincrement = true)
    private Long _id;
    @Unique
    @SerializedName(value="comment_id", alternate={"id"})
    private Long comment_id;// 评论的id
    private Long feed_mark;// 属于哪条动态
    @Unique
    private Long comment_mark;// 发评论的唯一标识

    private String created_at;// 评论创建的时间
    private String updated_at;// 评论更新的时间
    @SerializedName(value="comment_content", alternate={"body"})
    private String comment_content;// 评论内容
    private String commentable_type;
    private long  commentable_id;
    @SerializedName(value="feed_user_id", alternate={"target_user"})
    private long feed_user_id; // 发动态人的 id
    private long user_id;// 谁发的这条评论
    @ToOne(joinProperty = "user_id")// DynamicCommentBean 的 user_id 作为外键
    private UserInfoBean commentUser;
    @SerializedName(value="reply_to_user_id", alternate={"reply_user"})
    private long reply_to_user_id;// 评论要发给谁
    @ToOne(joinProperty = "reply_to_user_id")// DynamicCommentBean 的 user_id 作为外键
    private UserInfoBean replyUser;// 被评论的用户信息
    private int state = SEND_SUCCESS;// 动态发送状态 0 发送失败 1 正在发送 2 发送成功
    private boolean pinned ;// 是否是被固定（置顶）的评论 1 置顶 0 不置顶
    private int is_show;//1评论成功；0评论已提交成功，等待审核

    public DynamicCommentBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getComment_id() {
        return this.comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public Long getFeed_mark() {
        return this.feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public Long getComment_mark() {
        return this.comment_mark;
    }

    public void setComment_mark(Long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComment_content() {
        return this.comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getFeed_user_id() {
        return this.feed_user_id;
    }

    public void setFeed_user_id(long feed_user_id) {
        this.feed_user_id = feed_user_id;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getReply_to_user_id() {
        return this.reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 评论的 max_id
     * @return
     */
    @Override
    public Long getMaxId() {
        return comment_id;
    }

    public boolean getPinned() {
        return this.pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }


    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCommentable_type() {
        return this.commentable_type;
    }

    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }

    public long getCommentable_id() {
        return this.commentable_id;
    }

    public void setCommentable_id(long commentable_id) {
        this.commentable_id = commentable_id;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    @Override
    public String toString() {
        return "DynamicCommentBean{" +
                "_id=" + _id +
                ", comment_id=" + comment_id +
                ", feed_mark=" + feed_mark +
                ", comment_mark=" + comment_mark +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", comment_content='" + comment_content + '\'' +
                ", commentable_type='" + commentable_type + '\'' +
                ", commentable_id=" + commentable_id +
                ", feed_user_id=" + feed_user_id +
                ", user_id=" + user_id +
                ", commentUser=" + commentUser +
                ", reply_to_user_id=" + reply_to_user_id +
                ", replyUser=" + replyUser +
                ", state=" + state +
                ", pinned=" + pinned +
                ", is_show=" + is_show +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeValue(this.comment_id);
        dest.writeValue(this.feed_mark);
        dest.writeValue(this.comment_mark);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.comment_content);
        dest.writeString(this.commentable_type);
        dest.writeLong(this.commentable_id);
        dest.writeLong(this.feed_user_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.commentUser, flags);
        dest.writeLong(this.reply_to_user_id);
        dest.writeParcelable(this.replyUser, flags);
        dest.writeInt(this.state);
        dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
        dest.writeInt(this.is_show);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 397031426)
    public UserInfoBean getCommentUser() {
        long __key = this.user_id;
        if (commentUser__resolvedKey == null || !commentUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean commentUserNew = targetDao.load(__key);
            synchronized (this) {
                commentUser = commentUserNew;
                commentUser__resolvedKey = __key;
            }
        }
        return commentUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1714457217)
    public void setCommentUser(@NotNull UserInfoBean commentUser) {
        if (commentUser == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.commentUser = commentUser;
            user_id = commentUser.getUser_id();
            commentUser__resolvedKey = user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2112537803)
    public UserInfoBean getReplyUser() {
        long __key = this.reply_to_user_id;
        if (replyUser__resolvedKey == null || !replyUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean replyUserNew = targetDao.load(__key);
            synchronized (this) {
                replyUser = replyUserNew;
                replyUser__resolvedKey = __key;
            }
        }
        return replyUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1942204408)
    public void setReplyUser(@NotNull UserInfoBean replyUser) {
        if (replyUser == null) {
            throw new DaoException(
                    "To-one property 'reply_to_user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.replyUser = replyUser;
            reply_to_user_id = replyUser.getUser_id();
            replyUser__resolvedKey = reply_to_user_id;
        }
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
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.comment_content = in.readString();
        this.commentable_type = in.readString();
        this.commentable_id = in.readLong();
        this.feed_user_id = in.readLong();
        this.user_id = in.readLong();
        this.commentUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.reply_to_user_id = in.readLong();
        this.replyUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.state = in.readInt();
        this.pinned = in.readByte() != 0;
        this.is_show = in.readInt();
    }

    @Generated(hash = 1678220513)
    public DynamicCommentBean(Long _id, Long comment_id, Long feed_mark, Long comment_mark, String created_at,
            String updated_at, String comment_content, String commentable_type, long commentable_id,
            long feed_user_id, long user_id, long reply_to_user_id, int state, boolean pinned, int is_show) {
        this._id = _id;
        this.comment_id = comment_id;
        this.feed_mark = feed_mark;
        this.comment_mark = comment_mark;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.comment_content = comment_content;
        this.commentable_type = commentable_type;
        this.commentable_id = commentable_id;
        this.feed_user_id = feed_user_id;
        this.user_id = user_id;
        this.reply_to_user_id = reply_to_user_id;
        this.state = state;
        this.pinned = pinned;
        this.is_show = is_show;
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
