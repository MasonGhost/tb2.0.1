package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class TopDynamicCommentBean extends BaseListBean {

    public static final int TOP_REFUSE = 0;
    public static final int TOP_REVIEWING = 1;
    public static final int TOP_SUCCESS = 2;

    /**
     * id : 2
     * amount : 10
     * day : 1
     * user_id : 1
     * expires_at : 2017-07-05 08:29:49
     * created_at : 2017-06-30 12:04:15
     * comment : {"id":2,"content":"我是第2条评论","pinned":true,"user_id":1,"reply_to_user_id":0,
     * "created_at":"2017-06-27 08:59:14"}
     * feed : {"id":1,"content":"动态内容"}
     */
    @Id
    @Unique
    private Long id;
    private int amount;
    private int day;
    private Long user_id;
    private String expires_at;
    private String created_at;
    private int state = TOP_REVIEWING;// 置顶状态 0：置顶被拒绝 1：置顶审核中 2：置顶成功
    @Convert(converter = CommentConvert.class, columnType = String.class)
    private CommentBean comment;
    @Convert(converter = FeedConvert.class, columnType = String.class)
    private FeedBean feed;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean userInfoBean;
    @ToOne(joinProperty = "id")
    private DynamicDetailBeanV2 mDynamicDetailBeanV2;


    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }

    public FeedBean getFeed() {
        return feed;
    }

    public void setFeed(FeedBean feed) {
        this.feed = feed;
    }

    public static class CommentBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871009L;
        /**
         * id : 2
         * content : 我是第2条评论
         * pinned : true
         * user_id : 1
         * reply_to_user_id : 0
         * created_at : 2017-06-27 08:59:14
         */

        private int id;
        private String content;
        private boolean pinned;
        private int user_id;
        private int reply_to_user_id;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isPinned() {
            return pinned;
        }

        public void setPinned(boolean pinned) {
            this.pinned = pinned;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getReply_to_user_id() {
            return reply_to_user_id;
        }

        public void setReply_to_user_id(int reply_to_user_id) {
            this.reply_to_user_id = reply_to_user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.content);
            dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
            dest.writeInt(this.user_id);
            dest.writeInt(this.reply_to_user_id);
            dest.writeString(this.created_at);
        }

        public CommentBean() {
        }

        protected CommentBean(Parcel in) {
            this.id = in.readInt();
            this.content = in.readString();
            this.pinned = in.readByte() != 0;
            this.user_id = in.readInt();
            this.reply_to_user_id = in.readInt();
            this.created_at = in.readString();
        }

        public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
            @Override
            public CommentBean createFromParcel(Parcel source) {
                return new CommentBean(source);
            }

            @Override
            public CommentBean[] newArray(int size) {
                return new CommentBean[size];
            }
        };
    }

    public static class FeedBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871009L;
        /**
         * id : 1
         * content : 动态内容
         */

        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.content);
        }

        public FeedBean() {
        }

        protected FeedBean(Parcel in) {
            this.id = in.readInt();
            this.content = in.readString();
        }

        public static final Creator<FeedBean> CREATOR = new Creator<FeedBean>() {
            @Override
            public FeedBean createFromParcel(Parcel source) {
                return new FeedBean(source);
            }

            @Override
            public FeedBean[] newArray(int size) {
                return new FeedBean[size];
            }
        };
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2083937831)
    private transient TopDynamicCommentBeanDao myDao;

    @Generated(hash = 28345850)
    public TopDynamicCommentBean(Long id, int amount, int day, Long user_id, String expires_at, String created_at,
                                 int state, CommentBean comment, FeedBean feed) {
        this.id = id;
        this.amount = amount;
        this.day = day;
        this.user_id = user_id;
        this.expires_at = expires_at;
        this.created_at = created_at;
        this.state = state;
        this.comment = comment;
        this.feed = feed;
    }

    @Generated(hash = 2134686559)
    public TopDynamicCommentBean() {
    }

    @Generated(hash = 1005780391)
    private transient Long userInfoBean__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1288178437)
    public UserInfoBean getUserInfoBean() {
        Long __key = this.user_id;
        if (userInfoBean__resolvedKey == null || !userInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                userInfoBean = userInfoBeanNew;
                userInfoBean__resolvedKey = __key;
            }
        }
        return userInfoBean;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 251524817)
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        synchronized (this) {
            this.userInfoBean = userInfoBean;
            user_id = userInfoBean == null ? null : userInfoBean.getUser_id();
            userInfoBean__resolvedKey = user_id;
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

    public static class CommentConvert implements PropertyConverter<CommentBean, String> {
        @Override
        public CommentBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(CommentBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class FeedConvert implements PropertyConverter<FeedBean, String> {
        @Override
        public FeedBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(FeedBean entityProperty) {
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
        dest.writeInt(this.amount);
        dest.writeInt(this.day);
        dest.writeValue(this.user_id);
        dest.writeString(this.expires_at);
        dest.writeString(this.created_at);
        dest.writeInt(this.state);
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.feed, flags);
        dest.writeParcelable(this.userInfoBean, flags);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1468529555)
    public DynamicDetailBeanV2 getMDynamicDetailBeanV2() {
        Long __key = this.id;
        if (mDynamicDetailBeanV2__resolvedKey == null || !mDynamicDetailBeanV2__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DynamicDetailBeanV2Dao targetDao = daoSession.getDynamicDetailBeanV2Dao();
            DynamicDetailBeanV2 mDynamicDetailBeanV2New = targetDao.load(__key);
            synchronized (this) {
                mDynamicDetailBeanV2 = mDynamicDetailBeanV2New;
                mDynamicDetailBeanV2__resolvedKey = __key;
            }
        }
        return mDynamicDetailBeanV2;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 128069292)
    public void setMDynamicDetailBeanV2(DynamicDetailBeanV2 mDynamicDetailBeanV2) {
        synchronized (this) {
            this.mDynamicDetailBeanV2 = mDynamicDetailBeanV2;
            id = mDynamicDetailBeanV2 == null ? null : mDynamicDetailBeanV2.getFeed_mark();
            mDynamicDetailBeanV2__resolvedKey = id;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2079522954)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopDynamicCommentBeanDao() : null;
    }

    protected TopDynamicCommentBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.amount = in.readInt();
        this.day = in.readInt();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.expires_at = in.readString();
        this.created_at = in.readString();
        this.state = in.readInt();
        this.comment = in.readParcelable(CommentBean.class.getClassLoader());
        this.feed = in.readParcelable(FeedBean.class.getClassLoader());
        this.userInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<TopDynamicCommentBean> CREATOR = new Creator<TopDynamicCommentBean>() {
        @Override
        public TopDynamicCommentBean createFromParcel(Parcel source) {
            return new TopDynamicCommentBean(source);
        }

        @Override
        public TopDynamicCommentBean[] newArray(int size) {
            return new TopDynamicCommentBean[size];
        }
    };
    @Generated(hash = 1412668450)
    private transient Long mDynamicDetailBeanV2__resolvedKey;
}
