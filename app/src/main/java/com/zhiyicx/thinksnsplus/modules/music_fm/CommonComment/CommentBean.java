package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.zhiyicx.thinksnsplus.data.beans.DaoSession;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;

/**
 * @Author Jliuer
 * @Date 2017/04/12/10:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class CommentBean {
    protected static final int SEND_ERROR = 0;
    protected static final int SEND_ING = 1;
    protected static final int SEND_SUCCESS = 2;

    @Id
    protected Long id;
    protected int comment_id = -1;
    protected String created_at;
    protected String updated_at;
    protected String comment_content;
    protected int user_id;
    protected int reply_to_user_id;
    @ToOne(joinProperty = "user_id")
    protected UserInfoBean fromUserInfoBean;
    @ToOne(joinProperty = "reply_to_user_id")
    protected UserInfoBean toUserInfoBean;
    @Unique
    protected Long comment_mark;
    protected int state = SEND_SUCCESS;
    protected String netRequestUrl;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1673291628)
    private transient CommentBeanDao myDao;
    @Generated(hash = 386266430)
    private transient Integer fromUserInfoBean__resolvedKey;
    @Generated(hash = 1650243776)
    private transient Integer toUserInfoBean__resolvedKey;

    @Generated(hash = 192514366)
    public CommentBean(Long id, int comment_id, String created_at,
            String updated_at, String comment_content, int user_id,
            int reply_to_user_id, Long comment_mark, int state,
            String netRequestUrl) {
        this.id = id;
        this.comment_id = comment_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.comment_content = comment_content;
        this.user_id = user_id;
        this.reply_to_user_id = reply_to_user_id;
        this.comment_mark = comment_mark;
        this.state = state;
        this.netRequestUrl = netRequestUrl;
    }

    @Generated(hash = 373728077)
    public CommentBean() {
    }

    public String getNetRequestUrl() {
        return netRequestUrl;
    }

    public void setNetRequestUrl(String netRequestUrl) {
        this.netRequestUrl = netRequestUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(int reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    @Keep
    public UserInfoBean getFromUserInfoBean() {
        return fromUserInfoBean;
    }

    @Keep
    public void setFromUserInfoBean(UserInfoBean fromUserInfoBean) {
        this.fromUserInfoBean = fromUserInfoBean;
    }

    @Keep
    public UserInfoBean getToUserInfoBean() {
        return toUserInfoBean;
    }

    @Keep
    public void setToUserInfoBean(UserInfoBean toUserInfoBean) {
        this.toUserInfoBean = toUserInfoBean;
    }

    public Long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(Long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
    @Generated(hash = 920802939)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommentBeanDao() : null;
    }

}
