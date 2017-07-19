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

    public static final boolean APP_IS_NEED_SSH_CERTIFICATE = true;// 在线测试服务器 2
    public static final String APP_DOMAIN = "https://plus.medz.cn/";// 在线测试服务器 2

    //    public static final String APP_DOMAIN = "http://test-plus.zhibocloud.cn/";// 在线测试服务器
    public static final String APP_IM_DOMAIN = "ws://test-plus.zhibocloud.cn:9900";// im 在线测试服务器
//
//    public static final String APP_DOMAIN = "http://tsplus.zhibocloud.cn/";// 正式服务器
//    public static final String APP_IM_DOMAIN = "ws://tsplus.zhibocloud.cn:9900";// im 正式服务器


    public static final String URL_ABOUT_US = APP_DOMAIN + "api/" + API_VERSION + "/system/about";// 关于我们网站
    public static final String URL_JIPU_SHOP = "http://demo.jipukeji.com";// 极铺购物地址

    // 图片地址
    public static final String IMAGE_PATH = APP_DOMAIN + "api/" + API_VERSION + "/storages/%s/%d";// 带质量压缩
    // 图片地址
    public static final String NO_PROCESS_IMAGE_PATH = APP_DOMAIN + "api/" + API_VERSION + "/storages/%s";// 不带质量压缩

    // 图片地址 V2
    public static final String IMAGE_PATH_V2 = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/%s?w=%d&h=%d&q=%d";
    // 头像地址
    public static final String IMAGE_AVATAR_PATH_V2 = APP_DOMAIN + "api/" + API_VERSION_2 + "/users/%s/avatar";

    // 音乐地址 V2
    public static final String MUSIC_PATH = APP_DOMAIN + "api/" + API_VERSION_2 + "/files/%s";

    /*******************************************  接口 Path  *********************************************/

    /**
     * 登录 Login
     */
    public static final String APP_PATH_LOGIN = "api/" + API_VERSION_2 + "/tokens";

    /**
     * 密码 PasswordClient
     */
    public static final String APP_PATH_CHANGE_PASSWORD = "api/" + API_VERSION + "/users/password";// 修改密码
    public static final String APP_PATH_FIND_PASSWORD = "api/" + API_VERSION + "/auth/forgot";// 找回密码
    /**
     * 注册 RegitstClient
     */
    public static final String APP_PATH_REGISTER = "api/" + API_VERSION_2 + "/users";// 注册
    /**
     * 用户 UserInfoClient
     */
    public static final String APP_PATH_GET_USER_INFO = "api/" + API_VERSION + "/users";// 获取用户信息
    public static final String APP_PATH_CHANGE_USER_INFO = "api/" + API_VERSION + "/users";// 修改用户信息
    public static final String APP_PATH_GET_IM_INFO = "api/" + API_VERSION + "/im/users";// 获取 IM 帐号信息
    public static final String APP_PATH_GET_MY_DIGGS = "api/" + API_VERSION_2 + "/user/likes"; // 获取用户收到的点赞
    public static final String APP_PATH_GET_MY_COMMENTS = "api/" + API_VERSION_2 + "/user/comments"; // 获取用户收到的评论
    public static final String APP_PATH_GET_MY_FLUSHMESSAGES = "api/" + API_VERSION + "/users/flushmessages"; // 获取用户收到的最新消息  查询关键字 默认查询全部 多个以逗号隔开 可选参数有 diggs comments follows
    public static final String APP_PATH_UPDATE_USER_AVATAR = "api/" + API_VERSION_2 + "/user/avatar";// 修改用户头像
    /**
     * 通知来源频道，客户端需要根据 data.channel 值进行独立解析。已知频道:
     *
     * @see {https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/notifications.md}
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
    public static final String APP_PATH_CREATE_CONVERSAITON = "api/" + API_VERSION + "/im/conversations";// 创建对话
    public static final String APP_PATH_GET_CONVERSAITON_LIST = "api/" + API_VERSION + "/im/conversations/list/all";// 获取登陆用户的对话列表
    public static final String APP_PATH_GET_SINGLE_CONVERSAITON = "api/" + API_VERSION + "/im/conversations/{cid}";// 获取单个对话信息

    /**
     * 关注粉丝 FollowFansClient
     */
    //api/" + API_VERSION + "/follows/follows/{user_id}/{max_id}
    public static final String APP_PATH_FOLLOW_LIST = "api/" + API_VERSION + "/follows/follows/{user_id}/{max_id}";// 获取用户关注列表
    //api/" + API_VERSION + "/follows/followeds/{user_id}/{max_id}
    public static final String APP_PATH_FANS_LIST = "api/" + API_VERSION + "/follows/followeds/{user_id}/{max_id}";// 获取用户粉丝列表
    public static final String APP_PATH_FOLLOW_USER = "api/" + API_VERSION + "/users/follow";// 关注用户
    public static final String APP_PATH_CANCEL_FOLLOW_USER = "api/" + API_VERSION + "/users/unFollow";// 取消用户关注
    public static final String APP_PATH_GET_USER_FOLLOW_STATE = "api/" + API_VERSION + "/users/followstatus";// 获取用户关注状态

    public static final String APP_PATH_GET_DIGGS_RANK = "api/" + API_VERSION + "/diggsrank";//  用户点赞排行

    /**
     * 动态相关
     */
    public static final String APP_PATH_SEND_DYNAMIC = "api/" + API_VERSION + "/feeds";// 发布动态

    public static final String APP_PATH_SEND_DYNAMIC_V2 = "api/" + API_VERSION_2 + "/feeds";// 发布动态 V2

    public static final String APP_PATH_DELETE_DYNAMIC = "api/" + API_VERSION_2 + "/feeds/%s";// 删除一条动态
    // 获取动态列表
    // 最新：/api/" + API_VERSION + "/feeds;
    // 关注：/api/" + API_VERSION + "/feeds/follows;
    // 热门：/api/" + API_VERSION + "/feeds/hots;
    // 某个人的：/api/" + API_VERSION + "/feeds/users/{user_id}
    public static final String APP_PATH_GET_DYNAMIC_LIST = "api/" + API_VERSION + "/feeds/{type}";
    public static final String DYNAMIC_TYPE_NEW = "new"; // 最新动态
    public static final String DYNAMIC_TYPE_FOLLOWS = "follow"; // 关注动态
    public static final String DYNAMIC_TYPE_HOTS = "hot"; // 热门动态
    public static final String DYNAMIC_TYPE_USERS = "users"; // 用户动态
    public static final String DYNAMIC_TYPE_SOMEONE = "users/%s"; // 某个人的动态列表,%s表示用户id
    public static final String DYNAMIC_TYPE_MY_COLLECTION = "collections";// 我收藏的动态列表
    // 点赞一条动态,取消点赞
    public static final String APP_PATH_DYNAMIC_HANDLE_LIKE = "api/" + API_VERSION + "/feeds/{feed_id}/digg";
    public static final String APP_PATH_DYNAMIC_HANDLE_LIKE_FORMAT = "api/" + API_VERSION + "/feeds/%s/digg";

    public static final String APP_PATH_DYNAMIC_CLICK_LIKE_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/like";
    public static final String APP_PATH_DYNAMIC_CANCEL_CLICK_LIKE_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/unlike";
    public static final String APP_PATH_DYNAMIC_CLICK_LIKE_FORMAT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/like";
    public static final String APP_PATH_DYNAMIC_CANCEL_CLICK_LIKE_FORMAT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/unlike";

    // 删除一条评论评论
    public static final String APP_PATH_DYNAMIC_DELETE_COMMENT = "api/" + API_VERSION + "/feeds/%s/comment/%s";
    public static final String APP_PATH_DYNAMIC_DELETE_COMMENT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/comments/%s";
    // 对一条动态或一条动态评论进行评论
    public static final String APP_PATH_DYNAMIC_SEND_COMMENT = "api/" + API_VERSION + "/feeds/%s/comment";
    public static final String APP_PATH_DYNAMIC_SEND_COMMENT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/comments";
    // 获取点赞列表
    public static final String APP_PATH_DYNAMIC_DIG_LIST = "api/" + API_VERSION + "/feeds/{feed_id}/diggusers";
    public static final String APP_PATH_DYNAMIC_DIG_LIST_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/likes";
    // 一条动态的评论列表
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST = "api/" + API_VERSION + "/feeds/{feed_id}/comments";
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST_V2 = "api/" + API_VERSION_2 + "/feeds/{feed_id}/comments";
    // 根据id获取评论列表
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST_BY_COMMENT_ID = "api/" + API_VERSION + "/feeds/comments";
    // 收藏动态，取消收藏
    public static final String APP_PATH_HANDLE_COLLECT = "api/" + API_VERSION + "/feeds/{feed_id}/collections";
    public static final String APP_PATH_HANDLE_COLLECT_FORMAT = "api/" + API_VERSION + "/feeds/%s/collection";
    // 增加动态浏览量
    public static final String APP_PATH_HANDLE_DYNAMIC_VIEWCOUNT = "api/" + API_VERSION + "/feeds/{feed_id}/viewcount";

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

    /**
     * 资讯相关
     */
    public static final String APP_PATH_INFO_TYPE = "api/" + API_VERSION + "/news/cates";// 资讯分类列表
    public static final String APP_PATH_INFO_LIST = "api/" + API_VERSION + "/news";// 资讯列表
    public static final String APP_PATH_INFO_COLLECT_LIST = "api/" + API_VERSION + "/news/collections";// 普通的资讯列表，通过cate_uid获取
    public static final String INFO_TYPE_COLLECTIONS = "-1000";// 资讯收藏列表
    public static final String APP_PATH_INFO_COMMENT = "api/" + API_VERSION + "/news/{news_id}/comment";// 评论资讯
    public static final String APP_PATH_INFO_COMMENT_FORMAT = "api/" + API_VERSION + "/news/%s/comment";// 评论资讯

    // 删除资讯评论
    public static final String APP_PATH_INFO_DELETE_COMMENT_FORMAT = "api/" + API_VERSION + "/news/%s/comment/%s";
    public static final String APP_PATH_INFO_DELETE_COMMENT = "api/" + API_VERSION + "/news/{news_id}/comment/{comment_id}";

    // 资讯评论列表
    public static final String APP_PATH_INFO_COMMENT_LIST = "api/" + API_VERSION + "/news/{feed_id}/comments";//

    // 收藏资讯
    public static final String APP_PATH_INFO_COLLECT = "api/" + API_VERSION + "/news/{news_id}/collection";
    public static final String APP_PATH_INFO_COLLECT_FORMAT = "api/" + API_VERSION + "/news/%s/collection";

    // 点赞资讯
    public static final String APP_PATH_INFO_DIG = "api/" + API_VERSION + "/news/{news_id}/digg";
    public static final String APP_PATH_INFO_DIG_FORMAT = "api/" + API_VERSION + "/news/%s/digg";

    // 订阅资讯频道
    public static final String APP_PATH_INFO_FOLLOW_LIST = "api/" + API_VERSION + "/news/cates/follow";
    public static final String APP_PATH_INFO_SEARCH = "/api/" + API_VERSION + "/news/search";

    // 资讯详情网页
    public static final String APP_PATH_INFO_DETAILS = "/api/" + API_VERSION + "/news/{news_id}";
    public static final String APP_PATH_INFO_DETAILS_FORMAT = "/api/" + API_VERSION + "/news/%d";

    /**
     * 音乐相关
     */
    public static final String APP_PATH_MUSIC_ABLUM_LIST = "api/" + API_VERSION + "/music/specials";// 专辑列表
    public static final String APP_PATH_MUSIC_COLLECT_ABLUM_LIST = "api/" + API_VERSION + "/music/special/collections";// 专辑列表

    // 歌曲详情
    public static final String APP_PATH_MUSIC_DETAILS = "api/" + API_VERSION + "/music/{music_id}";

    // 评论歌曲
    public static final String APP_PATH_MUSIC_COMMENT = "api/" + API_VERSION + "/music/{music_id}/comment";
    public static final String APP_PATH_MUSIC_COMMENT_FORMAT = "api/" + API_VERSION + "/music/%s/comment";

    // 删除音乐评论
    public static final String APP_PATH_MUSIC_DELETE_COMMENT_FORMAT = "api/" + API_VERSION + "/music/comment/%s";
    public static final String APP_PATH_MUSIC_DELETE_COMMENT = "api/" + API_VERSION + "/music/comment/{comment_id}";

    // 评论专辑
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT = "api/" + API_VERSION + "/music/special/{special_id}/comment";
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT = "api/" + API_VERSION + "/music/special/%s/comment";

    // 专辑评论列表
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_LIST = "api/" + API_VERSION + "/music/special/{special_id}/comment";

    // 收藏专辑
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT = "api/" + API_VERSION + "/music/special/{special_id}/collection";
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT_FORMAT = "api/" + API_VERSION + "/music/special/%s/collection";

    // 音乐点赞
    public static final String APP_PATH_MUSIC_DIGG = "api/" + API_VERSION + "/music/{music_id}/digg";
    public static final String APP_PATH_MUSIC_DIGG_FORMAT = "api/" + API_VERSION + "/music/%s/digg";

    // 专辑详情
    public static final String APP_PATH_MUSIC_ABLUM_DETAILS = "api/" + API_VERSION + "/music/specials/{special_id}";

    // 分享歌曲
    public static final String APP_PATH_MUSIC_SHARE = "api/" + API_VERSION + "/music/%s/share";

    // 分享专辑
    public static final String APP_PATH_MUSIC_ABLUM_SHARE = "api/" + API_VERSION + "/music/special/%s/share";


    /**
     * 通用 CommonClient
     */
    public static final String APP_PATH_GET_VERTIFYCODE = "api/" + API_VERSION + "/auth/phone/send-code";// 获取验证码
    public static final String APP_PATH_REFRESH_TOKEN = "api/" + API_VERSION_2 + "/tokens/{token}";// 刷新 token
    public static final String APP_PATH_CREATE_STORAGE_TASK = "api/" + API_VERSION + "/storages/task";// 储存任务创建
    public static final String APP_PATH_NOTIFY_STORAGE_TASK =
            "api/" + API_VERSION + "/storages/task/{storage_task_id}";//  储存任务通知
    public static final String APP_PATH_DELETE_STORAGE_TASK =
            "api/" + API_VERSION + "/storages/task/{storage_task_id}";// 通知服务器，删除当前上传文件
    public static final String APP_PATH_HANDLE_BACKGROUND_TASK = "{path}";// 处理后台任务

    public static final String APP_PATH_GET_COMPONENT_STATUS = "api/" + API_VERSION + "/system/component/status";// 查看扩展包安装状态
    public static final String APP_PATH_GET_COMPONENT_CONFIGS = "api/" + API_VERSION + "/system/component/configs";// 获取扩展包配置信息
    public static final String APP_PATH_GET_COMPONENT_CONFIGS_IM = "im";// 获取扩展包配置信息 类容

    public static final String APP_PATH_SYSTEM_FEEDBACK = "api/" + API_VERSION + "/system/feedback";// 意见反馈
    public static final String APP_PATH_GET_SYSTEM_CONVERSATIONS = "api/" + API_VERSION + "/system/conversations";// 获取系统会话列表
    // 会话类型
    public static final String SYSTEM_CONVERSATIONS_TYPE_FEEDBACK = "feedback";// 用户意见反馈
    public static final String SYSTEM_CONVERSATIONS_TYPE_SYSTEM = "system";// 系统通知
    // 启动广告
    public static final String SYSTEM_LAUNCH_ADVERT = "system_launch_advert";// 系统启动广告

    ////////////////////////////////////////// 以下是通用 V2 接口
    public static final String APP_PATH_STORAGE_HASH = "api/" + API_VERSION_2 + "/files/uploaded/{hash}";// 校检文件hash V2

    public static final String APP_PATH_CHECK_NOTE = "api/" + API_VERSION_2 + "/purchases/{note}";// 节点付费相关

    public static final String APP_PATH_STORAGE_UPLAOD_FILE = "api/" + API_VERSION_2 + "/files";// 文件上传 V2

    public static final String APP_PATH_STORAGE_GET_FILE = "api/" + API_VERSION_2 + "/files/{file}";// 文件获取 V2

    /**
     * 分享相关
     */
    public static final String APP_PATH_SHARE_USERINFO = APP_DOMAIN + "web/users/feeds/%s";// 用户信息分享地址 url/web/users/feeds/{user_id}
    public static final String APP_PATH_SHARE_DYNAMIC = APP_DOMAIN + "web/feed/%s";// 动态信息分享地址 url/web/feed/{feed_id}
    public static final String APP_PATH_SHARE_DEFAULT = APP_DOMAIN + "api/develop";// 开发中的提示


    /**
     * 仅仅测试使用
     */
    public static final String APP_PATH_TOKEN_EXPIERD = "api/music_window_rotate-token";// token过期处理

    /**
     * 频道相关
     */
    // 处理频道订阅取消订阅的接口
    public static final String APP_PATH_HANDLE_SUBSCRIB_CHANNEL = "api/" + API_VERSION + "/channels/{channel_id}/follow";
    public static final String APP_PATH_HANDLE_SUBSCRIB_CHANNEL_S = "api/" + API_VERSION + "/channels/%s/follow";
    // 获取频道列表
    public static final String APP_PATH_GET_CHANNEL = "api/" + API_VERSION + "/channels/{type}";
    public static final String CHANNEL_TYPE_ALL_CHANNEL = "";// 所有的频道
    public static final String CHANNEL_TYPE_MY_SUBSCRIB_CHANNEL = "my";// 我订阅的频道
    // 发送动态到频道
    public static final String APP_PATH_SEND_DYNAMIC_TO_CHANNEL = "api/" + API_VERSION + "/channels/{channel_id}/feed";
    public static final String APP_PATH_SEND_DYNAMIC_TO_CHANNEL_S = "api/" + API_VERSION + "/channels/%s/feed";
    // 获取频道的动态列表
    public static final String APP_PATH_GET_CHANNEL_DYNAMIC_LIST = "api/" + API_VERSION + "/channels/{channel_id}/feeds";

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

    public static final String APP_PATH_COLLECT_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/collections";// 收藏圈子动态的
    public static final String APP_PATH_DELETE_GROUP_DYNAMIC_COLLECT = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/collection";// 取消对圈子动态的收藏
    public static final String APP_PATH_GET_MYCOLLECT_GROUP_DYNAMIC_LIST = "api/" + API_VERSION_2 + "/groups/posts/collections";// 我收藏的圈子动态列表
    public static final String APP_PATH_DELETE_MYCOLLECT_GROUP_DYNAMIC_DIGG = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/digg";// 取消点赞
    public static final String APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/digg";// 点赞
    public static final String APP_PATH_GET_MYCOLLECT_GROUP_DYNAMIC_DIGG_LIST = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/diggs";// 点赞列表
    public static final String APP_PATH_COMMENT_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/comment";// 创建圈子动态评论
    public static final String APP_PATH_GET_GROUP_DYNAMIC_COMMENT_LIST = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/comments";// 圈子动态评论列表
    public static final String APP_PATH_DELETE_GROUP_DYNAMIC_COMMENT = "api/" + API_VERSION_2 + "/groups/{group}/posts/{post}/comments/{comment}";// 删除圈子动态评论
    public static final String APP_PATH_SEND_GROUP_DYNAMIC = "api/" + API_VERSION_2 + "/groups/{group}/posts";// 创建圈子动态


    /**
     * 组件 目前：动态（feed）、音乐（music）、资讯（news）
     */
    public static final String APP_COMPONENT_FEED = "feed";
    public static final String APP_COMPONENT_MUSIC = "music";
    public static final String APP_COMPONENT_NEWS = "news";
    public static final String APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS = "music_special";
    /**
     * @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/user/likes.md}
     */
    public static final String APP_LIKE_FEED = "feeds";
    public static final String APP_LIKE_MUSIC = "musics";
    public static final String APP_LIKE_NEWS = "news";


    /*******************************************  API V2  *********************************************/

    /**
     * 系统相关
     */
    // 获取启动信息
    public static final String APP_PATH_GET_BOOTSTRAPERS_INFO = "api/" + API_VERSION_2 + "/bootstrappers";

    /**
     * 通用 CommonClient
     */
    // 非会员短信 ，用于发送不存在于系统中的用户短信，使用场景如注册等。
    public static final String APP_PATH_GET_NON_MEMBER_VERTIFYCODE = "api/" + API_VERSION_2 + "/verifycodes/register";
    // 获取会员短信验证码，使用场景如登陆、找回密码，其他用户行为验证等。
    public static final String APP_PATH_GET_MEMBER_VERTIFYCODE = "api/" + API_VERSION_2 + "/verifycodes";

    /**
     * 用户相关
     */
    // 获取当前用户
    public static final String APP_PATH_GET_CURRENT_USER_INFO = "api/" + API_VERSION_2 + "/user";
    // 获取指定用户
    public static final String APP_PATH_GET_SPECIFIED_USER_INFO = "api/" + API_VERSION_2 + "/users/{user_id}";
    // 批量获取指定用户
    public static final String APP_PATH_GET_BATCH_SPECIFIED_USER_INFO = "api/" + API_VERSION_2 + "/users";

    /**
     * 资讯
     */
    // 资讯分类列表
    public static final String APP_PATH_INFO_TYPE_V2 = "api/" + API_VERSION_2 + "/news/cates";

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
}
