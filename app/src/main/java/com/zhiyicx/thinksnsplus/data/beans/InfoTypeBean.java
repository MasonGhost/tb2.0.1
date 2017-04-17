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
    private List<InfoTypeMyCatesBean> my_cates;
    private List<InfoTypeMoreCatesBean> more_cates;

    public List<InfoTypeMyCatesBean> getMy_cates() {
        return my_cates;
    }

    public void setMy_cates(List<InfoTypeMyCatesBean> my_cates) {
        this.my_cates = my_cates;
    }

    public List<InfoTypeMoreCatesBean> getMore_cates() {
        return more_cates;
    }

    public void setMore_cates(List<InfoTypeMoreCatesBean> more_cates) {
        this.more_cates = more_cates;
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

    public InfoTypeBean() {
    }

}
