package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author LiuChao
 * @describe 粉丝，关注的数据库实体
 * @date 2017/2/15
 * @contact email:450127106@qq.com
 */
@Entity
public class FollowFansBean implements Parcelable {

    private long userId;// 主体用户：将要关注别人的人
    private int followState;// 关注状态 包含关注和相互关注
    private long followedUserId;// 被关注的用户
    @ToOne(joinProperty = "userId")
    private UserInfoBean user;
    @ToOne(joinProperty = "followedUserId")
    private UserInfoBean fllowedUser;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeInt(this.followState);
        dest.writeLong(this.followedUserId);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 903869499)
    public UserInfoBean getUser() {
        long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 114612859)
    public void setUser(@NotNull UserInfoBean user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            userId = user.getUser_id();
            user__resolvedKey = userId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 212477483)
    public UserInfoBean getFllowedUser() {
        long __key = this.followedUserId;
        if (fllowedUser__resolvedKey == null || !fllowedUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean fllowedUserNew = targetDao.load(__key);
            synchronized (this) {
                fllowedUser = fllowedUserNew;
                fllowedUser__resolvedKey = __key;
            }
        }
        return fllowedUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1147163779)
    public void setFllowedUser(@NotNull UserInfoBean fllowedUser) {
        if (fllowedUser == null) {
            throw new DaoException(
                    "To-one property 'followedUserId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.fllowedUser = fllowedUser;
            followedUserId = fllowedUser.getUser_id();
            fllowedUser__resolvedKey = followedUserId;
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
    @Generated(hash = 29967072)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFollowFansBeanDao() : null;
    }

    public FollowFansBean() {
    }

    protected FollowFansBean(Parcel in) {
        this.userId = in.readLong();
        this.followState = in.readInt();
        this.followedUserId = in.readLong();
    }

    @Generated(hash = 1061399675)
    public FollowFansBean(long userId, int followState, long followedUserId) {
        this.userId = userId;
        this.followState = followState;
        this.followedUserId = followedUserId;
    }

    public static final Creator<FollowFansBean> CREATOR = new Creator<FollowFansBean>() {
        @Override
        public FollowFansBean createFromParcel(Parcel source) {
            return new FollowFansBean(source);
        }

        @Override
        public FollowFansBean[] newArray(int size) {
            return new FollowFansBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1176234999)
    private transient FollowFansBeanDao myDao;
    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;
    @Generated(hash = 1121137644)
    private transient Long fllowedUser__resolvedKey;
}
