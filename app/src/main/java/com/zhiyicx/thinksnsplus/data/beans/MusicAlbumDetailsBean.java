package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
public class MusicAlbumDetailsBean implements Parcelable {

    /**
     * status : true
     * code : 0
     * message : 操作成功
     * data : {"info":"简介","taste_count":1000,"share_count":1000,"comment_count":1000,
     * "collect_count":1000,"music":[{"music_id":1,"title":"七里香","singer":"周杰伦","storage":3},
     * {"music_id":2,"title":"如果这都不算爱","singer":"张学友","storage":4}]}
     */

    private boolean status;
    private int code;
    private String message;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * info : 简介
         * taste_count : 1000
         * share_count : 1000
         * comment_count : 1000
         * collect_count : 1000
         * music : [{"music_id":1,"title":"七里香","singer":"周杰伦","storage":3},{"music_id":2,
         * "title":"如果这都不算爱","singer":"张学友","storage":4}]
         * <p>
         * info string 简介
         * taste_count int 播放数量
         * share_count int 转发数量
         * comment_count int 评论数量
         * collect_count int 收藏数量
         * music_id int 歌曲id
         * title string 歌曲名称
         * singer string 周杰伦
         * storage int 歌曲附件id
         */

        private String info;
        private int taste_count;
        private int share_count;
        private int comment_count;
        private int collect_count;
        private List<MusicBean> music;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
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

        public List<MusicBean> getMusic() {
            return music;
        }

        public void setMusic(List<MusicBean> music) {
            this.music = music;
        }

        public static class MusicBean implements Parcelable {
            /**
             * music_id : 1
             * title : 七里香
             * singer : 周杰伦
             * storage : 3
             */

            private int music_id;
            private String title;
            private String singer;
            private int storage;

            public int getMusic_id() {
                return music_id;
            }

            public void setMusic_id(int music_id) {
                this.music_id = music_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSinger() {
                return singer;
            }

            public void setSinger(String singer) {
                this.singer = singer;
            }

            public int getStorage() {
                return storage;
            }

            public void setStorage(int storage) {
                this.storage = storage;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.music_id);
                dest.writeString(this.title);
                dest.writeString(this.singer);
                dest.writeInt(this.storage);
            }

            public MusicBean() {
            }

            protected MusicBean(Parcel in) {
                this.music_id = in.readInt();
                this.title = in.readString();
                this.singer = in.readString();
                this.storage = in.readInt();
            }

            public static final Parcelable.Creator<MusicBean> CREATOR = new Parcelable.Creator<MusicBean>() {
                @Override
                public MusicBean createFromParcel(Parcel source) {
                    return new MusicBean(source);
                }

                @Override
                public MusicBean[] newArray(int size) {
                    return new MusicBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.info);
            dest.writeInt(this.taste_count);
            dest.writeInt(this.share_count);
            dest.writeInt(this.comment_count);
            dest.writeInt(this.collect_count);
            dest.writeTypedList(this.music);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.info = in.readString();
            this.taste_count = in.readInt();
            this.share_count = in.readInt();
            this.comment_count = in.readInt();
            this.collect_count = in.readInt();
            this.music = in.createTypedArrayList(MusicBean.CREATOR);
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeInt(this.code);
        dest.writeString(this.message);
        dest.writeParcelable(this.data, flags);
    }

    public MusicAlbumDetailsBean() {
    }

    protected MusicAlbumDetailsBean(Parcel in) {
        this.status = in.readByte() != 0;
        this.code = in.readInt();
        this.message = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MusicAlbumDetailsBean> CREATOR = new Parcelable
            .Creator<MusicAlbumDetailsBean>() {
        @Override
        public MusicAlbumDetailsBean createFromParcel(Parcel source) {
            return new MusicAlbumDetailsBean(source);
        }

        @Override
        public MusicAlbumDetailsBean[] newArray(int size) {
            return new MusicAlbumDetailsBean[size];
        }
    };
}
