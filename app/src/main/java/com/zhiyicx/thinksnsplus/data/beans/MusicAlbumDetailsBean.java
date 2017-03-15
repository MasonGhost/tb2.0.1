package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
public class MusicAlbumDetailsBean implements Parcelable {

    /**
     * id : 1
     * created_at : 2017-03-10 18:05:02
     * updated_at : 2017-03-10 18:05:03
     * title : 专辑1
     * storage : 1
     * taste_count : 0
     * share_count : 0
     * comment_count : 0
     * collect_count : 0
     * musics : [{"id":1,"created_at":"2017-03-10 18:05:15","updated_at":"2017-03-10 18:05:16",
     * "special_id":"1","music_id":1,"music_info":{"id":1,"created_at":"2017-03-10 18:05:22",
     * "updated_at":"2017-03-10 18:05:23","deleted_at":null,"title":"音乐1","singer":1,"storage":2,
     * "last_time":180,"lyric":"啦啦啦啦啦啦","taste_count":0,"share_count":0,"comment_count":0}},
     * {"id":2,"created_at":"2017-03-10 18:05:15","updated_at":"2017-03-10 18:05:16",
     * "special_id":"1","music_id":2,"music_info":{"id":2,"created_at":"2017-03-10 18:05:22",
     * "updated_at":"2017-03-10 18:05:23","deleted_at":null,"title":"音乐2","singer":1,"storage":2,
     * "last_time":180,"lyric":"啦啦啦啦啦啦","taste_count":0,"share_count":0,"comment_count":0}}]
     */

    private int id;
    private String created_at;
    private String updated_at;
    private String title;
    private int storage;
    private int taste_count;
    private int share_count;
    private int comment_count;
    private int collect_count;
    private List<MusicsBean> musics;

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

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
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

    public List<MusicsBean> getMusics() {
        return musics;
    }

    public void setMusics(List<MusicsBean> musics) {
        this.musics = musics;
    }

    public static class MusicsBean {
        /**
         * id : 1
         * created_at : 2017-03-10 18:05:15
         * updated_at : 2017-03-10 18:05:16
         * special_id : 1
         * music_id : 1
         * music_info : {"id":1,"created_at":"2017-03-10 18:05:22","updated_at":"2017-03-10
         * 18:05:23","deleted_at":null,"title":"音乐1","singer":1,"storage":2,"last_time":180,
         * "lyric":"啦啦啦啦啦啦","taste_count":0,"share_count":0,"comment_count":0}
         */

        private int id;
        private String created_at;
        private String updated_at;
        private String special_id;
        private int music_id;
        private MusicInfoBean music_info;

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

        public String getSpecial_id() {
            return special_id;
        }

        public void setSpecial_id(String special_id) {
            this.special_id = special_id;
        }

        public int getMusic_id() {
            return music_id;
        }

        public void setMusic_id(int music_id) {
            this.music_id = music_id;
        }

        public MusicInfoBean getMusic_info() {
            return music_info;
        }

        public void setMusic_info(MusicInfoBean music_info) {
            this.music_info = music_info;
        }

        public static class MusicInfoBean {
            /**
             * id : 1
             * created_at : 2017-03-10 18:05:22
             * updated_at : 2017-03-10 18:05:23
             * deleted_at : null
             * title : 音乐1
             * singer : 1
             * storage : 2
             * last_time : 180
             * lyric : 啦啦啦啦啦啦
             * taste_count : 0
             * share_count : 0
             * comment_count : 0
             */

            private int id;
            private String created_at;
            private String updated_at;
            private Object deleted_at;
            private String title;
            private int singer;
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

            public Object getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(Object deleted_at) {
                this.deleted_at = deleted_at;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getSinger() {
                return singer;
            }

            public void setSinger(int singer) {
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
        }
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
        dest.writeString(this.title);
        dest.writeInt(this.storage);
        dest.writeInt(this.taste_count);
        dest.writeInt(this.share_count);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.collect_count);
        dest.writeList(this.musics);
    }

    public MusicAlbumDetailsBean() {
    }

    protected MusicAlbumDetailsBean(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.title = in.readString();
        this.storage = in.readInt();
        this.taste_count = in.readInt();
        this.share_count = in.readInt();
        this.comment_count = in.readInt();
        this.collect_count = in.readInt();
        this.musics = new ArrayList<MusicsBean>();
        in.readList(this.musics, MusicsBean.class.getClassLoader());
    }

    public static final Creator<MusicAlbumDetailsBean> CREATOR = new Creator<MusicAlbumDetailsBean>() {
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
