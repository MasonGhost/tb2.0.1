package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Author Jliuer
 * @Date 2017/06/08/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RechargeSuccessBean {

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
    private Object deleted_at;

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

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }
}
