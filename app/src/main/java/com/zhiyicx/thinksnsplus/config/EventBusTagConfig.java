package com.zhiyicx.thinksnsplus.config;

/**
 * @Describe 存储所有android-eventbus Tag 标识
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class EventBusTagConfig {
    // 后台任务处理
    public static final String EVENT_BACKGROUND_TASK = "event_background_task";
    // 暂停后台任务处理
    public static final String EVENT_STOP_BACKGROUND_TASK = "event_stop_background_task";
    // 开始后台任务处理
    public static final String EVENT_START_BACKGROUND_TASK = "event_start_background_task";

    // 后台任务处理
    public static final String EVENT_BACKGROUND_TASK_CANT_NOT_DEAL =
            "event_background_task_cant_not_deal";
    // 后台刷新用户信息
    public static final String EVENT_USERINFO_UPDATE = "event_userinfo_update";

    // 发送动态到动态列表
    public static final String EVENT_SEND_DYNAMIC_TO_LIST = "event_send_dynamic_to_List";
    // 发送动态到频道动态列表

    public static final String EVENT_SEND_DYNAMIC_TO_CHANNEL="event_send_dynamic_to_channel";
    // 收藏动态
    public static final String EVENT_COLLECT_DYNAMIC="event_collect_dynamic";
    //更新动态列表
    public static final String EVENT_UPDATE_DYNAMIC = "event_update_dynamic";
    // 动态列表发送评论
    public static final String EVENT_SEND_COMMENT_TO_DYNAMIC_LIST = "event_send_dynamic_to_List";
    // 资讯列表发送评论
    public static final String EVENT_SEND_COMMENT_TO_INFO_LIST = "event_send_info_to_List";

    // 资讯收藏
    public static final String EVENT_SEND_INFO_LIST_COLLECT = "event_send__info_list_collect";

    // 资讯收藏--针对搜索的内存数据处理
    public static final String EVENT_SEND_INFO_LIST_COLLECT_SERACH = "event_send__info_list_collect_serach";

    // 音乐播放缓冲进度
    public static final String EVENT_SEND_MUSIC_CACHE_PROGRESS = "event_send_music_cache_progress";
    // 音乐加载
    public static final String EVENT_SEND_MUSIC_LOAD = "event_send_music_load";
    // 当前音乐播放完成
    public static final String EVENT_SEND_MUSIC_COMPLETE = "event_send_music_complete";
    // 音乐改变
    public static final String EVENT_SEND_MUSIC_CHANGE = "event_send_music_change";

    // 对某人进行关注或者取消关注，需要改变个人主页关注数量
    public static final String EVENT_FOLLOW_AND_CANCEL_FOLLOW = "event_follow_and_cancle_follow";

    // 频道进行订阅或者取消订阅
    public static final String EVENT_CHANNEL_SUBSCRIB = "event_channel_subscrib";


    /**
     * 音乐相关
     */
    // 音乐专辑收藏
    public static final String EVENT_ABLUM_COLLECT = "event_ablum_collect";

    // 音乐单曲喜欢
    public static final String EVENT_MUSIC_LIKE = "event_music_like";

    /**
     * IM 相关
     */
    public static final String EVENT_IM_ONMESSAGERECEIVED = "onMessageReceived";
    public static final String EVENT_IM_ONMESSAGEACKRECEIVED = "onMessageACKReceived";
    public static final String EVENT_IM_ONCONNECTED = "onConnected";
    public static final String EVENT_IM_AUTHSUCESSED = "onauthSucessed";
    public static final String EVENT_IM_ONDISCONNECT = "onDisconnect";
    public static final String EVENT_IM_ONERROR = "onError";
    public static final String EVENT_IM_ONMESSAGETIMEOUT = "onMessageTimeout";
    public static final String EVENT_IM_ONCONVERSATIONCRATED = "onConversationCrated";

    /**
     * 消息底部提示信息相关
     */
    public static final String EVENT_IM_SET_MESSAGE_TIP_VISABLE = "setMessageTipVisable";
    public static final String EVENT_IM_SET_MINE_TIP_VISABLE = "setMineTipVisable";
    public static final String EVENT_IM_SET_MINE_FANS_TIP_VISABLE = "setMineFansTipVisable";
    /**
     * Jpush  推送相关
     */
    public static final String EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST = "onJpushMessageRecievedUpdateMessageList";

}
