package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * @author Catherine
 * @describe 圈子的管理者 多个
 * @date 2017/7/17
 * @contact email:648129313@qq.com
 */
@Entity
public class GroupManagerBean extends BaseListBean implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long _id;
    private long group_id; // 圈子id
    private long user_id;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean userInfoBean;
    private int founder; // 1-创建者 0-管理者
    private String created_at;

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getFounder() {
        return founder;
    }

    public void setFounder(int founder) {
        this.founder = founder;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }



    public GroupManagerBean() {
    }

    @Override
    public String toString() {
        return "GroupManagerBean{" +
                "_id=" + _id +
                ", group_id=" + group_id +
                ", user_id=" + user_id +
                ", userInfoBean=" + userInfoBean +
                ", founder=" + founder +
                ", created_at='" + created_at + '\'' +
                '}';
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 822024536)
    public UserInfoBean getUserInfoBean() {
        long __key = this.user_id;
        if (userInfoBean__resolvedKey == null || !userInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                userInfoBean = userInfoBeanNew;
                userInfoBean__resolvedKey = __key;
            }
        }
        return userInfoBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1574359657)
    public void setUserInfoBean(@NotNull UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.userInfoBean = userInfoBean;
            user_id = userInfoBean.getUser_id();
            userInfoBean__resolvedKey = user_id;
        }
    }

    @Generated(hash = 1094568882)
    public GroupManagerBean(Long _id, long group_id, long user_id, int founder, String created_at) {
        this._id = _id;
        this.group_id = group_id;
        this.user_id = user_id;
        this.founder = founder;
        this.created_at = created_at;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2061380852)
    private transient GroupManagerBeanDao myDao;

    @Generated(hash = 1005780391)
    private transient Long userInfoBean__resolvedKey;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeLong(this.group_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.userInfoBean, flags);
        dest.writeInt(this.founder);
        dest.writeString(this.created_at);
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1148365955)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupManagerBeanDao() : null;
    }

    protected GroupManagerBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.group_id = in.readLong();
        this.user_id = in.readLong();
        this.userInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.founder = in.readInt();
        this.created_at = in.readString();
    }

    public static final Creator<GroupManagerBean> CREATOR = new Creator<GroupManagerBean>() {
        @Override
        public GroupManagerBean createFromParcel(Parcel source) {
            return new GroupManagerBean(source);
        }

        @Override
        public GroupManagerBean[] newArray(int size) {
            return new GroupManagerBean[size];
        }
    };
}
