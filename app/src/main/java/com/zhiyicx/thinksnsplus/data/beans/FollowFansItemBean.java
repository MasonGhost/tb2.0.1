package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

/**
 * @author LiuChao
 * @describe 关注和粉丝列表的item内容
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class FollowFansItemBean extends CacheBean implements Parcelable {
    private UserInfoBean mUserInfoBean;
    private int followState;

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public int getFollowState() {
        return followState;
    }

    public void setFollowState(int followState) {
        this.followState = followState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mUserInfoBean, flags);
        dest.writeInt(this.followState);
    }

    public FollowFansItemBean() {
    }

    protected FollowFansItemBean(Parcel in) {
        this.mUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.followState = in.readInt();
    }

    public static final Parcelable.Creator<FollowFansItemBean> CREATOR = new Parcelable.Creator<FollowFansItemBean>() {
        @Override
        public FollowFansItemBean createFromParcel(Parcel source) {
            return new FollowFansItemBean(source);
        }

        @Override
        public FollowFansItemBean[] newArray(int size) {
            return new FollowFansItemBean[size];
        }
    };
}
