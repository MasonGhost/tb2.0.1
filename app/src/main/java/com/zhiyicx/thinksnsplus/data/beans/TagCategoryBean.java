package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/31
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class TagCategoryBean extends CacheBean implements Parcelable {
    /**
     * {
     * "id": 1,
     * "name": "分类1",
     * "tags": [
     * {
     * "id": 1,
     * "name": "标签1",
     * "tag_category_id": 1
     * }
     * ]
     * }
     */
    @Id
    private Long id;
    private String name;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "tag_category_id")})
    private List<UserTagBean> tags;

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

    public List<UserTagBean> getTags() {
        return tags;
    }

    public void setTags(List<UserTagBean> tags) {
        this.tags = tags;
    }


    @Override
    public String toString() {
        return "TagCategoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.tags);
    }

    public TagCategoryBean() {
    }

    protected TagCategoryBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
    }

    public static final Creator<TagCategoryBean> CREATOR = new Creator<TagCategoryBean>() {
        @Override
        public TagCategoryBean createFromParcel(Parcel source) {
            return new TagCategoryBean(source);
        }

        @Override
        public TagCategoryBean[] newArray(int size) {
            return new TagCategoryBean[size];
        }
    };
}
