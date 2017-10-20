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
    public static final String DEFAULT_SYSTEM_CONFIG ="{\"im:helper\":[{\"uid\":\"2\",\"url\":\"https://plus.io/users/1\"}],\"im:serve\":\"114.215.203.142:9900\",\"wallet:ratio\":100,\"wallet:recharge-type\":[\"alipay_wap\",\"alipay_pc_direct\",\"wx_wap\",\"wx\",\"alipay\",\"alipay_qr\"],\"ad\":[{\"id\":1,\"space_id\":1,\"title\":\"bbbb\",\"type\":\"image\",\"data\":{\"image\":\"http://192.168.2.200/api/v2/files/24\",\"link\":\"http://www.baidu.com\",\"title\":\"\"},\"sort\":1,\"created_at\":\"2017-09-25 07:32:12\",\"updated_at\":\"2017-09-25 07:32:12\"},{\"id\":2,\"space_id\":1,\"title\":\"我的宝马\",\"type\":\"image\",\"data\":{\"image\":\"http://192.168.2.200/api/v2/files/25\",\"link\":\"https://www.bmw.com\",\"title\":\"\",\"duration\":\"3\"},\"sort\":1,\"created_at\":\"2017-09-25 07:43:38\",\"updated_at\":\"2017-09-25 07:50:13\"}],\"site\":{\"status\":true,\"off_reason\":\"站点维护中请稍后再访问\",\"app\":{\"status\":true},\"h5\":{\"status\":true},\"reserved_nickname\":\"root,admin\",\"client_email\":\"admin@123.com\",\"gold\":{\"status\":true},\"reward\":{\"status\":true,\"amounts\":\"5,10,15\"},\"user_invite_template\":\"我发现了一个全平台社交系统ThinkSNS+，快来加入吧：http://t.cn/RpFfbbi\",\"gold_name\":{\"name\":\"豆子\",\"unit\":\"个\"}},\"registerSettings\":{\"showTerms\":false,\"registerMode\":\"all\",\"completeData\":false,\"accountType\":\"all\",\"content\":\"不让注册\",\"open\":true,\"rules\":\"open\",\"method\":\"mail-only\",\"fixed\":\"no-need\",\"type\":\"thirdPart\"},\"question:apply_amount\":200,\"question:onlookers_amount\":100,\"feed\":{\"reward\":false,\"paycontrol\":true},\"plus-appversion\":{\"open\":true},\"news:contribute\":{\"pay\":false,\"verified\":false},\"news:pay_conyribute\":0}";
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
