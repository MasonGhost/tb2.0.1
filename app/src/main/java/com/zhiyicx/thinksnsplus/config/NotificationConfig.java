package com.zhiyicx.thinksnsplus.config;

/**
 * @Describe 通知分类 ，文档说明  @see {https://github.com/zhiyicx/thinksns-plus-document/blob/master/Summary/notifications.md#%E8%AF%84%E8%AE%BA%E5%8A%A8%E6%80%81}
 * @Author Jungle68
 * @Date 2017/9/4
 * @Contact master.jungle68@gmail.com
 */
public class NotificationConfig {
    // 关注
    public static final String NOTIFICATION_KEY_FOLLOWS = "follows";
    // 系统公告
    public static final String NOTIFICATION_KEY_NOTICES = "notices";

    /**
     * 打赏用户操作
     */
    // 打赏用户通知
    public static final String NOTIFICATION_KEY_USER_REWARD = "user:reward";
    // 被打赏用户通知
    public static final String NOTIFICATION_KEY_USER_HAD_REWARD = "user:reward";
    /**
     * 购买付费节点
     */
    // 购买者通知
    public static final String NOTIFICATION_KEY_PAID = "paid:xxxxx";

    /**
     * 评论动态
     */
    // 动态作者通知
    public static final String NOTIFICATION_KEY_FEED_COMMENTS = "feed:comment";
    // 有回复者时，被回复者通知
    public static final String NOTIFICATION_KEY_FEED_COMMENT_REPLY = "feed:comment-reply";
    /**
     * 申请动态评论置顶
     */
    // 动态作者通知
    public static final String NOTIFICATION_KEY_FEED_PINNED_COMMENT = "feed:pinned-comment";
    /**
     * 打赏动态
     */
    // 打赏用户通知
    public static final String NOTIFICATION_KEY_FEED_REWARD = "feed:reward";
    // 被打赏用户通知
    public static final String NOTIFICATION_KEY_FEED_HAD_REWARD = "feed:reward";
    /**
     * 点赞动态
     */
    public static final String NOTIFICATION_KEY_FEED_DIGGS = "feed:digg";

    /**
     * 评论音乐
     */
    // 有回复者时，被回复者通知
    public static final String NOTIFICATION_KEY_MUSIC_COMMENT_REPLY = "music:comment-reply";
    /**
     * 评论专辑
     */
    // 有回复者时，被回复者通知
    public static final String NOTIFICATION_KEY_MUSIC_SPECIAL_COMMENT_REPLY = "music:special-comment-reply";
    /**
     * 评论资讯
     */
    // 资讯作者通知
    public static final String NOTIFICATION_KEY_NEWS_COMMENT = "news:comment";
    // 有回复者时，被回复者通知
    public static final String NOTIFICATION_KEY_NEWS_COMMENT_REPLY = "news:comment-reply";
    /**
     * 申请资讯评论置顶
     */
    //  资讯作者通知
    public static final String NOTIFICATION_KEY_NEWS_PINNED_COMMENT = "news:pinned-comment";
    //资讯评论置顶审核通知
    public static final String NOTIFICATION_KEY_NEWS_PINNED_COMMENT_STATUS_SUCCESS = "news:pinned-comment";
    // 驳回时，申请者通知
    public static final String NOTIFICATION_KEY_NEWS_PINNED_COMMENT_STATUS_TURN_DOWN = "news:pinned-comment";
    /**
     * 申请资讯置顶
     */
    // 资讯作者通知
    public static final String NOTIFICATION_KEY_NEWS_PINNED_NEWS = "news:pinned-news";

    /**
     * 打赏资讯
     */
    // 打赏用户通知
    public static final String NOTIFICATION_KEY_NEWS_REWARD = "news:reward";
    // 被打赏用户(资讯作者)通知
    public static final String NOTIFICATION_KEY_NEWS_HAD_REWARD = "news:reward";
    /**
     * 回答问题
     */
    // 被邀请者回答时，问题发起者消息
    public static final String NOTIFICATION_KEY_INVITE_QUESTION_ANSWER = "question:answer";
    // 其他回答时，问题发起者消息
    public static final String NOTIFICATION_KEY_QUESTION_ANSWER = "question:answer";
    /**
     * 评论问题
     */
    // 问题作者通知
    public static final String NOTIFICATION_KEY_QUESTION_COMMENT = "question:comment";
    // 有回复者时，被回复者通知
    public static final String NOTIFICATION_KEY_QUESTION_COMMENT_REPLY = "question:comment-reply";

    /**
     * 评论回答
     */
    // 答案作者通知
    public static final String NOTIFICATION_KEY_ANSWER_COMMENT = "answer:comment";
    // 有回复者时，被回复者通知
    public static final String NOTIFICATION_KEY_ANSWER_COMMENT_REPLY = "answer:comment-reply";

    /**
     * 采纳答案
     */
    // 答案作者通知
    public static final String NOTIFICATION_KEY_QUESTION_ANSWER_ADOPTION = "question:answer-adoption";
    /**
     * 邀请回答
     */
    // 被邀请者通知
    public static final String NOTIFICATION_KEY_QUESTION_INVITE = "question";


    /**
     * 置顶审核
     */
    public static final String TOP_DYNAMIC_COMMENT = "comment";
    public static final String TOP_NEWS_COMMENT = "news:comment";
    public static final String TOP_POST_COMMENT = "post:comment";
    public static final String TOP_POST = "post";
    public static final String TOP_CIRCLE_MEMBER = "group:join";


}
