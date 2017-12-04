package com.zhiyicx.zhibolibrary.model.entity;

/**
 * Created by jess on 16/4/26.
 */
public class TradeOrder {

    /**
     * user_id : 324
     * channel : live
     * account : 324
     * subject : 直播获取的赞兑换金币
     * action : 1
     * amount : 200
     * body : 将直播获得的赞兑换成为金币
     * status : 1
     * updated_at : 2017-12-04 06:59:23
     * created_at : 2017-12-04 06:59:23
     * id : 8273
     */

    private int user_id;
    private String usid;
    private String channel;
    private int account;
    private String subject;
    private int action;
    private int amount;
    private String body;
    private int status;
    private String updated_at;
    private String created_at;
    private int id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
