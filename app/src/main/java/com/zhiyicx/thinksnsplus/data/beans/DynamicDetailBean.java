package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 动态内容的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicDetailBean implements Parcelable {
    @Id
    private Long feed_mark;// 属于哪条动态
    @Unique
    private Long feed_id;// 服务器返回的feed_id
    @SerializedName(value = "feed_title", alternate = {"title"})
    private String feed_title;// 动态标题
    @SerializedName(value = "feed_content", alternate = {"content"})
    private String feed_content;// 动态内容
    private String created_at;// 创建时间
    private int feed_from;// 来自哪个平台 //[1:pc 2:h5 3:ios 4:android 5:其他]
    @Convert(converter = ImageParamsConverter.class, columnType = String.class)
    private List<ImageBean> storages;// 图片的云端存储id
    @Convert(converter = IntegerParamsConverter.class, columnType = String.class)
    private List<Integer> storage_task_ids;// 图片的云端存储id

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public Long getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
    }

    public String getTitle() {
        return feed_title == null ? "" : feed_title;
    }

    public void setTitle(String title) {
        this.feed_title = title;
    }

    public String getContent() {
        return feed_content == null ? "" : feed_content;
    }

    public void setContent(String content) {
        this.feed_content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(int feed_from) {
        this.feed_from = feed_from;
    }

    public List<Integer> getStorage_task_ids() {
        return storage_task_ids;
    }

    public void setStorage_task_ids(List<Integer> storage_task_ids) {
        this.storage_task_ids = storage_task_ids;
    }

    /**
     * list<ImageBean> 转 String 形式存入数据库
     */
    public static class ImageParamsConverter implements PropertyConverter<List<ImageBean>, String>,Parcelable {

        @Override
        public List<ImageBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<ImageBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public ImageParamsConverter() {
        }

        protected ImageParamsConverter(Parcel in) {
        }

        public static final Creator<ImageParamsConverter> CREATOR = new Creator<ImageParamsConverter>() {
            @Override
            public ImageParamsConverter createFromParcel(Parcel source) {
                return new ImageParamsConverter(source);
            }

            @Override
            public ImageParamsConverter[] newArray(int size) {
                return new ImageParamsConverter[size];
            }
        };
    }

    /**
     * list<Integer> 转 String 形式存入数据库
     */
    public static class IntegerParamsConverter implements PropertyConverter<List<Integer>, String> {

        @Override
        public List<Integer> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<Integer> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public List<ImageBean> getStorages() {
        return this.storages;
    }

    public void setStorages(List<ImageBean> storages) {
        this.storages = storages;
    }

    public String getFeed_title() {
        return this.feed_title;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getFeed_content() {
        return this.feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }

    @Override
    public String toString() {
        return "DynamicDetailBean{" +
                "feed_mark=" + feed_mark +
                ", feed_id=" + feed_id +
                ", feed_title='" + feed_title + '\'' +
                ", feed_content='" + feed_content + '\'' +
                ", created_at=" + created_at +
                ", feed_from=" + feed_from +
                ", storages=" + storages +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.feed_mark);
        dest.writeValue(this.feed_id);
        dest.writeString(this.feed_title);
        dest.writeString(this.feed_content);
        dest.writeString(this.created_at);
        dest.writeInt(this.feed_from);
        dest.writeTypedList(this.storages);
        dest.writeList(this.storage_task_ids);
    }

    public DynamicDetailBean() {
    }

    protected DynamicDetailBean(Parcel in) {
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_title = in.readString();
        this.feed_content = in.readString();
        this.created_at = in.readString();
        this.feed_from = in.readInt();
        this.storages = in.createTypedArrayList(ImageBean.CREATOR);
        this.storage_task_ids = new ArrayList<Integer>();
        in.readList(this.storage_task_ids, Integer.class.getClassLoader());
    }

    @Generated(hash = 1476015793)
    public DynamicDetailBean(Long feed_mark, Long feed_id, String feed_title, String feed_content,
            String created_at, int feed_from, List<ImageBean> storages, List<Integer> storage_task_ids) {
        this.feed_mark = feed_mark;
        this.feed_id = feed_id;
        this.feed_title = feed_title;
        this.feed_content = feed_content;
        this.created_at = created_at;
        this.feed_from = feed_from;
        this.storages = storages;
        this.storage_task_ids = storage_task_ids;
    }

    public static final Creator<DynamicDetailBean> CREATOR = new Creator<DynamicDetailBean>() {
        @Override
        public DynamicDetailBean createFromParcel(Parcel source) {
            return new DynamicDetailBean(source);
        }

        @Override
        public DynamicDetailBean[] newArray(int size) {
            return new DynamicDetailBean[size];
        }
    };
}
