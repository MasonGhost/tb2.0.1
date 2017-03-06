package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author LiuChao
 * @describe 动态点赞列表,存在数据库中
 * @date 2017/3/2
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicDigListBean extends BaseListBean implements Parcelable {
    @Id
    private long feed_digg_id; // 服务器返回的maxId
    private long user_id;
    @ToOne(joinProperty = "user_id") //DynamicDigListBean的user_id作为外键与用户信息表相关联
    private UserInfoBean mUserInfoBean;

    public long getFeed_digg_id() {
        return feed_digg_id;
    }

    public void setFeed_digg_id(long feed_digg_id) {
        this.feed_digg_id = feed_digg_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    
    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    @Override
    public Long getMaxId() {
        return feed_digg_id;
    }

    public DynamicDigListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.feed_digg_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.mUserInfoBean, flags);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1262958292)
    public UserInfoBean getMUserInfoBean() {
        long __key = this.user_id;
        if (mUserInfoBean__resolvedKey == null || !mUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean mUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                mUserInfoBean = mUserInfoBeanNew;
                mUserInfoBean__resolvedKey = __key;
            }
        }
        return mUserInfoBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1065715559)
    public void setMUserInfoBean(@NotNull UserInfoBean mUserInfoBean) {
        if (mUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mUserInfoBean = mUserInfoBean;
            user_id = mUserInfoBean.getUser_id();
            mUserInfoBean__resolvedKey = user_id;
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
    @Generated(hash = 1044110578)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicDigListBeanDao() : null;
    }

    protected DynamicDigListBean(Parcel in) {
        super(in);
        this.feed_digg_id = in.readLong();
        this.user_id = in.readLong();
        this.mUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Generated(hash = 882682486)
    public DynamicDigListBean(long feed_digg_id, long user_id) {
        this.feed_digg_id = feed_digg_id;
        this.user_id = user_id;
    }

    public static final Creator<DynamicDigListBean> CREATOR = new Creator<DynamicDigListBean>() {
        @Override
        public DynamicDigListBean createFromParcel(Parcel source) {
            return new DynamicDigListBean(source);
        }

        @Override
        public DynamicDigListBean[] newArray(int size) {
            return new DynamicDigListBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2010508759)
    private transient DynamicDigListBeanDao myDao;
    @Generated(hash = 2004141746)
    private transient Long mUserInfoBean__resolvedKey;
}
