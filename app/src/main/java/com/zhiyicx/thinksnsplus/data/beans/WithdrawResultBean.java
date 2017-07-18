package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/02/9:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawResultBean {

    private List<String> value;
    private List<String> type;
    private List<String> account;
    private List<String> message;

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getAccount() {
        return account;
    }

    public void setAccount(List<String> account) {
        this.account = account;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
