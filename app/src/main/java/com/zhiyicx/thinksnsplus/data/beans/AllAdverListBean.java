package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/07/31/17:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class AllAdverListBean extends BaseListBean {

    /**
     * id : 1
     * channel : boot
     * space : boot
     * alias : 启动图广告
     * allow_type : image
     * format : {"image":{"image":"图片|string","link":"链接|string"}}
     * created_at : 2017-07-27 06:56:36
     * updated_at : 2017-07-27 06:56:36
     */
    @Id
    private Long id;
    private String channel;
    private String space;
    private String alias;
    private String allow_type;
    private String format;
    private String created_at;
    private String updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAllow_type() {
        return allow_type;
    }

    public void setAllow_type(String allow_type) {
        this.allow_type = allow_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.channel);
        dest.writeString(this.space);
        dest.writeString(this.alias);
        dest.writeString(this.allow_type);
        dest.writeString(this.format);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public AllAdverListBean() {
    }

    protected AllAdverListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.channel = in.readString();
        this.space = in.readString();
        this.alias = in.readString();
        this.allow_type = in.readString();
        this.format = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    @Generated(hash = 258784411)
    public AllAdverListBean(Long id, String channel, String space, String alias,
            String allow_type, String format, String created_at, String updated_at) {
        this.id = id;
        this.channel = channel;
        this.space = space;
        this.alias = alias;
        this.allow_type = allow_type;
        this.format = format;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static final Creator<AllAdverListBean> CREATOR = new Creator<AllAdverListBean>() {
        @Override
        public AllAdverListBean createFromParcel(Parcel source) {
            return new AllAdverListBean(source);
        }

        @Override
        public AllAdverListBean[] newArray(int size) {
            return new AllAdverListBean[size];
        }
    };
}
