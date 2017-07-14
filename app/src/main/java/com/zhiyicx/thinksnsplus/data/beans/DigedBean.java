package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * @Describe {@see https://github.com/zhiyicx/plus-likeable_type-feed/blob/master/documents/%E6%88%91%E6%94%B6%E5%88%B0%E7%9A%84%E8%B5%9E%E5%88%97%E8%A1%A8.md}
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DigedBean extends BaseListBean {
    /**
     * id : 4
     * likeable_type : feed
     * digg_table : feed_diggs
     * digg_id : 5
     * source_table : feeds
     * likeable_id : 17
     * user_id : 1
     * to_user_id : 1
     * created_at : 2017-04-11 02:41:42
     * updated_at : 2017-04-11 02:41:42
     */
    @Id
    private Long id; // 数据体 id
    private Long user_id; // 点赞者 id
    @ToOne(joinProperty = "user_id")
    private UserInfoBean digUserInfo;
    private Long target_user; // 资源作者 id
    @ToOne(joinProperty = "target_user")
    private UserInfoBean digedUserInfo;
    private String created_at;
    private String updated_at;
    private String likeable_type; // 数据所属扩展包名 目前可能的参数有 feed
    private Long likeable_id; // 关联资源 id
    //
//    private int source_cover; // 封面 id
//    private String source_content; // 资源描述
//    @Convert(converter = ObjectParamsConverter.class, columnType = String.class)
//    private Object likeable;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2113720789)
    private transient DigedBeanDao myDao;


    @Generated(hash = 353304193)
    public DigedBean(Long id, Long user_id, Long target_user, String created_at, String updated_at, String likeable_type, Long likeable_id) {
        this.id = id;
        this.user_id = user_id;
        this.target_user = target_user;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.likeable_type = likeable_type;
        this.likeable_id = likeable_id;
    }

    @Generated(hash = 1958494079)
    public DigedBean() {
    }


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

    public String getLikeable_type() {
        return this.likeable_type;
    }

    public void setLikeable_type(String likeable_type) {
        this.likeable_type = likeable_type;
    }

    public Long getLikeable_id() {
        return this.likeable_id;
    }

    public void setLikeable_id(Long likeable_id) {
        this.likeable_id = likeable_id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getTarget_user() {
        return this.target_user;
    }

    public void setTarget_user(Long target_user) {
        this.target_user = target_user;
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
    @Generated(hash = 316022512)
    public UserInfoBean getDigedUserInfo() {
        Long __key = this.target_user;
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
    @Generated(hash = 744662438)
    public void setDigedUserInfo(UserInfoBean digedUserInfo) {
        synchronized (this) {
            this.digedUserInfo = digedUserInfo;
            target_user = digedUserInfo == null ? null : digedUserInfo.getUser_id();
            digedUserInfo__resolvedKey = target_user;
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


    /**
     * Object 转 String 形式存入数据库
     */
    static class ObjectParamsConverter implements PropertyConverter<Object, String> {

        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(Object entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

}
