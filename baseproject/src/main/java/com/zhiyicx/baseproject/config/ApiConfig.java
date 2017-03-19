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

    public static final String APP_DOMAIN = "http://tsplus.zhibocloud.cn/";// 测试服务器
    //public static final String APP_DOMAIN = "http://192.168.2.222/";// 测试服务器
    // public static final String APP_DOMAIN = "http://192.168.2.222:8080/mockjs/2/";// rap 测试服务器

    public static final String URL_ABOUT_US = "http://blog.csdn" +
            ".net/hellohhj/article/details/50467502";// 关于我们网站

    // 图片地址
    public static final String IMAGE_PATH = APP_DOMAIN + "api/v1/storages/%s/%d";// 带质量压缩
    // 图片地址
    public static final String NO_PROCESS_IMAGE_PATH = APP_DOMAIN + "api/v1/storages/%s";// 不带质量压缩

    // 每次从服务器获取数据，一页的最大数量
    public static final int MAX_NUMBER_PER_PAGE = 10;

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

    /**
     * 聊天相关
     */
    public static final String APP_PATH_CREATE_CONVERSAITON = "api/v1/im/conversations";// 创建对话
    public static final String APP_PATH_GET_CONVERSAITON_LIST =
            "api/v1/im/conversations/list/all";// 创建对话

    /**
     * 关注粉丝 FollowFansClient
     */
    //api/v1/follows/follows/{user_id}/{max_id}
    public static final String APP_PATH_FOLLOW_LIST =
            "api/v1/follows/follows/{user_id}/{max_id}";// 获取用户关注列表
    //api/v1/follows/followeds/{user_id}/{max_id}
    public static final String APP_PATH_FANS_LIST =
            "api/v1/follows/followeds/{user_id}/{max_id}";// 获取用户粉丝列表
    public static final String APP_PATH_FOLLOW_USER = "api/v1/users/follow";// 关注用户
    public static final String APP_PATH_CANCEL_FOLLOW_USER = "api/v1/users/unFollow";// 取消用户关注
    public static final String APP_PATH_GET_USER_FOLLOW_STATE = "api/v1/users/followstatus";//
    // 获取用户关注状态

    /**
     * 动态相关
     */
    public static final String APP_PATH_SEND_DYNAMIC = "api/v1/feeds";// 发布动态
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
    public static final int DYNAMIC_PAGE_LIMIT = 10;// 不传 服务器默认10条
    // 点赞一条动态,取消点赞
    public static final String APP_PATH_DYNAMIC_HANDLE_LIKE = "api/v1/feeds/{feed_id}/digg";
    public static final String APP_PATH_DYNAMIC_HANDLE_LIKE_FORMAT = "api/v1/feeds/%s/digg";
    // 删除一条评论
    public static final String APP_PATH_DYNAMIC_DELETE_COMMENT = "api/v1/feeds/%s/comment/%s";
    // 对一条动态或一条动态评论进行评论
    public static final String APP_PATH_DYNAMIC_SEND_COMMENT = "api/v1/feeds/%s/comment";
    // 获取点赞列表
    public static final String APP_PATH_DYNAMIC_DIG_LIST = "api/v1/feeds/{feed_id}/diggusers";
    // 一条动态的评论列表
    public static final String APP_PATH_DYNAMIC_COMMENT_LIST = "api/v1/feeds/{feed_id}/comments";
    // 收藏动态，取消收藏
    public static final String APP_PATH_HANDLE_COLLECT = "api/v1/feeds/{feed_id}/collection";
    public static final String APP_PATH_HANDLE_COLLECT_FORMAT = "api/v1/feeds/%s/collection";

    /**
     * 资讯相关
     */
    public static final String APP_PATH_INFO_TYPE = "api/v1/news/cates";// 资讯分类列表
    public static final String APP_PATH_INFO_LIST = "api/v1/news";// 资讯列表
    public static final String APP_PATH_INFO_COMMENT = "api/v1/news/{news_id}/comment";// 评论资讯
    public static final String APP_PATH_INFO_COMMENT_LIST =
            "api/v1/news/{feed_id}/comments";// 资讯评论列表
    public static final String APP_PATH_INFO_COLLECT = "api/v1/news/{news_id}/collection";// 收藏资讯
    public static final String APP_PATH_INFO_FOLLOW_LIST = "api/v1/news/cates/follow";// 订阅资讯频道

    /**
     * 音乐相关
     */
    public static final String APP_PATH_MUSIC_ABLUM_LIST = "api/v1/music/specials";// 专辑列表
    public static final String APP_PATH_MUSIC_DETAILS = "api/v1/music/{music_id}";// 歌曲详情
    public static final String APP_PATH_MUSIC_COMMENT = "api/v1/music/{music_id}/comment";// 评论歌曲
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT =
            "api/v1/music/specail/{special_id}/comment";// 评论专辑
    public static final String APP_PATH_MUSIC_ABLUM_COLLECT =
            "api/v1/music/special/{special_id}/collection";// 收藏专辑
    public static final String APP_PATH_MUSIC_DIGG = "api/v1/music/{music_id}/digg";// 音乐点赞
    public static final String APP_PATH_MUSIC_ABLUM_DETAILS =
            "api/v1/music/specials/{special_id}";// 专辑详情


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

    /**
     * 仅仅测试使用
     */
    public static final String APP_PATH_TOKEN_EXPIERD = "api/music_window_rotate-token";// token过期处理
}
