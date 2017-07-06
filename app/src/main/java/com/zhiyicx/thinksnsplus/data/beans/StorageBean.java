package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class StorageBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id : 5
     * image_width : 1080
     * image_height : 1800
     */

    private int id;
    private String size;
    private int image_width;
    private int image_height;

    public void setSize(String size) {
        this.size = size;
        if (size != null && size.length() > 0) {
            String[] sizes = size.split("x");
            this.image_width = Integer.parseInt(sizes[0]);
            this.image_height = Integer.parseInt(sizes[1]);
        }
    }

    public int getWidth() {
        if (size != null && size.length() > 0) {
            String[] sizes = size.split("x");
            return Integer.parseInt(sizes[0]);
        }
        return 100;
    }

    public int getHeight() {
        if (size != null && size.length() > 0) {
            String[] sizes = size.split("x");
            return Integer.parseInt(sizes[1]);
        }
        return 100;
    }

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
        dest.writeString(this.size);
        dest.writeInt(this.image_width);
        dest.writeInt(this.image_height);
    }

    public StorageBean() {
    }

    protected StorageBean(Parcel in) {
        this.id = in.readInt();
        this.size = in.readString();
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