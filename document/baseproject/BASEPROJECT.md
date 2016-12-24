2016年12月24日17:07:52
# baseproject module
结构目录
 ```
 --base
    ...
 --config
    ...
 --impl
    --share
    --imageloader
 ```
 ts项目的基类包，主工程是在此基础上直接进行开发的


### base目录：
    包含项目主工程中的所有activity和fragment的父类

### config目录：
    项目中可能用到的一些配置数据，例如网络请求地址，第三方分享key

### impl目录：
    基于common包下的接口实现，一般都是一些框架替换，比如第三方分享
   - share目录：实现SharePolicy接口，当前完成[友盟分享的功能](THIRDSHARE.md)
   - imageloader：试下你ImageLoaderStrategy接口，当前完成[gilde加载图片](IMAGELOADER.md)的功能

### 其他：
   - [app启动优化](APPLAUNCHEROPTIMIZE.md)

