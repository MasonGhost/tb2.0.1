package com.zhiyicx.baseproject.config;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class ApiConfig {
    /**
     * 网络根地址  http://192.168.10.222/
     * 测试服务器：http://192.168.2.222:8080/mockjs/2/test-get-repose-head-normal?
     */

//    public static final String APP_DOMAIN = "http://tsplus.zhibocloud.cn/";// 测试服务器
    public static final String APP_DOMAIN = "http://192.168.2.222/";// 测试服务器
//    public static final String APP_DOMAIN = "http://192.168.2.222:8080/mockjs/2/";// rap 测试服务器

    public static final String URL_ABOUT_US = "http://blog.csdn.net/hellohhj/article/details/50467502";// 关于我们网站


    /*******************************************  接口 Path  *********************************************/
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
    public static final String APP_PATH_GET_USER_INFO = "api/v1/users/{user_id}";// 获取用户信息
    public static final String APP_PATH_CHANGE_USER_INFO = "api/v1/users";// 修改用户信息
    public static final String APP_PATH_GET_IM_INFO = "api/v1/im/users";// 获取 IM 帐号信息

    /**
     * 聊天相关
     */
    public static final String APP_PATH_CREATE_CONVERSAITON = "api/v1/im/conversations";// 创建对话
    public static final String APP_PATH_GET_CONVERSAITON_LIST = "api/v1/im/conversations/list/all";// 创建对话

    /**
     * 关注粉丝 FollowFansClient
     */
    public static final String APP_PATH_FOLLOW_LIST = "api/v1/follows/follows/{user_id}/{max_id?}";// 获取用户关注列表
    public static final String APP_PATH_FANS_LIST = "api/v1/follows/followeds/{user_id}/{max_id?}";// 获取用户粉丝列表
    public static final String APP_PATH_FOLLOW_USER = "api/v1/users/follow/{user_id}";// 关注用户
    public static final String APP_PATH_CANCEL_FOLLOW_USER = "api/v1/users/unFollow/{user_id}";// 取消用户关注

    /**
     * 通用 CommonClient
     */
    public static final String APP_PATH_GET_VERTIFYCODE = "api/v1/auth/phone/send-code";// 获取验证码
    public static final String APP_PATH_REFRESH_TOKEN = "api/v1/auth";// 刷新 token
    public static final String APP_PATH_CREATE_STORAGE_TASK = "api/v1/storages/task/{hash}/{origin_filename}";// 储存任务创建
    public static final String APP_PATH_NOTIFY_STORAGE_TASK = "api/v1/storages/task/{storage_task_id}";//  储存任务通知
    public static final String APP_PATH_DELETE_STORAGE_TASK = "api/v1/storages/task/{storage_task_id}";// 通知服务器，删除当前上传文件
    public static final String APP_PATH_HANDLE_BACKGROUND_TASK = "{path}";// 处理后台任务

    /**
     * 仅仅测试使用
     */
    public static final String APP_PATH_TOKEN_EXPIERD = "api/test-token";// token过期处理
}
