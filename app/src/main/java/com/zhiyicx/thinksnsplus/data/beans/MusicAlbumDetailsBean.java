package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
@Entity
public class MusicAlbumDetailsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id : 1
     * created_at : 2017-03-15 17:01:17
     * updated_at : 2017-03-21 02:29:48
     * title : 专辑1
     * storage : 2
     * taste_count : 4
     * share_count : 0
     * comment_count : 0
     * collect_count : 0
     * musics : [{"id":1,"created_at":"2017-03-16 17:22:39","updated_at":"2017-03-16 17:22:42",
     * "special_id":1,"music_id":1,"music_info":{"id":1,"created_at":"2017-03-16 17:11:26",
     * "updated_at":"2017-03-21 02:29:48","deleted_at":null,"title":"水手公园","singer":{"id":1,
     * "created_at":"2017-03-16 17:22:04","updated_at":"2017-03-16 17:22:08","name":"汤圆毛",
     * "cover":{"id":2,"image_width":3264,"image_height":2448}},"storage":129,"last_time":180,
     * "lyric":"lalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallal","taste_count":4,"share_count":0,"comment_count":0}},{"id":2,"created_at":"2017-03-16 17:22:48","updated_at":"2017-03-16 17:22:50","special_id":1,"music_id":2,"music_info":{"id":2,"created_at":"2017-03-16 17:20:40","updated_at":"2017-03-16 17:20:43","deleted_at":null,"title":"thankyou","singer":{"id":2,"created_at":"2017-03-16 17:22:18","updated_at":"2017-03-16 17:22:20","name":"刘zz","cover":{"id":54,"image_width":690,"image_height":932}},"storage":130,"last_time":240,"lyric":"sdafasfasdfasdfasdfasdfsadf","taste_count":0,"share_count":0,"comment_count":0}}]
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id;
    private String created_at;
    private String updated_at;
    private String title;
    private String intro;
    @Convert(converter = MusicAlbumLIstConverter.class, columnType = String.class)
    private StorageBean storage;
    private int taste_count;
    private int share_count;
    private int comment_count;
    private int collect_count;
    private int is_collection;
    @Convert(converter = MusicListConverter.class, columnType = String.class)
    private List<MusicsBean> musics;

    @Generated(hash = 1758975319)
    public MusicAlbumDetailsBean(int id, String created_at, String updated_at, String title, String intro, StorageBean storage, int taste_count, int share_count, int comment_count, int collect_count, int is_collection, List<MusicsBean> musics) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.title = title;
        this.intro = intro;
        this.storage = storage;
        this.taste_count = taste_count;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.collect_count = collect_count;
        this.is_collection = is_collection;
        this.musics = musics;
    }

    @Generated(hash = 1417869479)
    public MusicAlbumDetailsBean() {
    }

    public static class StorageBean implements Parcelable, Serializable {
        @Transient
        private static final long serialVersionUID = 1L;
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

        public static final Creator<MusicAlbumListBean.StorageBean> CREATOR = new Creator<MusicAlbumListBean.StorageBean>() {
            @Override
            public MusicAlbumListBean.StorageBean createFromParcel(Parcel source) {
                return new MusicAlbumListBean.StorageBean(source);
            }

            @Override
            public MusicAlbumListBean.StorageBean[] newArray(int size) {
                return new MusicAlbumListBean.StorageBean[size];
            }
        };
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

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

    public StorageBean getStorage() {
        return this.storage;
    }

    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }


    public static class MusicsBean implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * id : 1
         * created_at : 2017-03-16 17:22:39
         * updated_at : 2017-03-16 17:22:42
         * special_id : 1
         * music_id : 1
         * music_info : {"id":1,"created_at":"2017-03-16 17:11:26","updated_at":"2017-03-21
         * 02:29:48","deleted_at":null,"title":"水手公园","singer":{"id":1,"created_at":"2017-03-16
         * 17:22:04","updated_at":"2017-03-16 17:22:08","name":"汤圆毛","cover":{"id":2,
         * "image_width":3264,"image_height":2448}},"storage":129,"last_time":180,
         * "lyric":"lalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallal","taste_count":4,"share_count":0,"comment_count":0}
         */

        private int id;
        private String created_at;
        private String updated_at;
        private int special_id;
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

        public int getSpecial_id() {
            return special_id;
        }

        public void setSpecial_id(int special_id) {
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

        public static class MusicInfoBean implements Serializable {
            private static final long serialVersionUID = 1L;
            /**
             * id : 1
             * created_at : 2017-03-16 17:11:26
             * updated_at : 2017-03-21 02:29:48
             * deleted_at : null
             * title : 水手公园
             * singer : {"id":1,"created_at":"2017-03-16 17:22:04","updated_at":"2017-03-16
             * 17:22:08","name":"汤圆毛","cover":{"id":2,"image_width":3264,"image_height":2448}}
             * storage : 129
             * last_time : 180
             * lyric :
             * lalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallal
             * taste_count : 4
             * share_count : 0
             * comment_count : 0
             */

            private int id;
            private String created_at;
            private String updated_at;
            private Object deleted_at;
            private String title;
            private SingerBean singer;
            private int storage;
            private int last_time;
            private String lyric;
            private int taste_count;
            private int share_count;
            private int comment_count;
            private int isdiggmusic;

            public int getIsdiggmusic() {
                return isdiggmusic;
            }

            public void setIsdiggmusic(int isdiggmusic) {
                this.isdiggmusic = isdiggmusic;
            }

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

            public static class SingerBean implements Serializable {
                private static final long serialVersionUID = 1L;
                /**
                 * id : 1
                 * created_at : 2017-03-16 17:22:04
                 * updated_at : 2017-03-16 17:22:08
                 * name : 汤圆毛
                 * cover : {"id":2,"image_width":3264,"image_height":2448}
                 */

                private int id;
                private String created_at;
                private String updated_at;
                private String name;
                private CoverBean cover;

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

                public CoverBean getCover() {
                    return cover;
                }

                public void setCover(CoverBean cover) {
                    this.cover = cover;
                }

                public static class CoverBean implements Serializable {
                    private static final long serialVersionUID = 1L;
                    /**
                     * id : 2
                     * image_width : 3264
                     * image_height : 2448
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
                }
            }
        }
    }

    public static class MusicListConverter implements PropertyConverter<List<MusicsBean>, String> {
        @Override
        public List<MusicsBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<MusicsBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class MusicAlbumLIstConverter implements PropertyConverter<StorageBean, String> {
        @Override
        public StorageBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(StorageBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }
}
