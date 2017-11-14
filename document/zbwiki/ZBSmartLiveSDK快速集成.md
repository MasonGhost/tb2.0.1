# ZBSmartLiveSDK 概述

ZBSmartLiveSDK 是一个适用于 Android 平台快速集成直播功能的 SDK.配合智播云后台可轻松同步迁移用户数据.
同时 ZBSDK 提供高度可定制化和自由调整的 UI.
ZBSmartLiveSDK 的特色是使用 Android Camera 画面捕获并进行 H.264 硬编码,以及支持 Android 麦克风音频采样进行 AAC 编码;同时,还能适应移动网络的多变性,实现智能切换最佳的视频采集,编码配置.
借助 ZBSmartLiveSDK 和智播云后台,用户可以在几小时内集成智播聊天核心功能至已有应用中.

## 功能特性

- [x]  应用权限验证
- [x]  智能账户迁移(登录,注册,自动登录,自动更新授权,更新用户信息)
- [x]  直播功能(摄像头采集,智能编码,上传推流)
- [x]  直播播放(拉留解码,弱网重连,在线聊天,赠送礼物等功能)
- [x]  在线回放
- [x]  多平台分享功能
- [x]  钱包功能(绑定/解绑/查询 提现账户)
- [x]  礼物功能(申请提现,赠送礼物,积分兑换)
- [x]  用户关系管理(关注/取消关注,用户认证等)

## 内容摘要

