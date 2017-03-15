package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description 资讯分类列表
 */
public class InfoTypeBean extends BaseListBean{

    private List<MyCatesBean> my_cates;
    private List<MoreCatesBean> more_cates;

    public List<MyCatesBean> getMy_cates() {
        return my_cates;
    }

    public void setMy_cates(List<MyCatesBean> my_cates) {
        this.my_cates = my_cates;
    }

    public List<MoreCatesBean> getMore_cates() {
        return more_cates;
    }

    public void setMore_cates(List<MoreCatesBean> more_cates) {
        this.more_cates = more_cates;
    }

    public static class MyCatesBean implements Parcelable{
        /**
         * id : 1
         * name : 分类1
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public MyCatesBean() {
        }

        public MyCatesBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        protected MyCatesBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<MyCatesBean> CREATOR = new Creator<MyCatesBean>() {
            @Override
            public MyCatesBean createFromParcel(Parcel source) {
                return new MyCatesBean(source);
            }

            @Override
            public MyCatesBean[] newArray(int size) {
                return new MyCatesBean[size];
            }
        };
    }

    public static class MoreCatesBean implements Parcelable{
        /**
         * id : 1
         * name : 分类1
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public MoreCatesBean() {
        }

        public MoreCatesBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        protected MoreCatesBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<MoreCatesBean> CREATOR = new Creator<MoreCatesBean>() {
            @Override
            public MoreCatesBean createFromParcel(Parcel source) {
                return new MoreCatesBean(source);
            }

            @Override
            public MoreCatesBean[] newArray(int size) {
                return new MoreCatesBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.my_cates);
        dest.writeTypedList(this.more_cates);
    }

    public InfoTypeBean() {
    }

    protected InfoTypeBean(Parcel in) {
        super(in);
        this.my_cates = in.createTypedArrayList(MyCatesBean.CREATOR);
        this.more_cates = in.createTypedArrayList(MoreCatesBean.CREATOR);
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
