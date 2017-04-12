package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Describe   {@see https://github.com/zhiyicx/plus-component-feed/blob/master/documents/%E6%88%91%E6%94%B6%E5%88%B0%E7%9A%84%E8%B5%9E%E5%88%97%E8%A1%A8.md}
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DigedBean extends BaseListBean{
    /**
     * id : 4
     * component : feed
     * digg_table : feed_diggs
     * digg_id : 5
     * source_table : feeds
     * source_id : 17
     * user_id : 1
     * to_user_id : 1
     * created_at : 2017-04-11 02:41:42
     * updated_at : 2017-04-11 02:41:42
     */
    @Id
    private Long id; // 数据体 id
    private String component; // 数据所属扩展包名 目前可能的参数有 feed
    private String digg_table; // 点赞记录所属数据表 目前可能的参数有 feed_diggs
    private long digg_id; // 关联点赞 id
    private String source_table; // 所属资源所在表 目前可能参数有 feeds
    private Long source_id; // 关联资源 id
    private Long user_id; // 点赞者 id
    @ToOne(joinProperty ="user_id")
    private UserInfoBean digUserInfo;
    private Long to_user_id; // 资源作者 id
    @ToOne(joinProperty ="to_user_id")
    private UserInfoBean digedUserInfo;
    private String created_at;
    private String updated_at;

    private int source_cover; // 封面 id
    private String source_content; // 资源描述
    private String comment_content; // 评论类容




    @Override
    public String toString() {
        return "DigedBean{" +
                "id=" + id +
                ", component='" + component + '\'' +
                ", digg_table='" + digg_table + '\'' +
                ", digg_id=" + digg_id +
                ", source_table='" + source_table + '\'' +
                ", source_id=" + source_id +
                ", user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.component);
        dest.writeString(this.digg_table);
        dest.writeLong(this.digg_id);
        dest.writeString(this.source_table);
        dest.writeLong(this.source_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.digUserInfo, flags);
        dest.writeLong(this.to_user_id);
        dest.writeParcelable(this.digedUserInfo, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.source_cover);
        dest.writeString(this.source_content);
        dest.writeString(this.comment_content);
    }

    public DigedBean() {
    }

    protected DigedBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.component = in.readString();
        this.digg_table = in.readString();
        this.digg_id = in.readInt();
        this.source_table = in.readString();
        this.source_id = in.readLong();
        this.user_id = in.readLong();
        this.digUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.to_user_id = in.readLong();
        this.digedUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.source_cover = in.readInt();
        this.source_content = in.readString();
        this.comment_content = in.readString();
    }

    @Generated(hash = 377680155)
    public DigedBean(Long id, String component, String digg_table, long digg_id, String source_table, Long source_id, Long user_id, Long to_user_id,
            String created_at, String updated_at, int source_cover, String source_content, String comment_content) {
        this.id = id;
        this.component = component;
        this.digg_table = digg_table;
        this.digg_id = digg_id;
        this.source_table = source_table;
        this.source_id = source_id;
        this.user_id = user_id;
        this.to_user_id = to_user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.source_cover = source_cover;
        this.source_content = source_content;
        this.comment_content = comment_content;
    }

    public static final Creator<DigedBean> CREATOR = new Creator<DigedBean>() {
        @Override
        public DigedBean createFromParcel(Parcel source) {
            return new DigedBean(source);
        }

        @Override
        public DigedBean[] newArray(int size) {
            return new DigedBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2113720789)
    private transient DigedBeanDao myDao;
    @Generated(hash = 81788119)
    private transient Long digUserInfo__resolvedKey;
    @Generated(hash = 1719103363)
    private transient Long digedUserInfo__resolvedKey;

    @Override
    public Long getMaxId() {
        return this.id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDigg_table() {
        return this.digg_table;
    }

    public void setDigg_table(String digg_table) {
        this.digg_table = digg_table;
    }

    public long getDigg_id() {
        return this.digg_id;
    }

    public void setDigg_id(long digg_id) {
        this.digg_id = digg_id;
    }

    public String getSource_table() {
        return this.source_table;
    }

    public void setSource_table(String source_table) {
        this.source_table = source_table;
    }

    public Long getSource_id() {
        return this.source_id;
    }

    public void setSource_id(Long source_id) {
        this.source_id = source_id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getTo_user_id() {
        return this.to_user_id;
    }

    public void setTo_user_id(Long to_user_id) {
        this.to_user_id = to_user_id;
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

    public int getSource_cover() {
        return this.source_cover;
    }

    public void setSource_cover(int source_cover) {
        this.source_cover = source_cover;
    }

    public String getSource_content() {
        return this.source_content;
    }

    public void setSource_content(String source_content) {
        this.source_content = source_content;
    }

    public String getComment_content() {
        return this.comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1880931547)
    public UserInfoBean getDigUserInfo() {
        Long __key = this.user_id;
        if (digUserInfo__resolvedKey == null || !digUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean digUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                digUserInfo = digUserInfoNew;
                digUserInfo__resolvedKey = __key;
            }
        }
        return digUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 272986475)
    public void setDigUserInfo(UserInfoBean digUserInfo) {
        synchronized (this) {
            this.digUserInfo = digUserInfo;
            user_id = digUserInfo == null ? null : digUserInfo.getUser_id();
            digUserInfo__resolvedKey = user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1814660159)
    public UserInfoBean getDigedUserInfo() {
        Long __key = this.to_user_id;
        if (digedUserInfo__resolvedKey == null || !digedUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean digedUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                digedUserInfo = digedUserInfoNew;
                digedUserInfo__resolvedKey = __key;
            }
        }
        return digedUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1755632032)
    public void setDigedUserInfo(UserInfoBean digedUserInfo) {
        synchronized (this) {
            this.digedUserInfo = digedUserInfo;
            to_user_id = digedUserInfo == null ? null : digedUserInfo.getUser_id();
            digedUserInfo__resolvedKey = to_user_id;
        }
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
    @Generated(hash = 1633631379)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDigedBeanDao() : null;
    }

}
