package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @Describe 动态实体类：包含动态内容，工具栏参数，评论内容
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DynamicBean extends BaseListBean {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private Long feed_id;// 动态的唯一id，暂时没作用
    @Unique
    private String feed_mark;// 动态的唯一标识，由本地创建 用户id+时间戳
    private long user_id;// 发送动态的人id
    @ToOne(joinProperty = "feed_mark")// DynamicBean的feed_id作为外键
    private DynamicDetailBean feed;
    @ToOne(joinProperty = "feed_mark")// DynamicBean的feed_id作为外键
    private DynamicToolBean tool;
    // DynamicBean的feed_id与DynamicCommentBean的feed_id关联
    @ToMany(joinProperties = {@JoinProperty(name = "feed_mark", referencedName = "feed_mark")})
    private List<DynamicCommentBean> comments;
    @ToOne(joinProperty = "user_id")// DynamicBean 的 user_id作为外键
    private UserInfoBean mUserInfoBean;

    public Long getFeed_id() {
        return feed.getFeed_id();
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    @Keep
    public DynamicDetailBean getFeed() {
        return feed;
    }
    @Keep
    public void setFeed(DynamicDetailBean feed) {
        this.feed = feed;
    }
    @Keep
    public DynamicToolBean getTool() {
        return tool;
    }
    @Keep
    public void setTool(DynamicToolBean tool) {
        this.tool = tool;
    }
    @Keep
    public List<DynamicCommentBean> getComments() {
        return comments;
    }
    @Keep
    public void setComments(List<DynamicCommentBean> comments) {
        this.comments = comments;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.feed_id);
        dest.writeString(this.feed_mark);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.feed, flags);
        dest.writeParcelable(this.tool, flags);
        dest.writeTypedList(this.comments);
        dest.writeParcelable(this.mUserInfoBean, flags);
    }

    public String getFeed_mark() {
        return this.feed_mark;
    }

    public void setFeed_mark(String feed_mark) {
        this.feed_mark = feed_mark;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1262958292)
    public UserInfoBean getMUserInfoBean() {
        long __key = this.user_id;
        if (mUserInfoBean__resolvedKey == null || !mUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean mUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                mUserInfoBean = mUserInfoBeanNew;
                mUserInfoBean__resolvedKey = __key;
            }
        }
        return mUserInfoBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1065715559)
    public void setMUserInfoBean(@NotNull UserInfoBean mUserInfoBean) {
        if (mUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mUserInfoBean = mUserInfoBean;
            user_id = mUserInfoBean.getUser_id();
            mUserInfoBean__resolvedKey = user_id;
        }
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
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
    @Generated(hash = 210281324)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicBeanDao() : null;
    }

    public DynamicBean() {
    }

    protected DynamicBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = in.readString();
        this.user_id = in.readLong();
        this.feed = in.readParcelable(DynamicDetailBean.class.getClassLoader());
        this.tool = in.readParcelable(DynamicToolBean.class.getClassLoader());
        this.comments = in.createTypedArrayList(DynamicCommentBean.CREATOR);
        this.mUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Generated(hash = 1387774396)
    public DynamicBean(Long id, Long feed_id, String feed_mark, long user_id) {
        this.id = id;
        this.feed_id = feed_id;
        this.feed_mark = feed_mark;
        this.user_id = user_id;
    }

    public static final Creator<DynamicBean> CREATOR = new Creator<DynamicBean>() {
        @Override
        public DynamicBean createFromParcel(Parcel source) {
            return new DynamicBean(source);
        }

        @Override
        public DynamicBean[] newArray(int size) {
            return new DynamicBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 476616020)
    private transient DynamicBeanDao myDao;
    @Generated(hash = 1505457169)
    private transient String feed__resolvedKey;
    @Generated(hash = 333830307)
    private transient String tool__resolvedKey;
    @Generated(hash = 2004141746)
    private transient Long mUserInfoBean__resolvedKey;
}
