package com.zhiyicx.thinksnsplus.data.beans;

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
     * id : 4
     * value : 10
     * type : alipay
     * account : xxx@alipay.com
     * status : 0
     * remark : null
     * created_at : 2017-06-01 09:30:22
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private int id;
    private int value;
    private String type;
    private String account;
    private int status;
    private long remark;
    private String created_at;

    @Generated(hash = 138785795)
    public WithdrawalsListBean(Long _id, int id, int value, String type,
            String account, int status, long remark, String created_at) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Long getMaxId() {
        return (long) id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRemark() {
        return remark;
    }

    public void setRemark(long remark) {
        this.remark = remark;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
