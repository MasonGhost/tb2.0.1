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
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @Author Jliuer
 * @Date 2017/03/27
 * @Email Jliuer@aliyun.com
 * @Description 资讯分类列表 为关注资讯-greendao
 */
@Entity
public class InfoTypeMoreCatesBean implements Parcelable, Serializable {
    @Transient
    private static final long serialVersionUID = 1L;
    /**
     * id : 1
     * name : 分类1
     */
    @Id
    @Unique
    private Long id;
    @Unique
    private int info_type;
    private String name;
    @ToOne(joinProperty = "info_type")
    private InfoListBean mInfoListBean;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 962926367)
    private transient InfoTypeMoreCatesBeanDao myDao;
    @Generated(hash = 808470471)
    private transient Integer mInfoListBean__resolvedKey;
    
    @Keep
    public InfoTypeMoreCatesBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1954093373)
    public InfoTypeMoreCatesBean(Long id, int info_type, String name) {
        this.id = id;
        this.info_type = info_type;
        this.name = name;
    }

    @Generated(hash = 40465761)
    public InfoTypeMoreCatesBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInfo_type() {
        return this.info_type;
    }

    public void setInfo_type(int info_type) {
        this.info_type = info_type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2049587271)
    public InfoListBean getMInfoListBean() {
        int __key = this.info_type;
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
    @Generated(hash = 1546371682)
    public void setMInfoListBean(@NotNull InfoListBean mInfoListBean) {
        if (mInfoListBean == null) {
            throw new DaoException(
                    "To-one property 'info_type' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mInfoListBean = mInfoListBean;
            info_type = mInfoListBean.getInfo_type();
            mInfoListBean__resolvedKey = info_type;
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
        dest.writeInt(this.info_type);
        dest.writeString(this.name);
        dest.writeParcelable(this.mInfoListBean, flags);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1943686829)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoTypeMoreCatesBeanDao() : null;
    }

    protected InfoTypeMoreCatesBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.info_type = in.readInt();
        this.name = in.readString();
        this.mInfoListBean = in.readParcelable(InfoListBean.class.getClassLoader());
    }

    public static final Creator<InfoTypeMoreCatesBean> CREATOR = new Creator<InfoTypeMoreCatesBean>() {
        @Override
        public InfoTypeMoreCatesBean createFromParcel(Parcel source) {
            return new InfoTypeMoreCatesBean(source);
        }

        @Override
        public InfoTypeMoreCatesBean[] newArray(int size) {
            return new InfoTypeMoreCatesBean[size];
        }
    };
}