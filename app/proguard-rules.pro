#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

################baseprojectw###############
-keep public class com.zhiyicx.baseproject.R$*{
public static final int *;
}
-keep class com.zhiyicx.baseproject.** { *; }
-keep interface  com.zhiyicx.baseproject.** { *; }

################common###############
-keep public class com.zhiyicx.common.R$*{
public static final int *;
}

-keep interface  com.zhiyicx.common.** { *; }

-keep class com.zhiyicx.common.base.BaseJson { *; } #实体类不参与混淆
-keep class com.zhiyicx.common.base.BaseJsonV2 { *; } #实体类不参与混淆
-keep class com.zhiyicx.common.net.** { *; } #实体类不参与混淆
-keep class com.zhiyicx.common.utils.** { *; } #实体类不参与混淆
-keep class com.zhiyicx.common.widget.** { *; } #实体类不参与混淆
-keep class com.zhiyicx.common.thridmanager.share.** { *; } #　分享

################app###############
-keep interface  com.zhiyicx.thinksnsplus.** { *; }
-keep class com.zhiyicx.thinksnsplus.data.beans.** { *; } #实体类不参与混淆
-keep class com.zhiyicx.thinksnsplus.widget.** { *; } #自定义控件不参与混淆
-keep class com.zhiyicx.thinksnsplus.utils.** { *; }
-keep class com.zhiyicx.thinksnsplus.wxapi.** { *; }
-keep class com.zhiyicx.thinksnsplus.WBShareActivity { *; }

################mysnackbar###############
-keep class com.trycatch.mysnackbar.** { *; }
-keep interface  com.trycatch.mysnackbar.** { *; }

################PhotoPicker###############
-keep class me.iwf.photopicker.** { *; }
-keep interface  me.iwf.photopicker.** { *; }

################rxerrorhandler###############
-keep class com.zhiyicx.rxerrorhandler.** { *; }
-keep interface  com.zhiyicx.rxerrorhandler.** { *; }

################media-cache###############
-keep class com.danikula.videocache.** { *; }
-keep interface  com.danikula.videocache.** { *; }

################skinlibrary###############
-keep class solid.ren.skinlibrary.** { *; }
-keep interface  solid.ren.skinlibrary.** { *; }

################tspay (ping++ sdk)###############
# Ping++ 混淆过滤
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

# 支付宝混淆过滤
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

# 微信或QQ钱包混淆过滤
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

# 内部WebView混淆过滤
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}


################IM###############
-keep class com.zhiyicx.imsdk.** { *; } #实体类不参与混淆
-keep interface com.zhiyicx.imsdk.** { *; }
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keepattributes Signature
-keep class **.R$* {*;}
-ignorewarnings
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class com.zhiyicx.appupdate.** { *; } #实体类不参与混淆
-keep interface com.zhiyicx.appupdate.** { *; }

-keep class com.davemorrissey.labs.subscaleview.** { *; } #实体类不参与混淆
-keep interface com.davemorrissey.labs.subscaleview.** { *; }
#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------
 #3D 地图 V5.0.0之前：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.amap.mapcore.*{*;}
    -keep   class com.amap.api.trace.**{*;}

  #  3D 地图 V5.0.0之后：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.**{*;}
    -keep   class com.amap.api.trace.**{*;}

 #   定位
    -keep class com.amap.api.location.**{*;}
    -keep class com.amap.api.fence.**{*;}
    -keep class com.autonavi.aps.amapapi.model.**{*;}

  #  搜索
    -keep   class com.amap.api.services.**{*;}

  #  2D地图
    -keep class com.amap.api.maps2d.**{*;}
    -keep class com.amap.api.mapcore2d.**{*;}

   # 导航
    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}
################messagepack###############
-keep class org.** { *; }
-keep interface org.** { *; }

################javassist###############
-keep class javassist.** { *; }
-keep interface javassist.** { *; }


################crop###############
-keep class com.soundcloud.android.** { *; }
-keep interface com.soundcloud.android.** { *; }

################annotation###############
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

################pickerview###############
-keep class com.bigkoo.pickerview.** { *; }
-keep interface com.bigkoo.pickerview.** { *; }

################banner###############
-keep class com.youth.banner.** { *; }
-keep interface  com.youth.banner.** { *; }

