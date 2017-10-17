# 二次开发资料替换说明文档



### 包名应用签名替换
1. 包名修改 ，在项目路径下config.gradle文件中把 `com.zhiyicx.thinksnsplus` 修改自己的包名名称（路径地址：`thinksns-system-android\config.gradle` ）。
    > 1. 必须提供自己的包名名称。
    > 2. 包名规则：采用反写域名命名规则，即 `com.xx.xxx.xxxx` 形式，全部使用小写字母。一级包名为com，二级包名为xx（一般为公司或个人域名），三级包名根据应用进行命名，四级包名为功能模块名。如：`com.tencent.qq.activitys` ，这样具备较高可读性，一看就知道是腾讯公司QQ软件中存放activity的包。

    [![config_capture]][包名配置]

2. 签名文件分为调试版和正式发布版，生成自己的签名文件后替换图中所示文件（**注：** 必须提供自己的签名文件和对应的别名以及密码）[查看创建签名文档](AndroidCreateSignatureFileTutorial.md)。

    [![signature_show]][包名配置]

3. 配置编译时签名，`app/build.gradle`中
    ```
    apply plugin: 'com.android.application'

    ...
    android {
      ...
      signingConfigs {
           release {
                storeFile file('../thinksnsplus.jks')
                storePassword '........'
                keyAlias 'thinksnsplus'
                keyPassword '.........'
        }
           debug {
                storeFile file('../thinksnsplus.jks')
                storePassword '.........'
                keyAlias 'thinksnsplus'
                keyPassword '.........'
        }
    }
    ...

    ```

### 第三方账号配置

 项目使用中的三方账号包括：友盟、新浪微博、微信、QQ、极光推送、高德地图、支付宝支付。请在相应平台申请后修改配置文件。

1. 友盟（Umeng）配置 ，修改友盟 `UMENG_APPKEY`，位于 `baseproject/build.gradle`
    ```
    apply plugin: 'com.android.library'
    android {
    ...

            // 友盟三方key配置
            manifestPlaceholders = [
                    ...
                    UMENG_APPKEY: "58d0998e9f06fd05680011f6",
                    ...
            ]

        }
    ...

    ```
2. 高德地图配置 ，修改高德 `AMAP_APPKEY`，位于 `baseproject/build.gradle`
    ```
    apply plugin: 'com.android.library'
    android {
    ...


            manifestPlaceholders = [
                    ...
                    AMAP_APPKEY: "e954e82021efe2f4d334879cff3880cf", // 高德三方配置

            ]

        }
    ...

    ```
3. 极光推送配置，位于 `app/build.gradle`, 极光推送回调请查看 `app/src/main/java/jpush`
    ```
    apply plugin: 'com.android.library'
    android {
    ...

            // 极光推送
                 manifestPlaceholders = [
                         JPUSH_PKGNAME: applicationId,
                         JPUSH_APPKEY : "85f8e1049e913108b9d9bc67", //JPush上注册的包名对应的appkey.
                         JPUSH_CHANNEL: "developer-default", //暂时填写默认值即可.
                 ]

        }
    ...

    ```
4. QQ、Sina、Wixin 三方账号 key 配置。位于 `baseproject/src/main/java/com.zhiyicx.baseproject/config/UmengConfig.class`。 QQ 的 `QQ_APPID` 也需要在`baseproject/build.gradle`中配置
    ```
    package com.zhiyicx.baseproject.config;

    /**
     * @Describe 友盟三方帐号配置
     * @Author Jungle68
     * @Date 2016/12/21
     * @Contact master.jungle68@gmail.com
     */

    public class UmengConfig {
        // QQ
        public static String QQ_APPID = "1105978541";
        public static String QQ_SECRETKEY = "Q47tAluWzkd0v4Rp";
        // 微信
        public static String WEIXIN_APPID = "wx970d230ad5ab3b23";
        public static String WEIXIN_SECRETKEY = "b2a61add26e0ef3c4bf24ac9387a02d2";
        // 新浪
        public static String SINA_APPID = "732480598";
        public static String SINA_SECRETKEY = "51c1d72c618224a469531d39fa313ec7";
        public static String SINA_RESULT_RUL="https://sns.whalecloud.com/sina2/callback";
    }

    ```

    ```

    apply plugin: 'com.android.library'
    android {
    ...
        // 三方 key
        manifestPlaceholders = [
                TECENT_APPID: 1105978541, // 腾讯 QQ id
    ...
        ]

    }

    ```

     **注意：** 分享和支付回掉需要根据您的包名来判断存放位置，例如：您的包名是 `com.guduk.www`,那么您需要在 `app/main/java/` 创建自己的包名路径，并将 `包wxapi` 下的代码和`WBShareActivity`移动过去。

    [![share_packge_change]][包名配置]

### 服务器地址
1. 服务器地址与接口地址都位置 `baseproject/src/main/java/config/Apiconfig.class` 中。修改 `APP_DOMAIN_FORMAL` 即可。
    ```
    public class ApiConfig {
       ...
        /**
         * 网络根地址  http://192.168.10.222/
         * 测试服务器：http://192.168.2.222:8080/mockjs/2/test-get-repose-head-normal?
         */

        //public static final String APP_DOMAIN = "http://192.168.2.222:8080/mockjs/2/";// rap 测试服务器

        public static final boolean APP_IS_NEED_SSH_CERTIFICATE = true;// 自定义证书时使用false
        //        public static final String APP_DOMAIN = "https://plus.medz.cn/";// 在线测试服务器 2
        public static final String APP_DOMAIN_DEV = "http://dev.zhibocloud.cn/";// 模拟在线正式服务器
        public static final String APP_DOMAIN_TEST = "http://test-plus.zhibocloud.cn/";// 在线测试服务器
        public static final String APP_DOMAIN_FORMAL = "https://tsplus.zhibocloud.cn/";// 正式服务器
        public static final String APP_DOMAIN_FOR_TEARCHER_QIAO = "http://192.168.2.200/";// 乔老师本地服务器

        public static String APP_DOMAIN = APP_DOMAIN_FORMAL;

        public static final String URL_ABOUT_US = "api/" + API_VERSION_2 + "/aboutus";// 关于我们网站
        public static final String URL_JIPU_SHOP = "http://demo.jipukeji.com";// 极铺购物地址
        ...

    ```

### APP图标替换






--------------------------------
[config_capture]:../image/config_capture.png "包名配置"
[signature_show]:../image/signature_show.jpeg "包名配置"
[share_packge_change]:../image/share_packge_change.jpeg "包名配置"