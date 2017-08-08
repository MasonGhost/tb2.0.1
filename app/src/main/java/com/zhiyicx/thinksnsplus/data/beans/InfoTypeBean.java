package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description 资讯分类列表
 */
public class InfoTypeBean extends BaseListBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id = 1L;
    private List<InfoTypeCatesBean> my_cates;
    private List<InfoTypeCatesBean> more_cates;

    public List<InfoTypeCatesBean> getMy_cates() {
        return my_cates;
    }

    public void setMy_cates(List<InfoTypeCatesBean> my_cates) {
        this.my_cates = my_cates;
    }

    public List<InfoTypeCatesBean> getMore_cates() {
        return more_cates;
    }

    public void setMore_cates(List<InfoTypeCatesBean> more_cates) {
        this.more_cates = more_cates;
    }

    public InfoTypeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeTypedList(this.my_cates);
        dest.writeTypedList(this.more_cates);
    }

    protected InfoTypeBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.my_cates = in.createTypedArrayList(InfoTypeCatesBean.CREATOR);
        this.more_cates = in.createTypedArrayList(InfoTypeCatesBean.CREATOR);
    }

    public static final Creator<InfoTypeBean> CREATOR = new Creator<InfoTypeBean>() {
        @Override
        public InfoTypeBean createFromParcel(Parcel source) {
            return new InfoTypeBean(source);
        }

        @Override
        public InfoTypeBean[] newArray(int size) {
            return new InfoTypeBean[size];
        }
    };
}
