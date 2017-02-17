package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author LiuChao
 * @describe 粉丝，关注的数据库实体
 * @date 2017/2/15
 * @contact email:450127106@qq.com
 */
@Entity
public class FollowFansBean implements Cloneable{
    public static final int UNFOLLOWED_STATE = 0;// 未关注的状态
    public static final int IFOLLOWED_STATE = 1;// 我关注了他，他没有关注我
    public static final int FOLLOWED_EACHOTHER_STATE = 2;// 互相关注的状态
    @Id
    private Long id;
    // 当前并未找到greenDao设置联合唯一性的方案，所以使用该字段，拼接userId和followedUserId
    // ，作为唯一的标识
    @Unique
    private String userFollowedId;
    private long userId;// 主体用户：将要关注别人的人
    private int followState;// 关注状态 包含关注和相互关注
    private long followedUserId;// 被关注的用户
    @ToOne(joinProperty = "userId")
    private UserInfoBean user;
    @ToOne(joinProperty = "followedUserId")
    private UserInfoBean fllowedUser;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1176234999)
    private transient FollowFansBeanDao myDao;
    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;
    @Generated(hash = 1121137644)
    private transient Long fllowedUser__resolvedKey;

    @Generated(hash = 1665209293)
    public FollowFansBean(Long id, String userFollowedId, long userId, int followState,
            long followedUserId) {
        this.id = id;
        this.userFollowedId = userFollowedId;
        this.userId = userId;
        this.followState = followState;
        this.followedUserId = followedUserId;
    }

    @Generated(hash = 258062586)
    public FollowFansBean() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getFollowState() {
        return followState;
    }

    public void setFollowState(int followState) {
        this.followState = followState;
    }

    public long getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(long followedUserId) {
        this.followedUserId = followedUserId;
    }

    @Keep
    public UserInfoBean getUser() {
        return user;
    }

    @Keep
    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    @Keep
    public UserInfoBean getFllowedUser() {
        return fllowedUser;
    }

    @Keep
    public void setFllowedUser(UserInfoBean fllowedUser) {
        this.fllowedUser = fllowedUser;
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

    // greenDao生成该方法，应该用不到
    public String getUserFollowedId() {
        return userId + "$" + followedUserId;
    }

    // greenDao生成该方法，防止外界修改该值，修改了获取userFollowedId的方法
    public void setUserFollowedId(String userFollowedId) {
        this.userFollowedId = userId + "$" + followedUserId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 29967072)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFollowFansBeanDao() : null;
    }

}
