package com.zhiyicx.baseproject.impl.photoselector;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author LiuChao
 * @describe 图片信息的实体类
 * @date 2017/1/13
 * @contact email:450127106@qq.com
 */

public class ImageBean implements Parcelable ,Serializable {

    /**
     * storage_id : 2
     * width : 1152.0
     * height : 1701.0
     */
    private String imgUrl;// 图片的地址
    private int storage_id;
    private double width;
    private double height;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(int storage_id) {
        this.storage_id = storage_id;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeInt(this.storage_id);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
    }

    public ImageBean(int storage_id) {
        this.storage_id=storage_id;
    }
    public ImageBean() {
    }
    protected ImageBean(Parcel in) {
        this.imgUrl = in.readString();
        this.storage_id = in.readInt();
        this.width = in.readDouble();
        this.height = in.readDouble();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public String toString() {
        return "ImageBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", storage_id=" + storage_id +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
