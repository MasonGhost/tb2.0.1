package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.HashMap;
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
    private Long feed_id;// 属于哪条动态
    private String title;// 动态标题
    private String content;// 动态内容
    private long created_at;// 创建时间
    private int feed_from;// 来自哪个平台 //[1:pc 2:h5 3:ios 4:android 5:其他]
    @Convert(converter = ParamsConverter.class, columnType = String.class)
    private List<String> storage;// 图片的云端存储id


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.feed_id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeLong(this.created_at);
        dest.writeInt(this.feed_from);
        dest.writeStringList(this.storage);
    }

    public DynamicDetailBean() {
    }

    protected DynamicDetailBean(Parcel in) {
        this.feed_id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.content = in.readString();
        this.created_at = in.readLong();
        this.feed_from = in.readInt();
        this.storage = in.createStringArrayList();
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

    public Long getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
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

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(int feed_from) {
        this.feed_from = feed_from;
    }

    public List<String> getStorage() {
        return storage;
    }

    public void setStorage(List<String> storage) {
        this.storage = storage;
    }

    /**
     * list<String> 转 String 形式存入数据库
     */
    public static class ParamsConverter implements PropertyConverter<List<String>, String> {

        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }
}
