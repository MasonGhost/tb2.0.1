package com.zhiyicx.thinksnsplus.data.beans.request;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/23
 * @Contact master.jungle68@gmail.com
 */
public class DeleteUserPhoneOrEmailRequestBean {


    private String password; // 用户密码。
    private String verifiable_code; // 手机验证码或者邮箱验证码。

    public DeleteUserPhoneOrEmailRequestBean(String password, String verifiable_code) {
        this.password = password;
        this.verifiable_code = verifiable_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifiable_code() {
        return verifiable_code;
    }

    public void setVerifiable_code(String verifiable_code) {
        this.verifiable_code = verifiable_code;
    }
}
