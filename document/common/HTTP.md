# 网络请求说明

 ##  1.概述
 - 目的

    实现网络数据获取

 ## 2.定义
 - CommonRequestIntercept 可以定义统一请求内容（如：token，secret等）；
 - RequestIntercept 网络回掉拦截器；
 - RequestInterceptListener 网络拦截回掉，可做统一处理


 ## 3.使用
 ### Api配置
  - baseproject/config/ApiConfig 网络根域名、api接口配置

 ###


 ### 使用说明
 主项目中的UmengSharePolicyImpl是TSApplication中创建，dagger进行管理

 使用
 ```java
 Inject
 SharePolicy mSharePolicy;

 ShareContent shareContent=new ShareContent();// 内容自己创建；
 ...
 mSharePolicy.setShareContent(shareContent);

 // 显示统一分享框
 mSharePolicy.showShare();
 // 分享到qq
 mSharePolicy.shareQQ();
 // 分享到微信
 mSharePolicy.shareWechat();
 ...
 ```
 **注意：**   如果使用的是 qq 或者新浪精简版 jar，需要在您使用分享或授权的 Activity（ fragment 不行）中添加如下回调代码
 ```java
 UmengSharePolicyImpl. onActivityResult(int requestCode, int resultCode, Intent data,Context context);
 ```

 ## 4.逻辑描述

 - 扩展实现：

 如果需要替换成其他的分享平台，比如shareSdk，需要遵循以上规范,实现SharePolicy

 2016年12月22日 18:59:39



