package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/8/18 11:39
 * @Email Jliuer@aliyun.com
 * @Description 
 */
@Entity
public class AnswerDigListBean extends BaseListBean implements Serializable ,Parcelable{

    private static final long serialVersionUID = 5985608387262498425L;

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
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getTarget_user() {
        return this.target_user;
    }

    public void setTarget_user(Long target_user) {
        this.target_user = target_user;
    }

    public Long getLikeable_id() {
        return this.likeable_id;
    }

    public void setLikeable_id(Long likeable_id) {
        this.likeable_id = likeable_id;
    }

    public String getLikeable_type() {
        return this.likeable_type;
    }

    public void setLikeable_type(String likeable_type) {
        this.likeable_type = likeable_type;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Keep
    public UserInfoBean getDiggUserInfo() {
        return diggUserInfo;
    }

    @Keep
    public void setDiggUserInfo(UserInfoBean diggUserInfo) {
        this.diggUserInfo = diggUserInfo;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Keep
    public UserInfoBean getTargetUserInfo() {
        return targetUserInfo;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Keep
    public void setTargetUserInfo(UserInfoBean targetUserInfo) {
        this.targetUserInfo = targetUserInfo;
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

    @Generated(hash = 1486280086)
    public AnswerDigListBean(Long id, Long user_id, Long target_user, Long likeable_id,
            String likeable_type, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.target_user = target_user;
        this.likeable_id = likeable_id;
        this.likeable_type = likeable_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Generated(hash = 1766442027)
    public AnswerDigListBean() {
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1064328054)
    private transient AnswerDigListBeanDao myDao;

    @Generated(hash = 1533677598)
    private transient Long diggUserInfo__resolvedKey;

    @Generated(hash = 437417732)
    private transient Long targetUserInfo__resolvedKey;


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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1264428331)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnswerDigListBeanDao() : null;
    }

    protected AnswerDigListBean(Parcel in) {
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

    public static final Creator<AnswerDigListBean> CREATOR = new Creator<AnswerDigListBean>() {
        @Override
        public AnswerDigListBean createFromParcel(Parcel source) {
            return new AnswerDigListBean(source);
        }

        @Override
        public AnswerDigListBean[] newArray(int size) {
            return new AnswerDigListBean[size];
        }
    };
}
