package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯列表
 */
@Entity
public class InfoListBean extends BaseListBean implements Serializable{

    @Transient
    private static final long serialVersionUID = 1L;
    @Id
    @Unique
    private Long info_type;
    @ToMany(joinProperties = {@JoinProperty(name = "info_type", referencedName = "info_type")})
    private List<InfoListDataBean> list;
    @ToMany(joinProperties = {@JoinProperty(name = "info_type", referencedName = "info_type")})
    private List<InfoRecommendBean> recommend;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1359304970)
    private transient InfoListBeanDao myDao;

    @Generated(hash = 359076140)
    public InfoListBean(Long info_type) {
        this.info_type = info_type;
    }
    @Generated(hash = 1530804200)
    public InfoListBean() {
    }

    public Long getInfo_type() {
        return this.info_type;
    }
    public void setInfo_type(Long info_type) {
        this.info_type = info_type;
    }

    @Keep
    public void setList(List<InfoListDataBean> list) {
        this.list = list;
    }

    @Keep
    public void setRecommend(List<InfoRecommendBean> recommend) {
        this.recommend = recommend;
    }

    @Keep
    public List<InfoRecommendBean> getNetRecommend() {
        return recommend;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2050825902)
    public List<InfoListDataBean> getList() {
        if (list == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoListDataBeanDao targetDao = daoSession.getInfoListDataBeanDao();
            List<InfoListDataBean> listNew = targetDao._queryInfoListBean_List(info_type);
            synchronized (this) {
                if (list == null) {
                    list = listNew;
                }
            }
        }
        return list;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 589833612)
    public synchronized void resetList() {
        list = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1664827480)
    public List<InfoRecommendBean> getRecommend() {
        if (recommend == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoRecommendBeanDao targetDao = daoSession.getInfoRecommendBeanDao();
            List<InfoRecommendBean> recommendNew = targetDao._queryInfoListBean_Recommend(info_type);
            synchronized (this) {
                if (recommend == null) {
                    recommend = recommendNew;
                }
            }
        }
        return recommend;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1253671557)
    public synchronized void resetRecommend() {
        recommend = null;
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
        dest.writeValue(this.info_type);
        dest.writeTypedList(this.list);
        dest.writeTypedList(this.recommend);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1149138458)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoListBeanDao() : null;
    }
    protected InfoListBean(Parcel in) {
        super(in);
        this.info_type = (Long) in.readValue(Long.class.getClassLoader());
        this.list = in.createTypedArrayList(InfoListDataBean.CREATOR);
        this.recommend = in.createTypedArrayList(InfoRecommendBean.CREATOR);
    }

    public static final Creator<InfoListBean> CREATOR = new Creator<InfoListBean>() {
        @Override
        public InfoListBean createFromParcel(Parcel source) {
            return new InfoListBean(source);
        }

        @Override
        public InfoListBean[] newArray(int size) {
            return new InfoListBean[size];
        }
    };
}
