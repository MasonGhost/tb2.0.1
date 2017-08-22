package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * @author LiuChao
 * @describe 粉丝，关注的数据库实体
 * @date 2017/2/15
 * @contact email:450127106@qq.com
 */
@Entity
public class FollowFansBean extends BaseListBean implements Serializable {
    private static final long serialVersionUID = 536871009;
    public static final int UNFOLLOWED_STATE = 0;// 未关注的状态
    public static final int IFOLLOWED_STATE = 1;// 我关注了他，他没有关注我
    public static final int FOLLOWED_EACHOTHER_STATE = 2;// 互相关注的状态
    // 存储服务器返回的maxId
    @Id
    private Long id;
    // 当前并未找到greenDao设置联合唯一性的方案，所以使用该字段，拼接userId和followedUserId
    // ，作为唯一的标识
    @Unique
    private String origintargetUser;// 当前用户和目标用户的userId拼接字段
    private long originUserId;// 当前用户
    @SerializedName(value = "targetUserId",alternate = {"user_id"})
    private long targetUserId;// 目标用户
    @SerializedName(value = "origin_follow_status",alternate = {"my_follow_status"})
    private int origin_follow_status;// 当前用户对目标用户的关注状态
    @SerializedName(value = "target_follow_status",alternate = {"follow_status"})
    private int target_follow_status;// 目标用户对当前用户的关注状态
    @ToOne(joinProperty = "originUserId")
    private UserInfoBean originUserInfo;// 当前用户信息
    @ToOne(joinProperty = "targetUserId")
    private UserInfoBean targetUserInfo;// 目标用户信息

    // 当前用户对目标用户的关注状态
    public int getFollowState() {
        // 相互关注了
        if (origin_follow_status == IFOLLOWED_STATE && target_follow_status == IFOLLOWED_STATE) {
            return FOLLOWED_EACHOTHER_STATE;
        }
        // 返回当前用户对目标用户的关注状态
        return origin_follow_status;
    }

    public String getOrigintargetUser() {
        return origintargetUser;
    }

    public void setOrigintargetUser(String origintargetUser) {
        this.origintargetUser = originUserId + "$" + targetUserId;
    }

    public long getOriginUserId() {
        return originUserId;
    }

    public void setOriginUserId(long originUserId) {
        this.originUserId = originUserId;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getOrigin_follow_status() {
        return origin_follow_status;
    }

    public void setOrigin_follow_status(int origin_follow_status) {
        this.origin_follow_status = origin_follow_status;
    }

    public int getTarget_follow_status() {
        return target_follow_status;
    }

    public void setTarget_follow_status(int target_follow_status) {
        this.target_follow_status = target_follow_status;
    }

    @Keep
    public UserInfoBean getOriginUserInfo() {
        return originUserInfo;
    }

    @Keep
    public void setOriginUserInfo(UserInfoBean originUserInfo) {
        this.originUserInfo = originUserInfo;
    }

    @Keep
    public UserInfoBean getTargetUserInfo() {
        return targetUserInfo;
    }

    @Keep
    public void setTargetUserInfo(UserInfoBean targetUserInfo) {
        this.targetUserInfo = targetUserInfo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.origintargetUser);
        dest.writeLong(this.originUserId);
        dest.writeLong(this.targetUserId);
        dest.writeInt(this.origin_follow_status);
        dest.writeInt(this.target_follow_status);
        dest.writeParcelable(this.originUserInfo, flags);
        dest.writeParcelable(this.targetUserInfo, flags);
    }

    public FollowFansBean() {
    }

    protected FollowFansBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.origintargetUser = in.readString();
        this.originUserId = in.readLong();
        this.targetUserId = in.readLong();
        this.origin_follow_status = in.readInt();
        this.target_follow_status = in.readInt();
        this.originUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.targetUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Generated(hash = 851025626)
    public FollowFansBean(Long id, String origintargetUser, long originUserId,
                          long targetUserId, int origin_follow_status, int target_follow_status) {
        this.id = id;
        this.origintargetUser = origintargetUser;
        this.originUserId = originUserId;
        this.targetUserId = targetUserId;
        this.origin_follow_status = origin_follow_status;
        this.target_follow_status = target_follow_status;
    }

    public static final Creator<FollowFansBean> CREATOR = new Creator<FollowFansBean>() {
        @Override
        public FollowFansBean createFromParcel(Parcel source) {
            return new FollowFansBean(source);
        }

        @Override
        public FollowFansBean[] newArray(int size) {
            return new FollowFansBean[size];
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
    @Generated(hash = 1176234999)
    private transient FollowFansBeanDao myDao;
    @Generated(hash = 152244013)
    private transient Long originUserInfo__resolvedKey;
    @Generated(hash = 437417732)
    private transient Long targetUserInfo__resolvedKey;

    @Override
    public Long getMaxId() {
        return id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String toString() {
        return "FollowFansBean{" +
                "id=" + id +
                ", origintargetUser='" + origintargetUser + '\'' +
                ", originUserId=" + originUserId +
                ", targetUserId=" + targetUserId +
                ", origin_follow_status=" + origin_follow_status +
                ", target_follow_status=" + target_follow_status +
                ", originUserInfo=" + originUserInfo +
                ", targetUserInfo=" + targetUserInfo +
                ", myDao=" + myDao +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 29967072)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFollowFansBeanDao() : null;
    }
}
