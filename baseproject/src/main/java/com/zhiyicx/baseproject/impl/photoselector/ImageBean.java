package com.zhiyicx.baseproject.impl.photoselector;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LiuChao
 * @describe 图片信息的实体类
 * @date 2017/1/13
 * @contact email:450127106@qq.com
 */

public class ImageBean implements Parcelable {

    private String imgUrl;// 图片的地址
    private int imgWidth;// 图片宽度
    private int imgHeight;// 图片高度

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeInt(this.imgWidth);
        dest.writeInt(this.imgHeight);
    }

    public ImageBean() {
    }

    protected ImageBean(Parcel in) {
        this.imgUrl = in.readString();
        this.imgWidth = in.readInt();
        this.imgHeight = in.readInt();
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
}
