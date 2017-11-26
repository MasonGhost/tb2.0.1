package com.zhiyicx.old.imsdk.entity;

import java.io.Serializable;
import java.util.List;

/**
 * WebSocket通信消息内容
 * Created by jungle on 16/5/18.
 * com.zhiyicx.zhibo.model.entity
 * zhibo_android
 * email:335891510@qq.com
 */
@org.msgpack.annotation.Message
public class Message implements Serializable {
    public int uid;
    public int cid;
    public List<Integer> to;
    public int id;//每一条消息对应的单独的idint
    public int type;
    public String txt;//对应推送时显示的信息，和消息文本一致
    public MessageExt ext;
    public boolean rt;//是否是实时消息（Real-time Message），如果有此项且值为true时为实时消息，否则为普通消息
    public int err;//socket错误事件
    public long expire = -1;
    public long mid;//mid 服务端消息ID。mid >> 23 + 1451577600000 = 消息的毫秒时间戳
    public long create_time;
    public boolean is_del;//'是否被删除 1:是 0:否',
    public boolean is_read;//'消息阅读状态 1:是 0:否',
    public int seq;//消息序号，实时消息没有

    public boolean is_del() {
        return is_del;
    }

    public boolean is_read() {
        return is_read;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public boolean getIs_del() {
        return is_del;
    }

    public void setIs_del(boolean is_del) {
        this.is_del = is_del;
    }

    public boolean getIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }


    public Message(int id) {
        this.id = id;
    }

    public Message() {

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public List<Integer> getTo() {
        return to;
    }

    public void setTo(List<Integer> to) {
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public MessageExt getExt() {
        return ext;
    }

    public void setExt(MessageExt ext) {
        this.ext = ext;
    }

    public boolean isRt() {
        return rt;
    }

    public void setRt(boolean rt) {
        this.rt = rt;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uid=" + uid +
                ", cid=" + cid +
                ", to=" + to +
                ", id=" + id +
                ", type=" + type +
                ", txt='" + txt + '\'' +
                ", ext=" + ext +
                ", rt=" + rt +
                ", err=" + err +
                ", expire=" + expire +
                ", mid=" + mid +
                ", create_time=" + create_time +
                ", is_del=" + is_del +
                ", is_read=" + is_read +
                ", seq=" + seq +
                '}';
    }
}
