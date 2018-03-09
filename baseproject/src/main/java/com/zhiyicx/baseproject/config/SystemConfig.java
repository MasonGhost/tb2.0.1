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
    public static final String DEFAULT_SYSTEM_CONFIG ="{\n" +
            "    \"server:version\":\"1.7.0\",\n" +
            "    \"hots_area\":[\n" +
            "        {\n" +
            "            \"name\":\"中国 四川省 成都市\",\n" +
            "            \"sort\":10\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"中国 北京市 北京市\",\n" +
            "            \"sort\":0\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"中国 上海市 上海市\",\n" +
            "            \"sort\":0\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"中国 北京市 海淀区\",\n" +
            "            \"sort\":0\n" +
            "        }\n" +
            "    ],\n" +
            "    \"im:api\":\"http://tsplus.zhibocloud.cn:9900\",\n" +
            "    \"im:helper\":[\n" +
            "        {\n" +
            "            \"uid\":137,\n" +
            "            \"url\":\"http://www.thinksns.com\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"im:serve\":\"ws://tsplus.zhibocloud.cn:9900\",\n" +
            "    \"wallet:ratio\":1000,\n" +
            "    \"wallet:recharge-type\":[\n" +
            "        \"alipay\",\n" +
            "        \"alipay_pc_direct\",\n" +
            "        \"alipay_wap\"\n" +
            "    ],\n" +
            "    \"ad\":[\n" +
            "        {\n" +
            "            \"id\":66,\n" +
            "            \"space_id\":1,\n" +
            "            \"title\":\"ThinkSNS春节特惠价，限时抢购！\",\n" +
            "            \"type\":\"image\",\n" +
            "            \"data\":{\n" +
            "                \"image\":\"https://tsplus.zhibocloud.cn/api/v2/files/6557\",\n" +
            "                \"link\":\"https://tsplus.zhibocloud.cn/news/306\",\n" +
            "                \"duration\":2\n" +
            "            },\n" +
            "            \"sort\":0,\n" +
            "            \"created_at\":\"2018-02-06 06:54:43\",\n" +
            "            \"updated_at\":\"2018-02-06 06:54:43\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\":81,\n" +
            "            \"space_id\":1,\n" +
            "            \"title\":\"ThinkSNS＋第3阶段更新发布\",\n" +
            "            \"type\":\"image\",\n" +
            "            \"data\":{\n" +
            "                \"image\":\"https://tsplus.zhibocloud.cn/api/v2/files/6647\",\n" +
            "                \"link\":\"https://tsplus.zhibocloud.cn/news/308\",\n" +
            "                \"duration\":2\n" +
            "            },\n" +
            "            \"sort\":1,\n" +
            "            \"created_at\":\"2018-02-08 08:00:27\",\n" +
            "            \"updated_at\":\"2018-02-08 08:00:27\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"site\":{\n" +
            "        \"gold\":{\n" +
            "            \"status\":true\n" +
            "        },\n" +
            "        \"reward\":{\n" +
            "            \"status\":true,\n" +
            "            \"amounts\":\"10,50,100\"\n" +
            "        },\n" +
            "        \"reserved_nickname\":\"root,admin,TS,thinksns,thinksns+\",\n" +
            "        \"client_email\":\"admin@123.com\",\n" +
            "        \"user_invite_template\":\"我发现了一个全平台社交系统ThinkSNS+，快来加入吧：http://t.cn/RpFfbbi\",\n" +
            "        \"anonymous\":{\n" +
            "            \"status\":true,\n" +
            "            \"rule\":\"你是匿名，不能改为实名，真的要这样做？\"\n" +
            "        },\n" +
            "        \"about_url\":null,\n" +
            "        \"status\":true,\n" +
            "        \"off_reason\":\"站点维护中请稍后再访问\",\n" +
            "        \"app\":{\n" +
            "            \"status\":true\n" +
            "        },\n" +
            "        \"h5\":{\n" +
            "            \"status\":true\n" +
            "        },\n" +
            "        \"background\":{\n" +
            "            \"logo\":\"https://tsplus.zhibocloud.cn/plus.png\"\n" +
            "        },\n" +
            "        \"gold_name\":{\n" +
            "            \"name\":\"元\",\n" +
            "            \"unit\":\"\"\n" +
            "        },\n" +
            "        \"currency_name\":{\n" +
            "            \"id\":1,\n" +
            "            \"name\":\"积分\",\n" +
            "            \"unit\":\"\",\n" +
            "            \"enable\":1\n" +
            "        }\n" +
            "    },\n" +
            "    \"registerSettings\":{\n" +
            "        \"rules\":\"close\",\n" +
            "        \"method\":\"all\",\n" +
            "        \"type\":\"all\",\n" +
            "        \"content\":\"特别提示在此特别提醒您（用户）在注册成为ThinkSNS+用户之前，请认真阅读本《ThinkSNS+用户服务协议》（以下简称“协议”），确保您充分理解本协议中各条款。\",\n" +
            "        \"showTerms\":true,\n" +
            "        \"fixed\":\"need\"\n" +
            "    },\n" +
            "    \"wallet:cash\":{\n" +
            "        \"open\":true\n" +
            "    },\n" +
            "    \"wallet:recharge\":{\n" +
            "        \"open\":true\n" +
            "    },\n" +
            "    \"wallet:transform\":{\n" +
            "        \"open\":true\n" +
            "    },\n" +
            "    \"currency:cash\":{\n" +
            "        \"open\":true\n" +
            "    },\n" +
            "    \"currency:recharge\":{\n" +
            "        \"open\":true,\n" +
            "        \"IAP_only\":false\n" +
            "    },\n" +
            "    \"question:apply_amount\":200,\n" +
            "    \"question:onlookers_amount\":10,\n" +
            "    \"plus-appversion\":{\n" +
            "        \"open\":true\n" +
            "    },\n" +
            "    \"checkin\":true,\n" +
            "    \"checkin:attach_balance\":10,\n" +
            "    \"feed\":{\n" +
            "        \"reward\":true,\n" +
            "        \"paycontrol\":true,\n" +
            "        \"items\":[\n" +
            "            100,\n" +
            "            5000,\n" +
            "            1000\n" +
            "        ],\n" +
            "        \"limit\":50\n" +
            "    },\n" +
            "    \"news:contribute\":{\n" +
            "        \"pay\":true,\n" +
            "        \"verified\":true\n" +
            "    },\n" +
            "    \"news:pay_conyribute\":0,\n" +
            "    \"group:create\":{\n" +
            "        \"need_verified\":true\n" +
            "    },\n" +
            "    \"group:reward\":{\n" +
            "        \"status\":true\n" +
            "    }\n" +
            "}";
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