################baseadapter-recyclerview###############
-keep class com.zhy.adapter.recyclerview.** { *; }
-keep interface  com.zhy.adapter.recyclerview.** { *; }
################support###############
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

################retrofit###############
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

################butterknife###############
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
   @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
 @butterknife.* <methods>;
}


################gson###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.sunloto.shandong.bean.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

################glide###############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

################okhttp###############
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**


################androidEventBus###############
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}
-keepattributes *Annotation*

################Rxjava and RxAndroid###############
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

-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn rx.internal.util.unsafe.**


################nineoldandroids###############
-keep class com.nineoldandroids.animation.** { *; }
-keep interface com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.view.** { *; }
-keep interface com.nineoldandroids.view.** { *; }



################epresso###############
-keep class android.support.test.espresso.** { *; }
-keep interface android.support.test.espresso.** { *; }


################autobahn###############
-keep class de.tavendo.autobahn.** { *; }
-keep interface de.tavendo.autobahn.** { *; }

################umeng###############
 -dontusemixedcaseclassnames
 	-dontshrink
 	-dontoptimize
 	-dontwarn com.google.android.maps.**
 	-dontwarn android.webkit.WebView
 	-dontwarn com.umeng.**
 	-dontwarn com.tencent.weibo.sdk.**
 	-dontwarn com.facebook.**
 	-keep public class javax.**
 	-keep public class android.webkit.**
 	-dontwarn android.support.v4.**
 	-keep enum com.facebook.**
 	-keepattributes Exceptions,InnerClasses,Signature
 	-keepattributes *Annotation*
 	-keepattributes SourceFile,LineNumberTable

 	-keep public interface com.facebook.**
 	-keep public interface com.tencent.**
 	-keep public interface com.umeng.socialize.**
 	-keep public interface com.umeng.socialize.sensor.**
 	-keep public interface com.umeng.scrshot.**
 	-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
 	-keep public class com.umeng.socialize.* {*;}


 	-keep class com.facebook.**
 	-keep class com.facebook.** { *; }
 	-keep class com.umeng.scrshot.**
 	-keep public class com.tencent.** {*;}
 	-keep class com.umeng.socialize.sensor.**
 	-keep class com.umeng.socialize.handler.**
 	-keep class com.umeng.socialize.handler.*
 	-keep class com.umeng.weixin.handler.**
 	-keep class com.umeng.weixin.handler.*
 	-keep class com.umeng.qq.handler.**
 	-keep class com.umeng.qq.handler.*
 	-keep class UMMoreHandler{*;}
 	-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
 	-keep class com.tencent.mm.sdk.modelmsg.** implements 	com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
 	-keep class im.yixin.sdk.api.YXMessage {*;}
 	-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
 	-keep class com.tencent.mm.sdk.** {
   	 *;
 	}
 	-keep class com.tencent.mm.opensdk.** {
    *;
 	}
 	-dontwarn twitter4j.**
 	-keep class twitter4j.** { *; }

 	-keep class com.tencent.** {*;}
 	-dontwarn com.tencent.**
 	-keep public class com.umeng.com.umeng.soexample.R$*{
     public static final int *;
 	}
 	-keep public class com.linkedin.android.mobilesdk.R$*{
     public static final int *;
 		}
 	-keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 	}

 	-keep class com.tencent.open.TDialog$*
 	-keep class com.tencent.open.TDialog$* {*;}
 	-keep class com.tencent.open.PKDialog
 	-keep class com.tencent.open.PKDialog {*;}
 	-keep class com.tencent.open.PKDialog$*
 	-keep class com.tencent.open.PKDialog$* {*;}

 	-keep class com.sina.** {*;}
 	-dontwarn com.sina.**
 	-keep class  com.alipay.share.sdk.** {
 	   *;
 	}
 	-keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
 	}

 	-keep class com.linkedin.** { *; }
 	-keepattributes Signature


################ 极光推送 ###############
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

################ ucrop ###############

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }


################ greenDAO 3 ###############
#greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class com.zhiyicx.thinksnsplus.data.beans.*{ *; }

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

-keep class com.zhiyicx.baseproject.base.TSWebFragment{ *; }

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------



#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#--