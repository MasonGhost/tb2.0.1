# thinksns-plus-android

整个项目相关的决策工作安排等记录在[thinksns-plus-document](https://github.com/zhiyicx/thinksns-plus-document).

整个项目代码风格都遵守[智艺创想移动端开发代码风格指南](https://github.com/zhiyicx/mobile-devices-code-style-guide)
## 工程基础配置说明

该工程使用 java 语言编写.支持 Android 4.0 (api 15) 以上系统.

IDE 为 Android Studio 2.2 编辑器.

Gradle 版本

```grovry
    distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip

    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.0'
    }
```
#### gradle 全局配置

**注：** 所有三方依赖包统一写入`config.gradle`，gradle配置文件`config.gradle`,需要在主项目的`buid.grale`中进行声明 `apply from："config.gralde"`

### Git 忽略文件说明

本工程忽略文件配置位于主工程下 `.gitignore`文件，可手动修改配置内容

## 文档位置说明

本工程文档统一记录在`ThinksnsPlus/document`文件夹下.

## 分支使用说明

分支命名方式为`主分支名称_子分支名称_特性名称_作者名称`

示例:

```shell
master ( 主分支 )
master_release ( 发布名称 )
master_develop ( 开发名称 )
master_future_add_image ( 特性分支 )
master_future_add_image_lip ( 作者分支 )

```

**注意:**

`作者分支`和`作者分支`的子分支按照作者意愿合并,由`作者分支`作者负责管理.

因为远端仓库存储了大量的主版本和子版本分支,为了减少不必要的更新和下载操作,所以`作者分支`不允许推送到远端.

为了方便后续使用自动化工具筛检`commit`内容,规定每次提交和`issues`相关的代码时,使用统一格式: 在提交信息末尾空一格,空一格后记录下`commit`类型,然后再在英文符号的括号内填写`issues code`.默认的 commit 类型为: 代码,文档以及测试

    示例代码:```(#9527) 文档 提交信息```
    示例代码:```(#12 #30 #78) 测试 提交了测试信息```
    
## 框架选型

- 整体结构   [MVP+Dagger2](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/)

- 技术说明 ：  [retrofit](https://github.com/square/retrofit) + [dagger2](https://google.github.io/dagger/) + [rx](http://reactivex.io/)


###   why choose this?


| 框架 | 劣势 | 优势 |
|:-------------:|:-------------:|:-------------|
|MVP|接口过多|1.官方推荐；<br><br>2.代码结构清晰;<br><br>3.代码解耦|
|retrofit||1.Square提供，质量高，不断维护更新；<br><br>2.易扩展，提供不同的Converter、Intercept等实现（也可以自定义），同时提供RxJava支持(返回Observable对象)，配合Jackson(或者Gson)和RxJava使用；<br><br>3.网络请求简洁明了
|dagger2||1.google官方推荐，配合mvp;<br><br>2.依赖注入，解耦代码；<br><br>3.简化对象的创建以及管理|
|rx||1.使用干净的输入/输出函数;<br><br>2.减少代码行数；<br><br>3.异步的错误处理；<br><br>4.便于线程操作|


## 主要模块介绍

[app](document/app/APP.md) app 主工程

[baseproject](document/baseproject/BASEPROJECT.md) 项目基类定义，和各种配置信息

[common](document/common/COMMON.md) 基础框架包

2016年12月23日 11:55:58
