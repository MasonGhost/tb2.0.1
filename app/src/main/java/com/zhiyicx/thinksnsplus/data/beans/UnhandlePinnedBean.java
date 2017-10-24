package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/10/24
 * @Contact master.jungle68@gmail.com
 */
public class UnhandlePinnedBean {

    /**
     * news : 0
     * feeds : 1
     */

    private int news;
    private int feeds;

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    public int getFeeds() {
        return feeds;
    }

    public void setFeeds(int feeds) {
        this.feeds = feeds;
    }

    @Override
    public String toString() {
        return "UnhandlePinnedBean{" +
                "news=" + news +
                ", feeds=" + feeds +
                '}';
    }
}
