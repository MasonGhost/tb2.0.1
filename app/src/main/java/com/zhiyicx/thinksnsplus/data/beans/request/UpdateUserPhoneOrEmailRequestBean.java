package com.zhiyicx.thinksnsplus.data.beans.request;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/23
 * @Contact master.jungle68@gmail.com
 */
public class UpdateUserPhoneOrEmailRequestBean {


    private String phone; // 如果 email 不存在则必须，用户新的手机号码。
    private String email; // 如果 phone 不存在则必须，用户新的邮箱地址。
    private String verifiable_code; //必须，验证码。

    public UpdateUserPhoneOrEmailRequestBean(String phone, String email, String verifiable_code) {
        this.phone = phone;
        this.email = email;
        this.verifiable_code = verifiable_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifiable_code() {
        return verifiable_code;
    }

    public void setVerifiable_code(String verifiable_code) {
        this.verifiable_code = verifiable_code;
    }
}
