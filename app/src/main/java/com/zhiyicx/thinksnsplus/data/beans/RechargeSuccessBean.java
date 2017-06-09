package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @Author Jliuer
 * @Date 2017/06/08/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class RechargeSuccessBean extends BaseListBean implements Parcelable {

    /**
     * id : 1
     * user_id : 1
     * channel : alipay
     * account : alipay_account
     * charge_id : ch_vvP4u1H0evPGqn9qn5mPCGS4
     * action : 1
     * amount : 100
     * currency : cny
     * subject : 余额充值
     * body : 账户余额充值
     * transaction_no : 2017060879918233
     * status : 1
     * created_at : 2017-06-07 06:32:28
     * updated_at : 2017-06-08 06:46:23
     * deleted_at : null
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id;
    private int user_id;
    private String channel;
    private String account;
    private String charge_id;
    private int action;
    private int amount;
    private String currency;
    private String subject;
    private String body;
    private String transaction_no;
    private int status;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    @Generated(hash = 719075135)
    public RechargeSuccessBean(Long _id, int id, int user_id, String channel, String account,
                               String charge_id, int action, int amount, String currency, String subject,
                               String body, String transaction_no, int status, String created_at,
                               String updated_at, String deleted_at) {
        this._id = _id;
        this.id = id;
        this.user_id = user_id;
        this.channel = channel;
        this.account = account;
        this.charge_id = charge_id;
        this.action = action;
        this.amount = amount;
        this.currency = currency;
        this.subject = subject;
        this.body = body;
        this.transaction_no = transaction_no;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    @Generated(hash = 1436548267)
    public RechargeSuccessBean() {
    }

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getCharge_id() {
        return charge_id;
    }

    public void setCharge_id(String charge_id) {
        this.charge_id = charge_id;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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
        dest.writeInt(this.user_id);
        dest.writeString(this.channel);
        dest.writeString(this.account);
        dest.writeString(this.charge_id);
        dest.writeInt(this.action);
        dest.writeInt(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeString(this.transaction_no);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    protected RechargeSuccessBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readInt();
        this.user_id = in.readInt();
        this.channel = in.readString();
        this.account = in.readString();
        this.charge_id = in.readString();
        this.action = in.readInt();
        this.amount = in.readInt();
        this.currency = in.readString();
        this.subject = in.readString();
        this.body = in.readString();
        this.transaction_no = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
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

    @Override
    public String toString() {
        return "id=" + id + "\n" +
                "action=" + action + "\n" +
                "status=" + status + "\n";
    }
}
