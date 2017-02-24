package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author LiuChao
 * @describe 动态工具栏内容的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicToolBean implements Parcelable {
    public static final int STATUS_DIGG_FEED_UNCHECKED=0;
    public static final int STATUS_DIGG_FEED_CHECKED=1;

    @Id
    private Long feed_mark;// 属于哪条动态
    private int feed_digg_count;// 点赞数
    private int feed_view_count;// 浏览量
    private int feed_comment_count;// 评论数

    public int getIs_digg_feed() {
        return is_digg_feed;
    }

    public void setIs_digg_feed(int is_digg_feed) {
        this.is_digg_feed = is_digg_feed;
    }

    private int is_digg_feed;// 评论数

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
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

    public DynamicToolBean() {
    }

    @Generated(hash = 1207968670)
    public DynamicToolBean(Long feed_mark, int feed_digg_count, int feed_view_count,
            int feed_comment_count, int is_digg_feed) {
        this.feed_mark = feed_mark;
        this.feed_digg_count = feed_digg_count;
        this.feed_view_count = feed_view_count;
        this.feed_comment_count = feed_comment_count;
        this.is_digg_feed = is_digg_feed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.feed_mark);
        dest.writeInt(this.feed_digg_count);
        dest.writeInt(this.feed_view_count);
        dest.writeInt(this.feed_comment_count);
        dest.writeInt(this.is_digg_feed);
    }

    protected DynamicToolBean(Parcel in) {
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_digg_count = in.readInt();
        this.feed_view_count = in.readInt();
        this.feed_comment_count = in.readInt();
        this.is_digg_feed = in.readInt();
    }

    public static final Creator<DynamicToolBean> CREATOR = new Creator<DynamicToolBean>() {
        @Override
        public DynamicToolBean createFromParcel(Parcel source) {
            return new DynamicToolBean(source);
        }

        @Override
        public DynamicToolBean[] newArray(int size) {
            return new DynamicToolBean[size];
        }
    };
}
