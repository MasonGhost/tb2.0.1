package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.thinksnsplus.data.source.local.data_convert.DynamicListConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/12/17:16
 * @Email Jliuer@aliyun.com
 * @Description 置顶动态单独保存
 */
@Entity
public class TopDynamicBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 1234536L;

    public static final Long TYPE_HOT = 0L;
    public static final Long TYPE_NEW = 1L;

    @Id
    @Unique
    private Long type;
    @Convert(converter = DynamicListConvert.class, columnType = String.class)
    private List<DynamicDetailBeanV2> topDynamics;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.type);
        dest.writeTypedList(this.topDynamics);
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public List<DynamicDetailBeanV2> getTopDynamics() {
        return this.topDynamics;
    }

    public void setTopDynamics(List<DynamicDetailBeanV2> topDynamics) {
        this.topDynamics = topDynamics;
    }

    public TopDynamicBean() {
    }

    protected TopDynamicBean(Parcel in) {
        this.type = (Long) in.readValue(Long.class.getClassLoader());
        this.topDynamics = in.createTypedArrayList(DynamicDetailBeanV2.CREATOR);
    }

    @Generated(hash = 1528762705)
    public TopDynamicBean(Long type, List<DynamicDetailBeanV2> topDynamics) {
        this.type = type;
        this.topDynamics = topDynamics;
    }

    public static final Creator<TopDynamicBean> CREATOR = new Creator<TopDynamicBean>() {
        @Override
        public TopDynamicBean createFromParcel(Parcel source) {
            return new TopDynamicBean(source);
        }

        @Override
        public TopDynamicBean[] newArray(int size) {
            return new TopDynamicBean[size];
        }
    };
}
