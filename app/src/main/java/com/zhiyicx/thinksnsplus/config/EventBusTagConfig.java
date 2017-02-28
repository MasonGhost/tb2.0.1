package com.zhiyicx.thinksnsplus.config;

/**
 * @Describe 存储所有android-eventbus Tag 标识
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class EventBusTagConfig {
    public static final String EVENT_BACKGROUND_TASK = "event_background_task";// 后台任务处理
    public static final String EVENT_STOP_BACKGROUND_TASK = "event_stop_background_task";// 暂停后台任务处理
    public static final String EVENT_START_BACKGROUND_TASK = "event_start_background_task";//
    // 开始后台任务处理
    public static final String EVENT_BACKGROUND_TASK_CANT_NOT_DEAL =
            "event_background_task_cant_not_deal";// 后台任务处理
    public static final String EVENT_USERINFO_UPDATE = "event_userinfo_update";// 后台刷新用户信息
    public static final String EVENT_SELECTED_PHOTO_UPDATE = "event_selected_photo_update";//
    // 刷新相册图片列表
    public static final String EVENT_SEND_DYNAMIC_TO_LIST = "event_send_dynamic_to_List";//
    // 发送动态到动态列表
    public static final String EVENT_SEND_MUSIC_CACHE_PROGRESS =
            "event_send_music_cache_progress";// 音乐播放缓冲进度
    public static final String EVENT_SEND_MUSIC_START =
            "event_send_music_start";// 音乐开始播放
    public static final String EVENT_SEND_MUSIC_COMPLETE =
            "EVENT_SEND_MUSIC_COMPLETE";// 当前音乐播放完成
    // 发送动态到动态列表

    /*******************************************
     * IM 相关
     *********************************************/
    public static final String EVENT_IM_ONMESSAGERECEIVED = "onMessageReceived";
    public static final String EVENT_IM_ONMESSAGEACKRECEIVED = "onMessageACKReceived";
    public static final String EVENT_IM_ONCONNECTED = "onConnected";
    public static final String EVENT_IM_ONDISCONNECT = "onDisconnect";
    public static final String EVENT_IM_ONERROR = "onError";
    public static final String EVENT_IM_ONMESSAGETIMEOUT = "onMessageTimeout";
}