- [快速集成](#快速集成)
- [初始化SDK](#初始化SDK)
- [用户认证](#用户认证)
- [开启直播功能](#开启直播功能)
- [收发即时通讯文本消息](#收发即时通讯文本消息)
- [基础播放器功能](#基础播放器功能)

## SDK说明

SDK 提供了如下类(协议)和方法,点击类目查询详情

> [ZBSmartLiveSDK](http://www.baidu.com) 整个SDK的主入口
> 
> [ZBIMClient]() 聊天管理类,负责收发消息
> 
> [ZBStreamingClient]() 视频采集编码的核心
>
> [ZBPlayClient]() 观看直播\或者回放
> 
> [ZBVideoView\ZBTextureView\ZBMediaPlayer]() 播放器核心(播放在线回放视频和直播)
> 
> [ZBCoinManager]() 积分,金币,充值等管理

## 快速集成
将智播核心jar包导入到项目libs文件夹下

你可以通过 Gradle 自动集成该 SDK 

- 在工程的build.gradle 文件中加入

```groovy
dependencies {
    compile 'com.zhiyicx.zhibo:ZBSmartLiveSDK:1.0.0'
}
```

## 申请Appkey
1.注册智播云账号，在后台申请获取 ZHIBO_APPID,ZHIBO_APPTOKEN
2.在```AndroidManifest.xml```的```application```标签下配置ZHIBO_APPID,ZHIBO_APPTOKEN
```xml
<!--zhibo-->
<meta-data
    android:name="ZHIBO_APPID"
    android:value="your appid"></meta-data>
<meta-data
    android:name="ZHIBO_APPTOKEN"
    android:value="your token"></meta-data>
```

初始化Appkey
## 初始化SDK
在应用Applicaiton中的onCreate()方法中进行初始化

```java
ZBSmartLiveSDK.init(ApplicationContext);
```


## 用户认证
登录智播云进行获取授权.获取到智播云口令后
Android端需要在拿到票据```ticket```后调用以下代码进行用户认证后，才可以使用直播功能

```java
  ZBInitConfigManager.vertifyToken(String ticket);ticket票据由直播云服务器提供给第三方服务器;
```
## 开启直播
1.在开启直播前需要对流信息的一个校验，校验成后返回ApiIminfo里面有IM消息的房间id（cid）用于[聊天](#聊天)
```java

  ZBStreamingClient.checkStrem(new OncheckSteamStatusListener() {
        @Override
        public void onStartCheck() {
         
        }
        /**
        * ApiIminfo里面有IM消息的房间id（cid）用户[聊天](#聊天)
        */
        @Override
        public void onSuccess(ApiImInfo apiImInfo) {
             int cid=apiImInfo.cid;
        }
    
        @Override
        public void onError(Throwable throwable) {
          
        }
        /**
        * 错误码和错误原因  
        */
        @Override
        public void onFial(String code, String message) {
         
        }
        /**
        *如果被禁播，则返回解禁的时间戳
        */
        @Override
        public void onDisable(String time) {
    
        }
    });

```
2.添加播放页核心布局文件
```xml
<com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout
    android:id="@+id/cameraPreview_afl"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_alignParentTop="true"
    android:layout_centerHorizontal="true">

    <android.opengl.GLSurfaceView
        android:id="@+id/cameraPreview_surfaceView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"/>
</com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout>
```
3.开启直播初始化准备
注：吃方法涉及到ui加载，所以需要在Activity或者Fragment的onCreat（）方法中调用。
> 说明
>
>initListener();//初始化监听器，可选择设置

```java
 protected void onCreate(Bundle savedInstanceState) {
    zBasfl=(ZBAspectFrameLayout)findViewById(R.id.cameraPreview_afl);
    gsfv=(GLSurfaceView)findViewById(R.id.cameraPreview_surfaceView);
    try {
        mZBStreamingClient = ZBStreamingClient.getInstance();
        mZBStreamingClient.initConfig(getContext(),zBasfl,gsfv);
        } catch (JSONException e) {
            e.printStackTrace();
            mRootView.setWarnMessage("创建失败");
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            mRootView.setWarnMessage("校验流失败");
        }
    initListener();//初始化监听器，可选择
}    
private void initListener() {
    mZBStreamingClient.setNetworkJitterListener(new OnNetworkJitterListener() {
        /**
         * 当前网络不太稳定
         */
        @Override
        public void onNetworkJitter() {
            UiUtils.makeText("网络状况不稳定~");
        }

        /**
         * 当前网络为数据网络
         */
        @Override
        public void onNetInData() {
            UiUtils.makeText(UiUtils.getString(R.string.str_not_wifi_prompt));
        }
    });

    mZBStreamingClient.setReconnetListener(new OnReconnetListener() {
        @Override
        public void reconnectStart() {
            mRootView.setPlaceHolderVisible(true);
            LogUtils.warnInfo(TAG, "reconnectStart.....");
        }

        @Override
        public void reconnectScuccess() {
            mRootView.setPlaceHolderVisible(false);
            LogUtils.warnInfo(TAG, "enreconnectScuccessd.....");
        }

        @Override
        public void reConnentFailure() {
            mRootView.setPlaceHolderVisible(false);
            LogUtils.warnInfo(TAG, "reConnentFailure.....");
        }
    });
    }
```
4.开启直播
> 参数说明
>
> title:直播标题 （可以为空）
>
> mLocation:直播地址，```'lat纬度,lng经度'```由("纬度+','+经度")合成的字符串 （可以为空）
>
> mCropfile: 直播封面图文件 （可以为空）
```java
mZBStreamingClient.startPlay(title, mLocation, mCropfile,new OnLiveStartPlayListener() {
    /**
    *开启准备之前
    */
    @Override
    public void onStartPre() {
    }
    /**
    *准备完成
    */
    @Override
    public void onStartReady() {
    }
    /**
    *开启成功
    */
    @Override
    public void onStartSuccess() {
    }
    /**
    *开始失败
    */
    @Override
    public void onStartFail() {
    }
        });
```

5.关闭直播
```java
ZBStreamingClient.getInstance().closePlay(new OnCloseStatusListener() {
    /**
    *返回本次直播数据信息（获取赞，金币等）
    */
    @Override
    public void onSuccess(EndStreamJson endStreamJson) {
    }
    @Override
    public void onError(Throwable throwable) {
    }
    @Override
    public void onFial(String code, String message) {
    }
});
```

## 观看直播
1.xml中添加播放器视图
```java
<com.zhiyicx.zhibosdk.widget.ZBVideoView
    android:id="@+id/zv_live_play_player"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
2.初始化ZBPlayClent,在Activity或者Fragment的onResume、onPause、onDestroy方法中分别调用```     mZBPlayClient.onResume();    mZBPlayClient.onPause();    mZBPlayClient.onDestroy();```
```java
ZBPlayClient mZBPlayClient=ZBPlayClient.getInstance();
```
3.开始播放
直播
> 参数说明
>
> zBVideoView : xml中的播放器视图
> 
> uid : 主播加密后的uid，通过查看直播列表获取
>
> streamId : 流id，通过查看直播列表获取
>
> OnVideoStartPlayListener :开始播放状态监听
>
```java
 mRootView.getZBplayClient().startLive(zBVideoView, uid, streamId, new OnVideoStartPlayListener() {
            /**
            *观看直播连接建立成功，回放此回调返回数据为空
            */
            @Override
            public void onSuccess(ApiImInfo apiImInfo) {
                mApiImInfo = apiImInfo;//用于发消息
            }
            @Override
            public void onFail(String code, String message) {
            }
            @Override
            public void onError(Throwable throwable) {
            }
            /**
            *直播已经结束回调，回放此回调无效
            */
            @Override
            public void onLiveEnd(ApiPlay apiPlay, String uid) {
            }
        });
```

回放

> 参数说明
>
> zBVideoView : xml中的播放器视图
>
> vid : 视频id，通过查看回放列表获取

```java
mRootView.getZBplayClient().startVedio(zBVideoView, vid, new OnVideoStartPlayListener() {
    @Override
    public void onSuccess(ApiImInfo apiImInfo) {
    }
    @Override
    public void onFail(String code, String message) {
        mRootView.showWarn();
    }
    @Override
    public void onError(Throwable throwable) {
        mRootView.showMessage(UiUtils.getString("str_net_erro"));
    }
        });
```

## 聊天
1.发送文本消息
text文本内容，cid房间id（校验直播流和开始观看直播时返回ApiImInfo.cid）
```java
   ZBIMClient.getInstance().sendTextMsg(text, cid);
```
2.发送自定义[消息]
message类型为com.zhiyicx.imsdk.model.Message,[详情查看](http://www.baidu.com)
```java
   ZBIMClient.getInstance().sendMessage(message);
```








## IM通信
### 1 IM登录
是用服务器返回的im_uid和im_psw进行IM登录
```java
    /**
     * 登录Im服务
     */
    private void ImLogin(int im_uid,String im_psw) {
        IMConfig imConfig=new IMConfig();
        imConfig.setImUid(im_uid);
        imConfig.setImPsw(im_psw);
        //imConfig.setBin(ImService.BIN_MSGPACK);设置服务器返回的数据类型 当前提供msgpack以及Json
        //imConfig.setZlib(true);客户端是否支持zlib压缩、解压
        ZBIMClient.getInstance().login(imConfig);
    }
```
### 2 IM登出
账号切换时需要先登出后再是用新的im_uid和im_psw进行登录
```java
   ZBIMClient.getInstance().loginOut();
```

### 3 设置IM监听器

#### 3.1 消息监听器
     3.1.1 消息内容
     3.1.2 消息应答
     3.1.3 进入房间应答
     3.1.4 离开房间应答
     3.1.5 查看房间人数消息应答
     3.1.6 直播间结束消息
设置监听器 

```java
    //消息接收器
    ZBIMClient.getInstance().setImMsgReceveListener(new ImMsgReceveListener() {
            /**
            *收到消息内容
            */
            @Override
            public void onMessageReceived(Message message) {
                
            }
            /**
            *消息应答
            */
            @Override
            public void onMessageACKReceived(Message message) {

            }
            /**
            *进入房间应答
            */
            @Override
            public void onChatRoomJoinACKReceived(ChatRoomContainer chatRoomContainer) {

            }
            /**
            *离开房间应答
            */
            @Override
            public void onChatRoomLeaveACKReceived(ChatRoomContainer chatRoomContainer) {

            }
            /**
            *查看房间人数消息应答
            */
            @Override
            public void onChatRoomMCACKReceived(ChatRoomContainer chatRoomContainer) {

            }
            /**
            *直播间结束消息
            */
            @Override
            public void onConverEndReceived(Conver conver) {

            }
        });
    
```
#### 3.2 消息监听器

     3.2.1 IM连接成功
     3.2.2 IM断开连接
     3.2.3 IM连接错误
设置监听器 

```java
   //IM状态监听
   ZBIMClient.getInstance().setImStatusListener(new ImStatusListener() {
            /**
            *IM连接成功通知
            */
            @Override
            public void onConnected() {
                
            }
            /**
            *IM断开连接通知
            */
            @Override
            public void onDisconnect(int code, String reason) {

            }
            /**
            *IM连接错误通知
            */
            @Override
            public void onError(Exception error) {

            }
        });
```

##### 3.2.3 查看当前IM连接状态

```java
   ZBIMClient.getInstance().isConnected();
```

#### 3.3 聊天室消息事件
##### 3.3.1 进入聊天室
roomId房间为id,类型为int
```java
   ZBIMClient.getInstance().joinRoom(roomId);
```
##### 3.3.2 离开聊天室
roomId房间为id,类型为int
```java
   ZBIMClient.getInstance().leaveRoom(roomId);
```
##### 3.3.3 查看聊天室成员
roomIds房间为id列表，,类型为List<Integer>
```java
   ZBIMClient.getInstance().mc(roomIds);
```
##### 3.3.4 发送文本消息
text文本内容，cid房间id
```java
   ZBIMClient.getInstance().sendTextMsg(text, cid);
```
##### 3.3.4 发送自定义消息
message类型为com.zhiyicx.imsdk.model.Message
```java
   ZBIMClient.getInstance().sendMessage(message);
```


## 获取直播列表

```java
 
```

## 开启直播功能


## 收发即时通讯文本消息

## 基础播放器功能













