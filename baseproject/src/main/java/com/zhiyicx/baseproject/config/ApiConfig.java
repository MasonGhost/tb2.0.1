package com.zhiyicx.baseproject.config;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class ApiConfig {
    /**
     * 添加平台名称，用于接口
     * 1:pc 2:h5 3:ios 4:android 5:其他
     */
    public static final int ANDROID_PLATFORM = 4;

    /**
     * 接口请求失败后，最大重复请求次数
     */
    public static final int DEFAULT_MAX_RETRY_COUNT = 5;

    public static final String API_VERSION = "v1";
    public static final String API_VERSION_2 = "v2";

    /**
     * 网络根地址  http://192.168.10.222/
     * 测试服务器：http://192.168.2.222:8080/mockjs/2/test-get-repose-head-normal?
     */

    //public static final String APP_DOMAIN = "http://192.168.2.222:8080/mockjs/2/";// rap 测试服务器

    public static final boolean APP_IS_NEED_SSH_CERTIFICATE = true;// 自定义证书时使用false
    //        public static final String APP_DOMAIN = "https://plus.medz.cn/";// 在线测试服务器 2
    public static final String APP_DOMAIN_DEV = "http://dev.zhibocloud.cn/";// 模拟在线正式服务器
    public static final String APP_DOMAIN_TEST = "http://test-plus.zhibocloud.cn/";// 在线测试服务器
    public static final String APP_DOMAIN_FORMAL = "https://tsplus.zhibocloud.cn/";// 正式服务器
    public static final String APP_DOMAIN_FOR_TEARCHER_QIAO = "http://192.168.2.200/";// 乔老师本地服务器

    public static String APP_DOMAIN = APP_DOMAIN_DEV;

    public static final String URL_ABOUT_US = "api/" + API_VERSION_2 + "/aboutus";// 关于我们网站
    public static final String URL_JIPU_SHOP = "http://demo.jipukeji.com";// 极铺购物地址

    // 图片地址 V2
    public static final String IMAGE_PATH_V2 = "api/" + API_VERSION_2 + "/files/%s?w=%d&h=%d&q=%d";
    // 头像地址
    public static final String IMAGE_AVATAR_PATH_V2 = APP_DOMAIN + "api/" + API_VERSION_2 + "/users/%s/avatar";

    // 音乐地址 V2
    public static final String MUSIC_PATH = "api/" + API_VERSION_2 + "/files/%s";

    /*******************************************  接口 Path  *********************************************/

    /**
     * 登录 Login
     */
    public static final String APP_PATH_LOGIN = "api/" + API_VERSION_2 + "/tokens";

    /**
     * 密码 PasswordClient
     */
    public static final String APP_PATH_CHANGE_PASSWORD_V2 = "api/" + API_VERSION_2 + "/user/password";// 修改密码
    public static final String APP_PATH_FIND_PASSWORD_V2 = "api/" + API_VERSION_2 + "/user/retrieve-password";// 找回密码
    /**
     * 注册 RegitstClient
     */
    public static final String APP_PATH_REGISTER = "api/" + API_VERSION_2 + "/users";// 注册
    /**
     * 用户 UserInfoClient
     */
    public static final String APP_PATH_CHANGE_USER_INFO = "api/" + API_VERSION_2 + "/user";// 修改用户信息
    public static final String APP_PATH_GET_MY_DIGGS = "api/" + API_VERSION_2 + "/user/likes"; // 获取用户收到的点赞
    public static final String APP_PATH_GET_MY_COMMENTS = "api/" + API_VERSION_2 + "/user/comments"; // 获取用户收到的评论
    public static final String APP_PATH_UPDATE_USER_AVATAR = "api/" + API_VERSION_2 + "/user/avatar";// 修改用户头像
    public static final String APP_PATH_UPDATE_USER_BG = "api/" + API_VERSION_2 + "/user/bg";// 修改用户背景
    public static final String APP_PATH_UPDATE_USER_PHONE_OR_EMAIL = "api/" + API_VERSION_2 + "/user";// 更新认证用户的手机号码和邮箱
    public static final String APP_PATH_DELETE_USER_PHONE = "api/" + API_VERSION_2 + "/user/phone";// 解除用户 Phone 绑定
    public static final String APP_PATH_DELETE_USER_EMAIL = "api/" + API_VERSION_2 + "/user/email";// 解除用户 E-Mail 绑定


    // 用户标签
    public static final String APP_PATH_GET_USER_TAGS = "api/" + API_VERSION_2 + "/users/{user_id}/tags";// 获取一个用户的标签
    public static final String APP_PATH_GET_CURRENT_USER_TAGS = "api/" + API_VERSION_2 + "/user/tags";// 获取当前认证用户的标签
    public static final String APP_PATH_CURRENT_USER_ADD_TAGS = "api/" + API_VERSION_2 + "/user/tags/{tag_id}";// 当前认证用户附加一个标签
    public static final String APP_PATH_CURRENT_USER_DELETE_TAGS = "api/" + API_VERSION_2 + "/user/tags/{tag_id}";// 当前认证用户分离一个标签

    // 认证
    public static final String APP_PATH_CERTIFICATION = "api/" + API_VERSION_2 + "/user/certification"; // GET-获取认证信息 POST-申请认证 PACTH-更新认证

    // 打赏
    public static final String APP_PATH_REWARD_USER = "api/" + API_VERSION_2 + "/user/{user_id}/rewards"; // 打赏一个用户

    /**
     * 消息通知
     */
    // 未读通知数量检查
    public static final String APP_PATH_GET_CKECK_UNREAD_NOTIFICATION = "api/" + API_VERSION_2 + "/user/notifications";
    // 通知列表
    public static final String APP_PATH_GET_NOTIFICATION_LIST = "api/" + API_VERSION_2 + "/user/notifications";
    // 读取通知
    public static final String APP_PATH_GET_NOTIFICATION_DETIAL = "api/" + API_VERSION_2 + "/user/notifications/{notification}";
    // 标记通知阅读
    public static final String APP_PATH_MAKE_NOTIFICAITON_READED = "api/" + API_VERSION_2 + "/user/notifications/";
    // type  获取通知类型，可选 all,read,unread 默认 all

    public static final String NOTIFICATION_TYPE_ALL = "all";
    public static final String NOTIFICATION_TYPE_READ = "read";
    public static final String NOTIFICATION_TYPE_UNREAD = "unread ";

    /**
     * 聊天相关
     */
    public static final String APP_PATH_GET_IM_INFO = "api/" + API_VERSION_2 + "/im/users";// 获取 IM 帐号信息
    public static final String APP_PATH_CREATE_CONVERSAITON = "api/" + API_VERSION_2 + "/im/conversations";// 创建对话
    public static final String APP_PATH_GET_CONVERSAITON_LIST = "api/" + API_VERSION_2 + "/im/conversations/list/all";// 获取登陆用户的对话列表
    public static final String APP_PATH_GET_SINGLE_CONVERSAITON = "api/" + API_VERSION_2 + "/im/conversations/{cid}";// 获取单个对话信息

    /**
     * 关注粉丝 FollowFansClient
     */
    public static final String APP_PATH_FOLLOW_LIST = "api/" + API_VERSION_2 + "/users/{user_id}/followings";// 获取用户关注列表
    public static final String APP_PATH_FANS_LIST = "api/" + API_VERSION_2 + "/users/{user_id}/followers";// 获取用户粉丝列表
    public static final String APP_PATH_FOLLOW_USER = "api/" + API_VERSION_2 + "/user/followings/{user_id}";// 关注用户
    public static final String APP_PATH_FOLLOW_USER_FORMART = "api/" + API_VERSION_2 + "/user/followings/%d";// 关注用户
    public static final String APP_PATH_CANCEL_FOLLOW_USER = "api/" + API_VERSION_2 + "/user/followings/{user_id}";// 取消用户关注
    public static final String APP_PATH_CANCEL_FOLLOW_USER_FORMART = "api/" + API_VERSION_2 + "/user/followings/%d";// 取消用户关注

    /**
     * 动态相关
     */

    public static final String APP_PATH_SEND_DYNAMIC_V2 = "api/" + API_VERSION_2 + "/feeds";// 发布动态 V2

    public static final String APP_PATH_DELETE_DYNAMIC = "api/" + API_VERSION_2 + "/feeds/%s";// 删除一条动态
    // 获取动态列表
    public static final String DYNAMIC_TYPE_NEW = "new"; // 最新动态
    public static final String DYNAMIC_TYPE_FOLLOWS = "follow"; // 关注动态
    public static final String DYNAMIC_TYPE_HOTS = "hot"; // 热门动态
    public static final String DYNAMIC_TYPE_USERS = "users"; // 用户动态
    public static final String DYNAMIC_TYPE_SOMEONE = "users/%s"; // 某个人的动态列表,%s表示用户id
    public static final String DYNAMIC_TYPE_MY_COLLECTION = "collections";// 我收藏的动态列表
    // 点赞一条动态,取消点赞
    public static final String APP_PATH_DYNAMIC_CLICK_LIKE_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/like";
    public static final String APP_PATH_DYNAMIC_CANCEL_CLICK_LIKE_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/unlike";
    public static final String APP_PATH_DYNAMIC_CLICK_LIKE_FORMAT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/like";
    public static final String APP_PATH_DYNAMIC_CANCEL_CLICK_LIKE_FORMAT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/unlike";

    // 删除一条评论评论
    public static final String APP_PATH_DYNAMIC_DELETE_COMMENT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/comments/%s";
    // 对一条动态或一条动态评论进行评论
    public static final String APP_PATH_DYNAMIC_SEND_COMMENT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/comments";
    // 获取点赞列表
    public static final String APP_PATH_DYNAMIC_DIG_LIST_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/likes";
    // 一条动态的评论列表
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/comments";
    // 获取动态详情 V2
    public static final String APP_PATH_GET_DYNAMIC_DETAIL = "api/" + API_VERSION_2 + "/feeds/{feed_id}";

    // 获取动态列表 V2
    public static final String APP_PATH_GET_DYNAMIC_LIST_V2 = "api/" + API_VERSION_2 + "/feeds";//
    public static final String APP_PATH_GET_COLLECT_DYNAMIC_LIST_V2 = "api/" + API_VERSION_2 + "/feeds/collections";

    // 设置动态评论收费 V2
    public static final String APP_PATH_COMMENT_PAID_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/comment-paid";
    public static final String APP_PATH_COMMENT_PAID_V2_FORMAT = "api/" + API_VERSION_2 + "/feeds/%d/comment-paid";

    // 置顶动态 V2
    public static final String APP_PATH_TOP_DYNAMIC = "api/" + API_VERSION_2 + "/feeds/{feed_id}/pinneds";

    // 置顶动态评论 V2
    public static final String APP_PATH_TOP_DYNAMIC_COMMENT = "api/" + API_VERSION_2 + "/feeds/{feed_id}/comments/{comment_id}/pinneds";

    // 动态评论置顶审核列表 V2
    public static final String APP_PATH_REVIEW_DYNAMIC_COMMENT = "api/" + API_VERSION_2 +
            "/user/feed-comment-pinneds";

    // 同意动态评论置顶 V2
    public static final String APP_PATH_APPROVED_DYNAMIC_COMMENT = "api/" + API_VERSION_2 +
            "/feeds/{feed_id}/comments/{comment_id}/pinneds/{pinned_id}";

    // 拒绝动态评论置顶 V2
    public static final String APP_PATH_REFUSE_DYNAMIC_COMMENT = "api/" + API_VERSION_2 +
            "/user/feed-comment-pinneds/{pinned_id}";

    // 删除动态评论置顶 V2
    public static final String APP_PATH_DELETE_DYNAMIC_COMMENT = "api/" + API_VERSION_2 +
            "/feeds/{feed_id}/comments/{comment_id}/unpinned";

    // 收藏动态，取消收藏 V2
    public static final String APP_PATH_HANDLE_COLLECT_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/collections";
    public static final String APP_PATH_HANDLE_COLLECT_V2_FORMAT = "api/" + API_VERSION_2 + "/feeds/%s/collections";
    public static final String APP_PATH_HANDLE_UNCOLLECT_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/uncollect";
    public static final String APP_PATH_HANDLE_UNCOLLECT_V2_FORMAT = "api/" + API_VERSION_2 + "/feeds/%s/uncollect";


    // 动态打赏
    public static final String APP_PATH_DYNAMIC_REWARDS = "/api/" + API_VERSION_2 + "/feeds/{feed_id}/rewards";
    // 动态打赏用户列表
    public static final String APP_PATH_DYNAMIC_REWARDS_USER_LIST = "/api/" + API_VERSION_2 + "/feeds/{feed_id}/rewards";


    /**
     * 资讯相关
     */
    public static final String INFO_TYPE_COLLECTIONS = "-1000";// 资讯收藏列表

    // 资讯投稿
    public static final String APP_PATH_PUBLISH_INFO = "api/" + API_VERSION_2 + "/news/categories/{category}/news";

    // 修改资讯投稿
    public static final String APP_PATH_UPDATE_INFO = "api/" + API_VERSION_2 + "/news/categories/{category_id}/news/{news_id}";

    // 订阅资讯频道
    public static final String APP_PATH_INFO_FOLLOW_LIST = "api/" + API_VERSION_2 + "/news/categories/follows";

    // 资讯详情网页
    public static final String APP_PATH_INFO_DETAILS_FORMAT = "/h5/news/%s/detail";

    //置顶资讯、评论
    public static final String APP_PATH_TOP_INFO = "/api/" + API_VERSION_2 + "/news/{news_id}/pinneds";
    public static final String APP_PATH_TOP_INFO_COMMENT = "/api/" + API_VERSION_2 + "/news/{news_id}/comments/{comment_id}/pinneds";

    // 咨询打赏
    public static final String APP_PATH_INFO_REWARDS = "/api/" + API_VERSION_2 + "/news/{news_id}/rewards";
    // 打赏用户列表
    public static final String APP_PATH_INFO_REWARDS_USER_LIST = "/api/" + API_VERSION_2 + "/news/{news_id}/rewards";
    // 资讯打赏统计
    public static final String APP_PATH_INFO_REWARDS_COUNT = "/api/" + API_VERSION_2 + "/news/{news_id}/rewards/sum";

    // 获取用户投稿列表
    public static final String APP_PATH_GET_MY_INFO = "/api/" + API_VERSION_2 + "/user/news/contributes";

    // 查看资讯中申请置顶的评论列表
    public static final String APP_PATH_GET_REVIEW_INFO_COMMENT = "/api/" + API_VERSION_2 + "/news/comments/pinneds";

    // 同意资讯评论置顶
    public static final String APP_PATH_APPROVED_INFO_COMMENT = "/api/" + API_VERSION_2 + "/news/{news_id}/comments/{comment_id}/pinneds/{pinned_id}";

    // 拒绝资讯评论置顶
    public static final String APP_PATH_REFUSE_INFO_COMMENT = "/api/" + API_VERSION_2 + "/{news_id}/comments/{comment_id}/pinneds/{pinned_id}/reject";


    /**
     * 音乐相关 升级到V2
     */
    public static final String APP_PATH_MUSIC_ABLUM_LIST = "api/" + API_VERSION_2 + "/music/specials";// 专辑列表
    public static final String APP_PATH_MUSIC_COLLECT_ABLUM_LIST = "api/" + API_VERSION_2 + "/music/collections";// 收藏的专辑列表

    // 歌曲详情
    public static final String APP_PATH_MUSIC_DETAILS = "api/" + API_VERSION_2 + "/music/{music_id}";

    // 购买的单曲
    public static final String APP_PATH_MUSIC_PAIDS = "api/" + API_VERSION_2 + "/music/paids";

    // 购买的专辑
    public static final String APP_PATH_MUSIC_ALBUM_PAIDS = "api/" + API_VERSION_2 + "/music-specials/paids";

    // 评论歌曲
    public static final String APP_PATH_MUSIC_COMMENT = "api/" + API_VERSION_2 + "/music/{music_id}/comments";
    public static final String APP_PATH_MUSIC_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/music/%s/comments";

    // 删除音乐评论
    public static final String APP_PATH_MUSIC_DELETE_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/music/%d/comments/%d";
    public static final String APP_PATH_MUSIC_DELETE_COMMENT = "api/" + API_VERSION_2 + "/music/{music_id}/comments/{comment_id}";

    // 评论专辑
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT = "api/" + API_VERSION_2 + "/music/specials/{special_id}/comments";
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/music/specials/%s/comments";

    // 专辑评论列表
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_LIST = "api/" + API_VERSION_2 + "/music/specials/{special_id}/comments";

    // 收藏专辑
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT = "api/" + API_VERSION_2 + "/music/specials/{special_id}/collection";
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT_FORMAT = "api/" + API_VERSION_2 + "/music/specials/%s/collection";

    // 音乐点赞
    public static final String APP_PATH_MUSIC_DIGG = "api/" + API_VERSION_2 + "/music/{music_id}/like";
    public static final String APP_PATH_MUSIC_DIGG_FORMAT = "api/" + API_VERSION_2 + "/music/%s/like";

    // 专辑详情
    public static final String APP_PATH_MUSIC_ABLUM_DETAILS = "api/" + API_VERSION_2 + "/music/specials/{special_id}";

    // 分享歌曲
    public static final String APP_PATH_MUSIC_SHARE = "api/" + API_VERSION_2 + "/music/%s/share";

    // 分享专辑
    public static final String APP_PATH_MUSIC_ABLUM_SHARE = "api/" + API_VERSION_2 + "/music/specials/%s/share";

    /**
     * 问答相关
     */
    // 发布问题
    public static final String APP_PATH_PUBLISH_QUESTIONS = "api/" + API_VERSION_2 + "/questions";
    // 更新问题的悬赏 PATCH
    public static final String APP_PATH_UPDATE_QUESTION_REWARD = "api/" + API_VERSION_2 + "/questions/{question}/amount";
    // 获取全部话题
    public static final String APP_PATH_GET_ALL_TOPIC = "api/" + API_VERSION_2 + "/question-topics";
    // 申请创建话题
    public static final String APP_PATH_CREATE_TOPIC = "api/" + API_VERSION_2 + "/user/question-topics/application";
    // 获取认证用户关注的话题或者专家话题
    public static final String APP_PATH_GET_FOLLOW_TOPIC = "api/" + API_VERSION_2 + "/user/question-topics";
    // 获取话题下专家列表
    public static final String APP_PATH_GET_TOPIC_EXPERTS = "api/" + API_VERSION_2 + "/question-topics/{topic_id}/experts";
    // 话题详情
    public static final String APP_PATH_GET_TOPIC_DETAIL = "api/" + API_VERSION_2 + "/question-topics/{topic}";
    // 话题下的问答列表
    public static final String APP_PATH_GET_QUESTION_LIST_BY_TOPIC = "api/" + API_VERSION_2 + "/question-topics/{topic}/questions";
    // 关注或者取消一个话题
    public static final String APP_PATH_HANDLE_TOPIC_FOLLOW = "api/" + API_VERSION_2 + "/user/question-topics/{topic}";
    public static final String APP_PATH_HANDLE_TOPIC_FOLLOW_S = "api/" + API_VERSION_2 + "/user/question-topics/%s";
    // 获取问题详情
    public static final String APP_PATH_GET_QUESTION_DETAIL = "api/" + API_VERSION_2 + "/questions/{question}";
    // 关注问题
    public static final String APP_PATH_HANDLE_QUESTION_FOLLOW = "api/" + API_VERSION_2 + "/user/question-watches/{question}";
    public static final String APP_PATH_HANDLE_QUESTION_FOLLOW_S = "api/" + API_VERSION_2 + "/user/question-watches/%s";
    // 删除问题用
    public static final String APP_PATH_GET_DELETE_QUESTION = "api/" + API_VERSION_2 + "/questions/{question}";
    // 获取一个问题的回答列表
    public static final String APP_PATH_GET_QUESTION_ANSWER_LIST = "api/" + API_VERSION_2 + "/questions/{question}/answers";
    // 获取一个回答的详情
    public static final String APP_PATH_GET_ANSWER_DETAIL = "api/" + API_VERSION_2 + "/question-answers/{answer_id}";
    public static final String APP_PATH_GET_ANSWER_DETAIL_S = "api/" + API_VERSION_2 + "/question-answers/%s";
    // 申请精选问答 POST
    public static final String APP_PATH_APPLY_FOR_EXCELLENT = "api/" + API_VERSION_2 + "/user/question-application/{question}";
    public static final String APP_PATH_APPLY_FOR_EXCELLENT_S = "api/" + API_VERSION_2 + "/user/question-application/%s";
    // 获取问答的评论列表
    public static final String APP_PATH_GET_QUESTION_COMMENT_LIST = "api/" + API_VERSION_2 + "/questions/{question}/comments";
    // 评论问题
    public static final String APP_PATH_SEND_QUESTION_COMMENT = "api/" + API_VERSION_2 + "/questions/{question}/comments";
    public static final String APP_PATH_SEND_QUESTION_COMMENT_S = "api/" + API_VERSION_2 + "/questions/%s/comments";
    // 删除问题的评论
    public static final String APP_PATH_DELETE_QUESTION_COMMENT = "api/" + API_VERSION_2 + "/questions/{question}/comments/{answer}";
    // 发布回答
    public static final String APP_PATH_PUBLISH_ANSWER = "api/" + API_VERSION_2 + "/questions/{question}/answers";
    // 更新回答 PATCH
    public static final String APP_PATH_UPDATE_ANSWER = "api/" + API_VERSION_2 + "/question-answers/{answer_id}";
    // 采纳答案 PUT
    public static final String APP_PATH_ADOPT_ANSWER = "api/" + API_VERSION_2 + "/questions/{question_id}/adoptions/{answer_id}";
    public static final String APP_PATH_ADOPT_ANSWER_S = "api/" + API_VERSION_2 + "/questions/%d/adoptions/%d";
    // 删除答案 DELETE
    public static final String APP_PATH_DELETE_ANSWER = "api/" + API_VERSION_2 + "/question-answers/{answer}";
    public static final String APP_PATH_DELETE_ANSWER_S = "api/" + API_VERSION_2 + "/question-answers/%d";
    // 获取回答评论列表
    public static final String APP_PATH_GET_ANSWER_COMMENTS = "api/" + API_VERSION_2 + "/question-answers/{answer_id}/comments";
    // 点赞回答
    public static final String APP_PATH_LIKE_ANSWER = "api/" + API_VERSION_2 + "/question-answers/{answer_id}/likes";
    public static final String APP_PATH_LIKE_ANSWER_FORMAT = "api/" + API_VERSION_2 + "/question-answers/%d/likes";
    // 收藏回答
    public static final String APP_PATH_COLLECT_ANSWER_FORMAT = "api/" + API_VERSION_2 + "/user/question-answer/collections/%d";

    // 问答回答打赏
    public static final String APP_PATH_QA_ANSWER_REWARD = "api/" + API_VERSION_2 + "/question-answers/{answer_id}/rewarders";
    // 问答回答围观
    public static final String APP_PATH_QA_ANSWER_LOOK = "api/" + API_VERSION_2 + "/question-answers/{answer_id}/onlookers";
    // 获取回答打赏列表
    public static final String APP_PATH_QA_ANSWER_REWARD_USER_LIST = "api/" + API_VERSION_2 + "/question-answers/{answer_id}/rewarders";
    // 评论答案
    public static final String APP_PATH_COMMENT_QA_ANSWER_FORMAT = "api/" + API_VERSION_2 + "/question-answers/%d/comments";
    // 删除答案评论
    public static final String APP_PATH_DELETE_QA_ANSWER_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/question-answers/%d/comments/%d";
    // 批量获取专家列表
    public static final String APP_PATH_GET_TOPIC_EXPERT_LIST = "api/" + API_VERSION_2 + "/question-experts";
    // 获取用户发布的问题列表
    public static final String APP_PATH_GET_USER_QUESTIONS = "api/" + API_VERSION_2 + "/user/questions";
    // 获取用户发布的回答列表
    public static final String APP_PATH_GET_USER_ANSWER = "api/" + API_VERSION_2 + "/user/question-answer";


    /**
     * 通用 CommonClient
     */
    public static final String APP_PATH_REFRESH_TOKEN = "api/" + API_VERSION_2 + "/tokens/{token}";// 刷新 token
    public static final String APP_PATH_HANDLE_BACKGROUND_TASK = "{path}";// 处理后台任务

    public static final String APP_PATH_SYSTEM_FEEDBACK = "api/" + API_VERSION_2 + "/user/feedback";// 意见反馈
    public static final String APP_PATH_GET_SYSTEM_CONVERSATIONS = "api/" + API_VERSION_2 + "/conversations";// 获取系统会话列表

    ////////////////////////////////////////// 以下是通用 V2 接口
    public static final String APP_PATH_STORAGE_HASH = "api/" + API_VERSION_2 + "/files/uploaded/{hash}";// 校检文件hash V2

    public static final String APP_PATH_CHECK_NOTE = "api/" + API_VERSION_2 + "/purchases/{note}";// 节点付费相关

    public static final String APP_PATH_STORAGE_UPLAOD_FILE = "api/" + API_VERSION_2 + "/files";// 文件上传 V2

    public static final String APP_PATH_STORAGE_GET_FILE = "api/" + API_VERSION_2 + "/files/{file}";// 文件获取 V2

    // 标签
    public static final String APP_PATH_GET_ALL_TAGS = "api/" + API_VERSION_2 + "/tags";// 获取全部标签标签
    // 地区
    public static final String APP_PATH_SEARDCH_LOCATION = "api/" + API_VERSION_2 + "/locations/search";// 搜索位置
    public static final String APP_PATH_SGET_HOT_CITY = "api/" + API_VERSION_2 + "/locations/hots";// 热门城市


    /**
     * 分享相关
     */
    public static final String APP_PATH_SHARE_USERINFO = "h5/users/feeds/%s";// 用户信息分享地址 url/web/users/feeds/{user_id}
    public static final String APP_PATH_SHARE_DYNAMIC = "h5/feed/%s";// 动态信息分享地址 url/web/feed/{feed_id}
    public static final String APP_PATH_SHARE_GROUNP_DYNAMIC = "h5/feed/%s";// 动态信息分享地址 url/web/feed/{feed_id}
    public static final String APP_PATH_SHARE_DEFAULT = "api/develop";// 开发中的提示
    public static final String APP_PATH_SHARE_GROUP = "api/develop";// 分享圈子动态，目前暂时用开发中


    /**
     * 仅仅测试使用
     */
    public static final String APP_PATH_TOKEN_EXPIERD = "api/music_window_rotate-token";// token过期处理

    /**
     * 圈子相关
     */
    public static final String APP_PATH_GET_ALL_GROUP = "api/" + API_VERSION_2 + "/groups";// 所有的圈子列表/如果是post,则是创建圈子
    public static final String APP_PATH_GET_USER_JOINED_GROUP = "api/" + API_VERSION_2 + "/groups/joined";// 用户加入的圈子
    public static final String APP_PATH_JOIN_GROUP = "api/" + API_VERSION_2 + "/groups/{group}/join"; // 加入/退出圈子
    public static final String APP_PATH_JOIN_GROUP_S = "api/" + API_VERSION_2 + "/groups/%s/join"; // 加入/退出圈子
    public static final String APP_PATH_GET_GROUP_DETAIL = "api/" + API_VERSION_2 + "/groups/{group}"; // 圈子详情
    public static final String APP_PATH_GET_GROUP_DYNAMIC_DETAIL = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}"; // 动态详情
    public static final String APP_PATH_GET_GROUP_DYNAMIC_LIST = "api/" + API_VERSION_2 + "/groups/{group}/posts"; // 动态列表

    public static final String APP_PATH_DELETE_GROUP_DYNAMIC_COLLECT = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/collection";//
    // 取消对圈子动态的收藏
    public static final String APP_PATH_COLLECT_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/collection";// 收藏圈子动态
    public static final String APP_PATH_COLLECT_GROUP_DYNAMIC_S = "api/" + API_VERSION_2 + "/groups/%s/posts/%s/collection";// 收藏圈子动态
    public static final String APP_PATH_GET_MYCOLLECT_GROUP_DYNAMIC_LIST = "api/" + API_VERSION_2 + "/groups/posts/collections";// 我收藏的圈子动态列表
    public static final String APP_PATH_DELETE_MYCOLLECT_GROUP_DYNAMIC_DIGG = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/like";// 取消点赞
    public static final String APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/like";// 点赞
    public static final String APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC_S = "api/" + API_VERSION_2 + "/groups/%s/posts/%s/like";// 点赞
    public static final String APP_PATH_GET_MYCOLLECT_GROUP_DYNAMIC_DIGG_LIST = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/likes";// 点赞列表
    public static final String APP_PATH_COMMENT_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/comments";// 创建圈子动态评论
    public static final String APP_PATH_COMMENT_GROUP_DYNAMIC_FORMAT = "api/" + API_VERSION_2 + "/groups/%d/posts/%d/comments";// 创建圈子动态评论
    public static final String APP_PATH_GET_GROUP_DYNAMIC_COMMENT_LIST = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/comments";// 圈子动态评论列表
    public static final String APP_PATH_DELETE_GROUP_DYNAMIC_COMMENT = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/comments/{comment}";
    // 删除圈子动态评论
    public static final String APP_PATH_DELETE_GROUP_DYNAMIC_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/groups/%d/posts/%d/comments/%d";// 删除圈子动态评论
    public static final String APP_PATH_SEND_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts";// 创建圈子动态
    public static final String APP_PATH_DELETE_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}";// 删除圈子动态
    public static final String APP_PATH_DELETE_GROUP_DYNAMIC_FORMAT = "api/" + API_VERSION_2 + "/groups/%d/posts/%d";// 删除圈子动态

    /**
     * 组件 目前：动态（feed）、音乐（music）、资讯（news）
     */
