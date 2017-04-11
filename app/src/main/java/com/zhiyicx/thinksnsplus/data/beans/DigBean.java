package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DigBean extends BaseListBean {
    /**
     * id : 6
     * user_id : 2
     * value : 3
     */
    @Id
    private Long id;
    private Long user_id;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean digUserInfo;// 点赞人的用户信息
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.id);
        dest.writeLong(this.user_id);
        dest.writeString(this.value);
    }

    protected DigBean(Parcel in) {
        super(in);
        this.id = in.readLong();
        this.user_id = in.readLong();
        this.value = in.readString();
    }

    @Generated(hash = 491025592)
    public DigBean(Long id, Long user_id, String value) {
        this.id = id;
        this.user_id = user_id;
        this.value = value;
    }

    @Generated(hash = 842146675)
    public DigBean() {
    }

    public static final Creator<DigBean> CREATOR = new Creator<DigBean>() {
        @Override
        public DigBean createFromParcel(Parcel source) {
            return new DigBean(source);
        }

        @Override
        public DigBean[] newArray(int size) {
            return new DigBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 316156123)
    private transient DigBeanDao myDao;
    @Generated(hash = 81788119)
    private transient Long digUserInfo__resolvedKey;

    @Override
    public String toString() {
        return "DigBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", value='" + value + '\'' +
                '}';
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1880931547)
    public UserInfoBean getDigUserInfo() {
        Long __key = this.user_id;
        if (digUserInfo__resolvedKey == null
                || !digUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean digUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                digUserInfo = digUserInfoNew;
                digUserInfo__resolvedKey = __key;
            }
        }
        return digUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 272986475)
    public void setDigUserInfo(UserInfoBean digUserInfo) {
        synchronized (this) {
            this.digUserInfo = digUserInfo;
            user_id = digUserInfo == null ? null : digUserInfo.getUser_id();
            digUserInfo__resolvedKey = user_id;
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
    @Generated(hash = 248443797)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDigBeanDao() : null;
    }

}
