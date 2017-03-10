package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListBean extends BaseListBean {
    private List<String> iamges = new ArrayList<>();
    private String ids;

    public List<String> getIamges() {
        return iamges;
    }

    public void setIamges(List<String> iamges) {
        this.iamges = iamges;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringList(this.iamges);
        dest.writeString(this.ids);
    }

    public InfoListBean() {
    }

    protected InfoListBean(Parcel in) {
        super(in);
        this.iamges = in.createStringArrayList();
        this.ids = in.readString();
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
