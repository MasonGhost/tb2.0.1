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
    private ChargeBean pingpp_order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public ChargeBean getPingpp_order() {
        return pingpp_order;
    }

    public void setPingpp_order(ChargeBean pingpp_order) {
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
        private String target_id;
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

        public String getTarget_id() {
            return target_id;
        }

        public void setTarget_id(String target_id) {
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
    public static class ChargeBean {
        /**
         * id : ch_08anD0a9yjPCLyvbTODqXrnT
         * object : charge
         * created : 1496819712
         * livemode : false
         * paid : false
         * refunded : false
         * app : app_5anXP4ezfXvL8m5e
         * channel : applepay_upacp
         * order_no : a0000000000000000008
         * client_ip : 127.0.0.1
         * amount : 500
         * amount_settle : 500
         * currency : cny
         * subject : 余额充值
         * body : 账户余额充值
         * extra : {}
         * time_paid : null
         * time_expire : 1496906112
         * time_settle : null
         * transaction_no : null
         * refunds : {"object":"list","url":"/v1/charges/ch_08anD0a9yjPCLyvbTODqXrnT/refunds","has_more":false,"data":[]}
         * amount_refunded : 0
         * failure_code : null
         * failure_msg : null
         * metadata : {}
         * credential : {"object":"credential","applepay_upacp":{"tn":"201706071515122891443","mode":"00","merchant_id":"Your app merchant id"}}
         * description : null
         */

        private String id;
        private String object;
        private int created;
        private boolean livemode;
        private boolean paid;
        private boolean refunded;
        private String app;
        private String channel;
        private String order_no;
        private String client_ip;
        private int amount;
        private int amount_settle;
        private String currency;
        private String subject;
        private String body;
        private ExtraBean extra;
        private Object time_paid;
        private int time_expire;
        private Object time_settle;
        private Object transaction_no;
        private RefundsBean refunds;
        private int amount_refunded;
        private Object failure_code;
        private Object failure_msg;
        private MetadataBean metadata;
        private Object credential;
        private Object description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public boolean isLivemode() {
            return livemode;
        }

        public void setLivemode(boolean livemode) {
            this.livemode = livemode;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public boolean isRefunded() {
            return refunded;
        }

        public void setRefunded(boolean refunded) {
            this.refunded = refunded;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getClient_ip() {
            return client_ip;
        }

        public void setClient_ip(String client_ip) {
            this.client_ip = client_ip;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAmount_settle() {
            return amount_settle;
        }

        public void setAmount_settle(int amount_settle) {
            this.amount_settle = amount_settle;
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

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public Object getTime_paid() {
            return time_paid;
        }

        public void setTime_paid(Object time_paid) {
            this.time_paid = time_paid;
        }

        public int getTime_expire() {
            return time_expire;
        }

        public void setTime_expire(int time_expire) {
            this.time_expire = time_expire;
        }

        public Object getTime_settle() {
            return time_settle;
        }

        public void setTime_settle(Object time_settle) {
            this.time_settle = time_settle;
        }

        public Object getTransaction_no() {
            return transaction_no;
        }

        public void setTransaction_no(Object transaction_no) {
            this.transaction_no = transaction_no;
        }

        public RefundsBean getRefunds() {
            return refunds;
        }

        public void setRefunds(RefundsBean refunds) {
            this.refunds = refunds;
        }

        public int getAmount_refunded() {
            return amount_refunded;
        }

        public void setAmount_refunded(int amount_refunded) {
            this.amount_refunded = amount_refunded;
        }

        public Object getFailure_code() {
            return failure_code;
        }

        public void setFailure_code(Object failure_code) {
            this.failure_code = failure_code;
        }

        public Object getFailure_msg() {
            return failure_msg;
        }

        public void setFailure_msg(Object failure_msg) {
            this.failure_msg = failure_msg;
        }

        public MetadataBean getMetadata() {
            return metadata;
        }

        public void setMetadata(MetadataBean metadata) {
            this.metadata = metadata;
        }

        public Object getCredential() {
            return credential;
        }

        public void setCredential(Object credential) {
            this.credential = credential;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public static class ExtraBean {
        }

        public static class RefundsBean {
        }

        public static class MetadataBean {
        }

        public static class CredentialBean {
            /**
             * object : credential
             * applepay_upacp : {"tn":"201706071515122891443","mode":"00","merchant_id":"Your app merchant id"}
             */

            private String object;
            private ApplepayUpacpBean applepay_upacp;

            public String getObject() {
                return object;
            }

            public void setObject(String object) {
                this.object = object;
            }

            public ApplepayUpacpBean getApplepay_upacp() {
                return applepay_upacp;
            }

            public void setApplepay_upacp(ApplepayUpacpBean applepay_upacp) {
                this.applepay_upacp = applepay_upacp;
            }

            public static class ApplepayUpacpBean {
                /**
                 * tn : 201706071515122891443
                 * mode : 00
                 * merchant_id : Your app merchant id
                 */

                private String tn;
                private String mode;
                private String merchant_id;

                public String getTn() {
                    return tn;
                }

                public void setTn(String tn) {
                    this.tn = tn;
                }

                public String getMode() {
                    return mode;
                }

                public void setMode(String mode) {
                    this.mode = mode;
                }

                public String getMerchant_id() {
                    return merchant_id;
                }

                public void setMerchant_id(String merchant_id) {
                    this.merchant_id = merchant_id;
                }
            }
        }
    }

}
