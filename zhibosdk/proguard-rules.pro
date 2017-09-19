
-optimizationpasses 5


-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontskipnonpubliclibraryclassmembers

-dontpreverify
-dontshrink
-verbose
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#推流
-keep class com.pili.pldroid.** { *; }
-keep interface com.pili.pldroid.** { *; }
-keep class com.qiniu.pili.droid.**{*;}
-keep interface com.qiniu.pili.droid.**{*;}
#播放器
-keep class com.pili.pldroid.player.** { *; }
-keep class tv.danmaku.ijk.media.player.** {*;}
-keep class com.pili.pldroid.** {*;}

-keep interface com.qiniu.pili.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.zhiyicx.zhibosdk.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

################imsdk###############
-keep class com.zhiyicx.imsdk.entity.** { *; } #实体类不参与混淆
-keep class com.zhiyicx.imsdk.builder.** {
   public <methods>;
 }
-keep class com.zhiyicx.imsdk.service.ImService {
    public static <fields>;
    public ImService(***);
 }
 -keep class com.zhiyicx.imsdk.service.SocketService {
     public static <fields>;
  }
 -keep class com.zhiyicx.imsdk.manage.** {
    public <methods>;
  }


-keep interface com.zhiyicx.imsdk.manage.listener.** { *; } #实体类不参与混淆

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep public class * extends android.app.Service
-keep public class * extends android.preference.Preference
-keepnames class * implements java.io.Serializable
-keep public class * extends android.content.BroadcastReceiver
-keep class **.R$* {*;}
-ignorewarnings
-keepclassmembers class **.R$* {
    public static <fields>;
}

################gson###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.sunloto.shandong.bean.** { *; }

################androidEventBus###############
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}

################autobahn###############
-keep class com.zhiyicx.imsdk.de.tavendo.autobahn.** { *; }
-keep interface  com.zhiyicx.imsdk.de.tavendo.autobahn.** { *; }
-keepnames class com.zhiyicx.imsdk.de.tavendo.autobahn.WebSocket$* {
    public <fields>;
    public <methods>;
}


################messagepack###############
-keep class org.** { *; }
-keep interface org.** { *; }

################javassist###############
-keep class javassist.** { *; }
-keep interface javassist.** { *; }

-keep class  com.sun.jdi.**{*;}
-keep class  org.w3c.dom.bootstrap.**{*;}

-keep class  javax.tools.**{*;}

################zhibosdk###############
-keep interface com.zhiyicx.zhibosdk.policy.** { *; }
-keep class com.zhiyicx.zhibosdk.policy.impl.**{ *; }
 -keep class com.zhiyicx.zhibosdk.manage.** {
    public <methods>;
  }


 -keep class com.zhiyicx.zhibosdk.service.**{*;}

-keep class com.zhiyicx.zhibosdk.model.entity.** { *; } #实体类不参与混淆
-keep interface com.zhiyicx.zhibosdk.manage.** { *; }

-keep interface com.zhiyicx.zhibosdk.widget.** { *; }
-keep class com.zhiyicx.zhibosdk.widget.** { *; }

-keep class com.zhiyicx.zhibosdk.di.** { *; }
-keep interface com.zhiyicx.zhibosdk.di.** { *; }

-keep interface com.zhiyicx.zhibosdk.di.** { *; }

-keep class com.zhiyicx.zhibosdk.model.api.Baseclient{ *; }
-keep class com.zhiyicx.zhibosdk.model.api.RequestErroException{ *; }
-keep class com.zhiyicx.zhibosdk.model.api.RequestIntercept{ *; }
-keep class com.zhiyicx.zhibosdk.model.api.RetrofitClient{ *; }
-keep interface com.zhiyicx.zhibosdk.policy.** { *; }
-keep interface com.zhiyicx.zhibosdk.model.CloudApiModel{ *; }
-keep interface com.zhiyicx.zhibosdk.model.LivePlayModel{ *; }
-keep interface com.zhiyicx.zhibosdk.model.LoginModel{ *; }
-keep interface com.zhiyicx.zhibosdk.model.PublishModel{ *; }
-keep interface com.zhiyicx.zhibosdk.model.SplashModel{ *; }

-keep class com.zhiyicx.zhibosdk.model.imp.CloudApiModelImpl{  public <methods>;}
-keep class com.zhiyicx.zhibosdk.model.imp.LivePlayModelImpl{  public <methods>;}
-keep class com.zhiyicx.zhibosdk.model.imp.imp.LoginModelImpl{  public <methods>; }
-keep class com.zhiyicx.zhibosdk.model.imp.PublishModelImpl{  public <methods>; }
-keep class com.zhiyicx.zhibosdk.model.imp.SplashModelImpl{  public <methods>; }


-keep interface com.zhiyicx.zhibosdk.model.api.service.**{ *; }
-keep class com.zhiyicx.zhibosdk.model.api.service.**{ *; }


-keep class  com.zhiyicx.zhibosdk.ZBSmartLiveSDK{*;}




-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keep class **.R$* {*;}
-ignorewarnings
-keepclassmembers class **.R$* {
    public static <fields>;
}



################support###############
-keep class android.support.** { *; }
-keep interface android.support.** { *; }


################alipay###############

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

################retrofit###############
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions



################okhttp###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**



################Rxjava###############
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keep class rx.** { *; }
-keep interface rx.** { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class sun.misc.Unsafe { *; }

-dontwarn java.lang.invoke.*


################nineoldandroids###############
-keep class com.nineoldandroids.animation.** { *; }
-keep interface com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.view.** { *; }
-keep interface com.nineoldandroids.view.** { *; }


################crop###############
-keep class com.soundcloud.android.** { *; }
-keep interface com.soundcloud.android.** { *; }

################annotation###############
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

