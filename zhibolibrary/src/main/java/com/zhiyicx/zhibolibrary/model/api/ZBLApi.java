package com.zhiyicx.zhibolibrary.model.api;


import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;

/**
 * Created by zhiyicx on 2016/3/14.
 */
public class ZBLApi {
    public final static String EXTRAL_URL = "/api";
    //域名
    public final static String BASE_URL ="http://custom.zhibocloud.cn";
    public final static String ZHIBO_BASE_URL ="http://cloud.zhibocloud.cn";
    public final static String ZHIBO_BASE_VERSION ="1.0";
    //数据分页参数
    public static String VAR_PAGE = "p";
    public static ZBApiConfig sZBApiConfig;//礼物、搜索配置等

    public static final String STR_SHARE_NAME="[uname]";
    public static final String STR_SHARE_USID="[usid]";
    public static final String STR_SHARE_ME="我";

    public static final String SHARE_TITLE = STR_SHARE_NAME+"-正在直播";
    public static final String SHARE_CONTENT = "在智播直播啦!来一起看看吧";
    public static final String SHARE_URL = "/?api=ZBCloud_Show_Stream&usid="+STR_SHARE_USID;

    //返回码
    public static final String REQUEST_SUCESS = "00000";
    public static final String REQUEST_LIVE_END = "60005";
    public static final String REQUEST_LIMIT_PLAY = "60000";
    public static final String REQUEST_ILLEGAL = "00500";//授权失败
    public static final String REQUEST_EXCHANGE_PROTOKEN_INVALID = "70403";
    public static final String REQUEST_AUTH_INVALID = "30400";//用户不合法
    public static int SNS_TIME = 60;
    //发送短信口令
    public static String SEND_SNS_TOKEN = "zhibosms";
    /**
     * api映射表
     */
    /**
     * 获取用户个人信息
     */
    public static final String API_GET_USER_INFO = "ZB_User_Get_Info";

    /**
     * 获取第三方用户个人信息
     */
    public static final String API_GET_USID_INFO = "ZB_User_Get_Usidinfo";
    /**
     * 获取认证信息
     */
    public static final String API_USER_FOLLW = "ZB_User_Follow";
    /**
     * 获取关注、粉丝列表信息
     */
    public static final String API_GET_USER_LIST = "ZB_User_Get_List";

    /**
     * 用户搜索
     */
    public static final String API_USER_SEARCH = "ZB_User_Search";






    /***
     * 直播云api
     *
     */
    /**
     * 获取直播列表
     */
    public static final String ZB_API_GET_LIVE_LIST = "ZBCloud_Get_Live_List";

    /**
     * 获取随机推荐列表
     */
    public static final String ZB_API_GET_RECOMME_LIVE_LIST = "ZBCloud_Get_Randlive_List";
    /**
     * 搜索直播间
     */
    public static final String ZB_API_SEARCH_LIVE_LIST = "ZBCloud_Search_Live";
    /**
     * 获取回放列表
     */
    public static final String ZB_API_GET_VIDEO_LIST= "ZBCloud_Get_Video_List";
    /**
     * 搜索回放
     */
    public static final String ZB_API_SEARCH_VIDEO_LIST= "ZBCloud_Search_Video";

    /**
     * 获取主播排行榜
     */
    public static final String ZB_API_GET_PRESENTER_RANK_LIST= "ZBCloud_Presenter_TopList";

    /**
     * 获取礼物排行榜
     */
    public static final String ZB_API_GET_GOLD_RANK_LIST= "ZBCloud_Gift_TopList";

    /**
     * 获取主播直播状态
     * 1:正在直播 0:未直播
     */
    public final static String API_GET_PRESENTER_STATUS = "ZBCloud_Get_Stream_Status";



}
