package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

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
    // DynamicBean 的 feed_mark 与 DynamicCommentBean 的 feed_mark 关联
    @ToMany(joinProperties = {@JoinProperty(name = "feed_mark", referencedName = "feed_mark")})
    private List<DynamicCommentBean> comments;

    private Long hot_creat_time;// 标记热门，已及创建时间，用户数据库查询
    private boolean isFollowed;// 是否关注了该条动态（用户）
    private int state = SEND_SUCCESS;// 动态发送状态 0 发送失败 1 正在发送 2 发送成功

    @Convert(converter = DataConverter.class, columnType = String.class)
    private List<FollowFansBean> digUserInfoList;// 点赞用户的信息列表


    public DynamicBean() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeed_id() {
        return this.feed_id;
    }

    public void setFeed_id(Long feed_id) {
        this.feed_id = feed_id;
    }

    public Long getFeed_mark() {
        return this.feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Long getHot_creat_time() {
        return this.hot_creat_time;
    }

    public void setHot_creat_time(Long hot_creat_time) {
        this.hot_creat_time = hot_creat_time;
    }

    public boolean getIsFollowed() {
        return this.isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public int getState() {
        return this.state;
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
        tool = tool == null ? new DynamicToolBean() : tool;
        return tool;
    }

    @Keep
    public void setTool(DynamicToolBean tool) {
        this.tool = tool;
    }

    @Keep
    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    @Keep
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }


    @Keep
    public void setComments(List<DynamicCommentBean> comments) {
        this.comments = comments;
    }

    @Keep
    public boolean isFollowed() {
        return isFollowed;
    }

    @Keep
    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    @Keep
    public List<FollowFansBean> getDigUserInfoList() {
        return digUserInfoList;
    }

    @Keep
    public void setDigUserInfoList(List<FollowFansBean> digUserInfoList) {
        this.digUserInfoList = digUserInfoList;
    }


    @Keep
    public List<DynamicCommentBean> getComments() {
        return comments;
    }

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
                ", digUserInfoList=" + digUserInfoList +
                '}';
    }


    /**
     * list<FollowFansBean> 转 String 形式存入数据库
     */
    public static class DataConverter implements PropertyConverter<List<FollowFansBean>, String> {

        @Override
        public List<FollowFansBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<FollowFansBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
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
        dest.writeTypedList(this.digUserInfoList);
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
        this.digUserInfoList = in.createTypedArrayList(FollowFansBean.CREATOR);
    }


    @Generated(hash = 1888878893)
    public DynamicBean(Long id, Long feed_id, Long feed_mark, long user_id, Long hot_creat_time,
                       boolean isFollowed, int state, List<FollowFansBean> digUserInfoList) {
        this.id = id;
        this.feed_id = feed_id;
        this.feed_mark = feed_mark;
        this.user_id = user_id;
        this.hot_creat_time = hot_creat_time;
        this.isFollowed = isFollowed;
        this.state = state;
        this.digUserInfoList = digUserInfoList;
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
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DynamicBean) {
            DynamicBean dynamicBean = (DynamicBean) obj;
            return dynamicBean.getFeed_mark().longValue() == feed_mark.longValue();
        }
        return super.equals(obj);
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 210281324)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicBeanDao() : null;
    }
}
