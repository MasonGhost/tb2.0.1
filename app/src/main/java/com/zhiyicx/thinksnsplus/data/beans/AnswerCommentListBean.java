package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/08/16/9:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class AnswerCommentListBean extends BaseListBean implements Serializable{
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    private static final long serialVersionUID = 5546524549807185189L;
    /**
     * id : 22
     * user_id : 2
     * target_user : 1
     * reply_user : 1
     * body : llllllllasdfasdfasdfasdf
     * commentable_id : 1
     * commentable_type : question-answers
     * created_at : 2017-08-07 09:43:08
     * updated_at : 2017-08-07 09:43:08
     */
    @Id
    private Long id;
    @Unique
    private long comment_mark;
    private Long user_id;
    private Long target_user;
    private Long reply_user;
    private String body;
    private int commentable_id;
    private String commentable_type;
    private String created_at;
    private String updated_at;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean fromUserInfoBean;
    @ToOne(joinProperty = "reply_user")
    private UserInfoBean toUserInfoBean;
    @ToOne(joinProperty = "target_user")
    private UserInfoBean publishUserInfoBean;
    private int state = SEND_SUCCESS;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1772402694)
    private transient AnswerCommentListBeanDao myDao;
    @Generated(hash = 307901359)
    public AnswerCommentListBean(Long id, long comment_mark, Long user_id,
            Long target_user, Long reply_user, String body, int commentable_id,
            String commentable_type, String created_at, String updated_at,
            int state) {
        this.id = id;
        this.comment_mark = comment_mark;
        this.user_id = user_id;
        this.target_user = target_user;
        this.reply_user = reply_user;
        this.body = body;
        this.commentable_id = commentable_id;
        this.commentable_type = commentable_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.state = state;
    }
    @Generated(hash = 1755124660)
    public AnswerCommentListBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getComment_mark() {
        return this.comment_mark;
    }
    public void setComment_mark(long comment_mark) {
        this.comment_mark = comment_mark;
    }
    public Long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public Long getTarget_user() {
        return this.target_user;
    }
    public void setTarget_user(Long target_user) {
        this.target_user = target_user;
    }
    public Long getReply_user() {
        return this.reply_user;
    }
    public void setReply_user(Long reply_user) {
        this.reply_user = reply_user;
    }
    public String getBody() {
        return this.body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public int getCommentable_id() {
        return this.commentable_id;
    }
    public void setCommentable_id(int commentable_id) {
        this.commentable_id = commentable_id;
    }
    public String getCommentable_type() {
        return this.commentable_type;
    }
    public void setCommentable_type(String commentable_type) {
        this.commentable_type = commentable_type;
    }
    public String getCreated_at() {
        return this.created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getUpdated_at() {
        return this.updated_at;
    }
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    @Generated(hash = 262226026)
    private transient Long fromUserInfoBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1273129518)
    public UserInfoBean getFromUserInfoBean() {
        Long __key = this.user_id;
        if (fromUserInfoBean__resolvedKey == null
                || !fromUserInfoBean__resolvedKey.equals(__key)) {
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
    @Generated(hash = 1577113915)
    public void setFromUserInfoBean(UserInfoBean fromUserInfoBean) {
        synchronized (this) {
            this.fromUserInfoBean = fromUserInfoBean;
            user_id = fromUserInfoBean == null ? null
                    : fromUserInfoBean.getUser_id();
            fromUserInfoBean__resolvedKey = user_id;
        }
    }
    @Generated(hash = 89682145)
    private transient Long toUserInfoBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 367905572)
    public UserInfoBean getToUserInfoBean() {
        Long __key = this.reply_user;
        if (toUserInfoBean__resolvedKey == null
                || !toUserInfoBean__resolvedKey.equals(__key)) {
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
    @Generated(hash = 460174699)
    public void setToUserInfoBean(UserInfoBean toUserInfoBean) {
        synchronized (this) {
            this.toUserInfoBean = toUserInfoBean;
            reply_user = toUserInfoBean == null ? null
                    : toUserInfoBean.getUser_id();
            toUserInfoBean__resolvedKey = reply_user;
        }
    }
    @Generated(hash = 783792331)
    private transient Long publishUserInfoBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 79627206)
    public UserInfoBean getPublishUserInfoBean() {
        Long __key = this.target_user;
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
    @Generated(hash = 1961649923)
    public void setPublishUserInfoBean(UserInfoBean publishUserInfoBean) {
        synchronized (this) {
            this.publishUserInfoBean = publishUserInfoBean;
            target_user = publishUserInfoBean == null ? null
                    : publishUserInfoBean.getUser_id();
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.comment_mark);
        dest.writeValue(this.user_id);
        dest.writeValue(this.target_user);
        dest.writeValue(this.reply_user);
        dest.writeString(this.body);
        dest.writeInt(this.commentable_id);
        dest.writeString(this.commentable_type);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.fromUserInfoBean, flags);
        dest.writeParcelable(this.toUserInfoBean, flags);
        dest.writeParcelable(this.publishUserInfoBean, flags);
        dest.writeInt(this.state);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 917785637)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnswerCommentListBeanDao() : null;
    }
    protected AnswerCommentListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_mark = in.readLong();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.target_user = (Long) in.readValue(Long.class.getClassLoader());
        this.reply_user = (Long) in.readValue(Long.class.getClassLoader());
        this.body = in.readString();
        this.commentable_id = in.readInt();
        this.commentable_type = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.fromUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.toUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.publishUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.state = in.readInt();
    }

    public static final Creator<AnswerCommentListBean> CREATOR = new Creator<AnswerCommentListBean>() {
        @Override
        public AnswerCommentListBean createFromParcel(Parcel source) {
            return new AnswerCommentListBean(source);
        }

        @Override
        public AnswerCommentListBean[] newArray(int size) {
            return new AnswerCommentListBean[size];
        }
    };
}
