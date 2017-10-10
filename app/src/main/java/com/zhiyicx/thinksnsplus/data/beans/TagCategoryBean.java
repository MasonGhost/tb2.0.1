package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

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
    private int weight;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public TagCategoryBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.weight);
        dest.writeTypedList(this.tags);
    }

    protected TagCategoryBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.weight = in.readInt();
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
    }

    @Generated(hash = 445643113)
    public TagCategoryBean(Long id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
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
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1695353269)
    private transient TagCategoryBeanDao myDao;

    @Override
    public String toString() {
        return "TagCategoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", tags=" + tags +
                '}';
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1334573489)
    public List<UserTagBean> getTags() {
        if (tags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserTagBeanDao targetDao = daoSession.getUserTagBeanDao();
            List<UserTagBean> tagsNew = targetDao._queryTagCategoryBean_Tags(id);
            synchronized (this) {
                if (tags == null) {
                    tags = tagsNew;
                }
            }
        }
        return tags;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 404234)
    public synchronized void resetTags() {
        tags = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1742516942)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTagCategoryBeanDao() : null;
    }
}
