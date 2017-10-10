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
     * }
     */
    public static final String DEFAULT_SYSTEM_CONFIG = "{\"im:serve\":\"114.215.203.142:9900\",\"im:helper\":[{\"uid\":\"2\",\"url\":\"https://plus.io/users/1\"}]}";

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
