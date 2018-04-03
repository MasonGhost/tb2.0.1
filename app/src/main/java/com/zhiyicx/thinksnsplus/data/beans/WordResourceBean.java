package com.zhiyicx.thinksnsplus.data.beans;


import java.io.Serializable;

public class WordResourceBean implements Serializable {

    /**
     * id 要留言资源的 id
     * title 要留言资源的标题
     * des 要留言资源的内容
     */
    private UserInfoBean user;
    private String id;
    private String title;
    private String des;

    public WordResourceBean(UserInfoBean user, String id, String title, String des) {
        this.user = user;
        this.id = id;
        this.title = title;
        this.des = des;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
