package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Author Jliuer
 * @Date 2017/07/31/17:14
 * @Email Jliuer@aliyun.com
 * @Description 动态列表模拟数据广告
 */
public class DynamicListAdvert implements Serializable, Parcelable {
    private static final long serialVersionUID = 124L;

    /**
     * avatar : 头像图|string
     * name : 用户名|string
     * content : 内容|string
     * image : 图片|string
     * time : 时间|date
     * duration : 持续时间|duration
     */

    private String avatar;
    private String name;
    private String content;
    private String title;
    private String image;
    private String time;
    private String link;
    private String duration;

    public int getDuration() {
        try {
            return Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeString(this.duration);
        dest.writeString(this.link);
        dest.writeString(this.content);
        dest.writeString(this.image);
        dest.writeString(this.time);
        dest.writeString(this.title);
    }

    public DynamicListAdvert() {
    }

    protected DynamicListAdvert(Parcel in) {
        this.avatar = in.readString();
        this.name = in.readString();
        this.link = in.readString();
        this.content = in.readString();
        this.duration = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.time = in.readString();
    }

    public static final Creator<DynamicListAdvert> CREATOR = new Creator<DynamicListAdvert>() {
        @Override
        public DynamicListAdvert createFromParcel(Parcel source) {
            return new DynamicListAdvert(source);
        }

        @Override
        public DynamicListAdvert[] newArray(int size) {
            return new DynamicListAdvert[size];
        }
    };

    public static DynamicDetailBeanV2 advert2Dynamic(DynamicListAdvert advert, long max_id) {
        DynamicDetailBeanV2 dynamicDetailBeanV2 = new DynamicDetailBeanV2();
        dynamicDetailBeanV2.setFeed_from(-1);// 广告位标识
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUser_id(-1L);
        dynamicDetailBeanV2.setUser_id(-1L);
        userInfoBean.setName(advert.getName());// 广告名字
        userInfoBean.setAvatar(advert.getAvatar());// 广告头像
        dynamicDetailBeanV2.setId(max_id);// max_id 转移
        dynamicDetailBeanV2.setFeed_mark(System.currentTimeMillis());
        dynamicDetailBeanV2.setDeleted_at(advert.getLink());// 广告外链
        dynamicDetailBeanV2.setUserInfoBean(userInfoBean);
        dynamicDetailBeanV2.handleData();
        dynamicDetailBeanV2.setFeed_content(advert.getContent());// 广告内容
        dynamicDetailBeanV2.setCreated_at(advert.getTime());// 广告时间
        dynamicDetailBeanV2.setUpdated_at(advert.getTime());// 广告时间
        DynamicDetailBeanV2.ImagesBean imageBean = new DynamicDetailBeanV2.ImagesBean();
        imageBean.setImgUrl(advert.getImage());// 广告图片
        dynamicDetailBeanV2.setImages(Collections.singletonList(imageBean));
        return dynamicDetailBeanV2;
    }

    public static InfoListDataBean advert2Info(DynamicListAdvert advert, long max_id) {
        InfoListDataBean infoListDataBean = new InfoListDataBean();
        InfoListDataBean.InfoCategory category = new InfoListDataBean.InfoCategory();
        category.setName("广告");
        infoListDataBean.setFrom("广告");
        infoListDataBean.setInfo_type(-1L);
        infoListDataBean.setCategory(category);
        infoListDataBean.setUser_id(-1L);// 广告位标识
        infoListDataBean.setMaxId(max_id);// max_id 转移
        infoListDataBean.setId(max_id);
        infoListDataBean.setTitle(advert.getTitle());// 广告内容
        infoListDataBean.setCreated_at(advert.getTime());// 广告时间
        infoListDataBean.setUpdated_at(advert.getLink());// 广告外链
        infoListDataBean.setAuthor(advert.getImage());// 广告图片
        return infoListDataBean;
    }
}
