## ZBStreamingClient使用(主播端功能)


---
### 1. 创建推流布局界面
```java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                android:id="@+id/content"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@color/background_floating_material_dark"
    >
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
//focus_indicator_rotate_layout用户手动聚焦
<com.zhiyicx.zhibo.ui.view.FocusIndicatorRotateLayout 
    android:id="@+id/focus_indicator_rotate_layout"
    android:layout_height="wrap_content"
    android:layout_width="wrap_content"
    android:layout_centerInParent="true">
    <View android:id="@+id/focus_indicator"
        android:layout_height="120dp"
        android:layout_width="120dp"/>
</com.zhiyicx.zhibo.ui.view.FocusIndicatorRotateLayout>
    </com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout>
</RelativeLayout>
```
---

### 2. 校验流信息
```java
  ZBStreamingClient.checkStrem(new OncheckSteamStatusListener() {
        @Override
        public void onStartCheck() {
        }

        @Override
        public void onSuccess() {
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
---

### 3.开启直播初始化准备
★注：此方法涉及到ui加载，所以需要在Activity或者Fragment的onCreat（）方法中调用。并在相应的`onResume、onPause、onDestroy`方法中分别调用`ZBStreamingClient.onResume(); ZBStreamingClient.onPause(); ZBStreamingClient.onDestroy();`
```java
 protected void onCreate(Bundle savedInstanceState) {
    zBasfl=(ZBAspectFrameLayout)findViewById(R.id.cameraPreview_afl);
    gsfv=(GLSurfaceView)findViewById(R.id.cameraPreview_surfaceView);
    WatermarkSetting watermarksetting = new WatermarkSetting(this, R.drawable.zhibo_logo, WatermarkSetting.WATERMARK_LOCATION.SOUTH_WEST, WatermarkSetting.WATERMARK_SIZE.MEDIUM, 100);
    try {
        mZBStreamingClient = ZBStreamingClient.getInstance();
        mZBStreamingClient.initConfig(getContext(),zBasfl,gsfv,watermarkSetting);
        } catch (JSONException e) {
            e.printStackTrace();
            mRootView.setWarnMessage("创建失败");
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            mRootView.setWarnMessage("校验流失败");
        }

}   
```
####  设置 Listener
为了更好的和 SDK 交互，接受各种状态和其他信息，需要注册对应的 Listener:
```java
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
/**
*重连监听
*/
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
  //IM消息监听
  ZBStreamingClient.getInstance().setOnImListener(ImListener);
  //IM连接状态监听
  ZBStreamingClient.getInstance().setOnImStatusListener(this);
  //消息超时监听
  ZBStreamingClient.getInstance().setOnIMMessageTimeOutListener(this);
```
---

### 4.开始直播
>参数说明
title:直播标题 （可以为空）
mLocation:直播地址，'lat纬度,lng经度'由("纬度+','+经度")合成的字符串 （可以为空）
mCropfile: 直播封面图文件 （可以为空）
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
---

### 5.关闭直播
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
---
### 6.额外功能
<a name="6.1"></a>
#### 6.1 水印设置
所有水印相关的配置，都在 `WatermarkSetting` 类中进行。

<a name="6.2"></a>
#### 6.2 水印位置信息
水印的位置信息，目前内置四个方位，如：
``` java
public enum WATERMARK_LOCATION {
    NORTH_WEST,
    NORTH_EAST,
    SOUTH_WEST,
    SOUTH_EAST,
}
```
分别在屏幕的位置如下图所示：
```
/**
 * define the relative location of watermark on the screen when start streaming
 *
 *    |  NorthWest     |                |     NorthEast
 *    |                |                |
 *    |                |                |
 *    |  --------------|----------------|--------------
 *    |                |                |
 *    |                |                |
 *    |                |                |
 *    |  --------------|----------------|--------------
 *    |                |                |
 *    |                |                |
 *    |   SouthWest    |                |     SouthEast
 *
 */
```

<a name="6.2.1"></a>
#### 6.2.1 水印显示大小
``` java
/**
 * define de relative size of watermark
 */
 public enum WATERMARK_SIZE {
     LARGE,
     MEDIUM,
     SMALL,
 }
```
<a name="6.2.2"></a>
#### 6.2.2 构造 `WatermarkSetting`
传入 drawable 对象作为水印资源：

``` java
WatermarkSetting watermarkSetting = new WatermarkSetting(UiUtils.getContext());
    watermarkSetting.setResourceId(R.mipmap.logo);
    watermarkSetting.setAlpha(200); // [0~255]
    watermarkSetting.setCustomPosition(0.5f,0.5f);//position[0.0f~1.0f]自定义水印的位置
    watermarkSetting.setSize(WatermarkSetting.WATERMARK_SIZE.LARGE);
    watermarkSetting.setLocation(WatermarkSetting.WATERMARK_LOCATION.NORTH_EAST);
```

传入图片的绝对路径作为水印资源：

``` java
 watermarkSetting.setResourcePath("watermark resource absolute path");
```
<a name="6.3"></a>
#### 6.3 手动对焦
对焦之前传入 Focus Indicator , 如果不进行设置，对焦过程中将会没有对应的 UI 显示。

``` java
//在推流成功后设置
mZBStreamingClient.setFocusAreaIndicator(viewGroup,
                    viewGroup.findViewById(R.id.focus_indicator));
```

点击屏幕触发手动对焦，并设置对应的坐标值。

``` java
//在推流成功后操作
mZBStreamingClient.doSingleTapUp((int) e.getX(), (int) e.getY());
```

<a name="6.4"></a>
#### 6.4 Zoom
Camera Zoom 操作。

``` java
// mCurrentZoom must be in the range of [0, mZBStreamingClient.getMaxZoom()]
// 在推流成功后操作
if (mZBStreamingClient.isZoomSupported()) {
  mZBStreamingClient.setZoomValue(mCurrentZoom);
}
```
可以获取到当前的 Zoom 值：

``` java
mZBStreamingClient.getZoom();
```
<a name="6.5"></a>
#### 6.5 闪光灯操作
开启闪光灯。
``` java
mZBStreamingClient.turnLightOn();
```
关闭闪光灯。
``` java
mZBStreamingClient.turnLightOff();
```
<a name="6.6"></a>
#### 6.6 切换摄像头
切换摄像头。
``` java
mZBStreamingClient.switchCamera();
```
<a name="6.7"></a>
#### 6.7 禁音推流
在推流过程中，将声音禁用掉：

``` java
mZBStreamingClient.mute(true);
```
恢复声音：

```java
mZBStreamingClient.mute(false);
```

注：默认为 false

<a name="6.8"></a>
#### 6.8 截帧
在 Camera 正常预览之后，可以正常进行截帧功能。
在调用 captureFrame 的时候，您需要传入 width 和 height，以及 ZBFrameCapturedCallback，如果传入的 width 或者 height 小于等于 0，SDK 返回的 Bitmap 将会是预览的尺寸 。SDK 完成截帧之后，会回调 onFrameCaptured，并将结果以参数的形式返回给调用者。

``` java
mZBStreamingClient.captureFrame(w, h, new ZBFrameCapturedCallback() {
    @Override
    public void onFrameCaptured(Bitmap bmp) {

    }
}
```
注意：调用者有义务对 Bitmap 进行回收释放。截帧失败，bmp 会为 null。
