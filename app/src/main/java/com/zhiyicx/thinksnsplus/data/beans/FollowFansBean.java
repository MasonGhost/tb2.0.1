package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author LiuChao
 * @describe 粉丝，关注的数据库实体
 * @date 2017/2/15
 * @contact email:450127106@qq.com
 */
@Entity
public class FollowFansBean {
    public static final int UNFOLLOWED_STATE = 0;// 未关注的状态
    public static final int IFOLLOWED_STATE = 1;// 我关注了他，他没有关注我
    public static final int FOLLOWED_EACHOTHER_STATE = 2;// 互相关注的状态
    // 当前并未找到greenDao设置联合唯一性的方案，所以使用该字段，拼接userId和followedUserId
    // ，作为唯一的标识
    @Unique
    private String userFollowedId;
    private long userId;// 主体用户：将要关注别人的人
    private int followState;// 关注状态 包含关注和相互关注
    private long followedUserId;// 被关注的用户
    @ToOne(joinProperty = "userId")
    private UserInfoBean user;
    @ToOne(joinProperty = "followedUserId")
    private UserInfoBean fllowedUser;

    public FollowFansBean() {
        userFollowedId = userId + "$" + followedUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getFollowState() {
        return followState;
    }

    public void setFollowState(int followState) {
        this.followState = followState;
    }

    public long getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(long followedUserId) {
        this.followedUserId = followedUserId;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public UserInfoBean getFllowedUser() {
        return fllowedUser;
    }

    public void setFllowedUser(UserInfoBean fllowedUser) {
        this.fllowedUser = fllowedUser;
    }
}
