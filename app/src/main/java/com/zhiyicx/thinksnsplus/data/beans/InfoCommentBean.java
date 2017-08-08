package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/8
 * @contact email:648129313@qq.com
 */

public class InfoCommentBean implements Parcelable{

    private List<InfoCommentListBean> pinneds; // 置顶评论
    private List<InfoCommentListBean> comments;

    public List<InfoCommentListBean> getPinneds() {
        return pinneds;
    }

    public void setPinneds(List<InfoCommentListBean> pinneds) {
        this.pinneds = pinneds;
    }

    public List<InfoCommentListBean> getComments() {
        return comments;
    }

    public void setComments(List<InfoCommentListBean> comments) {
        this.comments = comments;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.pinneds);
        dest.writeTypedList(this.comments);
    }

    public InfoCommentBean() {
    }

    protected InfoCommentBean(Parcel in) {
        this.pinneds = in.createTypedArrayList(InfoCommentListBean.CREATOR);
        this.comments = in.createTypedArrayList(InfoCommentListBean.CREATOR);
    }

    public static final Creator<InfoCommentBean> CREATOR = new Creator<InfoCommentBean>() {
        @Override
        public InfoCommentBean createFromParcel(Parcel source) {
            return new InfoCommentBean(source);
        }

        @Override
        public InfoCommentBean[] newArray(int size) {
            return new InfoCommentBean[size];
        }
    };
}
