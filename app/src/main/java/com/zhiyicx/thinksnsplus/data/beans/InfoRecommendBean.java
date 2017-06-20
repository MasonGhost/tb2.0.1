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
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class InfoRecommendBean extends BaseListBean implements Serializable {
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
    @Id(autoincrement = true)
    Long _id;
    @Unique
    private int id;
    private Long info_type;
    private String created_at;
    private String updated_at;
    private int cate_id;
    private int news_id;
    @Convert(converter = InfoCoverBeanConverter.class, columnType = String.class)
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

    public static class CoverBean implements Parcelable, Serializable {
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

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getInfo_type() {
        return this.info_type;
    }

    public void setInfo_type(Long info_type) {
        this.info_type = info_type;
    }

    public InfoRecommendBean() {
    }

    protected InfoRecommendBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.cate_id = in.readInt();
        this.news_id = in.readInt();
        this.cover = in.readParcelable(CoverBean.class.getClassLoader());
        this.sort = in.readInt();
    }

    @Generated(hash = 792287468)
    public InfoRecommendBean(Long _id, int id, Long info_type, String created_at, String updated_at,
            int cate_id, int news_id, CoverBean cover, int sort) {
        this._id = _id;
        this.id = id;
        this.info_type = info_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.cate_id = cate_id;
        this.news_id = news_id;
        this.cover = cover;
        this.sort = sort;
    }


    public static final Creator<InfoRecommendBean> CREATOR = new Creator<InfoRecommendBean>() {
        @Override
        public InfoRecommendBean createFromParcel(Parcel source) {
            return new InfoRecommendBean(source);
        }

        @Override
        public InfoRecommendBean[] newArray(int size) {
            return new InfoRecommendBean[size];
        }
    };

    public static class InfoCoverBeanConverter implements PropertyConverter<CoverBean, String> {

        @Override
        public InfoRecommendBean.CoverBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(InfoRecommendBean.CoverBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }
}