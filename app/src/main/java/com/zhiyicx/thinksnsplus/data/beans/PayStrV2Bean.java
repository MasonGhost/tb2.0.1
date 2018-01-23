package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/23
 * @Contact master.jungle68@gmail.com
 */
public class PayStrV2Bean {

    /**
     * order : {"id":1,"owner_id":1,"target_type":"recharge_ping_p_p","target_id":1,"title":"充值","body":"积分充值","type":-1,"amount":500,"state":0,
     * "created_at":"2018-01-04 07:29:57","updated_at":"2018-01-04 07:29:57"}
     * pingpp_order : {"id":"ch_08anD0a9yjPCLyvbTODqXrnT","object":"charge","created":1496819712,"livemode":false,"paid":false,"refunded":false,
     * "app":"app_5anXP4ezfXvL8m5e","channel":"applepay_upacp","order_no":"C0000000000000000008","client_ip":"127.0.0.1","amount":500,
     * "amount_settle":500,"currency":"cny","subject":"余额充值","body":"积分余额充值","extra":{},"time_paid":null,"time_expire":1496906112,
     * "time_settle":null,"transaction_no":null,"refunds":{"object":"list","url":"/v1/charges/ch_08anD0a9yjPCLyvbTODqXrnT/refunds",
     * "has_more":false,"data":[]},"amount_refunded":0,"failure_code":null,"failure_msg":null,"metadata":{},"credential":{"object":"credential",
     * "applepay_upacp":{"tn":"201706071515122891443","mode":"00","merchant_id":"Your app merchant id"}},"description":null}
     */

    private OrderBean order;
    private PayStrBean.ChargeBean pingpp_order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public PayStrBean.ChargeBean getPingpp_order() {
        return pingpp_order;
    }

    public void setPingpp_order(PayStrBean.ChargeBean pingpp_order) {
        this.pingpp_order = pingpp_order;
    }

    public static class OrderBean {
        /**
         * id : 1
         * owner_id : 1
         * target_type : recharge_ping_p_p
         * target_id : 1
         * title : 充值
         * body : 积分充值
         * type : -1
         * amount : 500
         * state : 0
         * created_at : 2018-01-04 07:29:57
         * updated_at : 2018-01-04 07:29:57
         */

        private int id;
        private int owner_id;
        private String target_type;
        private int target_id;
        private String title;
        private String body;
        private int type;
        private int amount;
        private int state;
        private String created_at;
        private String updated_at;

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

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
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

}
