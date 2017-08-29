package com.zhiyicx.thinksnsplus.data.beans.request;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/22
 * @Contact master.jungle68@gmail.com
 */
public class BindAccountRequstBean {
    private String access_token;
    private String login;
    private String password;

    public BindAccountRequstBean(String access_token, String login, String password) {
        this.access_token = access_token;
        this.login = login;
        this.password = password;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
