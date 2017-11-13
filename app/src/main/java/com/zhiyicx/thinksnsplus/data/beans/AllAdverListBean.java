package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.AdvertFormatConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Author Jliuer
 * @Date 2017/07/31/17:09
 * @Email Jliuer@aliyun.com
 * @Description 广告位
 */
@Entity
public class AllAdverListBean extends BaseListBean {

    /**
     * id : 1
     * channel : boot
     * space : boot
     * alias : 启动图广告
     * allow_type : image
     * format : {"image":{"image":"图片|string","link":"链接|string"}}
     * created_at : 2017-07-27 06:56:36
     * updated_at : 2017-07-27 06:56:36
     */
    @Id
    private Long id;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "space_id")})
    private List<RealAdvertListBean> mRealAdvertListBeen;
    private String channel;
    private String space;
    private String alias;
    private String allow_type;
    @Convert(converter = AdvertFormatConvert.class, columnType = String.class)
    private AdvertFormat format;
    private String created_at;
    private String updated_at;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 161079186)
    private transient AllAdverListBeanDao myDao;

    @Generated(hash = 1237694984)
    public AllAdverListBean(Long id, String channel, String space, String alias,
            String allow_type, AdvertFormat format, String created_at, String updated_at) {
        this.id = id;
        this.channel = channel;
        this.space = space;
        this.alias = alias;
        this.allow_type = allow_type;
        this.format = format;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    @Generated(hash = 1839616593)
    public AllAdverListBean() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getSpace() {
        return this.space;
    }
    public void setSpace(String space) {
        this.space = space;
    }
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getAllow_type() {
        return this.allow_type;
    }
    public void setAllow_type(String allow_type) {
        this.allow_type = allow_type;
    }
    public AdvertFormat getFormat() {
        return this.format;
    }
    public void setFormat(AdvertFormat format) {
        this.format = format;
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
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 281901969)
    public List<RealAdvertListBean> getMRealAdvertListBeen() {
        if (mRealAdvertListBeen == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RealAdvertListBeanDao targetDao = daoSession.getRealAdvertListBeanDao();
            List<RealAdvertListBean> mRealAdvertListBeenNew = targetDao
                    ._queryAllAdverListBean_MRealAdvertListBeen(id);
            synchronized (this) {
                if (mRealAdvertListBeen == null) {
                    mRealAdvertListBeen = mRealAdvertListBeenNew;
                }
            }
        }
        return mRealAdvertListBeen;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1744419034)
    public synchronized void resetMRealAdvertListBeen() {
        mRealAdvertListBeen = null;
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeTypedList(this.mRealAdvertListBeen);
        dest.writeString(this.channel);
        dest.writeString(this.space);
        dest.writeString(this.alias);
        dest.writeString(this.allow_type);
        dest.writeParcelable(this.format, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1449673091)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAllAdverListBeanDao() : null;
    }
    protected AllAdverListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mRealAdvertListBeen = in.createTypedArrayList(RealAdvertListBean.CREATOR);
        this.channel = in.readString();
        this.space = in.readString();
        this.alias = in.readString();
        this.allow_type = in.readString();
        this.format = in.readParcelable(AdvertFormat.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<AllAdverListBean> CREATOR = new Creator<AllAdverListBean>() {
        @Override
        public AllAdverListBean createFromParcel(Parcel source) {
            return new AllAdverListBean(source);
        }

        @Override
        public AllAdverListBean[] newArray(int size) {
            return new AllAdverListBean[size];
        }
    };
}
