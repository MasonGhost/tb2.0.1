package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/09/04/13:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TSNotifyExtraBean implements Parcelable, Serializable {

    private static final long serialVersionUID = 4401349143860920340L;

    private UserInfoBean user;
    private CommentedBean comment;
    private PinnedBean pinned;
    private DynamicDetailBeanV2 feed;
    private InfoListDataBean news;

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public CommentedBean getComment() {
        return comment;
    }

    public void setComment(CommentedBean comment) {
        this.comment = comment;
    }

    public PinnedBean getPinned() {
        return pinned;
    }

    public void setPinned(PinnedBean pinned) {
        this.pinned = pinned;
    }

    public DynamicDetailBeanV2 getFeed() {
        return feed;
    }

    public void setFeed(DynamicDetailBeanV2 feed) {
        this.feed = feed;
    }

    public InfoListDataBean getNews() {
        return news;
    }

    public void setNews(InfoListDataBean news) {
        this.news = news;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.pinned, flags);
        dest.writeParcelable(this.feed, flags);
        dest.writeParcelable(this.news, flags);
    }

    public TSNotifyExtraBean() {
    }

    protected TSNotifyExtraBean(Parcel in) {
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.comment = in.readParcelable(CommentedBean.class.getClassLoader());
        this.pinned = in.readParcelable(PinnedBean.class.getClassLoader());
        this.feed = in.readParcelable(DynamicDetailBeanV2.class.getClassLoader());
        this.news = in.readParcelable(InfoListDataBean.class.getClassLoader());
    }

    public static final Creator<TSNotifyExtraBean> CREATOR = new Creator<TSNotifyExtraBean>() {
        @Override
        public TSNotifyExtraBean createFromParcel(Parcel source) {
            return new TSNotifyExtraBean(source);
        }

        @Override
        public TSNotifyExtraBean[] newArray(int size) {
            return new TSNotifyExtraBean[size];
        }
    };
}
