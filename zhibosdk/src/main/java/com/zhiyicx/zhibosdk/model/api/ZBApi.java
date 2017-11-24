package com.zhiyicx.zhibosdk.model.api;


import com.zhiyicx.zhibosdk.model.entity.ZBFilterWordConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;
import com.zhiyicx.zhibosdk.model.entity.ZBApiFilter;
import com.zhiyicx.zhibosdk.model.entity.ZBGift;
import com.zhiyicx.zhibosdk.model.entity.ZBWebIM;

import java.util.List;

/**
 * Created by zhiyicx on 2016/3/14.
 */
public class ZBApi {
    public final static String BASE_API = "/api";

    //配置的直播服务器根域名，获取 api domain 和 config
    public static String ZHIBO_DOMAIN = "http://test.zhibocloud.cn";

    //智播服务器更域名版本号

    public static String ZHIBO_DOMAIN_VERSION = "1.0";

    //域名(服务器分配的域名)
    public static String USENOW_DOMAIN =ZHIBO_DOMAIN;


    public static ZBApiFilter FILTER_LIST;

    public static List<ZBGift> GIFT_LIST;
    public static ZBApiConfig ZBAPICONFIG;

    public static List<ZBWebIM> WEBIM;
    public static ZBFilterWordConfig FILTERWORD;

    //返回码
    public static final String REQUEST_SUCESS = "00000";
    public static final String REQUEST_LIVE_END = "60005";
    public static final String REQUEST_LIMIT_PLAY = "60000";

    public static String getContantsSeckret() {
        return CONTANTS_SECKRET;
    }

    private static final String CONTANTS_SECKRET = "zhiyicx2016";

    /**
     * APi
     */
    /**
     * 获取数据获取域名
     */
    public final static String API_GET_DOMAIN = "/api";
    /**
     * 应用验证
     */
    public final static String API_GET_API = "/api/getconfig/getApi";
    /**
     * 获取版本号
     */
    public final static String API_GET_APIVERSION = "ZBCloud_Get_Apiversion";

    /**
     * 获取应用数据域名
     */
    public final static String API_GET_API_DOMAIN = "ZBCloud_Get_Api";

    public final static String API_GET_CONFIG = "ZBCloud_Get_Config";
    /**
     * 票据验证
     */
    public final static String API_GET_TICKET = "ZBCloud_Get_Auth";
    /**
     * 获取/创建自己的直播间
     */
    public final static String API_CREATE_STREAM = "ZBCloud_Create_Stream";

    /**
     * 校验直播间数据
     */
    public final static String API_CHECK_STREAM = "ZBCloud_Check_Stream";


    /**
     * 开始直播的准备数据
     */
    public final static String API_START_STREAM = "ZBCloud_Start_Stream";

    /**
     * 结束直播
     */
    public final static String API_END_STREAM = "ZBCloud_End_Stream";
    /**
     * 获取观看直播地址
     */
    public final static String API_GET_PLAYURL = "ZBCloud_Get_Playurl";


    /**
     * 获取观看回放地址
     */
    public final static String API_GET_VIDEOURL = "ZBCloud_Get_Video_Url";

    /**
     * 上传直播封面图
     */
    public final static String API_UPLOAD_STREAM = "ZBCloud_Upload_Stream";


    /**
     * 上传文件
     */
    public final static String API_UPLOAD_FILE = "ZBCloud_Upload_File";


    /**
     * 主播设置禁言
     */
    public final static String API_SHUTUP = "ZBCloud_Im_Stream_Disable";
    /**
     * 解除禁言
     */
    public final static String API_SHUTUP_RESET = "ZBCloud_Im_Stream_Enable";

    /**
     * 赠送主播礼物
     */
    public final static String API_SEND_GIFT= "ZBCloud_Give_Gift";
    /**
     * 赠送主播赞
     */
    public final static String API_SEND_ZAN= "ZBCloud_Give_Zan";

    /**
     * 创建对话
     */
    public final static String API_CREATE_CONVERSATION= "ZBCloud_Im_Conversation_Create";


}
