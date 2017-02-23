# 分享功能

##  1.概述
   主项目基于 Android U-share 6.0sdk 实现，根据扩展可替换成其他三方分享
   - 目的

   实现了分享（文字、图片、连接、文件）内容到QQ、QZone、微信、朋友圈、Sina微博

## 2.定义
- ShareContent 定义了分享的内容；
- Share 枚举类定义了调用第三方时，使用的平台类型；
- OnShareCallbackListener 定义了三方分享回调接口
- SharePolicy 三方分享接口


## 3.使用
### 配置
 - baseproject/config/UmengConfig 中配置三方key、secret
 - baseproject/build.gradle 中配置[友盟](http://dev.umeng.com/social/android/quick-integration)key,QQ的appId
 - baseproject/AndroidManifest.xml 中配置权限和三方使用的Activity

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

2016年12月24日10:08:01



