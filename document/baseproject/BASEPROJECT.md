# baseproject module
结构目录
 ```
 --base
    TSWebFragment.java
    ...
 --cache
 --config
    ...
 --crashhandler
 --impl
    --share
    --photoselector
    --imageloader
 ```
 ts项目的基类包，主工程是在此基础上直接进行开发的

### base目录：

      常用基类

- [包含项目主工程中的所有activity和fragment的父类](BASEACTIVITY.md)
- [基础浏览器](TSWEBFRAGMENT.md)
- [基础列表](TSLISTFRAGMENT.md)

### [cache目录](CACHE.md)
      通过Rxjava实现多级缓存
### config目录：
    项目中可能用到的一些配置数据，例如网络请求地址，第三方分享key
### crashhandler目录：
    程序异常崩溃的处理
### impl目录：
    基于common包下的接口实现，一般都是一些框架替换，比如第三方分享
   - share目录：实现SharePolicy接口，当前完成[友盟分享的功能](THIRDSHARE.md)
   - imageloader：试下你ImageLoaderStrategy接口，当前完成[gilde加载图片](IMAGELOADER.md)的功能
   - [photoselector图片选择的整合](PHOTOSELECTOR.md)：本地图片的选择，通过相册或者相机拍照获取图片，并进行裁剪，整合了[ucrop裁剪](UCROP.md)和[photoPicker图片选择](PHOTOPICKER.md)
### widget目录：
    一些为TS+项目定制的常用组件
   - [TabSelectView:ToolBar中集成Indicator](TABSELECTVIEW.md)
### 其他：
   - [app启动优化](APPLAUNCHEROPTIMIZE.md)
   - [动态工具栏](DYNAMICMENU.md)

2017年2月14日15:43:39