package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/07/18/11:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class GroupDynamicCommentListBean extends BaseListBean implements Serializable {
    private static final long serialVersionUID = 1234L;

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
    private long user_id;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean commentUser;
    private String content;
    private long reply_to_user_id;
    @ToOne(joinProperty = "reply_to_user_id")
    private UserInfoBean replyUser;
    private String created_at;
    private int to_user_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
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
        dest.writeParcelable(this.commentUser, flags);
        dest.writeString(this.content);
        dest.writeLong(this.reply_to_user_id);
        dest.writeParcelable(this.replyUser, flags);
        dest.writeString(this.created_at);
        dest.writeInt(this.to_user_id);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1714457217)
    public void setCommentUser(@NotNull UserInfoBean commentUser) {
        if (commentUser == null) {
            throw new DaoException("To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.commentUser = commentUser;
            user_id = commentUser.getUser_id();
            commentUser__resolvedKey = user_id;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1942204408)
    public void setReplyUser(@NotNull UserInfoBean replyUser) {
        if (replyUser == null) {
            throw new DaoException("To-one property 'reply_to_user_id' has not-null constraint; cannot set to-one to null");
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1028792152)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupDynamicCommentListBeanDao() : null;
    }

    public GroupDynamicCommentListBean() {
    }

    protected GroupDynamicCommentListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readLong();
        this.commentUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.content = in.readString();
        this.reply_to_user_id = in.readLong();
        this.replyUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.created_at = in.readString();
        this.to_user_id = in.readInt();
    }

    @Generated(hash = 2132460121)
    public GroupDynamicCommentListBean(Long id, long user_id, String content, long reply_to_user_id, String created_at,
                                       int to_user_id) {
        this.id = id;
        this.user_id = user_id;
        this.content = content;
        this.reply_to_user_id = reply_to_user_id;
        this.created_at = created_at;
        this.to_user_id = to_user_id;
    }

    public static final Creator<GroupDynamicCommentListBean> CREATOR = new Creator<GroupDynamicCommentListBean>() {
        @Override
        public GroupDynamicCommentListBean createFromParcel(Parcel source) {
            return new GroupDynamicCommentListBean(source);
        }

        @Override
        public GroupDynamicCommentListBean[] newArray(int size) {
            return new GroupDynamicCommentListBean[size];
        }
    };
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1079061794)
    private transient GroupDynamicCommentListBeanDao myDao;
    @Generated(hash = 734177030)
    private transient Long commentUser__resolvedKey;
    @Generated(hash = 1789712289)
    private transient Long replyUser__resolvedKey;
}
