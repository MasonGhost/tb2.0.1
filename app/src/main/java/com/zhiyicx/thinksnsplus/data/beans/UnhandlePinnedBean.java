package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/10/24
 * @Contact master.jungle68@gmail.com
 */
public class UnhandlePinnedBean implements Parcelable {


    /**
     * news : {"time":null,"count":0}
     * feeds : {"time":"2017-09-15 10:04:19","count":1}
     */

    private CountBean news;
    private CountBean feeds;
    @SerializedName("group-posts")
    private CountBean groupPosts;
    @SerializedName("group-comments")
    private CountBean groupComments;

    public CountBean getNews() {
        return news;
    }

    public void setNews(CountBean news) {
        this.news = news;
    }

    public CountBean getFeeds() {
        return feeds;
    }

    public void setFeeds(CountBean feeds) {
        this.feeds = feeds;
    }

    public CountBean getGroupPosts() {
        return groupPosts;
    }

    public void setGroupPosts(CountBean groupPosts) {
        this.groupPosts = groupPosts;
    }

    public CountBean getGroupComments() {
        return groupComments;
    }

    public void setGroupComments(CountBean groupComments) {
        this.groupComments = groupComments;
    }

    public static class CountBean implements Parcelable {
        /**
         * time : 2017-09-15 10:04:19
         * count : 1
         */

        private String time;
        private int count;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.time);
            dest.writeInt(this.count);
        }

        public CountBean() {
        }

        protected CountBean(Parcel in) {
            this.time = in.readString();
            this.count = in.readInt();
        }

        public static final Parcelable.Creator<CountBean> CREATOR = new Parcelable.Creator<CountBean>() {
            @Override
            public CountBean createFromParcel(Parcel source) {
                return new CountBean(source);
            }

            @Override
            public CountBean[] newArray(int size) {
                return new CountBean[size];
            }
        };

        @Override
        public String toString() {
            return "CountBean{" +
                    "time='" + time + '\'' +
                    ", count=" + count +
                    '}';
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.news, flags);
        dest.writeParcelable(this.feeds, flags);
        dest.writeParcelable(this.groupPosts, flags);
        dest.writeParcelable(this.groupComments, flags);
    }

    public UnhandlePinnedBean() {
    }

    protected UnhandlePinnedBean(Parcel in) {
        this.news = in.readParcelable(CountBean.class.getClassLoader());
        this.feeds = in.readParcelable(CountBean.class.getClassLoader());
        this.groupPosts = in.readParcelable(CountBean.class.getClassLoader());
        this.groupComments = in.readParcelable(CountBean.class.getClassLoader());
    }

    public static final Creator<UnhandlePinnedBean> CREATOR = new Creator<UnhandlePinnedBean>() {
        @Override
        public UnhandlePinnedBean createFromParcel(Parcel source) {
            return new UnhandlePinnedBean(source);
        }

        @Override
        public UnhandlePinnedBean[] newArray(int size) {
            return new UnhandlePinnedBean[size];
        }
    };

    @Override
    public String toString() {
        return "UnhandlePinnedBean{" +
                "news=" + news +
                ", feeds=" + feeds +
                ", groupPosts=" + groupPosts +
                ", groupComments=" + groupComments +
                '}';
    }
}
