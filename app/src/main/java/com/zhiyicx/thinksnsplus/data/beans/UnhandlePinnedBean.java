package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

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
    }

    public UnhandlePinnedBean() {
    }

    protected UnhandlePinnedBean(Parcel in) {
        this.news = in.readParcelable(CountBean.class.getClassLoader());
        this.feeds = in.readParcelable(CountBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UnhandlePinnedBean> CREATOR = new Parcelable.Creator<UnhandlePinnedBean>() {
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
                '}';
    }
}
