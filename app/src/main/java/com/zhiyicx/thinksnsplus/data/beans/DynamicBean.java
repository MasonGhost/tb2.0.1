package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * @Describe 动态实体类：包含动态内容，工具栏参数，评论内容
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DynamicBean extends BaseListBean {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private Long feed_id;// 动态的唯一id
    private long user_id;// 发送动态的人id
    @ToOne(joinProperty = "feed_id")// DynamicBean的feed_id作为外键
    private DynamicDetailBean feed;
    @ToOne(joinProperty = "feed_id")// DynamicBean的feed_id作为外键
    private DynamicToolBean tool;
    // DynamicBean的feed_id与DynamicCommentBean的feed_id关联
    @ToMany(joinProperties = {@JoinProperty(name = "feed_id", referencedName = "feed_id")})
    private List<DynamicCommentBean> comments;

    public Long getFeed_id() {
        return feed.getFeed_id();
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public DynamicDetailBean getFeed() {
        return feed;
    }

    public void setFeed(DynamicDetailBean feed) {
        this.feed = feed;
    }

    public DynamicToolBean getTool() {
        return tool;
    }

    public void setTool(DynamicToolBean tool) {
        this.tool = tool;
    }

    public List<DynamicCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<DynamicCommentBean> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.feed_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.feed, flags);
        dest.writeParcelable(this.tool, flags);
        dest.writeTypedList(this.comments);
    }

    public DynamicBean() {
    }

    protected DynamicBean(Parcel in) {
        super(in);
        this.feed_id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readLong();
        this.feed = in.readParcelable(DynamicDetailBean.class.getClassLoader());
        this.tool = in.readParcelable(DynamicToolBean.class.getClassLoader());
        this.comments = in.createTypedArrayList(DynamicCommentBean.CREATOR);
    }

    public static final Creator<DynamicBean> CREATOR = new Creator<DynamicBean>() {
        @Override
        public DynamicBean createFromParcel(Parcel source) {
            return new DynamicBean(source);
        }

        @Override
        public DynamicBean[] newArray(int size) {
            return new DynamicBean[size];
        }
    };
}
