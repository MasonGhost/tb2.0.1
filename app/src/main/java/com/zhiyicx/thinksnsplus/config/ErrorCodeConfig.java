package com.zhiyicx.thinksnsplus.config;

/**
 * @author LiuChao
 * @describe 错误码定义
 * @date 2017/1/19
 * @contact email:450127106@qq.com
 */

public class ErrorCodeConfig {
    public static final int TOKEN_EXPIERD = 1012;// token 过期
    public static final int NEED_RELOGIN = 1013;// token 刷新失败，需要重新登录
    public static final int NEED_NO_DEVICE= 1014;// 移动端设备登录/注册未传递设备号
    public static final int OTHER_DEVICE_LOGIN = 1015;// token 在其他设备登陆
    public static final int TOKEN_NOT_EXIST = 1016;// token 不存在
    public static final int USER_AUTH_FAIL = 1099;// 用户认证失败

}
