package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author Jliuer
 * @Date 17/12/05 13:49
 * @Email Jliuer@aliyun.com
 * @Description 
 */
@Entity
public class PostDigListBean extends BaseListBean implements Serializable {

    @Transient
    private static final long serialVersionUID = 5985608387262498425L;

    /*{
        "id": 5,
            "user_id": 1,
            "target_user": 1,
            "likeable_id": 4,
            "likeable_type": "news",
            "created_at": "2017-08-08 02:25:34",
            "updated_at": "2017-08-08 02:25:34"
    }*/

    @Id
    private Long id;
    private Long user_id; // 赞的用户
    @ToOne(joinProperty = "user_id")
    private UserInfoBean diggUserInfo;
    private Long target_user; // 目标用户
    @ToOne(joinProperty = "target_user")
    private UserInfoBean targetUserInfo;
    private Long likeable_id;
    private String likeable_type;
    private String created_at;
    private String updated_at;

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

    public Long getTarget_user() {
        return target_user;
    }

    public void setTarget_user(Long target_user) {
        this.target_user = target_user;
    }

    public Long getLikeable_id() {
        return likeable_id;
    }

    public void setLikeable_id(Long likeable_id) {
        this.likeable_id = likeable_id;
    }

    public String getLikeable_type() {
        return likeable_type;
    }

    public void setLikeable_type(String likeable_type) {
        this.likeable_type = likeable_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.diggUserInfo, flags);
        dest.writeValue(this.target_user);
        dest.writeParcelable(this.targetUserInfo, flags);
        dest.writeValue(this.likeable_id);
        dest.writeString(this.likeable_type);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1218409191)
    public UserInfoBean getDiggUserInfo() {
        Long __key = this.user_id;
        if (diggUserInfo__resolvedKey == null || !diggUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean diggUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                diggUserInfo = diggUserInfoNew;
                diggUserInfo__resolvedKey = __key;
            }
        }
        return diggUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 165058629)
    public void setDiggUserInfo(UserInfoBean diggUserInfo) {
        synchronized (this) {
            this.diggUserInfo = diggUserInfo;
            user_id = diggUserInfo == null ? null : diggUserInfo.getUser_id();
            diggUserInfo__resolvedKey = user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 368057598)
    public UserInfoBean getTargetUserInfo() {
        Long __key = this.target_user;
        if (targetUserInfo__resolvedKey == null
                || !targetUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean targetUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                targetUserInfo = targetUserInfoNew;
                targetUserInfo__resolvedKey = __key;
            }
        }
        return targetUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 875470237)
    public void setTargetUserInfo(UserInfoBean targetUserInfo) {
        synchronized (this) {
            this.targetUserInfo = targetUserInfo;
            target_user = targetUserInfo == null ? null : targetUserInfo.getUser_id();
            targetUserInfo__resolvedKey = target_user;
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
    @Generated(hash = 1142543440)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPostDigListBeanDao() : null;
    }

    public PostDigListBean() {
    }

    protected PostDigListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.diggUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.target_user = (Long) in.readValue(Long.class.getClassLoader());
        this.targetUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.likeable_id = (Long) in.readValue(Long.class.getClassLoader());
        this.likeable_type = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    @Generated(hash = 1789458496)
    public PostDigListBean(Long id, Long user_id, Long target_user, Long likeable_id,
            String likeable_type, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.target_user = target_user;
        this.likeable_id = likeable_id;
        this.likeable_type = likeable_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static final Creator<PostDigListBean> CREATOR = new Creator<PostDigListBean>() {
        @Override
        public PostDigListBean createFromParcel(Parcel source) {
            return new PostDigListBean(source);
        }

        @Override
        public PostDigListBean[] newArray(int size) {
            return new PostDigListBean[size];
        }
    };

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 253025317)
    private transient PostDigListBeanDao myDao;

    @Generated(hash = 1533677598)
    private transient Long diggUserInfo__resolvedKey;

    @Generated(hash = 437417732)
    private transient Long targetUserInfo__resolvedKey;
}
