package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/23
 * @Contact master.jungle68@gmail.com
 */
public class RechargeSuccessV2Bean extends BaseListBean {

    /**
     * id : 11
     * owner_id : 1
     * title : 积分充值
     * body : 充值积分：200积分
     * type : 1
     * target_type : recharge
     * target_id : 0
     * currency : 1
     * amount : 200
     * state : 0
     * created_at : 2018-01-18 07:57:21
     * updated_at : 2018-01-18 07:57:21
     * <p>
     * id	int	数据id
     * owner_id	int	用户（所属者）id
     * title	string	记录标题
     * body	string	记录信息
     * type	int	增减类型 1 - 收入、 -1 - 支出
     * target_type	string	操作类型 目前有： default - 默认操作、commodity - 购买积分商品、user - 用户到用户流程（如采纳、付费置顶等）、task - 积分任务、recharge - 充值、cash - 积分提取
     * target_id	string	当操作类型为user时，为用户id、当操作类型为recharge且充值完成时，为ping++订单号
     * currency	int	后台预设积分类型id，当前需求中暂无该需求，默认为1，类型为积分
     * amount	int	积分额
     * state	int	订单状态 0 - 等待、1 - 完成、-1 - 失败
     */

    private int id;
    private int owner_id;
    private String title;
    private String body;
    private int type;
    private String target_type;
    private String target_id;
    private int currency;
    private int amount;
    private int state;
    private String created_at;
    private String updated_at;

    @Override
    public Long getMaxId() {
        return Long.valueOf(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget_type() {
        return target_type;
    }

    public void setTarget_type(String target_type) {
        this.target_type = target_type;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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


}
