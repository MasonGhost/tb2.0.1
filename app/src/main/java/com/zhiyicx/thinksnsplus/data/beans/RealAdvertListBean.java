package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.AdvertFormatConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.DynamicListAdvertConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.ImageAdvertConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/08/01/11:20
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class RealAdvertListBean extends BaseListBean {

    /**
     * id : 1
     * space_id : 1
     * title : 广告1
     * type : image
     * data : {"image":"http://plus.bai/api/v2/files/1","link":"http://www.baidu.com"}
     * created_at : 2017-07-27 15:09:15
     * updated_at : 2017-07-27 15:09:16
     */
    @Id
    private Long id;
    private Long space_id;
    private String title;
    private String type;
    @Transient
    private Object data;
    private String created_at;
    private String updated_at;
    @Convert(converter = AdvertFormatConvert.class, columnType = String.class)
    private AdvertFormat advertFormat;
    @Convert(converter = ImageAdvertConvert.class, columnType = String.class)
    private ImageAdvert image;
    @Convert(converter = DynamicListAdvertConvert.class, columnType = String.class)
    private DynamicListAdvert analog;


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public AdvertFormat getAdvertFormat() {
        return advertFormat;
    }

    public void setAdvertFormat(AdvertFormat advertFormat) {
        this.advertFormat = advertFormat;
    }

    public ImageAdvert getImage() {
        return image;
    }

    public void setImage(ImageAdvert image) {
        if (image == null) {
            Gson gson = new Gson();
            switch (type) {
                case ApiConfig.APP_IMAGE_ADVERT:
                    image = (gson.fromJson(gson.toJson(data), ImageAdvert.class));
                    break;
                case ApiConfig.APP_DYNAMIC_ADVERT:
                    analog = (gson.fromJson(gson.toJson(data), DynamicListAdvert.class));
                    break;
            }
        }
        this.image = image;

    }

    public DynamicListAdvert getAnalog() {
        return analog;
    }

    public void setAnalog(DynamicListAdvert analog) {
        if (analog == null) {
            Gson gson = new Gson();
            switch (type) {
                case ApiConfig.APP_IMAGE_ADVERT:
                    image = (gson.fromJson(gson.toJson(data), ImageAdvert.class));
                    break;
                case ApiConfig.APP_DYNAMIC_ADVERT:
                    analog = (gson.fromJson(gson.toJson(data), DynamicListAdvert.class));
                    break;
            }
        }
        this.analog = analog;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpace_id() {
        return this.space_id;
    }

    public void setSpace_id(Long space_id) {
        this.space_id = space_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.space_id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.advertFormat, flags);
        dest.writeParcelable(this.image, flags);
        dest.writeParcelable(this.analog, flags);
    }

    public RealAdvertListBean() {
    }

    protected RealAdvertListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.space_id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.type = in.readString();
        this.data = in.readParcelable(Object.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.advertFormat = in.readParcelable(AdvertFormat.class.getClassLoader());
        this.image = in.readParcelable(ImageAdvert.class.getClassLoader());
        this.analog = in.readParcelable(DynamicListAdvert.class.getClassLoader());
    }

    @Generated(hash = 970465889)
    public RealAdvertListBean(Long id, Long space_id, String title, String type, String created_at,
            String updated_at, AdvertFormat advertFormat, ImageAdvert image, DynamicListAdvert analog) {
        this.id = id;
        this.space_id = space_id;
        this.title = title;
        this.type = type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.advertFormat = advertFormat;
        this.image = image;
        this.analog = analog;
    }

    public static final Creator<RealAdvertListBean> CREATOR = new Creator<RealAdvertListBean>() {
        @Override
        public RealAdvertListBean createFromParcel(Parcel source) {
            return new RealAdvertListBean(source);
        }

        @Override
        public RealAdvertListBean[] newArray(int size) {
            return new RealAdvertListBean[size];
        }
    };
}
