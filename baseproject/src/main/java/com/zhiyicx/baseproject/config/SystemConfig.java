package com.zhiyicx.baseproject.config;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/18
 * @Contact master.jungle68@gmail.com
 */

public class SystemConfig {
    /**
     * {
     * "im:serve":"114.215.203.142:9900",
     * "im:helper":[
     * {
     * "uid":"1",
     * "url":"https://plus.io/users/1"
     * }
     * ]
     * }"    \"im:helper\": [{\"uid\":\"2\",\"url\":\"https://plus.io/users/1\"}],\n" +
     "    \"im:serve\": \"114.215.203.142:9900\",\n" +
     */
    public static final String DEFAULT_SYSTEM_CONFIG ="{\"server:version\":\"1.7.0\",\"wallet:ratio\":100,\"ad\":[]," +
            "\"site\":{\"background\":{\"logo\":\"\\/api\\/v2\\/files\\/2\"},\"gold_name\":{\"name\":\"\\u91d1\\u5e01\",\"unit\":\"\\u4e2a\"}," +
            "\"currency_name\":{\"id\":1,\"name\":\"\\u79ef\\u5206\",\"unit\":\"\",\"enable\":1}},\"registerSettings\":null," +
            "\"wallet:cash\":{\"open\":true},\"wallet:recharge\":{\"open\":true},\"wallet:transform\":{\"open\":true}," +
            "\"currency:cash\":{\"open\":true},\"currency:recharge\":{\"open\":true,\"IAP_only\":true},\"question:apply_amount\":200," +
            "\"question:onlookers_amount\":100,\"plus-appversion\":{\"open\":false},\"checkin\":false,\"checkin:attach_balance\":1," +
            "\"feed\":{\"reward\":false,\"paycontrol\":false,\"items\":[],\"limit\":50},\"news:contribute\":{\"pay\":false,\"verified\":false}," +
            "\"news:pay_conyribute\":0,\"group:create\":{\"need_verified\":false},\"group:reward\":{\"status\":true}}";
    // 允许的注册方式
    public static final String REGITER_MODE_ALL = "all";
    public static final String REGITER_MODE_INVITED = "invited";
    public static final String REGITER_MODE_THIRDPART = "thirdPart";

    // 允许的注册类型
    public static final String REGITER_ACCOUNTTYPE_ALL = "all";
    public static final String REGITER_ACCOUNTTYPE_MOBILE_ONLY = "mobile-only";
    public static final String REGITER_ACCOUNTTYPE_MAIL_ONLY = "mail-only";

    // 投稿控制
    public static final String PUBLISH_INFO_NEED_VERIFIED = "verified";
    public static final String PUBLISH_INFO_NEED_PAY = "pay";
}
