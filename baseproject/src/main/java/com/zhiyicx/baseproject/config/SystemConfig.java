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
    public static final String DEFAULT_SYSTEM_CONFIG ="{\"server:version\":\"1.6.0\",\"hots_area\":[{\"name\":\"\\\\u4e2d\\\\u56fd " +
            "\\\\u56db\\\\u5ddd\\\\u7701 \\\\u6210\\\\u90fd\\\\u5e02\",\"sort\":10},{\"name\":\"\\\\u4e2d\\\\u56fd \\\\u5317\\\\u4eac\\\\u5e02 " +
            "\\\\u5317\\\\u4eac\\\\u5e02\",\"sort\":0},{\"name\":\"\\\\u4e2d\\\\u56fd \\\\u4e0a\\\\u6d77\\\\u5e02 \\\\u4e0a\\\\u6d77\\\\u5e02\"," +
            "\"sort\":0},{\"name\":\"\\\\u4e2d\\\\u56fd \\\\u5317\\\\u4eac\\\\u5e02 \\\\u6d77\\\\u6dc0\\\\u533a\",\"sort\":0}]," +
            "\"im:api\":\"http:\\\\/\\\\/tsplus.zhibocloud.cn:9900\",\"im:helper\":[{\"uid\":137,\"url\":\"http:\\\\/\\\\/www.thinksns.com\"}]," +
            "\"im:serve\":\"ws:\\\\/\\\\/tsplus.zhibocloud.cn:9900\",\"wallet:ratio\":1000,\"wallet:recharge-type\":[\"alipay\"," +
            "\"alipay_pc_direct\",\"applepay_upacp\",\"alipay_wap\",\"alipay_qr\",\"wx\",\"wx_wap\"],\"ad\":[{\"id\":56,\"space_id\":1," +
            "\"title\":\"ThinkSNS\\\\u54c1\\\\u724c10\\\\u5468\\\\u5e74\\\\u5e86\",\"type\":\"image\",\"data\":{\"image\":\"http:\\\\/\\\\/tsplus" +
            ".zhibocloud.cn\\\\/api\\\\/v2\\\\/files\\\\/5825\",\"link\":\"http:\\\\/\\\\/www.thinksns.com\\\\/reader\\\\/163.html\"," +
            "\"duration\":2},\"sort\":0,\"created_at\":\"2017-12-25 10:27:57\",\"updated_at\":\"2017-12-26 02:46:58\"},{\"id\":23,\"space_id\":1," +
            "\"title\":\"\\\\u53cc11\\\\u6574\\\\u6708\\\\u72c2\\\\u6b22\\\\u8d2d\\\\uff0c\\\\u53cc\\\\u798f\\\\u5229\\\\u6765\\\\u88ad\\\\uff01" +
            "\\\\uff01\\\\uff01\",\"type\":\"image\",\"data\":{\"image\":\"http:\\\\/\\\\/tsplus.zhibocloud.cn\\\\/api\\\\/v2\\\\/files\\\\/4563\"," +
            "\"link\":\"http:\\\\/\\\\/tsplus.zhibocloud.cn\\\\/news\\\\/214\",\"title\":\"\",\"duration\":2},\"sort\":1," +
            "\"created_at\":\"2017-09-30 09:03:47\",\"updated_at\":\"2017-11-09 03:32:19\"},{\"id\":1,\"space_id\":1," +
            "\"title\":\"\\\\u5e7f\\\\u544a1\",\"type\":\"image\",\"data\":{\"image\":\"http:\\\\/\\\\/tsplus.zhibocloud" +
            ".cn\\\\/api\\\\/v2\\\\/files\\\\/3555\",\"link\":\"http:\\\\/\\\\/tsplus.zhibocloud.cn\\\\/news\\\\/154\",\"title\":\"\"," +
            "\"duration\":2},\"sort\":2,\"created_at\":\"2017-07-27 15:09:15\",\"updated_at\":\"2017-11-09 03:53:30\"}]," +
            "\"site\":{\"gold\":{\"status\":true},\"reward\":{\"status\":true,\"amounts\":\"10,50,100\"},\"reserved_nickname\":\"root,admin,TS," +
            "thinksns,thinksns+\",\"client_email\":\"admin@123.com\"," +
            "\"user_invite_template\":\"\\\\u6211\\\\u53d1\\\\u73b0\\\\u4e86\\\\u4e00\\\\u4e2a\\\\u5168\\\\u5e73\\\\u53f0\\\\u793e\\\\u4ea4" +
            "\\\\u7cfb\\\\u7edfThinkSNS+\\\\uff0c\\\\u5feb\\\\u6765\\\\u52a0\\\\u5165\\\\u5427\\\\uff1ahttp:\\\\/\\\\/t.cn\\\\/RpFfbbi\"," +
            "\"anonymous\":{\"status\":false,\"rule\":null},\"status\":true," +
            "\"off_reason\":\"\\\\u7ad9\\\\u70b9\\\\u7ef4\\\\u62a4\\\\u4e2d\\\\u8bf7\\\\u7a0d\\\\u540e\\\\u518d\\\\u8bbf\\\\u95ee\"," +
            "\"app\":{\"status\":true},\"h5\":{\"status\":true},\"gold_name\":{\"name\":\"\\\\u8d1d\\\\u58f3\",\"unit\":\"\\\\u4e2a\"}," +
            "\"currency_name\":{\"name\":\"\\\\u79ef\\\\u5206\",\"unit\":\"\"}},\"registerSettings\":{\"rules\":\"close\",\"method\":\"all\"," +
            "\"type\":\"all\",\"content\":\"\\\\u4e00\\\\u3001\\\\u7279\\\\u522b\\\\u63d0\\\\u793a\\n\\\\u5728\\\\u6b64\\\\u7279\\\\u522b\\\\u63d0" +
            "\\\\u9192\\\\u60a8\\\\uff08\\\\u7528\\\\u6237\\\\uff09\\\\u5728\\\\u6ce8\\\\u518c\\\\u6210\\\\u4e3aThinkSNS+\\\\u7528\\\\u6237" +
            "\\\\u4e4b\\\\u524d\\\\uff0c\\\\u8bf7\\\\u8ba4\\\\u771f\\\\u9605\\\\u8bfb\\\\u672c\\\\u300aThinkSNS+\\\\u7528\\\\u6237\\\\u670d" +
            "\\\\u52a1\\\\u534f\\\\u8bae\\\\u300b\\\\uff08\\\\u4ee5\\\\u4e0b\\\\u7b80\\\\u79f0\\\\u201c\\\\u534f\\\\u8bae\\\\u201d\\\\uff09" +
            "\\\\uff0c\\\\u786e\\\\u4fdd\\\\u60a8\\\\u5145\\\\u5206\\\\u7406\\\\u89e3\\\\u672c\\\\u534f\\\\u8bae\\\\u4e2d\\\\u5404\\\\u6761" +
            "\\\\u6b3e\\\\u3002\\\\u8bf7\\\\u60a8\\\\u5ba1\\\\u614e\\\\u9605\\\\u8bfb\\\\u5e76\\\\u9009\\\\u62e9\\\\u63a5\\\\u53d7\\\\u6216" +
            "\\\\u4e0d\\\\u63a5\\\\u53d7\\\\u672c\\\\u534f\\\\u8bae\\\\u3002\\\\u9664\\\\u975e\\\\u60a8\\\\u63a5\\\\u53d7\\\\u672c\\\\u534f" +
            "\\\\u8bae\\\\u6240\\\\u6709\\\\u6761\\\\u6b3e\\\\uff0c\\\\u5426\\\\u5219\\\\u60a8\\\\u65e0\\\\u6743\\\\u6ce8\\\\u518c\\\\u3001" +
            "\\\\u767b\\\\u5f55\\\\u6216\\\\u4f7f\\\\u7528\\\\u672c\\\\u534f\\\\u8bae\\\\u6240\\\\u6d89\\\\u670d\\\\u52a1\\\\u3002\\\\u60a8" +
            "\\\\u7684\\\\u6ce8\\\\u518c\\\\u3001\\\\u767b\\\\u5f55\\\\u3001\\\\u4f7f\\\\u7528\\\\u7b49\\\\u884c\\\\u4e3a\\\\u5c06\\\\u89c6" +
            "\\\\u4e3a\\\\u5bf9\\\\u672c\\\\u534f\\\\u8bae\\\\u7684\\\\u63a5\\\\u53d7\\\\uff0c\\\\u5e76\\\\u540c\\\\u610f\\\\u63a5\\\\u53d7" +
            "\\\\u672c\\\\u534f\\\\u8bae\\\\u5404\\\\u9879\\\\u6761\\\\u6b3e\\\\u7684\\\\u7ea6\\\\u675f\\\\u3002 \",\"showTerms\":true," +
            "\"fixed\":\"need\"},\"wallet:cash\":{\"open\":true},\"wallet:recharge\":{\"open\":true},\"wallet:transform\":{\"open\":true}," +
            "\"currency:cash\":{\"open\":true},\"currency:recharge\":{\"open\":true,\"IAP_only\":true},\"question:apply_amount\":200," +
            "\"question:onlookers_amount\":10,\"plus-appversion\":{\"open\":true},\"checkin\":true,\"checkin:attach_balance\":10," +
            "\"feed\":{\"reward\":true,\"paycontrol\":true,\"items\":[100,5000,1000],\"limit\":50},\"news:contribute\":{\"pay\":true," +
            "\"verified\":true},\"news:pay_conyribute\":0,\"group:create\":{\"need_verified\":false},\"group:reward\":{\"status\":true}}";
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
