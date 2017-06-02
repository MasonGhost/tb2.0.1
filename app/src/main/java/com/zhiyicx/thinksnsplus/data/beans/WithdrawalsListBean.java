package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/05/24/9:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
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

    private int id;
    private int value;
    private String type;
    private String account;
    private int status;
    private long remark;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        setMaxId((long) id);
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
}
