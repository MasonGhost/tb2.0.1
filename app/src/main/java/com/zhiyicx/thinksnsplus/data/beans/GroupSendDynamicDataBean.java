package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/18/14:19
 * @Email Jliuer@aliyun.com
 * @Description 圈子发布动态的数据类
 */
public class GroupSendDynamicDataBean implements Serializable, Parcelable {

    private static final long serialVersionUID = 1234L;
    /**
     * title : 圈子动态标题
     * content : 圈子动态内容
     * images : [{"id":1},{"id":2}]
     */
    private long group_post_mark;
    private int group_id;
    private String title;
    private String content;
    private List<ImagesBean> images;
    private List<ImageBean> photos;// 待发送的本地图片信息

    public long getGroup_post_mark() {
        return group_post_mark;
    }

    public void setGroup_post_mark(long group_post_mark) {
        this.group_post_mark = group_post_mark;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public List<ImageBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageBean> photos) {
        this.photos = photos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 12234L;
        /**
         * id : 1
         */

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
        }

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            this.id = in.readInt();
        }

        public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel source) {
                return new ImagesBean(source);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.group_id);
        dest.writeLong(this.group_post_mark);
        dest.writeString(this.content);
        dest.writeList(this.images);
        dest.writeList(this.photos);
    }

    public GroupSendDynamicDataBean() {
    }

    protected GroupSendDynamicDataBean(Parcel in) {
        this.title = in.readString();
        this.group_id = in.readInt();
        this.group_post_mark = in.readLong();
        this.content = in.readString();
        this.images = new ArrayList<>();
        this.photos = new ArrayList<>();
        in.readList(this.images, ImagesBean.class.getClassLoader());
        in.readList(this.photos, ImagesBean.class.getClassLoader());
    }

    public static final Creator<GroupSendDynamicDataBean> CREATOR = new Creator<GroupSendDynamicDataBean>() {
        @Override
        public GroupSendDynamicDataBean createFromParcel(Parcel source) {
            return new GroupSendDynamicDataBean(source);
        }

        @Override
        public GroupSendDynamicDataBean[] newArray(int size) {
            return new GroupSendDynamicDataBean[size];
        }
    };
}
