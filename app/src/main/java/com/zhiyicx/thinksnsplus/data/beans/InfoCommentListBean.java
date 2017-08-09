package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class InfoCommentListBean extends BaseListBean {
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    /**
     * id : 3
     * created_at : 2017-03-13 16:35:33
     * comment_content : 爱我的
     * user_id : 1
     * reply_to_user_id : 0
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id = -1;
    private int info_id = -1;// 自己创建的，用于记录隶属于哪一条资讯。
    private String created_at;
    private String comment_content;
    private long user_id;
    private long reply_to_user_id;
    @Unique
    private long comment_mark;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean fromUserInfoBean;
    @ToOne(joinProperty = "reply_to_user_id")
    private UserInfoBean toUserInfoBean;
    private int state = SEND_SUCCESS;

    @Override
    public String toString() {
        return ""+id+"\n"+comment_content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Keep
    public UserInfoBean getToUserInfoBean() {
        return toUserInfoBean;
    }

    @Keep
    public void setToUserInfoBean(UserInfoBean toUserInfoBean) {
        this.toUserInfoBean = toUserInfoBean;
    }

    @Keep
    public UserInfoBean getFromUserInfoBean() {
        return fromUserInfoBean;
    }

    @Keep
    public void setFromUserInfoBean(UserInfoBean fromUserInfoBean) {
        this.fromUserInfoBean = fromUserInfoBean;
    }

    public long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(long reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    @Override
    public Long getMaxId() {
        return (long)id;
    }

    public InfoCommentListBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.comment_content);
        dest.writeValue(this.user_id);
        dest.writeValue(this.reply_to_user_id);
        dest.writeLong(this.comment_mark);
        dest.writeParcelable(this.fromUserInfoBean, flags);
        dest.writeParcelable(this.toUserInfoBean, flags);
        dest.writeInt(this.state);
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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

    public int getInfo_id() {
        return this.info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 841333318)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoCommentListBeanDao() : null;
    }

    protected InfoCommentListBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readInt();
        this.created_at = in.readString();
        this.comment_content = in.readString();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.reply_to_user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.comment_mark = in.readLong();
        this.fromUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.toUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.state = in.readInt();
    }

    @Generated(hash = 884472755)
    public InfoCommentListBean(Long _id, int id, int info_id, String created_at, String comment_content,
            long user_id, long reply_to_user_id, long comment_mark, int state) {
        this._id = _id;
        this.id = id;
        this.info_id = info_id;
        this.created_at = created_at;
        this.comment_content = comment_content;
        this.user_id = user_id;
        this.reply_to_user_id = reply_to_user_id;
        this.comment_mark = comment_mark;
        this.state = state;
    }

    public static final Creator<InfoCommentListBean> CREATOR = new Creator<InfoCommentListBean>() {
        @Override
        public InfoCommentListBean createFromParcel(Parcel source) {
            return new InfoCommentListBean(source);
        }

        @Override
        public InfoCommentListBean[] newArray(int size) {
            return new InfoCommentListBean[size];
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
    @Generated(hash = 786979885)
    private transient InfoCommentListBeanDao myDao;
    @Generated(hash = 262226026)
    private transient Long fromUserInfoBean__resolvedKey;
    @Generated(hash = 89682145)
    private transient Long toUserInfoBean__resolvedKey;
}
