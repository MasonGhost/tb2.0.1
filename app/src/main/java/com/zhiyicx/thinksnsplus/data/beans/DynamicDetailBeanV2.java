package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/06/22/15:55
 * @Email Jliuer@aliyun.com
 * @Description 动态详情 V2
 */
@Entity
public class DynamicDetailBeanV2 implements Parcelable {

    /**
     * id : 13
     * created_at : 2017-06-21 01:54:52
     * updated_at : 2017-06-21 01:54:52
     * deleted_at : null
     * user_id : 1
     * feed_content : 动态内容
     * feed_from : 2
     * feed_digg_count : 0
     * feed_view_count : 0
     * feed_comment_count : 0
     * feed_latitude : null
     * feed_longtitude : null
     * feed_geohash : null
     * audit_status : 1
     * feed_mark : 12
     * has_digg : true
     * amount : 20
     * paid : true
     * images : [{"file":4,"amount":100,"type":"download","paid":false},{"file":5}]
     * diggs : [1]
     */
    @Unique
    private int id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private int user_id;
    private String feed_content;
    private int feed_from;
    private int feed_digg_count;
    private int feed_view_count;
    private int feed_comment_count;
    private String feed_latitude;
    private String feed_longtitude;
    private String feed_geohash;
    private int audit_status;
    @Id
    private Long feed_mark;
    private boolean has_digg;
    private double amount;
    private boolean paid;
    @Convert(converter = ImagesBeansVonvert.class,columnType = String.class)
    private List<ImagesBean> images;
    @Convert(converter = IntegerParamsConverter.class,columnType = String.class)
    private List<Integer> diggs;

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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }

    public int getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(int feed_from) {
        this.feed_from = feed_from;
    }

    public int getFeed_digg_count() {
        return feed_digg_count;
    }

    public void setFeed_digg_count(int feed_digg_count) {
        this.feed_digg_count = feed_digg_count;
    }

    public int getFeed_view_count() {
        return feed_view_count;
    }

    public void setFeed_view_count(int feed_view_count) {
        this.feed_view_count = feed_view_count;
    }

    public int getFeed_comment_count() {
        return feed_comment_count;
    }

    public void setFeed_comment_count(int feed_comment_count) {
        this.feed_comment_count = feed_comment_count;
    }

    public String getFeed_latitude() {
        return feed_latitude;
    }

    public void setFeed_latitude(String feed_latitude) {
        this.feed_latitude = feed_latitude;
    }

    public String getFeed_longtitude() {
        return feed_longtitude;
    }

    public void setFeed_longtitude(String feed_longtitude) {
        this.feed_longtitude = feed_longtitude;
    }

    public String getFeed_geohash() {
        return feed_geohash;
    }

    public void setFeed_geohash(String feed_geohash) {
        this.feed_geohash = feed_geohash;
    }

    public int getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(int audit_status) {
        this.audit_status = audit_status;
    }

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public boolean isHas_digg() {
        return has_digg;
    }

    public void setHas_digg(boolean has_digg) {
        this.has_digg = has_digg;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public List<Integer> getDiggs() {
        return diggs;
    }

    public void setDiggs(List<Integer> diggs) {
        this.diggs = diggs;
    }

    public boolean getHas_digg() {
        return this.has_digg;
    }

    public boolean getPaid() {
        return this.paid;
    }

    public static class ImagesBean implements Parcelable {
        /**
         * file : 4
         * amount : 100
         * type : download
         * paid : false
         */

        private int file;
        private int amount;
        private String type;
        private String size;
        private boolean paid = true;

        public int getFile() {
            return file;
        }

        public void setFile(int file) {
            this.file = file;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.file);
            dest.writeInt(this.amount);
            dest.writeString(this.type);
            dest.writeString(this.size);
            dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
        }

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            this.file = in.readInt();
            this.amount = in.readInt();
            this.type = in.readString();
            this.size = in.readString();
            this.paid = in.readByte() != 0;
        }

        public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel source) {
                return new ImagesBean(source);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };
    }

    

    public static class ImagesBeansVonvert implements PropertyConverter<List<ImagesBean>,String>{
        @Override
        public List<ImagesBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<ImagesBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeInt(this.user_id);
        dest.writeString(this.feed_content);
        dest.writeInt(this.feed_from);
        dest.writeInt(this.feed_digg_count);
        dest.writeInt(this.feed_view_count);
        dest.writeInt(this.feed_comment_count);
        dest.writeString(this.feed_latitude);
        dest.writeString(this.feed_longtitude);
        dest.writeString(this.feed_geohash);
        dest.writeInt(this.audit_status);
        dest.writeValue(this.feed_mark);
        dest.writeByte(this.has_digg ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.amount);
        dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.images);
        dest.writeList(this.diggs);
    }

    public DynamicDetailBeanV2() {
    }

    protected DynamicDetailBeanV2(Parcel in) {
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.user_id = in.readInt();
        this.feed_content = in.readString();
        this.feed_from = in.readInt();
        this.feed_digg_count = in.readInt();
        this.feed_view_count = in.readInt();
        this.feed_comment_count = in.readInt();
        this.feed_latitude = in.readString();
        this.feed_longtitude = in.readString();
        this.feed_geohash = in.readString();
        this.audit_status = in.readInt();
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.has_digg = in.readByte() != 0;
        this.amount = in.readDouble();
        this.paid = in.readByte() != 0;
        this.images = in.createTypedArrayList(ImagesBean.CREATOR);
        this.diggs = new ArrayList<Integer>();
        in.readList(this.diggs, Integer.class.getClassLoader());
    }

    @Generated(hash = 1464461115)
    public DynamicDetailBeanV2(int id, String created_at, String updated_at, String deleted_at,
            int user_id, String feed_content, int feed_from, int feed_digg_count, int feed_view_count,
            int feed_comment_count, String feed_latitude, String feed_longtitude, String feed_geohash,
            int audit_status, Long feed_mark, boolean has_digg, double amount, boolean paid,
            List<ImagesBean> images, List<Integer> diggs) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.user_id = user_id;
        this.feed_content = feed_content;
        this.feed_from = feed_from;
        this.feed_digg_count = feed_digg_count;
        this.feed_view_count = feed_view_count;
        this.feed_comment_count = feed_comment_count;
        this.feed_latitude = feed_latitude;
        this.feed_longtitude = feed_longtitude;
        this.feed_geohash = feed_geohash;
        this.audit_status = audit_status;
        this.feed_mark = feed_mark;
        this.has_digg = has_digg;
        this.amount = amount;
        this.paid = paid;
        this.images = images;
        this.diggs = diggs;
    }

    public static final Creator<DynamicDetailBeanV2> CREATOR = new Creator<DynamicDetailBeanV2>() {
        @Override
        public DynamicDetailBeanV2 createFromParcel(Parcel source) {
            return new DynamicDetailBeanV2(source);
        }

        @Override
        public DynamicDetailBeanV2[] newArray(int size) {
            return new DynamicDetailBeanV2[size];
        }
    };
}
