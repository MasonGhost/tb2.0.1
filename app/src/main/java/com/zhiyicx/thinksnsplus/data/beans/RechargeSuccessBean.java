package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.DaoException;

/**
 * @Author Jliuer
 * @Date 2017/06/08/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class RechargeSuccessBean extends BaseListBean implements Parcelable {

    /**
     * id	int	记录id
     * owner_id	int	所属者id
     * target_type	string	操作类型 recharge_ping_p_p - 充值, widthdraw - 提现, user - 转账, reward - 打赏
     * target_id	string	账户
     * title	string	标题
     * body	string	内容
     * type	int	1 - 收入 -1 - 支出
     * amount	int	金额，分单位
     * state	int	订单状态，0: 等待，1：成功，-1: 失败
     */

    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id;
    @SerializedName("owner_id")
    private Long user_id;
    @SerializedName("target_type")
    private String channel;
    @SerializedName("target_id")
    private String account;
    @SerializedName("type")
    private int action;
    private int amount;
    @SerializedName("title")
    private String subject;
    private String body;
    @SerializedName("state")
    private int status;
    private String created_at;
    private String updated_at;

    @ToOne(joinProperty = "user_id")
    private UserInfoBean userInfoBean;

    @Override
    public Long getMaxId() {
        return (long) this.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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
        dest.writeValue(this.user_id);
        dest.writeString(this.channel);
        dest.writeString(this.account);
        dest.writeInt(this.action);
        dest.writeInt(this.amount);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.userInfoBean, flags);
    }

    /** To-one relationship, resolved on first access. */
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

    /** called by internal mechanisms, do not call yourself. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2001597343)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRechargeSuccessBeanDao() : null;
    }

    public RechargeSuccessBean() {
    }

    protected RechargeSuccessBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readInt();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.channel = in.readString();
        this.account = in.readString();
        this.action = in.readInt();
        this.amount = in.readInt();
        this.subject = in.readString();
        this.body = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.userInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Generated(hash = 2019727700)
    public RechargeSuccessBean(Long _id, int id, Long user_id, String channel, String account,
            int action, int amount, String subject, String body, int status, String created_at,
            String updated_at) {
        this._id = _id;
        this.id = id;
        this.user_id = user_id;
        this.channel = channel;
        this.account = account;
        this.action = action;
        this.amount = amount;
        this.subject = subject;
        this.body = body;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static final Creator<RechargeSuccessBean> CREATOR = new Creator<RechargeSuccessBean>() {
        @Override
        public RechargeSuccessBean createFromParcel(Parcel source) {
            return new RechargeSuccessBean(source);
        }

        @Override
        public RechargeSuccessBean[] newArray(int size) {
            return new RechargeSuccessBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1729717820)
    private transient RechargeSuccessBeanDao myDao;
    @Generated(hash = 1005780391)
    private transient Long userInfoBean__resolvedKey;
}
