package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

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
    private Long source_id; // 关联资源 id
    private Long comment_id; // 关联评论 id
    private Long user_id; // 评论者 id
    @ToOne(joinProperty ="user_id")
    private UserInfoBean commentUserInfo;
    private Long to_user_id; // 资源作者 id
    @ToOne(joinProperty ="to_user_id")
    private UserInfoBean sourceUserInfo;
    private Long reply_to_user_id;  // 被回复着 id
    @ToOne(joinProperty ="reply_to_user_id")
    private UserInfoBean replyUserInfo;
    private String created_at;
    private String updated_at;

    private int source_cover; // 封面 id
    private String source_content; // 资源描述
    private String comment_content; // 评论类容



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
    public Long getMaxId() {
        return this.id;
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
        dest.writeValue(this.source_id);
        dest.writeValue(this.comment_id);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.commentUserInfo, flags);
        dest.writeValue(this.to_user_id);
        dest.writeParcelable(this.sourceUserInfo, flags);
        dest.writeValue(this.reply_to_user_id);
        dest.writeParcelable(this.replyUserInfo, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.source_cover);
        dest.writeString(this.source_content);
        dest.writeString(this.comment_content);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComment_table() {
        return this.comment_table;
    }

    public void setComment_table(String comment_table) {
        this.comment_table = comment_table;
    }

    public String getSource_table() {
        return this.source_table;
    }

    public void setSource_table(String source_table) {
        this.source_table = source_table;
    }

    public Long getSource_id() {
        return this.source_id;
    }

    public void setSource_id(Long source_id) {
        this.source_id = source_id;
    }

    public Long getComment_id() {
        return this.comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getTo_user_id() {
        return this.to_user_id;
    }

    public void setTo_user_id(Long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public Long getReply_to_user_id() {
        return this.reply_to_user_id;
    }

    public void setReply_to_user_id(Long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
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

    public int getSource_cover() {
        return this.source_cover;
    }

    public void setSource_cover(int source_cover) {
        this.source_cover = source_cover;
    }

    public String getSource_content() {
        return this.source_content;
    }

    public void setSource_content(String source_content) {
        this.source_content = source_content;
    }

    public String getComment_content() {
        return this.comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 418864499)
    public UserInfoBean getCommentUserInfo() {
        Long __key = this.user_id;
        if (commentUserInfo__resolvedKey == null || !commentUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean commentUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                commentUserInfo = commentUserInfoNew;
                commentUserInfo__resolvedKey = __key;
            }
        }
        return commentUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 28373690)
    public void setCommentUserInfo(UserInfoBean commentUserInfo) {
        synchronized (this) {
            this.commentUserInfo = commentUserInfo;
            user_id = commentUserInfo == null ? null : commentUserInfo.getUser_id();
            commentUserInfo__resolvedKey = user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1720386870)
    public UserInfoBean getSourceUserInfo() {
        Long __key = this.to_user_id;
        if (sourceUserInfo__resolvedKey == null || !sourceUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean sourceUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                sourceUserInfo = sourceUserInfoNew;
                sourceUserInfo__resolvedKey = __key;
            }
        }
        return sourceUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1172655311)
    public void setSourceUserInfo(UserInfoBean sourceUserInfo) {
        synchronized (this) {
            this.sourceUserInfo = sourceUserInfo;
            to_user_id = sourceUserInfo == null ? null : sourceUserInfo.getUser_id();
            sourceUserInfo__resolvedKey = to_user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 59687787)
    public UserInfoBean getReplyUserInfo() {
        Long __key = this.reply_to_user_id;
        if (replyUserInfo__resolvedKey == null || !replyUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean replyUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                replyUserInfo = replyUserInfoNew;
                replyUserInfo__resolvedKey = __key;
            }
        }
        return replyUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1841591919)
    public void setReplyUserInfo(UserInfoBean replyUserInfo) {
        synchronized (this) {
            this.replyUserInfo = replyUserInfo;
            reply_to_user_id = replyUserInfo == null ? null : replyUserInfo.getUser_id();
            replyUserInfo__resolvedKey = reply_to_user_id;
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
    @Generated(hash = 1828279436)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommentedBeanDao() : null;
    }

    public CommentedBean() {
    }

    protected CommentedBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.component = in.readString();
        this.comment_table = in.readString();
        this.source_table = in.readString();
        this.source_id = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.commentUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.to_user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.sourceUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.reply_to_user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.replyUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.source_cover = in.readInt();
        this.source_content = in.readString();
        this.comment_content = in.readString();
    }

    @Generated(hash = 2049548015)
    public CommentedBean(Long id, String component, String comment_table, String source_table, Long source_id, Long comment_id, Long user_id, Long to_user_id,
            Long reply_to_user_id, String created_at, String updated_at, int source_cover, String source_content, String comment_content) {
        this.id = id;
        this.component = component;
        this.comment_table = comment_table;
        this.source_table = source_table;
        this.source_id = source_id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.to_user_id = to_user_id;
        this.reply_to_user_id = reply_to_user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.source_cover = source_cover;
        this.source_content = source_content;
        this.comment_content = comment_content;
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
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 143748434)
    private transient CommentedBeanDao myDao;
    @Generated(hash = 70652404)
    private transient Long commentUserInfo__resolvedKey;
    @Generated(hash = 636785044)
    private transient Long sourceUserInfo__resolvedKey;
    @Generated(hash = 214417853)
    private transient Long replyUserInfo__resolvedKey;
}
