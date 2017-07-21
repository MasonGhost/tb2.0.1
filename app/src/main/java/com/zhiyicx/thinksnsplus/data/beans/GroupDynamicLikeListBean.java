package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @Author Jliuer
 * @Date 2017/07/18/11:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class GroupDynamicLikeListBean extends BaseListBean {

    /**
     * id : 6
     * user_id : 2
     */
    @Id
    private Long id;
    @ToOne(joinProperty = "user_id")
    UserInfoBean mUserInfoBean;
    private long user_id;

    @Override
    public Long getMaxId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeParcelable(this.mUserInfoBean, flags);
        dest.writeLong(this.user_id);
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
    @Generated(hash = 491922874)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupDynamicLikeListBeanDao() : null;
    }

    public GroupDynamicLikeListBean() {
    }

    protected GroupDynamicLikeListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.user_id = in.readLong();
    }

    @Generated(hash = 746643971)
    public GroupDynamicLikeListBean(Long id, long user_id) {
        this.id = id;
        this.user_id = user_id;
    }

    public static final Creator<GroupDynamicLikeListBean> CREATOR = new Creator<GroupDynamicLikeListBean>() {
        @Override
        public GroupDynamicLikeListBean createFromParcel(Parcel source) {
            return new GroupDynamicLikeListBean(source);
        }

        @Override
        public GroupDynamicLikeListBean[] newArray(int size) {
            return new GroupDynamicLikeListBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 187443500)
    private transient GroupDynamicLikeListBeanDao myDao;
    @Generated(hash = 2004141746)
    private transient Long mUserInfoBean__resolvedKey;
}
