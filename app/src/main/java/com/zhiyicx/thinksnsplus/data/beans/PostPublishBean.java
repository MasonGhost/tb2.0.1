package com.zhiyicx.thinksnsplus.data.beans;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/11/29/15:36
 * @Email Jliuer@aliyun.com
 * @Description 圈子发帖数据类
 */
public class PostPublishBean {

    @Expose(serialize = false, deserialize = false)
    private long circle_id;
    /**
     * 必须 帖子标题
     */
    private String title;
    /**
     * 必须 帖子内容
     */
    private String body;
    /**
     * 列表专用字段，概述，简短内容
     */
    private String summary;
    /**
     * 文件id,例如[1,2,3]
     */
    private List<Integer> images;
    /**
     * 同步至动态，同步需要传sync_feed = 1
     */
    private int sync_feed;
    /**
     * 设备标示 同步动态需要传 1:pc ; 2:h5 ; 3:ios ; 4:android ; 5:其他
     */
    private int feed_from;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }

    public int getSync_feed() {
        return sync_feed;
    }

    public void setSync_feed(int sync_feed) {
        this.sync_feed = sync_feed;
    }

    public int getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(int feed_from) {
        this.feed_from = feed_from;
    }

    public long getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(long circle_id) {
        this.circle_id = circle_id;
    }
}
