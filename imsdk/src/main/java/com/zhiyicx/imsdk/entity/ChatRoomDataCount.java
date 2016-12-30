package com.zhiyicx.imsdk.entity;

import java.io.Serializable;

/**
 * Created by jungle on 16/8/26.
 * com.zhiyicx.imsdk.entity
 * zhibo_android
 * email:335891510@qq.com
 */
public class ChatRoomDataCount implements Serializable {
    /**
     * `reviewCount` = value; // 浏览量
     * `viererCount` = number; // 在线人数
     */
    private int reviewCount;
    private int viererCount;

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getViererCount() {
        return viererCount;
    }

    @Override
    public String toString() {
        return "ChatRoomDataCount{" +
                "reviewCount=" + reviewCount +
                ", viererCount=" + viererCount +
                '}';
    }

    public void setViererCount(int viererCount) {
        this.viererCount = viererCount;
    }
}
