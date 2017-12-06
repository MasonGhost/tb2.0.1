package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author Jliuer
 * @Date 2017/11/30/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class CirclePostCommentBean extends BaseListBean implements Serializable {
    private static final long serialVersionUID = 1234L;
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    /**
     * id : 14
     * user_id : 2
     * content : xxxxxxx
     * reply_to_user_id : 0
     * created_at : 2017-07-14 10:13:53
     * to_user_id : 2
     */

    @Id
    private Long id;
    @Unique
    @SerializedName(value = "comment_mark", alternate = {"group_post_comment_mark"})
    private Long comment_mark;
    private int circle_id;
    private int post_id;
    private long user_id;// 谁发的这条评论
    @ToOne(joinProperty = "user_id")
    private UserInfoBean commentUser;
    @SerializedName(value = "content", alternate = {"body"})
    private String content;
    private String commentable_type;
    private long commentable_id;
    @SerializedName(value = "reply_to_user_id", alternate = {"reply_user"})
    private long reply_to_user_id;// 评论要发给谁
    @ToOne(joinProperty = "reply_to_user_id")
    private UserInfoBean replyUser;
    private String created_at;
    @SerializedName(value = "to_user_id", alternate = {"target_user"})
    private long to_user_id;// 发动态人的 id
    private int state = SEND_ING;
    private boolean pinned ;// 是否是被固定

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(Long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public int getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(int circle_id) {
        this.circle_id = circle_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentable_type() {
        return commentable_type;
    }

    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }

    public long getCommentable_id() {
        return commentable_id;
    }

    public void setCommentable_id(long commentable_id) {
        this.commentable_id = commentable_id;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean getPinned() {
        return this.pinned;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.comment_mark);
        dest.writeInt(this.circle_id);
        dest.writeInt(this.post_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.commentUser, flags);
        dest.writeString(this.content);
        dest.writeString(this.commentable_type);
        dest.writeLong(this.commentable_id);
        dest.writeLong(this.reply_to_user_id);
        dest.writeParcelable(this.replyUser, flags);
        dest.writeString(this.created_at);
        dest.writeLong(this.to_user_id);
        dest.writeInt(this.state);
        dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
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

    public CirclePostCommentBean() {
    }

    protected CirclePostCommentBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.circle_id = in.readInt();
        this.post_id = in.readInt();
        this.user_id = in.readLong();
        this.commentUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.content = in.readString();
        this.commentable_type = in.readString();
        this.commentable_id = in.readLong();
        this.reply_to_user_id = in.readLong();
        this.replyUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.created_at = in.readString();
        this.to_user_id = in.readLong();
        this.state = in.readInt();
        this.pinned = in.readByte() != 0;
    }

    @Generated(hash = 2049682898)
    public CirclePostCommentBean(Long id, Long comment_mark, int circle_id, int post_id, long user_id,
            String content, String commentable_type, long commentable_id, long reply_to_user_id,
            String created_at, long to_user_id, int state, boolean pinned) {
        this.id = id;
        this.comment_mark = comment_mark;
        this.circle_id = circle_id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.commentable_type = commentable_type;
        this.commentable_id = commentable_id;
        this.reply_to_user_id = reply_to_user_id;
        this.created_at = created_at;
        this.to_user_id = to_user_id;
        this.state = state;
        this.pinned = pinned;
    }

    public static final Creator<CirclePostCommentBean> CREATOR = new Creator<CirclePostCommentBean>() {
        @Override
        public CirclePostCommentBean createFromParcel(Parcel source) {
            return new CirclePostCommentBean(source);
        }

        @Override
        public CirclePostCommentBean[] newArray(int size) {
            return new CirclePostCommentBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 408533568)
    private transient CirclePostCommentBeanDao myDao;
    @Generated(hash = 734177030)
    private transient Long commentUser__resolvedKey;
    @Generated(hash = 1789712289)
    private transient Long replyUser__resolvedKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CirclePostCommentBean that = (CirclePostCommentBean) o;

        if (circle_id != that.circle_id) {
            return false;
        }
        if (post_id != that.post_id) {
            return false;
        }
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + circle_id;
        result = 31 * result + post_id;
        return result;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 305822522)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCirclePostCommentBeanDao() : null;
    }
}
