package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author LiuChao
 * @describe 频道信息内容
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */
@Entity
public class ChannelInfoBean implements Parcelable {
    @Id
    private Long id;// 频道id
    private String created_at;// 创建时间 2017-03-17 16:51:46
    private String updated_at;// 更新时间
    private String title;// 频道标题
    private String description; // 频道描述
    private int follow_count;// 订阅数量
    private int feed_count;// 分享数量
    private int follow_status;// 0 未订阅 1 已订阅
    @Convert(converter = DataConverter.class, columnType = String.class)
    private ChannelCoverBean cover;// 频道封面

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public int getFeed_count() {
        return feed_count;
    }

    public void setFeed_count(int feed_count) {
        this.feed_count = feed_count;
    }

    public ChannelCoverBean getCover() {
        return cover;
    }

    public void setCover(ChannelCoverBean cover) {
        this.cover = cover;
    }

    public int getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }

    // 频道封面
    public static class ChannelCoverBean implements Parcelable, Serializable {
        private static final long serialVersionUID = -1424606641213510836L;
        private int id;// 图片资源id
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

        public ChannelCoverBean() {
        }

        protected ChannelCoverBean(Parcel in) {
            this.id = in.readInt();
            this.image_width = in.readInt();
            this.image_height = in.readInt();
        }

        public static final Creator<ChannelCoverBean> CREATOR = new Creator<ChannelCoverBean>() {
            @Override
            public ChannelCoverBean createFromParcel(Parcel source) {
                return new ChannelCoverBean(source);
            }

            @Override
            public ChannelCoverBean[] newArray(int size) {
                return new ChannelCoverBean[size];
            }
        };

        @Override
        public String toString() {
            return "ChannelCoverBean{" +
                    "id=" + id +
                    ", image_width=" + image_width +
                    ", image_height=" + image_height +
                    '}';
        }
    }

    public static class DataConverter implements PropertyConverter<ChannelCoverBean, String> {
        @Override
        public ChannelCoverBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(ChannelCoverBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public ChannelInfoBean() {
    }

    @Generated(hash = 1993653623)
    public ChannelInfoBean(Long id, String created_at, String updated_at, String title, String description,
            int follow_count, int feed_count, int follow_status, ChannelCoverBean cover) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.title = title;
        this.description = description;
        this.follow_count = follow_count;
        this.feed_count = feed_count;
        this.follow_status = follow_status;
        this.cover = cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.follow_count);
        dest.writeInt(this.feed_count);
        dest.writeInt(this.follow_status);
        dest.writeParcelable(this.cover, flags);
    }

    protected ChannelInfoBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.follow_count = in.readInt();
        this.feed_count = in.readInt();
        this.follow_status = in.readInt();
        this.cover = in.readParcelable(ChannelCoverBean.class.getClassLoader());
    }

    public static final Creator<ChannelInfoBean> CREATOR = new Creator<ChannelInfoBean>() {
        @Override
        public ChannelInfoBean createFromParcel(Parcel source) {
            return new ChannelInfoBean(source);
        }

        @Override
        public ChannelInfoBean[] newArray(int size) {
            return new ChannelInfoBean[size];
        }
    };
}
