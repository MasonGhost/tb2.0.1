package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 音乐详情
 */
public class MusicDetaisBean implements Parcelable {


    /**
     * id : 1
     * created_at : 2017-03-10 18:05:22
     * updated_at : 2017-03-15 01:37:34
     * deleted_at : 22
     * title : 音乐1
     * singer : {"id":1,"created_at":"2017-03-15 09:36:06","updated_at":"2017-03-15 09:36:07",
     * "name":"歌手1","cover":3}
     * storage : 2
     * last_time : 180
     * lyric : 啦啦啦啦啦啦
     * taste_count : 3
     * share_count : 0
     * comment_count : 0
     */

    private int id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String title;
    private SingerBean singer;
    private int storage;
    private int last_time;
    private String lyric;
    private int taste_count;
    private int share_count;
    private int comment_count;

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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SingerBean getSinger() {
        return singer;
    }

    public void setSinger(SingerBean singer) {
        this.singer = singer;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getLast_time() {
        return last_time;
    }

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
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

    public static class SingerBean implements Parcelable{
        /**
         * id : 1
         * created_at : 2017-03-15 09:36:06
         * updated_at : 2017-03-15 09:36:07
         * name : 歌手1
         * cover : 3
         */

        private int id;
        private String created_at;
        private String updated_at;
        private String name;
        private int cover;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCover() {
            return cover;
        }

        public void setCover(int cover) {
            this.cover = cover;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeString(this.name);
            dest.writeInt(this.cover);
        }

        public SingerBean() {
        }

        protected SingerBean(Parcel in) {
            this.id = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.name = in.readString();
            this.cover = in.readInt();
        }

        public static final Creator<SingerBean> CREATOR = new Creator<SingerBean>() {
            @Override
            public SingerBean createFromParcel(Parcel source) {
                return new SingerBean(source);
            }

            @Override
            public SingerBean[] newArray(int size) {
                return new SingerBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeString(this.title);
        dest.writeParcelable(this.singer, flags);
        dest.writeInt(this.storage);
        dest.writeInt(this.last_time);
        dest.writeString(this.lyric);
        dest.writeInt(this.taste_count);
        dest.writeInt(this.share_count);
        dest.writeInt(this.comment_count);
    }

    public MusicDetaisBean() {
    }

    protected MusicDetaisBean(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.title = in.readString();
        this.singer = in.readParcelable(SingerBean.class.getClassLoader());
        this.storage = in.readInt();
        this.last_time = in.readInt();
        this.lyric = in.readString();
        this.taste_count = in.readInt();
        this.share_count = in.readInt();
        this.comment_count = in.readInt();
    }

    public static final Creator<MusicDetaisBean> CREATOR = new Creator<MusicDetaisBean>() {
        @Override
        public MusicDetaisBean createFromParcel(Parcel source) {
            return new MusicDetaisBean(source);
        }

        @Override
        public MusicDetaisBean[] newArray(int size) {
            return new MusicDetaisBean[size];
        }
    };
}
