package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/3/15.
 */
public class UserInfo implements Serializable {
    public String password;
    public String uid;
    public String usid;
    public String uname;
    public String phone;
    public Integer sex;
    public String intro;
    public String first_letter;
    public String location;
    public String last_login_time;
    public Integer phone_status;
    public String reg_time;
    public int is_verified;
    public int gold;
    public int follow_count;
    public int fans_count;
    public int zan_count;
    public int is_follow;
    public String auth_accesskey;
    public String auth_secretkey;
    public Icon cover;
    public Icon avatar;
    public Integer live_time;
    public ImInfo im;
    public String ticket;//智播sdk票据

    @Override
    public String toString() {
        return "UserInfo{" +
                "password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", usid='" + usid + '\'' +
                ", uname='" + uname + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", intro='" + intro + '\'' +
                ", first_letter='" + first_letter + '\'' +
                ", location='" + location + '\'' +
                ", last_login_time='" + last_login_time + '\'' +
                ", phone_status=" + phone_status +
                ", reg_time='" + reg_time + '\'' +
                ", is_verified=" + is_verified +
                ", gold=" + gold +
                ", follow_count=" + follow_count +
                ", fans_count=" + fans_count +
                ", zan_count=" + zan_count +
                ", is_follow=" + is_follow +
                ", auth_accesskey='" + auth_accesskey + '\'' +
                ", auth_secretkey='" + auth_secretkey + '\'' +
                ", cover=" + cover +
                ", avatar=" + avatar +
                ", live_time=" + live_time +
                ", im=" + im +
                ", ticket='" + ticket + '\'' +
                '}';
    }

    public UserInfo(String uid, String uname, int is_verified, String location, Icon avatar) {
        this.uid = uid;
        this.uname = uname;
        this.location = location;
        this.avatar = avatar;
        this.is_verified=is_verified;
    }

    public UserInfo(String phone) {
        this.phone = phone;
    }

    public UserInfo(Icon avatar) {
        this.avatar = avatar;
    }

    public UserInfo() {
    }

    public UserInfo(String uid, String uname) {
        this.uid=uid;
        this.uname=uname;
    }

}
