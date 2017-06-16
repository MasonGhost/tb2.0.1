package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/16/14:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SendDynamicDataBeanV2 implements Serializable, Parcelable {
    private static final long serialVersionUID = 4113706643912669235L;

    /**
     * feed_title : 标题
     * feed_content : 内容
     * feed_from : 5
     * feed_mark : xxxxx1
     * storage_task : [{"id":1,"amount":100,"type":"read"},{"id":1,"amount":100,"type":"read"}]
     * feed_latitude : 12.32132123
     * feed_longtitude : 32.33332123
     * feed_geohash : GdUDHyfghjd==
     * amount : 450
     */

    private String feed_title;
    private String feed_content;
    private String feed_from;
    private String feed_mark;
    private String feed_latitude;
    private String feed_longtitude;
    private String feed_geohash;
    private int amount;
    private List<StorageTaskBean> storage_task;

    public String getFeed_title() {
        return feed_title;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }

    public String getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(String feed_from) {
        this.feed_from = feed_from;
    }

    public String getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(String feed_mark) {
        this.feed_mark = feed_mark;
    }

    public String getFeed_latitude() {
        return feed_latitude;
    }

    public void setFeed_latitude(String feed_latitude) {
        this.feed_latitude = feed_latitude;
    }

    public String getFeed_longtitude() {
        return feed_longtitude;
    }

    public void setFeed_longtitude(String feed_longtitude) {
        this.feed_longtitude = feed_longtitude;
    }

    public String getFeed_geohash() {
        return feed_geohash;
    }

    public void setFeed_geohash(String feed_geohash) {
        this.feed_geohash = feed_geohash;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<StorageTaskBean> getStorage_task() {
        return storage_task;
    }

    public void setStorage_task(List<StorageTaskBean> storage_task) {
        this.storage_task = storage_task;
    }

    public static class StorageTaskBean {
        /**
         * id : 1
         * amount : 100
         * type : read
         */

        private int id;
        private int amount;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.feed_title);
        dest.writeString(this.feed_content);
        dest.writeString(this.feed_from);
        dest.writeString(this.feed_mark);
        dest.writeString(this.feed_latitude);
        dest.writeString(this.feed_longtitude);
        dest.writeString(this.feed_geohash);
        dest.writeInt(this.amount);
        dest.writeList(this.storage_task);
    }

    public SendDynamicDataBeanV2() {
    }

    protected SendDynamicDataBeanV2(Parcel in) {
        this.feed_title = in.readString();
        this.feed_content = in.readString();
        this.feed_from = in.readString();
        this.feed_mark = in.readString();
        this.feed_latitude = in.readString();
        this.feed_longtitude = in.readString();
        this.feed_geohash = in.readString();
        this.amount = in.readInt();
        this.storage_task = new ArrayList<StorageTaskBean>();
        in.readList(this.storage_task, StorageTaskBean.class.getClassLoader());
    }

    public static final Creator<SendDynamicDataBeanV2> CREATOR = new Creator<SendDynamicDataBeanV2>() {
        @Override
        public SendDynamicDataBeanV2 createFromParcel(Parcel source) {
            return new SendDynamicDataBeanV2(source);
        }

        @Override
        public SendDynamicDataBeanV2[] newArray(int size) {
            return new SendDynamicDataBeanV2[size];
        }
    };
}