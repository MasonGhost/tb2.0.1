package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class WithdrawalsListBean extends BaseListBean {

    /**
     * {
     * "id": 4, // 提现记录ID
     * "value": 10, // 提现金额
     * "type": "alipay", // 提现方式
     * "account": "xxx@alipay.com", // 提现账户
     * "status": 0, // 提现状态， 0 - 待审批，1 - 已审批，2 - 被拒绝
     * "remark": null, // 备注，审批或者拒绝的时候由管理填写
     * "created_at": "2017-06-01 09:30:22" // 申请时间
     * }
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id;
    private int value;
    private String type;
    private String account;
    private int status;
    private String remark;
    private String created_at;

    @Generated(hash = 1425554202)
    public WithdrawalsListBean(Long _id, int id, int value, String type,
                               String account, int status, String remark, String created_at) {
        this._id = _id;
        this.id = id;
        this.value = value;
        this.type = type;
        this.account = account;
        this.status = status;
        this.remark = remark;
        this.created_at = created_at;
    }

    @Generated(hash = 474679993)
    public WithdrawalsListBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreated_at() {
        return this.created_at;
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
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeInt(this.id);
        dest.writeInt(this.value);
        dest.writeString(this.type);
        dest.writeString(this.account);
        dest.writeInt(this.status);
        dest.writeString(this.remark);
        dest.writeString(this.created_at);
    }

    protected WithdrawalsListBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readInt();
        this.value = in.readInt();
        this.type = in.readString();
        this.account = in.readString();
        this.status = in.readInt();
        this.remark = in.readString();
        this.created_at = in.readString();
    }

    public static final Creator<WithdrawalsListBean> CREATOR = new Creator<WithdrawalsListBean>() {
        @Override
        public WithdrawalsListBean createFromParcel(Parcel source) {
            return new WithdrawalsListBean(source);
        }

        @Override
        public WithdrawalsListBean[] newArray(int size) {
            return new WithdrawalsListBean[size];
        }
    };
}
