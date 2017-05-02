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

    /**
     * 网络根地址  http://192.168.10.222/
     * 测试服务器：http://192.168.2.222:8080/mockjs/2/test-get-repose-head-normal?
     */

    //public static final String APP_DOMAIN = "http://192.168.2.222:8080/mockjs/2/";// rap 测试服务器
//    public static final String APP_DOMAIN = "http://192.168.2.222/";// 测试服务器
//    public static final String APP_IM_DOMAIN = "192.168.2.222:9900";// im 测试服务器

    public static final String APP_DOMAIN = "http://test-plus.zhibocloud.cn/";// 在线测试服务器
    public static final String APP_IM_DOMAIN = "ws://test-plus.zhibocloud.cn:9900";// im 在线测试服务器

//    public static final String APP_DOMAIN = "http://tsplus.zhibocloud.cn/";// 正式服务器
//    public static final String APP_IM_DOMAIN = "ws://tsplus.zhibocloud.cn:9900";// im 正式服务器









    public static final String URL_ABOUT_US = APP_DOMAIN + "api/v1/system/about";// 关于我们网站
    public static final String URL_JIPU_SHOP = "http://demo.jipukeji.com";// 极铺购物地址

    // 图片地址
    public static final String IMAGE_PATH = APP_DOMAIN + "api/v1/storages/%s/%d";// 带质量压缩
    // 图片地址
    public static final String NO_PROCESS_IMAGE_PATH = APP_DOMAIN + "api/v1/storages/%s";// 不带质量压缩

    /*******************************************  接口 Path
     * *********************************************/
    /**
     * 登录 Login
     */
    public static final String APP_PATH_LOGIN = "api/v1/auth";
    /**
     * 密码 PasswordClient
     */
    public static final String APP_PATH_CHANGE_PASSWORD = "api/v1/users/password";// 修改密码
    public static final String APP_PATH_FIND_PASSWORD = "api/v1/auth/forgot";// 找回密码
    /**
     * 注册 RegitstClient
     */
    public static final String APP_PATH_REGISTER = "api/v1/auth/register";// 注册
    /**
     * 用户 UserInfoClient
     */
    public static final String APP_PATH_GET_USER_INFO = "api/v1/users";// 获取用户信息
    public static final String APP_PATH_CHANGE_USER_INFO = "api/v1/users";// 修改用户信息
    public static final String APP_PATH_GET_IM_INFO = "api/v1/im/users";// 获取 IM 帐号信息
    public static final String APP_PATH_GET_MY_DIGGS = "api/v1/users/mydiggs"; // 获取用户收到的点赞
    public static final String APP_PATH_GET_MY_COMMENTS = "api/v1/users/mycomments"; // 获取用户收到的评论
    public static final String APP_PATH_GET_MY_FLUSHMESSAGES = "api/v1/users/flushmessages"; // 获取用户收到的最新消息  查询关键字 默认查询全部 多个以逗号隔开 可选参数有 diggs comments follows
    public static final String FLUSHMESSAGES_KEY_DIGGS = "diggs";
    public static final String FLUSHMESSAGES_KEY_COMMENTS = "comments";
    public static final String FLUSHMESSAGES_KEY_FOLLOWS = "follows";
    public static final String FLUSHMESSAGES_KEY_NOTICES = "notices";

    /**
     * 聊天相关
     */
    public static final String APP_PATH_CREATE_CONVERSAITON = "api/v1/im/conversations";// 创建对话
    public static final String APP_PATH_GET_CONVERSAITON_LIST = "api/v1/im/conversations/list/all";// 获取登陆用户的对话列表
    public static final String APP_PATH_GET_SINGLE_CONVERSAITON = "api/v1/im/conversations/{cid}";// 获取单个对话信息

    /**
     * 关注粉丝 FollowFansClient
     */
    //api/v1/follows/follows/{user_id}/{max_id}
    public static final String APP_PATH_FOLLOW_LIST = "api/v1/follows/follows/{user_id}/{max_id}";// 获取用户关注列表
    //api/v1/follows/followeds/{user_id}/{max_id}
    public static final String APP_PATH_FANS_LIST = "api/v1/follows/followeds/{user_id}/{max_id}";// 获取用户粉丝列表
    public static final String APP_PATH_FOLLOW_USER = "api/v1/users/follow";// 关注用户
    public static final String APP_PATH_CANCEL_FOLLOW_USER = "api/v1/users/unFollow";// 取消用户关注
    public static final String APP_PATH_GET_USER_FOLLOW_STATE = "api/v1/users/followstatus";// 获取用户关注状态
    public static final String APP_PATH_GET_DIGGS_RANK = "api/v1/diggsrank";//  用户点赞排行

    /**
     * 动态相关
     */
    public static final String APP_PATH_SEND_DYNAMIC = "api/v1/feeds";// 发布动态
    public static final String APP_PATH_DELETE_DYNAMIC = "api/v1/feeds/%s";// 删除一条动态
    // 获取动态列表
    // 最新：/api/v1/feeds;
    // 关注：/api/v1/feeds/follows;
    // 热门：/api/v1/feeds/hots;
    // 某个人的：/api/v1/feeds/users/{user_id}
    public static final String APP_PATH_GET_DYNAMIC_LIST = "api/v1/feeds/{type}";
    public static final String DYNAMIC_TYPE_NEW = ""; // 最新动态
    public static final String DYNAMIC_TYPE_FOLLOWS = "follows"; // 关注动态
    public static final String DYNAMIC_TYPE_HOTS = "hots"; // 热门动态
    public static final String DYNAMIC_TYPE_SOMEONE = "users/%s"; // 某个人的动态列表,%s表示用户id
    public static final String DYNAMIC_TYPE_MY_COLLECTION = "collections";// 我收藏的动态列表
    // 点赞一条动态,取消点赞
    public static final String APP_PATH_DYNAMIC_HANDLE_LIKE = "api/v1/feeds/{feed_id}/digg";
    public static final String APP_PATH_DYNAMIC_HANDLE_LIKE_FORMAT = "api/v1/feeds/%s/digg";
    // 删除一条评论评论
    public static final String APP_PATH_DYNAMIC_DELETE_COMMENT = "api/v1/feeds/%s/comment/%s";
    // 对一条动态或一条动态评论进行评论
    public static final String APP_PATH_DYNAMIC_SEND_COMMENT = "api/v1/feeds/%s/comment";
    // 获取点赞列表
    public static final String APP_PATH_DYNAMIC_DIG_LIST = "api/v1/feeds/{feed_id}/diggusers";
    // 一条动态的评论列表
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST = "api/v1/feeds/{feed_id}/comments";
    // 根据id获取评论列表
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST_BY_COMMENT_ID = "api/v1/feeds/comments";
    // 收藏动态，取消收藏
    public static final String APP_PATH_HANDLE_COLLECT = "api/v1/feeds/{feed_id}/collection";
    public static final String APP_PATH_HANDLE_COLLECT_FORMAT = "api/v1/feeds/%s/collection";
    // 增加动态浏览量
    public static final String APP_PATH_HANDLE_DYNAMIC_VIEWCOUNT = "api/v1/feeds/{feed_id}/viewcount";

    /**
     * 资讯相关
     */
    public static final String APP_PATH_INFO_TYPE = "api/v1/news/cates";// 资讯分类列表
    public static final String APP_PATH_INFO_LIST = "api/v1/news";// 资讯列表
    public static final String APP_PATH_INFO_COLLECT_LIST = "api/v1/news/collections";// 普通的资讯列表，通过cate_uid获取
    public static final String INFO_TYPE_COLLECTIONS = "-1000";// 资讯收藏列表
    public static final String APP_PATH_INFO_COMMENT = "api/v1/news/{news_id}/comment";// 评论资讯
    public static final String APP_PATH_INFO_COMMENT_FORMAT = "api/v1/news/%s/comment";// 评论资讯

    // 删除资讯评论
    public static final String APP_PATH_INFO_DELETE_COMMENT_FORMAT = "api/v1/news/%s/comment/%s";
    public static final String APP_PATH_INFO_DELETE_COMMENT = "api/v1/news/{news_id}/comment/{comment_id}";

    // 资讯评论列表
    public static final String APP_PATH_INFO_COMMENT_LIST = "api/v1/news/{feed_id}/comments";//

    // 收藏资讯
    public static final String APP_PATH_INFO_COLLECT = "api/v1/news/{news_id}/collection";
    public static final String APP_PATH_INFO_COLLECT_FORMAT = "api/v1/news/%s/collection";

    // 点赞资讯
    public static final String APP_PATH_INFO_DIG = "api/v1/news/{news_id}/digg";
    public static final String APP_PATH_INFO_DIG_FORMAT = "api/v1/news/%s/digg";

    // 订阅资讯频道
    public static final String APP_PATH_INFO_FOLLOW_LIST = "api/v1/news/cates/follow";
    public static final String APP_PATH_INFO_SEARCH = "/api/v1/news/search";

    // 资讯详情网页
    public static final String APP_PATH_INFO_DETAILS = "/api/v1/news/{news_id}";
    public static final String APP_PATH_INFO_DETAILS_FORMAT = "/api/v1/news/%d";

    /**
     * 音乐相关
     */
    public static final String APP_PATH_MUSIC_ABLUM_LIST = "api/v1/music/specials";// 专辑列表
    public static final String APP_PATH_MUSIC_COLLECT_ABLUM_LIST = "api/v1/music/special/collections";// 专辑列表

    // 歌曲详情
    public static final String APP_PATH_MUSIC_DETAILS = "api/v1/music/{music_id}";

    // 评论歌曲
    public static final String APP_PATH_MUSIC_COMMENT = "api/v1/music/{music_id}/comment";
    public static final String APP_PATH_MUSIC_COMMENT_FORMAT = "api/v1/music/%s/comment";

    // 删除音乐评论
    public static final String APP_PATH_MUSIC_DELETE_COMMENT_FORMAT = "api/v1/music/comment/%s";
    public static final String APP_PATH_MUSIC_DELETE_COMMENT = "api/v1/music/comment/{comment_id}";

    // 评论专辑
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT = "api/v1/music/special/{special_id}/comment";
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT = "api/v1/music/special/%s/comment";

    // 专辑评论列表
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_LIST = "api/v1/music/special/{special_id}/comment";

    // 收藏专辑
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT = "api/v1/music/special/{special_id}/collection";
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT_FORMAT = "api/v1/music/special/%s/collection";

    // 音乐点赞
    public static final String APP_PATH_MUSIC_DIGG = "api/v1/music/{music_id}/digg";
    public static final String APP_PATH_MUSIC_DIGG_FORMAT = "api/v1/music/%s/digg";

    // 专辑详情
    public static final String APP_PATH_MUSIC_ABLUM_DETAILS = "api/v1/music/specials/{special_id}";

    // 分享歌曲
    public static final String APP_PATH_MUSIC_SHARE = "api/v1/music/%s/share";

    // 分享专辑
    public static final String APP_PATH_MUSIC_ABLUM_SHARE = "api/v1/music/special/%s/share";


    /**
     * 通用 CommonClient
     */
    public static final String APP_PATH_GET_VERTIFYCODE = "api/v1/auth/phone/send-code";// 获取验证码
    public static final String APP_PATH_REFRESH_TOKEN = "api/v1/auth";// 刷新 token
    public static final String APP_PATH_CREATE_STORAGE_TASK = "api/v1/storages/task";// 储存任务创建
    public static final String APP_PATH_NOTIFY_STORAGE_TASK =
            "api/v1/storages/task/{storage_task_id}";//  储存任务通知
    public static final String APP_PATH_DELETE_STORAGE_TASK =
            "api/v1/storages/task/{storage_task_id}";// 通知服务器，删除当前上传文件
    public static final String APP_PATH_HANDLE_BACKGROUND_TASK = "{path}";// 处理后台任务

    public static final String APP_PATH_GET_COMPONENT_STATUS = "api/v1/system/component/status";// 查看扩展包安装状态
    public static final String APP_PATH_GET_COMPONENT_CONFIGS = "api/v1/system/component/configs";// 获取扩展包配置信息
    public static final String APP_PATH_GET_COMPONENT_CONFIGS_IM = "im";// 获取扩展包配置信息 类容

    public static final String APP_PATH_SYSTEM_FEEDBACK = "api/v1/system/feedback";// 意见反馈
    public static final String APP_PATH_GET_SYSTEM_CONVERSATIONS = "api/v1/system/conversations";// 获取系统会话列表
    // 会话类型
    public static final String SYSTEM_CONVERSATIONS_TYPE_FEEDBACK = "feedback";// 用户意见反馈
    public static final String SYSTEM_CONVERSATIONS_TYPE_SYSTEM = "system";// 系统通知

    /**
     * 分享相关
     */
    public static final String APP_PATH_SHARE_USERINFO = APP_DOMAIN + "profile/%s";// 用户信息分享地址 url/profile/{user_id}
    public static final String APP_PATH_SHARE_DYNAMIC = APP_DOMAIN + "feeds/detail/%s";// 动态信息分享地址 url/feeds/detail/{feed_id}
    public static final String APP_PATH_SHARE_DEFAULT = APP_DOMAIN + "api/develop";// 开发中的提示


    /**
     * 仅仅测试使用
     */
    public static final String APP_PATH_TOKEN_EXPIERD = "api/music_window_rotate-token";// token过期处理

    /**
     * 频道相关
     */
    // 处理频道订阅取消订阅的接口
    public static final String APP_PATH_HANDLE_SUBSCRIB_CHANNEL = "api/v1/channels/{channel_id}/follow";
    public static final String APP_PATH_HANDLE_SUBSCRIB_CHANNEL_S = "api/v1/channels/%s/follow";
    // 获取频道列表
    public static final String APP_PATH_GET_CHANNEL = "api/v1/channels/{type}";
    public static final String CHANNEL_TYPE_ALL_CHANNEL = "";// 所有的频道
    public static final String CHANNEL_TYPE_MY_SUBSCRIB_CHANNEL = "my";// 我订阅的频道
    // 发送动态到频道
    public static final String APP_PATH_SEND_DYNAMIC_TO_CHANNEL = "api/v1/channels/{channel_id}/feed";
    public static final String APP_PATH_SEND_DYNAMIC_TO_CHANNEL_S = "api/v1/channels/%s/feed";
    // 获取频道的动态列表
    public static final String APP_PATH_GET_CHANNEL_DYNAMIC_LIST = "api/v1/channels/{channel_id}/feeds";

    /**
     * 组件 目前：动态（feed）、音乐（music）、资讯（news）
     */
    public static final String APP_COMPONENT_FEED = "feed";
    public static final String APP_COMPONENT_MUSIC = "music";
    public static final String APP_COMPONENT_NEWS = "news";
    //     feeds musics music_specials news
    public static final String APP_COMPONENT_SOURCE_TABLE_FEEDS = "feeds";
    public static final String APP_COMPONENT_SOURCE_TABLE_MUSICS = "musics";
    public static final String APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS = "music_specials";
    public static final String APP_COMPONENT_SOURCE_TABLE_NEWS = "news";
}
