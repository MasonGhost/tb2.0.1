package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListBean extends BaseListBean {


    /**
     * id : 1
     * title : 123123
     * updated_at : 2017-03-13 09:59:32
     * storage : {"id":1,"image_width":1,"image_height":1}
     */

    private int id;
    private String title;
    private String updated_at;
    private StorageBean storage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public StorageBean getStorage() {
        return storage;
    }

    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }

    public static class StorageBean implements Parcelable{
        /**
         * id : 1
         * image_width : 1
         * image_height : 1
         */

        private int id;
        private int image_width;
        private int image_height;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImage_width() {
            return image_width;
        }

        public void setImage_width(int image_width) {
            this.image_width = image_width;
        }

        public int getImage_height() {
            return image_height;
        }

        public void setImage_height(int image_height) {
            this.image_height = image_height;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.image_width);
            dest.writeInt(this.image_height);
        }

        public StorageBean() {
        }

        protected StorageBean(Parcel in) {
            this.id = in.readInt();
            this.image_width = in.readInt();
            this.image_height = in.readInt();
        }

        public static final Creator<StorageBean> CREATOR = new Creator<StorageBean>() {
            @Override
            public StorageBean createFromParcel(Parcel source) {
                return new StorageBean(source);
            }

            @Override
            public StorageBean[] newArray(int size) {
                return new StorageBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.storage, flags);
    }

    public InfoListBean() {
    }

    protected InfoListBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.title = in.readString();
        this.updated_at = in.readString();
        this.storage = in.readParcelable(StorageBean.class.getClassLoader());
    }

    public static final Creator<InfoListBean> CREATOR = new Creator<InfoListBean>() {
        @Override
        public InfoListBean createFromParcel(Parcel source) {
            return new InfoListBean(source);
        }

        @Override
        public InfoListBean[] newArray(int size) {
            return new InfoListBean[size];
        }
    };
}
