package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.DynamicConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.InfoConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by lx on 2018/3/24.
 */

@Entity
public class TbMessageBean extends BaseListBean {
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private Long user_id;
    private boolean mIsPinned;
    private boolean mIsRead;
    @Convert(converter = InfoConvert.class, columnType = String.class)
    private InfoListDataBean news;
    @Convert(converter = DynamicConvert.class, columnType = String.class)
    private DynamicDetailBeanV2 feed;
    private long pinnedTime;
    private long mLoginUserId;

    /**
     * publish:feed  publish:news
     */
    private String channel;
    public final static String FEED = "feed";
    public final static String NEWS = "news";
    @Generated(hash = 1937955951)
    public TbMessageBean(Long _id, Long user_id, boolean mIsPinned, boolean mIsRead,
            InfoListDataBean news, DynamicDetailBeanV2 feed, long pinnedTime,
            long mLoginUserId, String channel) {
        this._id = _id;
        this.user_id = user_id;
        this.mIsPinned = mIsPinned;
        this.mIsRead = mIsRead;
        this.news = news;
        this.feed = feed;
        this.pinnedTime = pinnedTime;
        this.mLoginUserId = mLoginUserId;
        this.channel = channel;
    }
    @Generated(hash = 844374587)
    public TbMessageBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public Long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public boolean getMIsPinned() {
        return this.mIsPinned;
    }
    public void setMIsPinned(boolean mIsPinned) {
        this.mIsPinned = mIsPinned;
    }
    public boolean getMIsRead() {
        return this.mIsRead;
    }
    public void setMIsRead(boolean mIsRead) {
        this.mIsRead = mIsRead;
    }
    public InfoListDataBean getNews() {
        return this.news;
    }
    public void setNews(InfoListDataBean news) {
        this.news = news;
    }
    public DynamicDetailBeanV2 getFeed() {
        return this.feed;
    }
    public void setFeed(DynamicDetailBeanV2 feed) {
        this.feed = feed;
    }
    public long getPinnedTime() {
        return this.pinnedTime;
    }
    public void setPinnedTime(long pinnedTime) {
        this.pinnedTime = pinnedTime;
    }
    public long getMLoginUserId() {
        return this.mLoginUserId;
    }
    public void setMLoginUserId(long mLoginUserId) {
        this.mLoginUserId = mLoginUserId;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }


}
