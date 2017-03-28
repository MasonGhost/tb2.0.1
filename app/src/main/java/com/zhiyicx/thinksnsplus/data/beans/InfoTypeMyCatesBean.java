package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Author Jliuer
 * @Date 2017/03/27
 * @Email Jliuer@aliyun.com
 * @Description 资讯分类列表 我关注的资讯-greendao
 */
@Entity
public class InfoTypeMyCatesBean implements Parcelable, Serializable {

    @Transient
    private static final long serialVersionUID = 1L;
    /**
     * id : 1
     * name : 分类1
     */
    @Id
    @Unique
    private Long id;
    private String name;
    @ToOne(joinProperty = "id")
    private InfoListBean mInfoListBean;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1359965412)
    private transient InfoTypeMyCatesBeanDao myDao;
    @Generated(hash = 1585318719)
    private transient Long mInfoListBean__resolvedKey;

    @Keep
    public InfoTypeMyCatesBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1718315182)
    public InfoTypeMyCatesBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InfoListBean getInfoListBean() {
        return mInfoListBean;
    }

    public void setInfoListBean(InfoListBean infoListBean) {
        mInfoListBean = infoListBean;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1750666845)
    public InfoListBean getMInfoListBean() {
        Long __key = this.id;
        if (mInfoListBean__resolvedKey == null
                || !mInfoListBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoListBeanDao targetDao = daoSession.getInfoListBeanDao();
            InfoListBean mInfoListBeanNew = targetDao.load(__key);
            synchronized (this) {
                mInfoListBean = mInfoListBeanNew;
                mInfoListBean__resolvedKey = __key;
            }
        }
        return mInfoListBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1919827090)
    public void setMInfoListBean(InfoListBean mInfoListBean) {
        synchronized (this) {
            this.mInfoListBean = mInfoListBean;
            id = mInfoListBean == null ? null : mInfoListBean.getId();
            mInfoListBean__resolvedKey = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.mInfoListBean, flags);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1827653171)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoTypeMyCatesBeanDao() : null;
    }

    protected InfoTypeMyCatesBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.mInfoListBean = in.readParcelable(InfoListBean.class.getClassLoader());
    }

    public static final Creator<InfoTypeMyCatesBean> CREATOR = new Creator<InfoTypeMyCatesBean>() {
        @Override
        public InfoTypeMyCatesBean createFromParcel(Parcel source) {
            return new InfoTypeMyCatesBean(source);
        }

        @Override
        public InfoTypeMyCatesBean[] newArray(int size) {
            return new InfoTypeMyCatesBean[size];
        }
    };
}