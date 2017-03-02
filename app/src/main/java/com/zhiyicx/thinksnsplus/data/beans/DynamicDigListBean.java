package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LiuChao
 * @describe 动态点赞列表
 * @date 2017/3/2
 * @contact email:450127106@qq.com
 */

public class DynamicDigListBean implements Parcelable {
    private int feed_digg_id;
    private long user_id;

    public int getFeed_digg_id() {
        return feed_digg_id;
    }

    public void setFeed_digg_id(int feed_digg_id) {
        this.feed_digg_id = feed_digg_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.feed_digg_id);
        dest.writeLong(this.user_id);
    }

    public DynamicDigListBean() {
    }

    protected DynamicDigListBean(Parcel in) {
        this.feed_digg_id = in.readInt();
        this.user_id = in.readLong();
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
}
