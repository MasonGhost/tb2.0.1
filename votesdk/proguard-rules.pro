
-optimizationpasses 5


-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontskipnonpubliclibraryclassmembers

-dontpreverify
-dontshrink
-verbose
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-keep class **.R$* {*;}
-ignorewarnings
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep public class * extends android.widget.PopupWindow
-keep public class * extends android.widget.ProgressBar
-keep public class * extends android.widget.LinearLayout
-keep public class * extends android.widget.RelativeLayout
-keep class * extends java.util.TimerTask
-keep public class android.widget.**
-keep class android.content.Context

-keep class com.zhiyicx.imsdk.entity.**
-keep class com.zhiyicx.imsdk.utils.**{*;}

-keep class com.zhiyicx.votesdk.entity.** { *; }
-keep interface com.zhiyicx.votesdk.listener.** { *; }
-keep class com.zhiyicx.votesdk.listener.** { *; }
-keep class com.zhiyicx.votesdk.utils.** { *; }

-keep interface com.zhiyicx.votesdk.policy.** { *; }
-keep class android.content.Context
-keep abstract class com.zhiyicx.votesdk.policy.** { *; }
-keepattributes InnerClasses
-keep class com.zhiyicx.votesdk.manage.**{
 public static <fields>;
 public <methods>;
}
-keep interface com.zhiyicx.votesdk.ui.**{*;}
-keep class com.zhiyicx.votesdk.ui.**{*;}

#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }

-keep public class com.zhiyicx.zhibosdk.R$*{
public static final int *;
}
-dontwarn com.zhiyicx.zhibosdk.**
-dontwarn com.zhiyicx.imsdk.**

-keep class com.zhiyicx.zhibosdk.**{*;}
-keep class com.zhiyicx.imsdk.**{*;}

-keep interface com.zhiyicx.zhibosdk.**{*;}
-keep interface com.zhiyicx.imsdk.**{*;}





#如果引用了v4或者v7包
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep class android.support.annotation.**{*;}
-keep interface android.support.annotation.**{*;}
-keep interface android.support.** { *; }
-keepattributes *Annotation*
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }
-keep class * extends java.lang.annotation.Annotation {*;}
-keep class * extends android.support.**



-dontwarn java.lang.invoke.*







-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keep class org.json.**

