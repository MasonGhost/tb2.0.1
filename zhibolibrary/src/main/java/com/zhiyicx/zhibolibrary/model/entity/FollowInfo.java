package com.zhiyicx.zhibolibrary.model.entity;

import java.io.Serializable;

/**
 * Created by jess on 16/4/23.
 */
public class FollowInfo implements Serializable{
    public Integer is_follow;

    @Override
    public String toString() {
        return "FollowInfo{" +
                "is_follow=" + is_follow +
                '}';
    }
}
