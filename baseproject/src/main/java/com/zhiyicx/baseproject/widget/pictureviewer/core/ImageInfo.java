package com.zhiyicx.baseproject.widget.pictureviewer.core;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;


/**
 * @Describe  image info  define
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
public class ImageInfo implements Parcelable{

    // 内部图片在整个手机界面的位置
    RectF mRect = new RectF();

    // 控件在窗口的位置
    RectF mImgRect = new RectF();

    RectF mWidgetRect = new RectF();

    RectF mBaseRect = new RectF();

    PointF mScreenCenter = new PointF();

    float mScale;

    float mDegrees;

    ImageView.ScaleType mScaleType;
    int with;
    int height;

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ImageInfo(RectF rect, RectF img, RectF widget, RectF base, PointF screenCenter, float scale, float degrees, ImageView.ScaleType scaleType) {
        mRect.set(rect);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
        mBaseRect.set(base);
        mScreenCenter.set(screenCenter);
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "mRect=" + mRect +
                ", mImgRect=" + mImgRect +
                ", mWidgetRect=" + mWidgetRect +
                ", mBaseRect=" + mBaseRect +
                ", mScreenCenter=" + mScreenCenter +
                ", mScale=" + mScale +
                ", mDegrees=" + mDegrees +
                ", mScaleType=" + mScaleType +
                ", with=" + with +
                ", height=" + height +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mRect, flags);
        dest.writeParcelable(this.mImgRect, flags);
        dest.writeParcelable(this.mWidgetRect, flags);
        dest.writeParcelable(this.mBaseRect, flags);
        dest.writeParcelable(this.mScreenCenter, flags);
        dest.writeFloat(this.mScale);
        dest.writeFloat(this.mDegrees);
        dest.writeInt(this.mScaleType == null ? -1 : this.mScaleType.ordinal());
        dest.writeInt(this.with);
        dest.writeInt(this.height);
    }

    protected ImageInfo(Parcel in) {
        this.mRect = in.readParcelable(RectF.class.getClassLoader());
        this.mImgRect = in.readParcelable(RectF.class.getClassLoader());
        this.mWidgetRect = in.readParcelable(RectF.class.getClassLoader());
        this.mBaseRect = in.readParcelable(RectF.class.getClassLoader());
        this.mScreenCenter = in.readParcelable(PointF.class.getClassLoader());
        this.mScale = in.readFloat();
        this.mDegrees = in.readFloat();
        int tmpMScaleType = in.readInt();
        this.mScaleType = tmpMScaleType == -1 ? null : ImageView.ScaleType.values()[tmpMScaleType];
        this.with = in.readInt();
        this.height = in.readInt();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };
}
