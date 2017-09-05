package com.zhiyicx.thinksnsplus.data.beans;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_FEED;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_GROUP_POST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_MUSIC;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_NEWS;

/**
 * @Describe {@see https://github.com/zhiyicx/plus-component-feed/blob/master/documents/%E6%88%91%E6%94%B6%E5%88%B0%E7%9A%84%E8%AF%84%E8%AE%BA%E5%88%97%E8%A1%A8.md}
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class CommentedBean extends BaseListBean implements Serializable {

    private static final long serialVersionUID = -1702831133869286746L;
    /**
     * {
     * "id": 3,
     * "user_id": 1,
     * "target_user": 1,
     * "reply_user": 0,
     * "body": "我是第三条评论",
     * "commentable_id": 1,
     * "commentable_type": "feeds",
     * "created_at": "2017-07-20 08:53:24",
     * "updated_at": "2017-07-20 08:53:24",
     * "commentable": {
     * "id": 1,
     * "user_id": 1,
     * "feed_content": "动态内容",
     * "feed_from": 1,
     * "like_count": 1,
     * "feed_view_count": 0,
     * "feed_comment_count": 6,
     * "feed_latitude": null,
     * "feed_longtitude": null,
     * "feed_geohash": null,
     * "audit_status": 1,
     * "feed_mark": 1,
     * "pinned": 0,
     * "created_at": "2017-06-27 07:04:32",
     * "updated_at": "2017-07-20 08:53:24",
     * "deleted_at": null,
     * "pinned_amount": 0,
     * "images": [],
     * "paid_node": null
     * }
     * }
     */
    @Id
    private Long id; // 数据体 id
    @SerializedName(value = "channel", alternate = {"commentable_type"})
    private String channel; // 数据所属扩展包名 目前可能的参数有 feeds musics news
    @SerializedName(value = "target_id", alternate = {"commentable_id"})
    private Long target_id;
    @SerializedName(value = "comment_content", alternate = {"body"})
    private String comment_content; // 评论类容
    private String target_title;
    private Long target_image; // 封面 id
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
    private boolean isDelete;
    @Transient
    private Object commentable;

    private long source_id; // 所属资源的父 id; 圈子动态的评论，那source_id == post_id
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 143748434)
    private transient CommentedBeanDao myDao;

    @Generated(hash = 8688308)
    public CommentedBean(Long id, String channel, Long target_id, String comment_content, String target_title, Long target_image, Long user_id, Long target_user,
                         Long reply_user, String created_at, String updated_at, boolean isDelete, long source_id) {
        this.id = id;
        this.channel = channel;
        this.target_id = target_id;
        this.comment_content = comment_content;
        this.target_title = target_title;
        this.target_image = target_image;
        this.user_id = user_id;
        this.target_user = target_user;
        this.reply_user = reply_user;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.isDelete = isDelete;
        this.source_id = source_id;
    }


    @Generated(hash = 1089447575)
    public CommentedBean() {
    }

    @Generated(hash = 70652404)
    private transient Long commentUserInfo__resolvedKey;
    @Generated(hash = 636785044)
    private transient Long sourceUserInfo__resolvedKey;
    @Generated(hash = 214417853)
    private transient Long replyUserInfo__resolvedKey;

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

    public Long getTarget_id() {
        return this.target_id;
    }

    public void setTarget_id(Long target_id) {
        this.target_id = target_id;
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

    public Long getTarget_image() {
        if (target_image != null || commentable == null) {
            return target_image;
        }
        Gson gson = new Gson();
        switch (channel) {
            case APP_LIKE_FEED:
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(commentable));
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    target_image = (long) jsonArray.getJSONObject(0).getDouble("file_id");
                } catch (Exception e) {
                }
                break;
            case APP_LIKE_GROUP_POST:
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(commentable));
                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    target_image = (long) jsonArray.getJSONObject(0).getDouble("file_id");
                } catch (Exception e) {
                }
                break;
            case APP_LIKE_MUSIC:

                break;
            case APP_LIKE_NEWS:

                break;

        }
        return this.target_image;
    }

    public void setTarget_image(Long target_image) {
        this.target_image = target_image;
    }

    public String getComment_content() {
        return this.comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getTarget_title() {
        if (!TextUtils.isEmpty(target_title) || commentable == null) {
            return this.target_title;
        }

        Gson gson = new Gson();
        switch (channel) {
            case APP_LIKE_FEED:
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(commentable));
                    target_title = jsonObject.getString("feed_content");
                } catch (Exception e) {
                }
                break;
            case APP_LIKE_GROUP_POST:
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(commentable));
                    target_title = jsonObject.getString("content");
                } catch (Exception e) {
                }
                break;

            case APP_LIKE_MUSIC:

                break;
            case APP_LIKE_NEWS:

                break;

        }
        return target_title;
    }

    public void setTarget_title(String target_title) {
        this.target_title = target_title;
    }

    public Object getCommentable() {
        return commentable;
    }

    public void setCommentable(Object commentable) {
        this.commentable = commentable;
    }


    public boolean getIsDelete() {

        return this.isDelete;
    }

    public void initDelet() {
        if (commentable != null) {
            isDelete = false;
        } else {
            isDelete = true;
        }
    }

    public void setIsDelete(boolean isDelete) {

        this.isDelete = isDelete;
    }

    public long getSource_id() {
        if (source_id != 0) {
            return this.source_id;
        }

        Gson gson = new Gson();
        switch (channel) {
            case APP_LIKE_FEED:

                break;
            case APP_LIKE_GROUP_POST:
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(commentable));
                    source_id = jsonObject.getLong("group_id");
                } catch (Exception e) {
                }
                break;

            case APP_LIKE_MUSIC:

                break;
            case APP_LIKE_NEWS:

                break;

        }


        return source_id;
    }

    public void setSource_id(long source_id) {
        this.source_id = source_id;
    }


    /**
     * To-one relationship, resolved on first access.
     */
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


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 28373690)
    public void setCommentUserInfo(UserInfoBean commentUserInfo) {
        synchronized (this) {
            this.commentUserInfo = commentUserInfo;
            user_id = commentUserInfo == null ? null : commentUserInfo.getUser_id();
            commentUserInfo__resolvedKey = user_id;
        }
    }


    /**
     * To-one relationship, resolved on first access.
     */
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


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 672311628)
    public void setSourceUserInfo(UserInfoBean sourceUserInfo) {
        synchronized (this) {
            this.sourceUserInfo = sourceUserInfo;
            target_user = sourceUserInfo == null ? null : sourceUserInfo.getUser_id();
            sourceUserInfo__resolvedKey = target_user;
        }
    }


    /**
     * To-one relationship, resolved on first access.
     */
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


    /**
     * called by internal mechanisms, do not call yourself.
     */
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
