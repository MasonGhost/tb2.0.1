# Lint 检测说明

    Lint 工具可检查您的 Android 项目源文件是否包含潜在错误，以及在正确性、安全性、性能、易用性、便利性和国际化方面是否需要优化改进。
    目前使用的是 Android 自带的 lint 规则 ；了解更多可以查看 [Android Studio 开发指南](https://developer.android.google.cn/studio/write/lint.html)

## Lint 报告
在 `Terminal` 中执行`gradlew.bat lint`即可,日志报告会在对应`module`中的`build/outputs/`中.

注意：为防止 `lint` 错误导致的检测中断，需要在gradle.build中增加
```grovvy
  lintOptions {
     abortOnError false
   }
```

### 推荐文章
 - [Android Lint Checks](http://tools.android.com/tips/lint-checks)
 - [如何自定义Lint规则](https://github.com/Jungle68/android-tech-frontier/blob/master/issue-33/%E5%A6%82%E4%BD%95%E8%87%AA%E5%AE%9A%E4%B9%89Lint%E8%A7%84%E5%88%99.md)