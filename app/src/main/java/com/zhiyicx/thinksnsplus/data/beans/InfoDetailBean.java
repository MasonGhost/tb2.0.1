package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/8
 * @contact email:648129313@qq.com
 */

public class InfoDetailBean extends BaseListBean{

    private InfoListDataBean infoData;
    private List<InfoDigListBean> infoDigList;
    private List<InfoCommentListBean> infoCommentList;
    private List<InfoListDataBean> relatedInfoList;

    public InfoDetailBean() {
    }

    public InfoListDataBean getInfoData() {
        return infoData;
    }

    public void setInfoData(InfoListDataBean infoData) {
        this.infoData = infoData;
    }

    public List<InfoDigListBean> getInfoDigList() {
        return infoDigList;
    }

    public void setInfoDigList(List<InfoDigListBean> infoDigList) {
        this.infoDigList = infoDigList;
    }

    public List<InfoCommentListBean> getInfoCommentList() {
        return infoCommentList;
    }

    public void setInfoCommentList(List<InfoCommentListBean> infoCommentList) {
        this.infoCommentList = infoCommentList;
    }

    public List<InfoListDataBean> getRelatedInfoList() {
        return relatedInfoList;
    }

    public void setRelatedInfoList(List<InfoListDataBean> relatedInfoList) {
        this.relatedInfoList = relatedInfoList;
    }

    @Override
    public String toString() {
        return "InfoDetailBean{" +
                "infoData=" + infoData +
                ", infoDigList=" + infoDigList +
                ", infoCommentList=" + infoCommentList +
                ", relatedInfoList=" + relatedInfoList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.infoData, flags);
        dest.writeTypedList(this.infoDigList);
        dest.writeTypedList(this.infoCommentList);
        dest.writeTypedList(this.relatedInfoList);
    }

    protected InfoDetailBean(Parcel in) {
        this.infoData = in.readParcelable(InfoListDataBean.class.getClassLoader());
        this.infoDigList = in.createTypedArrayList(InfoDigListBean.CREATOR);
        this.infoCommentList = in.createTypedArrayList(InfoCommentListBean.CREATOR);
        this.relatedInfoList = in.createTypedArrayList(InfoListDataBean.CREATOR);
    }

    public static final Creator<InfoDetailBean> CREATOR = new Creator<InfoDetailBean>() {
        @Override
        public InfoDetailBean createFromParcel(Parcel source) {
            return new InfoDetailBean(source);
        }

        @Override
        public InfoDetailBean[] newArray(int size) {
            return new InfoDetailBean[size];
        }
    };
}
