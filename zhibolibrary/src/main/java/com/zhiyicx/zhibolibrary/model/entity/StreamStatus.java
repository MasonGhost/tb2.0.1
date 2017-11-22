package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class StreamStatus implements Serializable {
    public String usid;
    public int status;
    public SearchResult info;

    @Override
    public String toString() {
        return "StreamStatus{" +
                "usid='" + usid + '\'' +
                ", status=" + status +
                ", info=" + info +
                '}';
    }
}
