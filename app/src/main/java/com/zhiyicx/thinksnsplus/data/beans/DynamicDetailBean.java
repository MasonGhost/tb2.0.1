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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author LiuChao
 * @describe 动态内容的实体类
 * @date 2017/2/22
 * @contact email:450127106@qq.com
 */
@Entity
public class DynamicDetailBean implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private Long feed_id;// 服务器返回的feed_id
    @Unique
    private String feed_mark;// 属于哪条动态
    private String title;// 动态标题
    private String content;// 动态内容
    private long created_at;// 创建时间
    private int feed_from;// 来自哪个平台 //[1:pc 2:h5 3:ios 4:android 5:其他]
    @Convert(converter = ParamsConverter.class, columnType = String.class)
    private List<Integer> storage;// 图片的云端存储id
    @Transient
    private List<String> localPhotos;// 本地图片的路径
    private int state;// 动态发送状态 0 发送失败 1 正在发送 2 发送成功

    public String getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(String feed_mark) {
        this.feed_mark = feed_mark;
    }

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

    public List<Integer> getStorage() {
        return storage;
    }

    public void setStorage(List<Integer> storage) {
        this.storage = storage;
    }

    public List<String> getLocalPhotos() {
        return localPhotos;
    }

    public void setLocalPhotos(List<String> localPhotos) {
        this.localPhotos = localPhotos;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * list<String> 转 String 形式存入数据库
     */
    public static class ParamsConverter implements PropertyConverter<List<Integer>, String> {

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public DynamicDetailBean() {
    }

    @Generated(hash = 643557137)
    public DynamicDetailBean(Long id, Long feed_id, String feed_mark, String title, String content,
            long created_at, int feed_from, List<Integer> storage, int state) {
        this.id = id;
        this.feed_id = feed_id;
        this.feed_mark = feed_mark;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.feed_from = feed_from;
        this.storage = storage;
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.feed_id);
        dest.writeString(this.feed_mark);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeLong(this.created_at);
        dest.writeInt(this.feed_from);
        dest.writeList(this.storage);
        dest.writeStringList(this.localPhotos);
        dest.writeInt(this.state);
    }

    protected DynamicDetailBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.created_at = in.readLong();
        this.feed_from = in.readInt();
        this.storage = new ArrayList<Integer>();
        in.readList(this.storage, Integer.class.getClassLoader());
        this.localPhotos = in.createStringArrayList();
        this.state = in.readInt();
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
