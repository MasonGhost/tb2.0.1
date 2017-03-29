package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
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
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯列表
 */
@Entity
public class InfoListBean extends BaseListBean implements Serializable{

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Unique
    private Long id;
    private int info_type;
    @Convert(converter = InfoListConverter.class,columnType = String.class)
    private List<ListBean> list;
    @Convert(converter = InfoBannnerConverter.class,columnType = String.class)
    private List<RecommendBean> recommend;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<RecommendBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<RecommendBean> recommend) {
        this.recommend = recommend;
    }

    public static class ListBean extends BaseListBean implements Serializable{
        @Transient
        private static final long serialVersionUID = 1L;
        /**
         * id : 1
         * title : 123123
         * updated_at : 2017-03-13 09:59:32
         * storage : {"id":1,"image_width":null,"image_height":null}
         */

        private int id;
        private int is_collection_news;
        private String title;
        private String from;
        private String updated_at;
        private StorageBean storage;

        @Override
        public String toString() {
            return ""+id+"\n"+title+"\n"+"is_collection_news:"+(is_collection_news==1)
                    +"\n"+from+"\n"+updated_at;
        }

        public int getIs_collection_news() {
            return is_collection_news;
        }

        public void setIs_collection_news(int is_collection_news) {
            this.is_collection_news = is_collection_news;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public StorageBean getStorage() {
            return storage;
        }

        public void setStorage(StorageBean storage) {
            this.storage = storage;
        }

        public static class StorageBean implements Parcelable ,Serializable{
            @Transient
            private static final long serialVersionUID = 1L;
            /**
             * id : 1
             * image_width : null
             * image_height : null
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

            public StorageBean() {
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

        public ListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.id);
            dest.writeInt(this.is_collection_news);
            dest.writeString(this.title);
            dest.writeString(this.from);
            dest.writeString(this.updated_at);
            dest.writeParcelable(this.storage, flags);
        }

        protected ListBean(Parcel in) {
            super(in);
            this.id = in.readInt();
            this.is_collection_news = in.readInt();
            this.title = in.readString();
            this.from = in.readString();
            this.updated_at = in.readString();
            this.storage = in.readParcelable(StorageBean.class.getClassLoader());
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    public static class RecommendBean extends BaseListBean implements  Serializable{
        @Transient
        private static final long serialVersionUID = 1L;
        /**
         * id : 1
         * created_at : 2017-03-16 11:31:52
         * updated_at : 2017-03-16 11:31:52
         * cate_id : 2
         * news_id : 1
         * cover : {"id":1,"image_width":null,"image_height":null}
         * sort : 0
         */

        private int id;
        private String created_at;
        private String updated_at;
        private int cate_id;
        private int news_id;
        private CoverBean cover;
        private int sort;

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

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
            this.news_id = news_id;
        }

        public CoverBean getCover() {
            return cover;
        }

        public void setCover(CoverBean cover) {
            this.cover = cover;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public static class CoverBean implements Parcelable,Serializable {
            @Transient
            private static final long serialVersionUID = 1L;
            /**
             * id : 1
             * image_width : null
             * image_height : null
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

            public CoverBean() {
            }

            protected CoverBean(Parcel in) {
                this.id = in.readInt();
                this.image_width = in.readInt();
                this.image_height = in.readInt();
            }

            public static final Creator<CoverBean> CREATOR = new Creator<CoverBean>() {
                @Override
                public CoverBean createFromParcel(Parcel source) {
                    return new CoverBean(source);
                }

                @Override
                public CoverBean[] newArray(int size) {
                    return new CoverBean[size];
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
            dest.writeInt(this.cate_id);
            dest.writeInt(this.news_id);
            dest.writeParcelable(this.cover, flags);
            dest.writeInt(this.sort);
        }

        public RecommendBean() {
        }

        protected RecommendBean(Parcel in) {
            super(in);
            this.id = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.cate_id = in.readInt();
            this.news_id = in.readInt();
            this.cover = in.readParcelable(CoverBean.class.getClassLoader());
            this.sort = in.readInt();
        }

        public static final Creator<RecommendBean> CREATOR = new Creator<RecommendBean>() {
            @Override
            public RecommendBean createFromParcel(Parcel source) {
                return new RecommendBean(source);
            }

            @Override
            public RecommendBean[] newArray(int size) {
                return new RecommendBean[size];
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
        dest.writeTypedList(this.list);
        dest.writeTypedList(this.recommend);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInfo_type() {
        return this.info_type;
    }

    public void setInfo_type(int info_type) {
        this.info_type = info_type;
    }

    public InfoListBean() {
    }

    protected InfoListBean(Parcel in) {
        super(in);
        this.list = in.createTypedArrayList(ListBean.CREATOR);
        this.recommend = in.createTypedArrayList(RecommendBean.CREATOR);
    }

    @Generated(hash = 1580739463)
    public InfoListBean(Long id, int info_type, List<ListBean> list, List<RecommendBean> recommend) {
        this.id = id;
        this.info_type = info_type;
        this.list = list;
        this.recommend = recommend;
    }

    public static final Creator<InfoListBean> CREATOR = new Creator<InfoListBean>() {
        @Override
        public InfoListBean createFromParcel(Parcel source) {
            return new InfoListBean(source);
        }

        @Override
        public InfoListBean[] newArray(int size) {
            return new InfoListBean[size];
        }
    };

    public static class InfoListConverter implements PropertyConverter<List<ListBean>, String> {
        @Override
        public List<ListBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<ListBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class InfoBannnerConverter implements PropertyConverter<List<RecommendBean>,
            String> {

        @Override
        public List<RecommendBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<RecommendBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }
}
