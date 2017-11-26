
-optimizationpasses 5


-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontskipnonpubliclibraryclassmembers

-dontpreverify
-dontshrink
-verbose
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

################common###############
-keep class com.zhiyicx.old.imsdk.entity.** { *; } #实体类不参与混淆

-keep class com.zhiyicx.old.imsdk.db.dao.** {
    public  <fields>;
    public <methods>;} #实体类不参与混淆
-keep class com.zhiyicx.old.imsdk.builder.** {
   public <methods>;
 }
-keep class com.zhiyicx.old.imsdk.utils.common.** { *; }#实体类不参与混淆
-keep class com.zhiyicx.old.imsdk.service.ImService {
    public static <fields>;
    public ImService(***);
 }
 -keep class com.zhiyicx.old.imsdk.service.SocketService {
     public static <fields>;
  }
 -keep class com.zhiyicx.old.imsdk.manage.** {
    public <methods>;
  }


-keep interface com.zhiyicx.old.imsdk.manage.listener.** { *; } #实体类不参与混淆

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep public class * extends android.app.Service
-keep public class * extends android.preference.Preference
-keepnames class * implements java.io.Serializable
-keep public class * extends android.content.BroadcastReceiver
-keepattributes Signature
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
-keepattributes *Annotation*

################autobahn###############
-keep class com.zhiyicx.old.imsdk.de.tavendo.autobahn.** { *; }
-keep interface  com.zhiyicx.old.imsdk.de.tavendo.autobahn.** { *; }
-keepnames class com.zhiyicx.old.imsdk.de.tavendo.autobahn.WebSocket$* {
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



