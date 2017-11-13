package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class InfoCommentListBean extends BaseListBean {
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    /**
     * id : 3
     * created_at : 2017-03-13 16:35:33
     * comment_content : 爱我的
     * user_id : 1
     * reply_to_user_id : 0
     */
    @Id
    private Long id;
    @SerializedName(value = "info_id",alternate = {"commentable_id"})
    private long info_id = -1;// 用于记录隶属于哪一条资讯。
    private String created_at;
    @SerializedName(value = "comment_content",alternate = {"body"})
    private String comment_content;
    private long user_id;
    @SerializedName(value = "reply_to_user_id",alternate = {"reply_user"})
    private long reply_to_user_id;
    @Unique
    private long comment_mark;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean fromUserInfoBean;
    @ToOne(joinProperty = "reply_to_user_id")
    private UserInfoBean toUserInfoBean;
    private long target_user;
    @ToOne(joinProperty = "target_user")
    private UserInfoBean publishUserInfoBean;
    private String commentable_type;
    private int state = SEND_SUCCESS;
    private boolean pinned ;// 是否是被固定（置顶）的评论 1 置顶 0 不置顶

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCommentable_type() {
        return commentable_type;
    }

    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public Long getMaxId() {
        if(id==null){
            return 0L;
        }
        return id;
    }

    @Override
    public String toString() {
        return "InfoCommentListBean{" +
                "id=" + id +
                ", info_id=" + info_id +
                ", created_at='" + created_at + '\'' +
                ", comment_content='" + comment_content + '\'' +
                ", user_id=" + user_id +
                ", reply_to_user_id=" + reply_to_user_id +
                ", comment_mark=" + comment_mark +
                ", fromUserInfoBean=" + fromUserInfoBean +
                ", toUserInfoBean=" + toUserInfoBean +
                ", target_user=" + target_user +
                ", publishUserInfoBean=" + publishUserInfoBean +
                ", commentable_type='" + commentable_type + '\'' +
                ", pinned='" + pinned + '\'' +
                '}';
    }

    public InfoCommentListBean() {
    }


    public long getInfo_id() {
        return this.info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }



    public long getTarget_user() {
        return this.target_user;
    }

    public void setTarget_user(long target_user) {
        this.target_user = target_user;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.info_id);
        dest.writeString(this.created_at);
        dest.writeString(this.comment_content);
        dest.writeLong(this.user_id);
        dest.writeLong(this.reply_to_user_id);
        dest.writeLong(this.comment_mark);
        dest.writeParcelable(this.fromUserInfoBean, flags);
        dest.writeParcelable(this.toUserInfoBean, flags);
        dest.writeLong(this.target_user);
        dest.writeParcelable(this.publishUserInfoBean, flags);
        dest.writeString(this.commentable_type);
        dest.writeInt(this.state);
        dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
    }

    public boolean getPinned() {
        return this.pinned;
    }

    public void setInfo_id(long info_id) {
        this.info_id = info_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 662071464)
    public UserInfoBean getFromUserInfoBean() {
        long __key = this.user_id;
        if (fromUserInfoBean__resolvedKey == null || !fromUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean fromUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                fromUserInfoBean = fromUserInfoBeanNew;
                fromUserInfoBean__resolvedKey = __key;
            }
        }
        return fromUserInfoBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1773681647)
    public void setFromUserInfoBean(@NotNull UserInfoBean fromUserInfoBean) {
        if (fromUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.fromUserInfoBean = fromUserInfoBean;
            user_id = fromUserInfoBean.getUser_id();
            fromUserInfoBean__resolvedKey = user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2139866802)
    public UserInfoBean getToUserInfoBean() {
        long __key = this.reply_to_user_id;
        if (toUserInfoBean__resolvedKey == null || !toUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean toUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                toUserInfoBean = toUserInfoBeanNew;
                toUserInfoBean__resolvedKey = __key;
            }
        }
        return toUserInfoBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1445632445)
    public void setToUserInfoBean(@NotNull UserInfoBean toUserInfoBean) {
        if (toUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'reply_to_user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.toUserInfoBean = toUserInfoBean;
            reply_to_user_id = toUserInfoBean.getUser_id();
            toUserInfoBean__resolvedKey = reply_to_user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1683320735)
    public UserInfoBean getPublishUserInfoBean() {
        long __key = this.target_user;
        if (publishUserInfoBean__resolvedKey == null
                || !publishUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean publishUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                publishUserInfoBean = publishUserInfoBeanNew;
                publishUserInfoBean__resolvedKey = __key;
            }
        }
        return publishUserInfoBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 781188919)
    public void setPublishUserInfoBean(@NotNull UserInfoBean publishUserInfoBean) {
        if (publishUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'target_user' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.publishUserInfoBean = publishUserInfoBean;
            target_user = publishUserInfoBean.getUser_id();
            publishUserInfoBean__resolvedKey = target_user;
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
    @Generated(hash = 841333318)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoCommentListBeanDao() : null;
    }

    protected InfoCommentListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.info_id = in.readLong();
        this.created_at = in.readString();
        this.comment_content = in.readString();
        this.user_id = in.readLong();
        this.reply_to_user_id = in.readLong();
        this.comment_mark = in.readLong();
        this.fromUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.toUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.target_user = in.readLong();
        this.publishUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.commentable_type = in.readString();
        this.state = in.readInt();
        this.pinned = in.readByte() != 0;
    }

    @Generated(hash = 2113544214)
    public InfoCommentListBean(Long id, long info_id, String created_at, String comment_content,
            long user_id, long reply_to_user_id, long comment_mark, long target_user,
            String commentable_type, int state, boolean pinned) {
        this.id = id;
        this.info_id = info_id;
        this.created_at = created_at;
        this.comment_content = comment_content;
        this.user_id = user_id;
        this.reply_to_user_id = reply_to_user_id;
        this.comment_mark = comment_mark;
        this.target_user = target_user;
        this.commentable_type = commentable_type;
        this.state = state;
        this.pinned = pinned;
    }

    public static final Creator<InfoCommentListBean> CREATOR = new Creator<InfoCommentListBean>() {
        @Override
        public InfoCommentListBean createFromParcel(Parcel source) {
            return new InfoCommentListBean(source);
        }

        @Override
        public InfoCommentListBean[] newArray(int size) {
            return new InfoCommentListBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 786979885)
    private transient InfoCommentListBeanDao myDao;
    @Generated(hash = 262226026)
    private transient Long fromUserInfoBean__resolvedKey;
    @Generated(hash = 89682145)
    private transient Long toUserInfoBean__resolvedKey;
    @Generated(hash = 783792331)
    private transient Long publishUserInfoBean__resolvedKey;
}
