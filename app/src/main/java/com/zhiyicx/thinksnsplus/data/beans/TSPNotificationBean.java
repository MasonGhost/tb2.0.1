package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_COMMENTS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_DIGGS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_PINNED_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_REPLY_COMMENTS;

/**
 * @Describe detail to @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md#%E9%80%9A%E7%9F%A5%E5%88%97%E8%A1%A8}
 * @Author Jungle68
 * @Date 2017/7/11
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class TSPNotificationBean implements Parcelable {


    /**
     * id : 98aaae93-9d9e-446e-b894-691569b686b5
     * read_at : 2017-07-11 04:23:08
     * data : {"channel":"feed:pinned-comment","target":1,"content":"我是测试消息","extra":null}
     * created_at : 2017-07-10 04:23:08
     */
    @Id
    private Long _id;
    @Unique
    private String id;
    private String read_at;
    private String created_at;
    @Convert(converter = DataBeanParamsConverter.class, columnType = String.class)
    private DataBean data;
    private long user_id;// 这条通知的操作者
    @ToOne(joinProperty = "user_id")
    private UserInfoBean userInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRead_at() {
        return read_at;
    }

    public void setRead_at(String read_at) {
        this.read_at = read_at;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public long getUser_id() {
        if (user_id != 0) {
            return user_id;
        }
        if (data != null) {
            Gson gson = new Gson();
            switch (data.getChannel()) {
                case NOTIFICATION_KEY_FEED_COMMENTS:
                case NOTIFICATION_KEY_FEED_REPLY_COMMENTS:

                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(data.getExtra()));
                        user_id = (long) jsonObject.getDouble("user_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case NOTIFICATION_KEY_FEED_DIGGS:
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(data.getExtra()));
                        user_id = (long) jsonObject.getDouble("user_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case NOTIFICATION_KEY_FEED_PINNED_COMMENT:
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(data.getExtra()));
                        user_id = (long) jsonObject.getDouble("user_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:

            }
        }
        return user_id;
    }

    public void setUser_id(long user_id) {
        if (user_id != 0) {
            this.user_id = user_id;
        } else {
            this.user_id = getUser_id();
        }

    }

    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 6464434974795251975L;
        /**
         * channel : feed:pinned-comment
         * target : 1
         * content : 我是测试消息
         * extra : null
         */

        private String channel;
        private int target;
        private String content;
        private Object extra;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getExtra() {
            return extra;
        }

        public void setExtra(Object extra) {
            this.extra = extra;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "channel='" + channel + '\'' +
                    ", target=" + target +
                    ", content='" + content + '\'' +
                    ", extra=" + extra +
                    '}';
        }
    }

    /**
     * list<DataBean> 转 String 形式存入数据库
     */
    public static class DataBeanParamsConverter implements PropertyConverter<DataBean, String> {

        @Override
        public DataBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(DataBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TSPNotificationBean that = (TSPNotificationBean) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TSPNotificationBean{" +
                "_id=" + _id +
                ", id='" + id + '\'' +
                ", read_at='" + read_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", data=" + data +
                ", user_id=" + user_id +
                ", userInfo=" + userInfo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this._id);
        dest.writeString(this.id);
        dest.writeString(this.read_at);
        dest.writeString(this.created_at);
        dest.writeSerializable(this.data);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.userInfo, flags);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 669954399)
    public UserInfoBean getUserInfo() {
        long __key = this.user_id;
        if (userInfo__resolvedKey == null || !userInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userInfoNew = targetDao.load(__key);
            synchronized (this) {
                userInfo = userInfoNew;
                userInfo__resolvedKey = __key;
            }
        }
        return userInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1286036388)
    public void setUserInfo(@NotNull UserInfoBean userInfo) {
        if (userInfo == null) {
            throw new DaoException("To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.userInfo = userInfo;
            user_id = userInfo.getUser_id();
            userInfo__resolvedKey = user_id;
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
    @Generated(hash = 2063187465)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTSPNotificationBeanDao() : null;
    }

    public TSPNotificationBean() {
    }

    protected TSPNotificationBean(Parcel in) {
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readString();
        this.read_at = in.readString();
        this.created_at = in.readString();
        this.data = (DataBean) in.readSerializable();
        this.user_id = in.readLong();
        this.userInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Generated(hash = 1589879726)
    public TSPNotificationBean(Long _id, String id, String read_at, String created_at, DataBean data, long user_id) {
        this._id = _id;
        this.id = id;
        this.read_at = read_at;
        this.created_at = created_at;
        this.data = data;
        this.user_id = user_id;
    }

    public static final Parcelable.Creator<TSPNotificationBean> CREATOR = new Parcelable.Creator<TSPNotificationBean>() {
        @Override
        public TSPNotificationBean createFromParcel(Parcel source) {
            return new TSPNotificationBean(source);
        }

        @Override
        public TSPNotificationBean[] newArray(int size) {
            return new TSPNotificationBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 523085633)
    private transient TSPNotificationBeanDao myDao;
    @Generated(hash = 2066097151)
    private transient Long userInfo__resolvedKey;
}
