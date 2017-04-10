package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */

public class JpushMessageBean {

    private String message;
    private String type;
    private String action;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "JpushMessageBean{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
