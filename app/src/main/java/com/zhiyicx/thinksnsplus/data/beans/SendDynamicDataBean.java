package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @author LiuChao
 * @describe 跳转到发送动态页面，可能需要携带一些数据用来出里相关逻辑，用对象打包传过去
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class SendDynamicDataBean implements Serializable, Parcelable {
    private static final long serialVersionUID = 4113706643912669235L;
    /**
     * 动态类型
     */
    public static final int PHOTO_TEXT_DYNAMIC = 0;// 图片文字动态
    public static final int TEXT_ONLY_DYNAMIC = 1;// 纯文字动态

    /**
     * 动态归属
     */
    public static final int MORMAL_DYNAMIC = 0;// 普通的动态类型，显示在首页而已
    public static final int GROUP_DYNAMIC = 1;// 属于频道的动态类型，会显示在频道和首页

    private int dynamicType;// 动态类型
    private List<ImageBean> dynamicPrePhotos;// 进入发送页面，已经选好的图片
    private int dynamicBelong;// 动态归属：属于哪儿的动态，频道，非频道。。。
    private long dynamicChannlId;// 动态所属频道id
    private long feedMark;// 动态唯一约束值

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public List<ImageBean> getDynamicPrePhotos() {
        return dynamicPrePhotos;
    }

    public void setDynamicPrePhotos(List<ImageBean> dynamicPrePhotos) {
        this.dynamicPrePhotos = dynamicPrePhotos;
    }

    public int getDynamicBelong() {
        return dynamicBelong;
    }

    public void setDynamicBelong(int dynamicBelong) {
        this.dynamicBelong = dynamicBelong;
    }

    public long getDynamicChannlId() {
        return dynamicChannlId;
    }

    public void setDynamicChannlId(long dynamicChannlId) {
        this.dynamicChannlId = dynamicChannlId;
    }

    public long getFeedMark() {
        return feedMark;
    }

    public void setFeedMark(long feedMark) {
        this.feedMark = feedMark;
    }

    public SendDynamicDataBean() {
    }

    @Override
    public String toString() {
        return "SendDynamicDataBean{" +
                "dynamicType=" + dynamicType +
                ", dynamicPrePhotos=" + dynamicPrePhotos +
                ", dynamicBelong=" + dynamicBelong +
                ", dynamicChannlId=" + dynamicChannlId +
                ", feedMark=" + feedMark +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dynamicType);
        dest.writeTypedList(this.dynamicPrePhotos);
        dest.writeInt(this.dynamicBelong);
        dest.writeLong(this.dynamicChannlId);
        dest.writeLong(this.feedMark);
    }

    protected SendDynamicDataBean(Parcel in) {
        this.dynamicType = in.readInt();
        this.dynamicPrePhotos = in.createTypedArrayList(ImageBean.CREATOR);
        this.dynamicBelong = in.readInt();
        this.dynamicChannlId = in.readLong();
        this.feedMark = in.readLong();
    }

    public static final Creator<SendDynamicDataBean> CREATOR = new Creator<SendDynamicDataBean>() {
        @Override
        public SendDynamicDataBean createFromParcel(Parcel source) {
            return new SendDynamicDataBean(source);
        }

        @Override
        public SendDynamicDataBean[] newArray(int size) {
            return new SendDynamicDataBean[size];
        }
    };
}
