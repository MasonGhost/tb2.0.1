package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;

@Entity
public class InfoListDataBean extends BaseListBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;
    /**
     * id : 1
     * title : 123123
     * updated_at : 2017-03-13 09:59:32
     * storage : {"id":1,"image_width":null,"image_height":null}
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id;
    private Long info_type;
    private int is_collection_news;
    private int is_digg_news;
    private String title;
    private String from;
    private String updated_at;
    @Convert(converter = InfoStorageBeanConverter.class, columnType = String.class)
    private StorageBean storage;

    @Override
    public String toString() {
        return "" + id + "\n" + title + "\n" + "is_collection_news:" + (is_collection_news == 1)
                + "\n" + from + "\n" + updated_at;
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

    public InfoListDataBean() {
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

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    protected InfoListDataBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.is_collection_news = in.readInt();
        this.title = in.readString();
        this.from = in.readString();
        this.updated_at = in.readString();
        this.storage = in.readParcelable(StorageBean.class.getClassLoader());
    }


    @Generated(hash = 1322048907)
    public InfoListDataBean(Long _id, int id, Long info_type, int is_collection_news, int is_digg_news,
                            String title, String from, String updated_at, StorageBean storage) {
        this._id = _id;
        this.id = id;
        this.info_type = info_type;
        this.is_collection_news = is_collection_news;
        this.is_digg_news = is_digg_news;
        this.title = title;
        this.from = from;
        this.updated_at = updated_at;
        this.storage = storage;
    }


    public static final Creator<InfoListDataBean> CREATOR = new Creator<InfoListDataBean>() {
        @Override
        public InfoListDataBean createFromParcel(Parcel source) {
            return new InfoListDataBean(source);
        }

        @Override
        public InfoListDataBean[] newArray(int size) {
            return new InfoListDataBean[size];
        }
    };


    public static class InfoStorageBeanConverter implements PropertyConverter<StorageBean, String> {

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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof InfoListDataBean) {
            InfoListDataBean infoListDataBean = (InfoListDataBean) obj;
            return infoListDataBean.getId() == id;
        }
        return super.equals(obj);
    }


    public Long getInfo_type() {
        return this.info_type;
    }


    public void setInfo_type(Long info_type) {
        this.info_type = info_type;
    }

    public int getIs_digg_news() {
        return this.is_digg_news;
    }

    public void setIs_digg_news(int is_digg_news) {
        this.is_digg_news = is_digg_news;
    }
}

