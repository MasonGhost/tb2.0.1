package com.zhiyicx.old.imsdk.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.old.imsdk.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class Conversation implements Serializable {

    public static final int CONVERSATION_TYPE_PRIVATE = 0;
    public static final int CONVERSATION_TYPE_TEAM = 1;
    public static final int CONVERSATION_TYPE_CHAROOM = 2;
    /**
     * CREATE TABLE `conversation` (
     * `cid` int(11) NOT NULL COMMENT '对话ID',
     * `type` tinyint(1) NOT NULL COMMENT '对话类型',0私聊，1群聊2聊天室
     * `name` varchar(32) DEFAULT NULL COMMENT '对话名称',
     * `disable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '对话禁言状态',
     * `pair` varchar(21) DEFAULT NULL COMMENT '私有对话双方uid标识',
     * `pwd` varchar(32) DEFAULT NULL COMMENT '加入对话的秘钥',
     * `last_message_time` int(11) NOT NULL DEFAULT '0' COMMENT '最新消息时间',
     * `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被删除 1:是 0:否',
     * `im_uid` int(11) NOT NULL COMMENT '当前登陆的用户IM_uid'
     * `mc` int(11) 当前群聊中的人数
     * )
     */
    private int cid;
    private int type = -1;
    private String name;
    private int disa;
    private String pair;
    private String pwd;
    private long last_message_time;
    private String last_message_text;
    private String usids;//聊天对方的usids
    private boolean is_del;
    private int im_uid;
    private int mc;

    public int getMc() {
        return mc;
    }

    public void setMc(int mc) {
        this.mc = mc;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisa() {
        return disa;
    }

    public void setDisa(int disa) {
        this.disa = disa;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public long getLast_message_time() {
        return last_message_time;
    }

    public void setLast_message_time(long last_message_time) {
        this.last_message_time = last_message_time;
    }

    public String getLast_message_text() {
        return last_message_text;
    }

    public void setLast_message_text(String last_message_text) {
        this.last_message_text = last_message_text;
    }

    public String getUsids() {
        return usids;
    }

    public void setUsids(String usids) {
        this.usids = usids;
    }

    public boolean is_del() {
        return is_del;
    }

    public void setIs_del(boolean is_del) {
        this.is_del = is_del;
    }

    public int getIm_uid() {
        return im_uid;
    }

    public void setIm_uid(int im_uid) {
        this.im_uid = im_uid;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "cid=" + cid +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", disa=" + disa +
                ", pair='" + pair + '\'' +
                ", pwd='" + pwd + '\'' +
                ", last_message_time=" + last_message_time +
                ", last_message_text='" + last_message_text + '\'' +
                ", usids='" + usids + '\'' +
                ", is_del=" + is_del +
                ", im_uid=" + im_uid +
                ", mc=" + mc +
                '}';
    }
}
