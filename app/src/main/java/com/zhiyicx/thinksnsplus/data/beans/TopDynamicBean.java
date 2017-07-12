package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Author Jliuer
 * @Date 2017/07/12/17:16
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class TopDynamic implements Parcelable{

    @Id
    @Unique
    private Long feed_mark;
    @ToOne(joinProperty = "feed_mark")// DynamicBean 的 user_id作为外键
    private DynamicDetailBeanV2 mDynamicDetailBeanV2;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.feed_mark);
        dest.writeParcelable(this.mDynamicDetailBeanV2, flags);
    }

    public Long getFeed_mark() {
        return this.feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1243034283)
    public DynamicDetailBeanV2 getMDynamicDetailBeanV2() {
        Long __key = this.feed_mark;
        if (mDynamicDetailBeanV2__resolvedKey == null
                || !mDynamicDetailBeanV2__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DynamicDetailBeanV2Dao targetDao = daoSession.getDynamicDetailBeanV2Dao();
            DynamicDetailBeanV2 mDynamicDetailBeanV2New = targetDao.load(__key);
            synchronized (this) {
                mDynamicDetailBeanV2 = mDynamicDetailBeanV2New;
                mDynamicDetailBeanV2__resolvedKey = __key;
            }
        }
        return mDynamicDetailBeanV2;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 523672187)
    public void setMDynamicDetailBeanV2(DynamicDetailBeanV2 mDynamicDetailBeanV2) {
        synchronized (this) {
            this.mDynamicDetailBeanV2 = mDynamicDetailBeanV2;
            feed_mark = mDynamicDetailBeanV2 == null ? null : mDynamicDetailBeanV2.getFeed_mark();
            mDynamicDetailBeanV2__resolvedKey = feed_mark;
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
    @Generated(hash = 1087881591)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopDynamicDao() : null;
    }

    public TopDynamic() {
    }

    protected TopDynamic(Parcel in) {
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.mDynamicDetailBeanV2 = in.readParcelable(DynamicDetailBeanV2.class.getClassLoader());
    }

    @Generated(hash = 1816663854)
    public TopDynamic(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public static final Creator<TopDynamic> CREATOR = new Creator<TopDynamic>() {
        @Override
        public TopDynamic createFromParcel(Parcel source) {
            return new TopDynamic(source);
        }

        @Override
        public TopDynamic[] newArray(int size) {
            return new TopDynamic[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1478943690)
    private transient TopDynamicDao myDao;
    @Generated(hash = 1412668450)
    private transient Long mDynamicDetailBeanV2__resolvedKey;
}
