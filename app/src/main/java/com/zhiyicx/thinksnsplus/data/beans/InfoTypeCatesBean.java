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
public class InfoTypeCatesBean implements Parcelable, Serializable {

    @Transient
    private static final long serialVersionUID = 1L;
    /**
     * id : 1
     * name : 分类1
     */
    @Id
    private Long _id;
    @Unique
    private Long id;
    private boolean isMyCate;
    private Long info_type;
    private String name;
    @ToOne(joinProperty = "info_type")
    private InfoListBean mInfoListBean;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 977661916)
    private transient InfoTypeCatesBeanDao myDao;
    @Generated(hash = 1585318719)
    private transient Long mInfoListBean__resolvedKey;

    @Keep
    public InfoTypeCatesBean(Long id, String name, boolean isMyCate) {
        this.id = id;
        this.name = name;
        this.isMyCate = isMyCate;
    }

    @Generated(hash = 557030714)
    public InfoTypeCatesBean(Long _id, Long id, boolean isMyCate, Long info_type,
            String name) {
        this._id = _id;
        this.id = id;
        this.isMyCate = isMyCate;
        this.info_type = info_type;
        this.name = name;
    }

    @Generated(hash = 66708940)
    public InfoTypeCatesBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsMyCate() {
        return this.isMyCate;
    }

    public void setIsMyCate(boolean isMyCate) {
        this.isMyCate = isMyCate;
    }

    public Long getInfo_type() {
        return this.info_type;
    }

    public void setInfo_type(Long info_type) {
        this.info_type = info_type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1758891489)
    public InfoListBean getMInfoListBean() {
        Long __key = this.info_type;
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
    @Generated(hash = 1677007819)
    public void setMInfoListBean(InfoListBean mInfoListBean) {
        synchronized (this) {
            this.mInfoListBean = mInfoListBean;
            info_type = mInfoListBean == null ? null : mInfoListBean.getInfo_type();
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
        dest.writeValue(this._id);
        dest.writeValue(this.id);
        dest.writeByte(this.isMyCate ? (byte) 1 : (byte) 0);
        dest.writeValue(this.info_type);
        dest.writeString(this.name);
        dest.writeParcelable(this.mInfoListBean, flags);
    }

    protected InfoTypeCatesBean(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.isMyCate = in.readByte() != 0;
        this.info_type = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.mInfoListBean = in.readParcelable(InfoListBean.class.getClassLoader());
    }

    public static final Creator<InfoTypeCatesBean> CREATOR = new Creator<InfoTypeCatesBean>() {
        @Override
        public InfoTypeCatesBean createFromParcel(Parcel source) {
            return new InfoTypeCatesBean(source);
        }

        @Override
        public InfoTypeCatesBean[] newArray(int size) {
            return new InfoTypeCatesBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoTypeCatesBean that = (InfoTypeCatesBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1233517617)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoTypeCatesBeanDao() : null;
    }
}