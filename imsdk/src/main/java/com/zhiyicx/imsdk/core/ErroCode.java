package com.zhiyicx.imsdk.core;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/14
 * @Contact master.jungle68@gmail.com
 */

public class ErroCode {
    /**
     * Im错误事件回执码
     */
    public static final int SUCCESS_CODE = 0;//通信成功
    public static final int SERVER_EXCEPTION = 1000;//服务器发生了未知的异常，导致处理中断
    public static final int PACKET_EXCEPTION_ERR_DATA = 1010;//无效的数据包
    public static final int PACKET_EXCEPTION_ERR_PACKET_TYPE = 1011;//无效的数据包类型
    public static final int PACKET_EXCEPTION_ERR_BODY_TYPE = 1012;//无效的消息主体
    public static final int PACKET_EXCEPTION_ERR_SERILIZE_TYPE = 1013;//无效的序列化类型
    public static final int PACKET_EXCEPTION_ERR_KEY_TYPE = 1014;//无效的键名路由

    public static final int AUTH_FAILED_NO_UID_OR_PWD = 1020;//未提供认证需要的uid和pwd
    public static final int AUTH_FAILED_ERR_UID_OR_PWD = 1021;//认证失败，可能原因是uid不存在或密码错误，或账号已被禁用

    public static final int CHATROOM_JOIN_FAILED = 2001;//对话加入失败
    public static final int CHATROOM_LEAVE_FAILED = 2002;//对话离开失败
    public static final int CHATROOM_MC_FAILED = 2003;//对话成员查询失败

    public static final int CHATROOM_SEND_MESSAGE_FAILED = 3001;//消息发送失败
    public static final int CHATROOM_BANNED_WORDS = 3004;//对话成员被禁言
    public static final int CHATROOM_NOT_JOIN_ROOM = 3003;//未加入房间
}