//    public static final String APP_COMPONENT_FEED = "feed";
    public static final String APP_COMPONENT_MUSIC = "music";
    //    public static final String APP_COMPONENT_NEWS = "news";
    public static final String APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS = "music_special";
    /**
     * @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/user/likes.md}
     */
    public static final String APP_LIKE_FEED = "feeds";
    public static final String APP_LIKE_MUSIC = "musics";
    public static final String APP_LIKE_MUSIC_SPECIALS = "music_specials";
    public static final String APP_LIKE_NEWS = "news";
    public static final String APP_LIKE_GROUP_POST = "group-posts";
    public static final String APP_QUESTIONS = "questions";
    public static final String APP_QUESTIONS_ANSWER = "question-answers";

    /*******************************************  API V2  *********************************************/

    /**
     * 系统相关
     */
    // 获取启动信息
    public static final String APP_PATH_GET_BOOTSTRAPERS_INFO = "api/" + API_VERSION_2 + "/bootstrappers";
    // 获取所有广告
    public static final String APP_PATH_GET_ADVERT_INFO = "api/" + API_VERSION_2 + "/advertisingspace";
    // 获取一个广告位的广告列表
    public static final String APP_PATH_GET_SINGLE_ADVERT_INFO = "api/" + API_VERSION_2 + "/advertisingspace/{advert_id}/advertising";
    // 获取批量广告位的广告列表
    public static final String APP_PATH_GET_All_ADVERT_INFO = "api/" + API_VERSION_2 + "/advertisingspace/advertising";
    // 版本更新
    public static final String APP_PATH_GET_APP_NEW_VERSION = "api/" + API_VERSION_2 + "/plus-appversion";

    /**
     * 通用 CommonClient
     */
    // 非会员短信 ，用于发送不存在于系统中的用户短信，使用场景如注册等。
    public static final String APP_PATH_GET_NON_MEMBER_VERTIFYCODE = "api/" + API_VERSION_2 + "/verifycodes/register";
    // 获取会员短信验证码，使用场景如登陆、找回密码，其他用户行为验证等。
    public static final String APP_PATH_GET_MEMBER_VERTIFYCODE = "api/" + API_VERSION_2 + "/verifycodes";
    public static final String APP_PATH_GET_APP_VERSION = "api/" + API_VERSION_2 + "/plus-appversion";

    /**
     * 用户相关
     */
    // 获取当前用户
    public static final String APP_PATH_GET_CURRENT_USER_INFO = "api/" + API_VERSION_2 + "/user";
    // 获取指定用户
    public static final String APP_PATH_GET_SPECIFIED_USER_INFO = "api/" + API_VERSION_2 + "/users/{user_id}";
    // 批量获取指定用户
    public static final String APP_PATH_GET_BATCH_SPECIFIED_USER_INFO = "api/" + API_VERSION_2 + "/users";
    // 热门用户
    public static final String APP_PATH_GET_HOT_USER_INFO = "api/" + API_VERSION_2 + "/user/populars";
    // 最新用户
    public static final String APP_PATH_GET_NEW_USER_INFO = "api/" + API_VERSION_2 + "/user/latests";
    // 通过 tag 推荐
    public static final String APP_PATH_GET_RECOMMENT_BY_TAG_USER_INFO = "api/" + API_VERSION_2 + "/user/find-by-tags";
    // 获取后台推荐用户
    public static final String APP_PATH_GET_RECOMMENT_USER_INFO = "api/" + API_VERSION_2 + "/user/recommends";

    public static final String APP_PATH_SEARCH_RECOMMENT_USER = "api/" + API_VERSION_2 + "/user/search";
    // 通过 phone 推荐
    public static final String APP_PATH_GET_BY_PHONE_USER_INFO = "api/" + API_VERSION_2 + "/user/find-by-phone";
    // 更新用户地址
    public static final String APP_PATH_UPDATE_USER_LOCATION = "api/" + API_VERSION_2 + "/around-amap";
    // 根据经纬度查询周围最多50KM内的 TS+ 用户
    public static final String APP_PATH_GET_USER_AROUND = "api/" + API_VERSION_2 + "/around-amap";

    /**
     * 签到
     */
    // 获取签到信息
    public static final String APP_PATH_GET_CHECK_IN_INFO = "api/" + API_VERSION_2 + "/user/checkin";
    // 签到
    public static final String APP_PATH_CHECK_IN = "api/" + API_VERSION_2 + "/user/checkin";
    // 连续签到排行榜
    public static final String APP_PATH_GET_CHECK_IN_RANKS = "api/" + API_VERSION_2 + "/checkin-ranks";

    /**
     * 三方登录绑定
     */
    public static final String PROVIDER_QQ = "qq";
    public static final String PROVIDER_WEIBO = "weibo";
    public static final String PROVIDER_WECHAT = "wechat";

    // 获取已绑定的第三方
    public static final String APP_PATH_GET_BIND_THIRDS = "api/" + API_VERSION_2 + "/user/socialite";
    // 检查绑定并获取用户授权
    public static final String APP_PATH_CHECK_BIND_OR_GET_USER_INFO = "api/" + API_VERSION_2 + "/socialite/{provider}";
    // 检查注册信息或者注册用户
    public static final String APP_PATH_CHECK_REGISTER_OR_GET_USER_INFO = "api/" + API_VERSION_2 + "/socialite/{provider}";
    // 已登录账号绑定
    public static final String APP_PATH_BIND_WITH_LOGIN = "api/" + API_VERSION_2 + "/user/socialite/{provider}";
    // 输入账号密码绑定
    public static final String APP_PATH_BIND_WITH_INPUT = "api/" + API_VERSION_2 + "/socialite/{provider}";
    // 输取消绑定
    public static final String APP_PATH_CANDEL_BIND = "api/" + API_VERSION_2 + "/user/socialite/{provider}";


    /**
     * 资讯
     */
    // 资讯分类列表
    public static final String APP_PATH_INFO_TYPE_V2 = "api/" + API_VERSION_2 + "/news/cates";
    public static final String APP_PATH_INFO_LIST_V2 = "api/" + API_VERSION_2 + "/news";// 资讯列表
    public static final String APP_PATH_INFO_TOP_LIST = "api/" + API_VERSION_2 + "/news/categories/pinneds";// 获取置顶资讯
    public static final String APP_PATH_INFO_DETAIL = "api/" + API_VERSION_2 + "/news/{news}";// 详情
    public static final String APP_PATH_INFO_DETAIL_RELATION = "api/" + API_VERSION_2 + "/news/{news}/correlations";// 相关资讯
    public static final String APP_PATH_INFO_DIG_V2 = "api/" + API_VERSION_2 + "/news/{news}/likes";// 点赞
    public static final String APP_PATH_INFO_DIG_V2_S = "api/" + API_VERSION_2 + "/news/%s/likes";// 点赞
    public static final String APP_PATH_INFO_DIG_LIST = "api/" + API_VERSION_2 + "/news/{news}/likes";// 点赞列表
    public static final String APP_PATH_INFO_COLLECTION = "api/" + API_VERSION_2 + "/news/{news}/collections";// 收藏
    public static final String APP_PATH_INFO_COLLECTION_S = "api/" + API_VERSION_2 + "/news/%s/collections";// 收藏
    public static final String APP_PATH_INFO_COLLECTION_LIST = "api/" + API_VERSION_2 + "/news/collections";// 获取收藏列表
    public static final String APP_PATH_INFO_COMMENT_V2 = "api/" + API_VERSION_2 + "/news/{news}/comments";// 评论
    public static final String APP_PATH_INFO_COMMENT_V2_S = "api/" + API_VERSION_2 + "/news/%s/comments";// 评论
    public static final String APP_PATH_INFO_GET_COMMENT = "api/" + API_VERSION_2 + "/news/{news}/comments";// 获取评论列表
    public static final String APP_PATH_INFO_DELETE_COMMENT_V2 = "api/" + API_VERSION_2 + " /news/{news}/comments/{comment}";// 删除评论
    public static final String APP_PATH_INFO_DELETE_COMMENT_V2_S = "api/" + API_VERSION_2 + "/news/%s/comments/%s";// 删除评论
    public static final String APP_PATH_INFO_DELETE = "api/" + API_VERSION_2 + "/news/categories/{category}/news/{news}";// 删除资讯
    public static final String APP_PATH_INFO_PINNED = "api/" + API_VERSION_2 + "/news/{news}/pinneds";// 置顶
    public static final String APP_PATH_INFO_PINNED_S = "api/" + API_VERSION_2 + "/news/%s/pinneds";// 置顶


    /**
     * 钱包
     */
    // 钱包信息
    public static final String APP_PAHT_WALLET_CONFIG = "api/" + API_VERSION_2 + "/wallet";
    // 提现
    public static final String APP_PAHT_WALLET_WITHDRAW = "api/" + API_VERSION_2 + "/wallet/cashes";
    // 钱包余额充值
    public static final String APP_PAHT_WALLET_RECHARGE = "api/" + API_VERSION_2 + "/wallet/recharge";
    // 钱包余额充值凭据
    public static final String APP_PAHT_WALLET_RECHARGE_SUCCESS = "api/" + API_VERSION_2 + "/wallet/charges/{charge}";
    // 凭据列表
    public static final String APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST = "api/" + API_VERSION_2 + "/wallet/charges";
    // 凭据回执
    public static final String APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK = "api/" + API_VERSION_2 + "/wallet/charges/{charge}?mode=retrieve";
    public static final String APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK_FORMAT = "api/" + API_VERSION_2 + "/wallet/charges/%s?mode=retrieve";

    /*排行榜相关*/
    /*用户*/
    // 全站粉丝排行榜
    public static final String APP_PATH_RANK_ALL_FOLLOWER = "api/" + API_VERSION_2 + "/ranks/followers";
    // 财富达人排行
    public static final String APP_PATH_RANK_ALL_RICHES = "api/" + API_VERSION_2 + "/ranks/balance";
    // 收入排行榜
    public static final String APP_PATH_RANK_INCOME = "api/" + API_VERSION_2 + "/ranks/income";
    // 连续签到排行榜
    public static final String APP_PATH_RANK_CHECK_IN = "api/" + API_VERSION_2 + "/checkin-ranks";
    /*用户 end*/
    /*问答*/
    // 解答排行 type	string	-	筛选类型 day - 日排行 week - 周排行 month - 月排行
    public static final String APP_PATH_RANK_QUESTION_ANSWER = "api/" + API_VERSION_2 + "/question-ranks/answers";
    // 问答达人排行
    public static final String APP_PATH_RANK_QUESTION_LIKES = "api/" + API_VERSION_2 + "/question-ranks/likes";
    // 社区专家排行
    public static final String APP_PATH_RANK_QUESTION_EXPERTS = "api/" + API_VERSION_2 + "/question-ranks/experts";
    /*问答 end*/
    /*动态*/
    // 动态排行
    public static final String APP_PATH_RANK_FEEDS = "api/" + API_VERSION_2 + "/feeds/ranks";
    /*动态 end*/
    /*资讯*/
    // 资讯排行
    public static final String APP_PATH_RANK_NEWS = "api/" + API_VERSION_2 + "/news/ranks";
    /*资讯 end*/
}
