package com.zhiyicx.thinksnsplus.config;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/9/4
 * @Contact master.jungle68@gmail.com
 */
public class NotificationConfig {


    /**
     * 通知来源频道，客户端需要根据 data.channel 值进行独立解析。已知频道:
     *
     * @see {https://github.com/zhiyicx/thinksns-plus-document/blob/master/Summary/notifications.md#%E8%AF%84%E8%AE%BA%E5%8A%A8%E6%80%81}
     * <p>
     * feed:comment 动态被评论
     * feed:reply-comment 动态评论被回复
     * feed:pinned-comment 动态评论申请置顶
     * feed:digg 动态被点赞
     */
    public static final String NOTIFICATION_KEY_FEED_DIGGS = "feed:digg";
    public static final String NOTIFICATION_KEY_FEED_COMMENTS = "feed:comment";
    public static final String NOTIFICATION_KEY_FEED_REPLY_COMMENTS = "feed:reply-comment";
    public static final String NOTIFICATION_KEY_FEED_PINNED_COMMENT = "feed:pinned-comment";
    public static final String NOTIFICATION_KEY_FOLLOWS = "follows";
    public static final String NOTIFICATION_KEY_NOTICES = "notices";
}
