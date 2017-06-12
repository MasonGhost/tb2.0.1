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

public class ImageBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 7077767228027093637L;  //Serializable 用于 DynamicDetailBean中 Convert base64

    /**
     * storage_id : 2
     * width : 1152.0
     * height : 1701.0
     */
    private String imgUrl;// 图片的地址
    private int storage_id;
    private int toll_type;
    private float toll_monye;
    private double width;
    private double height;
    private int part;// 图片压缩比例
    private String imgMimeType;// 图片类型
    private Toll toll;

    public Toll getToll() {
        return toll;
    }

    public void setToll(Toll toll) {
        this.toll = toll;
        if (toll == null) {
            setToll_type(0);
            setToll_monye(0);
            return;
        }
        setToll_type(toll.toll_type);
        setToll_monye(toll.toll_money);
    }

    public int getToll_type() {
        return toll_type;
    }

    public void setToll_type(int toll_way) {
        this.toll_type = toll_way;
    }

    public float getToll_monye() {
        return toll_monye;
    }

    public void setToll_monye(float toll_monye) {
        this.toll_monye = toll_monye;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

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

    public ImageBean(int storage_id) {
        this.storage_id = storage_id;
    }

    public String getImgMimeType() {
        return imgMimeType;
    }

    public void setImgMimeType(String imgMimeType) {
        this.imgMimeType = imgMimeType;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", storage_id=" + storage_id +
                ", width=" + width +
                ", height=" + height +
                ", part=" + part +
                ", imgMimeType='" + imgMimeType + '\'' +
                "\n"+"toll_type="+toll_type+
                "\n"+"toll_monye="+toll_monye+"\n"+
                '}';
    }

    public ImageBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeInt(this.storage_id);
        dest.writeInt(this.toll_type);
        dest.writeFloat(this.toll_monye);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeInt(this.part);
        dest.writeString(this.imgMimeType);
        dest.writeParcelable(this.toll, flags);
    }

    protected ImageBean(Parcel in) {
        this.imgUrl = in.readString();
        this.storage_id = in.readInt();
        this.toll_type = in.readInt();
        this.toll_monye = in.readFloat();
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.part = in.readInt();
        this.imgMimeType = in.readString();
        this.toll = in.readParcelable(Toll.class.getClassLoader());
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
