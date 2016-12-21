# baseproject module的概述

##base目录：TSActivity和TSFragment类，继承自common下的相关基类

- 项目主工程中的所有activity和fragment的父类

##config目录：
- UmengConfig类：配置友盟第三方的相关key和secret，目前包括qq，微信，新浪（含回调地址）

##impl.share目录：
- UmengSharePolicyImpl类：实现了友盟分享功能

##utils.imageloader目录：
- GlideConfiguration类：自定义的GlideModule,对Glide缓存大小进行配置
- GlideImageConfig类：继承自ImageConfig类，封装Glide的图片加载信息
- GlideImageLoaderStrategy类：实现ImageLoaderStrategy接口，GLide加载图片的方法

