package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description 专辑列表
 */
public class MusicAlbumListBean extends BaseListBean{

    /**
     * id : 2
     * created_at : 2017-03-15 17:04:31
     * updated_at : 2017-03-15 17:04:34
     * title : 专辑2
     * storage : {"id":5,"image_width":1080,"image_height":1800}
     * taste_count : 0
     * share_count : 0
     * comment_count : 0
     * collect_count : 0
     */

    private int id;
    private String created_at;
    private String updated_at;
    private String title;
    private StorageBean storage;
    private int taste_count;
    private int share_count;
    private int comment_count;
    private int collect_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StorageBean getStorage() {
        return storage;
    }

    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }

    public int getTaste_count() {
        return taste_count;
    }

    public void setTaste_count(int taste_count) {
        this.taste_count = taste_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public static class StorageBean implements Parcelable{
        /**
         * id : 5
         * image_width : 1080
         * image_height : 1800
         */

        private int id;
        private int image_width;
        private int image_height;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImage_width() {
            return image_width;
        }

        public void setImage_width(int image_width) {
            this.image_width = image_width;
        }

        public int getImage_height() {
            return image_height;
        }

        public void setImage_height(int image_height) {
            this.image_height = image_height;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.image_width);
            dest.writeInt(this.image_height);
        }

        public StorageBean() {
        }

        protected StorageBean(Parcel in) {
            this.id = in.readInt();
            this.image_width = in.readInt();
            this.image_height = in.readInt();
        }

        public static final Creator<StorageBean> CREATOR = new Creator<StorageBean>() {
            @Override
            public StorageBean createFromParcel(Parcel source) {
                return new StorageBean(source);
            }

            @Override
            public StorageBean[] newArray(int size) {
                return new StorageBean[size];
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
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.title);
        dest.writeParcelable(this.storage, flags);
        dest.writeInt(this.taste_count);
        dest.writeInt(this.share_count);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.collect_count);
    }

    public MusicAlbumListBean() {
    }

    protected MusicAlbumListBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.title = in.readString();
        this.storage = in.readParcelable(StorageBean.class.getClassLoader());
        this.taste_count = in.readInt();
        this.share_count = in.readInt();
        this.comment_count = in.readInt();
        this.collect_count = in.readInt();
    }

    public static final Creator<MusicAlbumListBean> CREATOR = new Creator<MusicAlbumListBean>() {
        @Override
        public MusicAlbumListBean createFromParcel(Parcel source) {
            return new MusicAlbumListBean(source);
        }

        @Override
        public MusicAlbumListBean[] newArray(int size) {
            return new MusicAlbumListBean[size];
        }
    };
}
