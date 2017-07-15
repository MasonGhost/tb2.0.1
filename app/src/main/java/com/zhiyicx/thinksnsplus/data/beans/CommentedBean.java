package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Describe {@see https://github.com/zhiyicx/plus-component-feed/blob/master/documents/%E6%88%91%E6%94%B6%E5%88%B0%E7%9A%84%E8%AF%84%E8%AE%BA%E5%88%97%E8%A1%A8.md}
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class CommentedBean extends BaseListBean {

    /**
     * "id": 5, // 评论 ID
     * "user_id": 1, // 评论发送用户
     * "target_user": 1, // 目标用户
     * "reply_user": 0, // 被回复用户
     * "channel": "feed", // 评论来自频道
     * "target": "8", // 来自频道目标id 例如 channel = feed 则 target 就是 feed 频道评论 ID,
     * "created_at": "2017-07-11 09:53:21", // 评论时间
     * "updated_at": "2017-07-11 09:53:21", // 更新时间
     * "comment_content": "测试一些评论发送", // 评论内容
     * "target_image": 0, // 目标频道图片 ID
     * "target_title": "动态内容", // 目标频道标题
     * "target_id": 1 // 目标频道ID，例如 channel = feed 则 target_id 就是 feed_id
     */
    @Id
    private Long id; // 数据体 id
    private String channel; // 数据所属扩展包名 目前可能的参数有 feed music news

    private String comment_table; // 评论所属数据表 目前可能的参数有 feed_comments music_comments news_comments

    private Long target_id;
    private Long target;

    private String comment_content; // 评论类容
    private String target_title;
    private int target_image; // 封面 id
    private Long user_id; // 评论者 id
    @ToOne(joinProperty = "user_id")
    private UserInfoBean commentUserInfo;
    private Long target_user; // 资源作者 id
    @ToOne(joinProperty = "target_user")
    private UserInfoBean sourceUserInfo;
    private Long reply_user;  // 被回复着 id
    @ToOne(joinProperty = "reply_user")
    private UserInfoBean replyUserInfo;
    private String created_at;
    private String updated_at;


    @Override
    public Long getMaxId() {
        return this.id;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public String getComment_table() {
        return this.comment_table;
    }

    public void setComment_table(String comment_table) {
        this.comment_table = comment_table;
    }


    public Long getTarget_id() {
        return this.target_id;
    }

    public void setTarget_id(Long target_id) {
        this.target_id = target_id;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
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

    public int getTarget_image() {
        return this.target_image;
    }

    public void setTarget_image(int target_image) {
        this.target_image = target_image;
    }

    public String getComment_content() {
        return this.comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getTarget_title() {
        return target_title;
    }

    public void setTarget_title(String target_title) {
        this.target_title = target_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.channel);
        dest.writeString(this.comment_table);
        dest.writeValue(this.target_id);
        dest.writeValue(this.target);
        dest.writeString(this.comment_content);
        dest.writeString(this.target_title);
        dest.writeInt(this.target_image);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.commentUserInfo, flags);
        dest.writeValue(this.target_user);
        dest.writeParcelable(this.sourceUserInfo, flags);
        dest.writeValue(this.reply_user);
        dest.writeParcelable(this.replyUserInfo, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public CommentedBean() {
    }

    protected CommentedBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.channel = in.readString();
        this.comment_table = in.readString();
        this.target_id = (Long) in.readValue(Long.class.getClassLoader());
        this.target = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_content = in.readString();
        this.target_title = in.readString();
        this.target_image = in.readInt();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.commentUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.target_user = (Long) in.readValue(Long.class.getClassLoader());
        this.sourceUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.reply_user = (Long) in.readValue(Long.class.getClassLoader());
        this.replyUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }


    @Generated(hash = 270334609)
    public CommentedBean(Long id, String channel, String comment_table, Long target_id, Long target, String comment_content, String target_title, int target_image,
            Long user_id, Long target_user, Long reply_user, String created_at, String updated_at) {
        this.id = id;
        this.channel = channel;
        this.comment_table = comment_table;
        this.target_id = target_id;
        this.target = target;
        this.comment_content = comment_content;
        this.target_title = target_title;
        this.target_image = target_image;
        this.user_id = user_id;
        this.target_user = target_user;
        this.reply_user = reply_user;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    @Override
    public String toString() {
        return "CommentedBean{" +
                "id=" + id +
                ", channel='" + channel + '\'' +
                ", comment_table='" + comment_table + '\'' +
                ", target_id=" + target_id +
                ", target=" + target +
                ", comment_content='" + comment_content + '\'' +
                ", target_title='" + target_title + '\'' +
                ", target_image=" + target_image +
                ", user_id=" + user_id +
                ", commentUserInfo=" + commentUserInfo +
                ", target_user=" + target_user +
                ", sourceUserInfo=" + sourceUserInfo +
                ", reply_user=" + reply_user +
                ", replyUserInfo=" + replyUserInfo +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
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
    @Generated(hash = 1491152852)
    public UserInfoBean getSourceUserInfo() {
        Long __key = this.target_user;
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
    @Generated(hash = 672311628)
    public void setSourceUserInfo(UserInfoBean sourceUserInfo) {
        synchronized (this) {
            this.sourceUserInfo = sourceUserInfo;
            target_user = sourceUserInfo == null ? null : sourceUserInfo.getUser_id();
            sourceUserInfo__resolvedKey = target_user;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1858190697)
    public UserInfoBean getReplyUserInfo() {
        Long __key = this.reply_user;
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
    @Generated(hash = 1797342590)
    public void setReplyUserInfo(UserInfoBean replyUserInfo) {
        synchronized (this) {
            this.replyUserInfo = replyUserInfo;
            reply_user = replyUserInfo == null ? null : replyUserInfo.getUser_id();
            replyUserInfo__resolvedKey = reply_user;
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
}
