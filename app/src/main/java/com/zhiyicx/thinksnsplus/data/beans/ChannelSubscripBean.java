package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author LiuChao
 * @describe 对某个频道的订阅状态
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */
@Entity
public class ChannelSubscripBean extends BaseListBean implements Parcelable {
    @Id(autoincrement = true)
    private Long key;
    @Unique
    private String userIdAndIdforUnique;//将用户id和id组合作为唯一约束
    private long userId;// 和该频道产生联系的用户
    private boolean channelSubscriped;// 频道是否被订阅
    private long id;// 频道id
    @ToOne(joinProperty = "id")// ChannelSubscripBean 的 id 作为外键和频道信息关联
    private ChannelInfoBean mChannelInfoBean;// 该频道的信息
    @ToOne(joinProperty = "userId")// ChannelSubscripBean 的 userId作为外键和用户信息关联
    private UserInfoBean mUserInfoBean;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isChannelSubscriped() {
        return channelSubscriped;
    }

    public void setChannelSubscriped(boolean channelSubscriped) {
        this.channelSubscriped = channelSubscriped;
    }

    public String getUserIdAndIdforUnique() {
        return userIdAndIdforUnique;
    }

    public void setUserIdAndIdforUnique(String userIdAndIdforUnique) {
        // 添加数据库唯一约束键
        this.userIdAndIdforUnique = userId + "$" + id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChannelInfoBean getChannelInfoBean() {
        return mChannelInfoBean;
    }

    public void setChannelInfoBean(ChannelInfoBean channelInfoBean) {
        mChannelInfoBean = channelInfoBean;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public Long getKey() {
        return this.key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public boolean getChannelSubscriped() {
        return this.channelSubscriped;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 521097787)
    public ChannelInfoBean getMChannelInfoBean() {
        long __key = this.id;
        if (mChannelInfoBean__resolvedKey == null || !mChannelInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChannelInfoBeanDao targetDao = daoSession.getChannelInfoBeanDao();
            ChannelInfoBean mChannelInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                mChannelInfoBean = mChannelInfoBeanNew;
                mChannelInfoBean__resolvedKey = __key;
            }
        }
        return mChannelInfoBean;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 649410201)
    public void setMChannelInfoBean(@NotNull ChannelInfoBean mChannelInfoBean) {
        if (mChannelInfoBean == null) {
            throw new DaoException("To-one property 'id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mChannelInfoBean = mChannelInfoBean;
            id = mChannelInfoBean.getId();
            mChannelInfoBean__resolvedKey = id;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1510937107)
    public UserInfoBean getMUserInfoBean() {
        long __key = this.userId;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1569339064)
    public void setMUserInfoBean(@NotNull UserInfoBean mUserInfoBean) {
        if (mUserInfoBean == null) {
            throw new DaoException("To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mUserInfoBean = mUserInfoBean;
            userId = mUserInfoBean.getUser_id();
            mUserInfoBean__resolvedKey = userId;
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

    public ChannelSubscripBean() {
    }

    @Generated(hash = 1879086995)
    public ChannelSubscripBean(Long key, String userIdAndIdforUnique, long userId, boolean channelSubscriped,
                               long id) {
        this.key = key;
        this.userIdAndIdforUnique = userIdAndIdforUnique;
        this.userId = userId;
        this.channelSubscriped = channelSubscriped;
        this.id = id;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1284161844)
    private transient ChannelSubscripBeanDao myDao;
    @Generated(hash = 2019625659)
    private transient Long mChannelInfoBean__resolvedKey;
    @Generated(hash = 2004141746)
    private transient Long mUserInfoBean__resolvedKey;

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChannelSubscripBean) {
            ChannelSubscripBean channelSubscripBean = (ChannelSubscripBean) obj;
            if (channelSubscripBean.getId() == this.id && channelSubscripBean.getUserId() == this.userId) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.key);
        dest.writeString(this.userIdAndIdforUnique);
        dest.writeLong(this.userId);
        dest.writeByte(this.channelSubscriped ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
        dest.writeParcelable(this.mChannelInfoBean, flags);
        dest.writeParcelable(this.mUserInfoBean, flags);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1110154875)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChannelSubscripBeanDao() : null;
    }

    protected ChannelSubscripBean(Parcel in) {
        super(in);
        this.key = (Long) in.readValue(Long.class.getClassLoader());
        this.userIdAndIdforUnique = in.readString();
        this.userId = in.readLong();
        this.channelSubscriped = in.readByte() != 0;
        this.id = in.readLong();
        this.mChannelInfoBean = in.readParcelable(ChannelInfoBean.class.getClassLoader());
        this.mUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<ChannelSubscripBean> CREATOR = new Creator<ChannelSubscripBean>() {
        @Override
        public ChannelSubscripBean createFromParcel(Parcel source) {
            return new ChannelSubscripBean(source);
        }

        @Override
        public ChannelSubscripBean[] newArray(int size) {
            return new ChannelSubscripBean[size];
        }
    };
}
