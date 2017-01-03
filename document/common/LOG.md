2017年1月3日 17:43:34
`LogUtils`基于[Logger](https://github.com/orhanobut/logger) 构建的日志多等级显示.

## 概览

`Logger`提供
- 线程信息
- 类信息
- 方法信息
- 漂亮的json内容打印
- 漂亮打印新行“\ n”
- 清洁输出
- 跳转到源


## 使用说明

日志配置:

```
public class LogUtils {
    private static final String APPLICATION_TAG = "LogUtils";// 日志打印默认tag
    public static final int LOGGER_METHODCOUNT = 3;
    public static final int LOGGER_METHODOFFSET = 2;

    public static void init() {
        Logger
                .init(APPLICATION_TAG)           // default PRETTYLOGGER or use just init()
                .methodCount(LOGGER_METHODCOUNT)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(BuildConfig.USE_LOG?LogLevel.FULL:LogLevel.NONE)        // debug时进行日子打印，release不会打印日志
                .methodOffset(LOGGER_METHODOFFSET);              // default 0
        // .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }
    ｝
```

需要输入某些信息到控制台时,使用以下方法:

```java
LogUtils.d("hello");
LogUtils.e("hello");
LogUtils.w("hello");
LogUtils.v("hello");
LogUtils.wtf("hello");
LogUtils.json(JSON_CONTENT);
LogUtils.xml(XML_CONTENT);
LogUtils.log(DEBUG, "tag", "message", throwable);

```

## 注意事项

本工程只允许使用该日志输出方式,方便统一配置和管理.
`BuildConfig.USE_LOG`配置在`common`下的`build.gradle`中修改
```grovy
    release {
            buildConfigField "boolean", "USE_LOG", "true"
            buildConfigField "boolean", "USE_CANARY", "true"
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
        debug {
            buildConfigField "boolean", "USE_LOG", "true" //在Module做为library时，只会使用release中的
            buildConfigField "boolean", "USE_CANARY", "true"
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
```