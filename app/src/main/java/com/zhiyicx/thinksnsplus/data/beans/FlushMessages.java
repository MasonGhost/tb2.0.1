package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/13
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class FlushMessages {

    /**
     * key : comments
     * uids :
     * count : 0
     * time : 2017-04-13 3:31:10
     */
    @Id
    long id;
    @Unique
    private String key;
    private String uids;
    private int count;
    private String time;

    @Generated(hash = 1857906646)
    public FlushMessages(long id, String key, String uids, int count, String time) {
        this.id = id;
        this.key = key;
        this.uids = uids;
        this.count = count;
        this.time = time;
    }

    @Generated(hash = 505793851)
    public FlushMessages() {
    }

    @Override
    public String toString() {
        return "FlushMessages{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", uids='" + uids + '\'' +
                ", count=" + count +
                ", time='" + time + '\'' +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUids() {
        return this.uids;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
