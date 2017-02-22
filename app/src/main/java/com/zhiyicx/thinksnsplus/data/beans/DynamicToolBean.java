package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author LiuChao
 * @describe 动态工具栏内容的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicToolBean implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String feed_mark;// 属于哪条动态
    private int feed_digg_count;// 点赞数
    private int feed_view_count;// 浏览量
    private int feed_comment_count;// 评论数


    public String getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(String feed_mark) {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.feed_mark);
        dest.writeInt(this.feed_digg_count);
        dest.writeInt(this.feed_view_count);
        dest.writeInt(this.feed_comment_count);
    }

    protected DynamicToolBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = in.readString();
        this.feed_digg_count = in.readInt();
        this.feed_view_count = in.readInt();
        this.feed_comment_count = in.readInt();
    }

    @Generated(hash = 1638296577)
    public DynamicToolBean(Long id, String feed_mark, int feed_digg_count,
            int feed_view_count, int feed_comment_count) {
        this.id = id;
        this.feed_mark = feed_mark;
        this.feed_digg_count = feed_digg_count;
        this.feed_view_count = feed_view_count;
        this.feed_comment_count = feed_comment_count;
    }

    @Generated(hash = 1118422400)
    public DynamicToolBean() {
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
