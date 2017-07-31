package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

/**
 * Created by huajunhe on 2017/7/31.
 */

public class TagCategoryBean extends CacheBean implements Parcelable {
    /**  {
     "id": 1,
     "name": "分类1",
     "tags": [
     {
     "id": 1,
     "name": "标签1",
     "tag_category_id": 1
     }
     ]
     }*/

    private Long id;
    private String name;
    private UserTagBean tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserTagBean getTags() {
        return tags;
    }

    public void setTags(UserTagBean tags) {
        this.tags = tags;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.tags, flags);
    }

    public TagCategoryBean() {
    }

    protected TagCategoryBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.tags = in.readParcelable(UserTagBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<TagCategoryBean> CREATOR = new Parcelable.Creator<TagCategoryBean>() {
        @Override
        public TagCategoryBean createFromParcel(Parcel source) {
            return new TagCategoryBean(source);
        }

        @Override
        public TagCategoryBean[] newArray(int size) {
            return new TagCategoryBean[size];
        }
    };

    @Override
    public String toString() {
        return "TagCategoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
