package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;

import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * @Describe 动态实体类：包含动态内容，工具栏参数，评论内容
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DynamicBean extends BaseListBean {

    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private Long feed_id;// 动态的唯一id，暂时没作用
    @Unique
    private Long feed_mark;// 动态的唯一标识，由本地创建 用户id+时间戳
    private long user_id;// 发送动态的人id
    @ToOne(joinProperty = "feed_mark")// DynamicBean的feed_id作为外键
    private DynamicDetailBean feed;
    @ToOne(joinProperty = "feed_mark")// DynamicBean的feed_id作为外键
    private DynamicToolBean tool;
    @ToOne(joinProperty = "user_id")// DynamicBean 的 user_id作为外键
    private UserInfoBean userInfoBean;
    // DynamicBean的feed_id与DynamicCommentBean的feed_id关联
    @ToMany(joinProperties = {@JoinProperty(name = "feed_mark", referencedName = "feed_mark")})
    private List<DynamicCommentBean> comments;

    private Long hot_creat_time;// 标记热门，已及创建时间，用户数据库查询
    private boolean isFollowed;// 是否关注了该条动态（用户）
    private int state = SEND_SUCCESS;// 动态发送状态 0 发送失败 1 正在发送 2 发送成功

    public Long getHot_creat_time() {
        return hot_creat_time;
    }

    public void setHot_creat_time(Long hot_creat_time) {
        this.hot_creat_time = hot_creat_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
    }

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
    public UserInfoBean getUserInfoBean() {

        return userInfoBean == null ? new UserInfoBean() : userInfoBean;
    }

    @Keep
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 618015855)
    public List<DynamicCommentBean> getComments() {
        if (comments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DynamicCommentBeanDao targetDao = daoSession.getDynamicCommentBeanDao();
            List<DynamicCommentBean> commentsNew = targetDao._queryDynamicBean_Comments(feed_mark);
            synchronized (this) {
                if (comments == null) {
                    comments = commentsNew;
                }
            }
        }
        return comments;
    }

    @Keep
    public void setComments(List<DynamicCommentBean> comments) {
        this.comments = comments;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
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

    public DynamicBean() {
    }

    @Generated(hash = 46860411)
    public DynamicBean(Long id, Long feed_id, Long feed_mark, long user_id, Long hot_creat_time,
                       boolean isFollowed, int state) {
        this.id = id;
        this.feed_id = feed_id;
        this.feed_mark = feed_mark;
        this.user_id = user_id;
        this.hot_creat_time = hot_creat_time;
        this.isFollowed = isFollowed;
        this.state = state;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 476616020)
    private transient DynamicBeanDao myDao;
    @Generated(hash = 1613724019)
    private transient Long feed__resolvedKey;
    @Generated(hash = 297170499)
    private transient Long tool__resolvedKey;
    @Generated(hash = 1005780391)
    private transient Long userInfoBean__resolvedKey;

    @Override
    public Long getMaxId() {
        if (hot_creat_time != null && hot_creat_time.intValue() != 0) { // 用于区别热门，和其他分类，查询数据库分页时使用
            return hot_creat_time;
        }
        return feed_id;
    }

    public boolean getIsFollowed() {
        return this.isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
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
        dest.writeValue(this.feed_mark);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.feed, flags);
        dest.writeParcelable(this.tool, flags);
        dest.writeParcelable(this.userInfoBean, flags);
        dest.writeTypedList(this.comments);
        dest.writeValue(this.hot_creat_time);
        dest.writeByte(this.isFollowed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.state);
    }

    protected DynamicBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readLong();
        this.feed = in.readParcelable(DynamicDetailBean.class.getClassLoader());
        this.tool = in.readParcelable(DynamicToolBean.class.getClassLoader());
        this.userInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.comments = in.createTypedArrayList(DynamicCommentBean.CREATOR);
        this.hot_creat_time = (Long) in.readValue(Long.class.getClassLoader());
        this.isFollowed = in.readByte() != 0;
        this.state = in.readInt();
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

    @Override
    public String toString() {
        return "DynamicBean{" +
                "id=" + id +
                ", feed_id=" + feed_id +
                ", feed_mark=" + feed_mark +
                ", user_id=" + user_id +
                ", feed=" + feed +
                ", tool=" + tool +
                ", userInfoBean=" + userInfoBean +
                ", comments=" + comments +
                ", hot_creat_time=" + hot_creat_time +
                ", isFollowed=" + isFollowed +
                ", state=" + state +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                ", feed__resolvedKey=" + feed__resolvedKey +
                ", tool__resolvedKey=" + tool__resolvedKey +
                ", userInfoBean__resolvedKey=" + userInfoBean__resolvedKey +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 210281324)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicBeanDao() : null;
    }
}
