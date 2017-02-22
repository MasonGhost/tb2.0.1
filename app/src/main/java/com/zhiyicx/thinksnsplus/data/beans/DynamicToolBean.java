package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author LiuChao
 * @describe 动态工具栏内容的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicToolBean implements Parcelable {
    @Id
    private long feed_id;// 属于哪条动态
    private int feed_digg_count;// 点赞数
    private int feed_view_count;// 浏览量
    private int feed_comment_count;// 评论数


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.feed_id);
        dest.writeInt(this.feed_digg_count);
        dest.writeInt(this.feed_view_count);
        dest.writeInt(this.feed_comment_count);
    }

    public DynamicToolBean() {
    }

    protected DynamicToolBean(Parcel in) {
        this.feed_id = in.readLong();
        this.feed_digg_count = in.readInt();
        this.feed_view_count = in.readInt();
        this.feed_comment_count = in.readInt();
    }

    public static final Parcelable.Creator<DynamicToolBean> CREATOR = new Parcelable.Creator<DynamicToolBean>() {
        @Override
        public DynamicToolBean createFromParcel(Parcel source) {
            return new DynamicToolBean(source);
        }

        @Override
        public DynamicToolBean[] newArray(int size) {
            return new DynamicToolBean[size];
        }
    };

    public long getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(long feed_id) {
        this.feed_id = feed_id;
    }

    public int getFeed_digg_count() {
        return feed_digg_count;
    }

    public void setFeed_digg_count(int feed_digg_count) {
        this.feed_digg_count = feed_digg_count;
    }

    public int getFeed_view_count() {
        return feed_view_count;
    }

    public void setFeed_view_count(int feed_view_count) {
        this.feed_view_count = feed_view_count;
    }

    public int getFeed_comment_count() {
        return feed_comment_count;
    }

    public void setFeed_comment_count(int feed_comment_count) {
        this.feed_comment_count = feed_comment_count;
    }
}
