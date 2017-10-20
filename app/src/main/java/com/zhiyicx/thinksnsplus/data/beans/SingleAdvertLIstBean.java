package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ImageAdvert;

/**
 * @Author Jliuer
 * @Date 2017/07/31/17:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SingleAdvertLIstBean extends BaseListBean {

    /**
     * id : 1
     * space_id : 1
     * title : 广告1
     * type : image
     * data : {"image":"http://plus.bai/api/v2/files/1","link":"http://www.baidu.com"}
     * created_at : 2017-07-27 15:09:15
     * updated_at : 2017-07-27 15:09:16
     */

    private Long id;
    private int space_id;
    private String title;
    private String type;
    private ImageAdvert data;
    private String created_at;
    private String updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageAdvert getData() {
        return data;
    }

    public void setData(ImageAdvert data) {
        this.data = data;
    }

    public int getSpace_id() {
        return space_id;
    }

    public void setSpace_id(int space_id) {
        this.space_id = space_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public static class DataBean {
        /**
         * image : http://plus.bai/api/v2/files/1
         * link : http://www.baidu.com
         */

        private String image;
        private String link;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeInt(this.space_id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeSerializable(this.data);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public SingleAdvertLIstBean() {
    }

    protected SingleAdvertLIstBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.space_id = in.readInt();
        this.title = in.readString();
        this.type = in.readString();
        this.data = (ImageAdvert) in.readSerializable();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<SingleAdvertLIstBean> CREATOR = new Creator<SingleAdvertLIstBean>() {
        @Override
        public SingleAdvertLIstBean createFromParcel(Parcel source) {
            return new SingleAdvertLIstBean(source);
        }

        @Override
        public SingleAdvertLIstBean[] newArray(int size) {
            return new SingleAdvertLIstBean[size];
        }
    };
}
