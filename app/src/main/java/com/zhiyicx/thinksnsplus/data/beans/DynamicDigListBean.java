package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * @author LiuChao
 * @describe 动态点赞列表, 存在数据库中
 * @date 2017/3/2
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicDigListBean extends BaseListBean implements Parcelable, Serializable {
    private static final long serialVersionUID = -570059504828130442L;
    /**
     * id : 2 // 赞 ID
     * user_id : 1 // 赞的用户
     * target_user : 1 // 目标用户
     * likeable_id : 1 // 目标内容ID
     * likeable_type : feeds // 目标来源
     * created_at : 2017-07-12 08:09:07  // 点赞时间
     * updated_at : 2017-07-12 08:09:07 // 点赞更新时间
     */
    @Id
    @SerializedName(value = "id",alternate = {"feed_digg_id"})
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

    @Override
    public Long getMaxId() {
        return id;
    }

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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1044110578)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicDigListBeanDao() : null;
    }

    public DynamicDigListBean() {
    }

    protected DynamicDigListBean(Parcel in) {
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

    @Generated(hash = 1110661922)
    public DynamicDigListBean(Long id, Long user_id, Long target_user, Long likeable_id,
                              String likeable_type, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.target_user = target_user;
        this.likeable_id = likeable_id;
        this.likeable_type = likeable_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static final Creator<DynamicDigListBean> CREATOR = new Creator<DynamicDigListBean>() {
        @Override
        public DynamicDigListBean createFromParcel(Parcel source) {
            return new DynamicDigListBean(source);
        }

        @Override
        public DynamicDigListBean[] newArray(int size) {
            return new DynamicDigListBean[size];
        }
    };
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2010508759)
    private transient DynamicDigListBeanDao myDao;
    @Generated(hash = 1533677598)
    private transient Long diggUserInfo__resolvedKey;
    @Generated(hash = 437417732)
    private transient Long targetUserInfo__resolvedKey;
}
