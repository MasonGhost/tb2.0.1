package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author LiuChao
 * @describe 动态点赞列表,存在数据库中
 * @date 2017/3/2
 * @contact email:450127106@qq.com
 */
public class DynamicDigListBean extends BaseListBean implements Parcelable {
    private long feed_digg_id; // 服务器返回的maxId
    private long user_id;

    public long getFeed_digg_id() {
        return feed_digg_id;
    }

    public void setFeed_digg_id(long feed_digg_id) {
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
        super.writeToParcel(dest, flags);
        dest.writeLong(this.feed_digg_id);
        dest.writeLong(this.user_id);
    }

    public DynamicDigListBean() {
    }

    protected DynamicDigListBean(Parcel in) {
        super(in);
        this.feed_digg_id = in.readLong();
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
