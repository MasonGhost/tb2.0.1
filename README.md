# thinksns-plus-android
Thinksns plus Android code repository.

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


