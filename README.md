# thinksns-plus-android
Thinksns plus Android code repository.

该工程使用 java 语言编写.支持 Android 4.0 (api 15) 以上系统.

IDE 为 Android Studio 2.2 编辑器.

Gradle

```grovry
    distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip

    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.0'
    }
```
整体结构   MVP

技术说明 ：  retrofit + dagger2 + rx +relm

#### gradle 全局配置
gradle配置文件`config.gradle`,需要在主项目的`buid.grale`中进行声明 `apply from："config.gralde"`
