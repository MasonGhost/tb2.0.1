package com.zhiyicx.zhibolibrary.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Surface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jess.camerafilters.base.FilterManager;
import com.jess.camerafilters.base.OnGlSurfaceShotListener;
import com.jess.camerafilters.entity.FilterInfo;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.soundcloud.android.crop.Crop;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.PublishModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLEndStreamingActivity;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLPublishLiveActivity;
import com.zhiyicx.zhibolibrary.ui.view.PublishView;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.DeviceUtils;
import com.zhiyicx.zhibolibrary.util.DrawableProvider;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.SensitivewordFilter;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;
import com.zhiyicx.zhibosdk.manage.listener.OnCloseStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.OnGiftConfigCallback;
import com.zhiyicx.zhibosdk.manage.listener.OnLiveStartPlayListener;
import com.zhiyicx.zhibosdk.manage.listener.ZBFrameCapturedCallback;
import com.zhiyicx.zhibosdk.manage.listener.ZBStreamingPreviewListener;
import com.zhiyicx.zhibosdk.manage.listener.ZBSurfaceTextureListener;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBGift;
import com.zhiyicx.zhibosdk.policy.OnNetworkJitterListener;
import com.zhiyicx.zhibosdk.policy.OnReconnetListener;

import org.json.JSONException;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/3/23.
 */
@ActivityScope
public class PublishPresenter extends BasePresenter<PublishModel, PublishView> implements OnShareCallbackListener, AMapLocationListener {
    public static final float COVER_WIDTH = 350f;
    public static final float COVER_HEIGHT = 350f;
    private UserInfo myInfo;
    private File mCropfile;


    /**
     * 声明AMapLocationClientOption对象
     */
    public AMapLocationClientOption mLocationOption = null;
    /**
     * 声明定位回调监听器
     */
    private AMapLocationClient mLocationClient;

    private String mLocation = "";
    public boolean isStreaming;
    public final String TAG = this.getClass().getSimpleName();

    private UmengSharePolicyImpl mSharePolicy;
    private boolean isBackGround;
    public boolean isException;
    private boolean isHolder = false;
    private FilterManager mFilterManager;
    private int mGLTextureId;
    private Subscription glsSubscrebtion;

    ZBFrameCapturedCallback mCapturedCallback = new ZBFrameCapturedCallback() {
        @Override
        public void onFrameCaptured(final Bitmap bmp) {
            Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            mRootView.setCaptureBitmap(bmp);
                            if (isHolder) {
                                mRootView.setLoadingCaptureBitap(bmp);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }
    };

    OnGlSurfaceShotListener mShotLitener = new OnGlSurfaceShotListener() {
        @Override
        public void onGlSurfaceShot(final Bitmap bitmap, final String path) {

            glsSubscrebtion = Observable.just(bitmap).subscribeOn(Schedulers.io()).map(new Func1<Bitmap, Bitmap>() {
                @Override
                public Bitmap call(Bitmap bitmap) {
                    if (bitmap == null) {
                        return null;
                    }
                    Bitmap map = null;
                    android.graphics.Matrix matrix = new android.graphics.Matrix();


                    if (ZBStreamingClient.getInstance().isBackCamera()) {
                        matrix.postScale(-1, 1);
                        matrix.postRotate(270);
                        map = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        //map = DrawableProvider.rotateBitmapByDegree(bitmap, 90);

                    } else {
                        matrix.postRotate(-90);
                        map = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        // map = DrawableProvider.rotateBitmapByDegree(bitmap, 270);
                    }
                    return map;
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    mRootView.setCaptureBitmap(bitmap);
                    if (isHolder) {
                        mRootView.setLoadingCaptureBitap(bitmap);
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    };


    @Inject
    public PublishPresenter(PublishModel model, PublishView rootView) {
        super(model, rootView);
        this.mSharePolicy = new UmengSharePolicyImpl(rootView.getActivity());
        mSharePolicy.setOnShareCallbackListener(this);
        initSensitiveWordFilter();
        myInfo = ZhiboApplication.getUserInfo();
        setShareData();
        initLocation();
    }

    /**
     * 设置分享数据
     */
    private void setShareData() {
        mSharePolicy.setShareContent(UserInfo.getShareContentByUserInfo(myInfo));
    }

    public int setCameraDisplayOrientation(Activity activity,
                                           int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 初始化敏感词过滤
     */
    private void initSensitiveWordFilter() {
        if (ZBLApi.sZBApiConfig == null || ZBLApi.sZBApiConfig.filter_word_conf == null) {
            mRootView.showMessage("str_zhibosdk_error");
            return;
        }

        if (ZhiboApplication.filter == null) {
            ZhiboApplication.initFilterWord();
        }
    }

    /**
     * 设置监听
     */
    private void initListener() {
        ZBStreamingClient.getInstance().setNetworkJitterListener(new OnNetworkJitterListener() {
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

        ZBStreamingClient.getInstance().setReconnetListener(new OnReconnetListener() {
            @Override
            public void reconnectStart() {
                LogUtils.debugInfo(TAG, " ZBStreamingClient.getInstance() =-------------reconnectStart ");
                mRootView.setPlaceHolderVisible(true);
            }

            @Override
            public void reconnectScuccess() {
                mRootView.setPlaceHolderVisible(false);
                LogUtils.debugInfo(TAG, " ZBStreamingClient.getInstance() =-------------reconnectScuccess ");
            }

            @Override
            public void reConnentFailure() {
                mRootView.setPlaceHolderVisible(false);
                LogUtils.debugInfo(TAG, " ZBStreamingClient.getInstance() =-------------reConnentFailure ");
                isException = true;//重连失败标记为异常状态
                close();
            }
        });

        mFilterManager = FilterManager
                .builder()
                .context(UiUtils.getContext())
                .defaultFilter(new FilterInfo(false, 0))
                .addGlSurfaceShotListener(mShotLitener)
                .build();
        ZBStreamingClient.getInstance().setZBStreamingPreviewListener(new ZBStreamingPreviewListener() {

            @Override
            public boolean onPreviewFrame(final byte[] var1, final int var2, final int var3) {
                return true;
            }
        });


        ZBStreamingClient.getInstance().setZBSurfaceTextureListener(new ZBSurfaceTextureListener() {
            @Override
            public void onSurfaceCreated() {
                mFilterManager.initialize();
            }

            @Override
            public void onSurfaceChanged(int var1, int var2) {
                mFilterManager.updateSurfaceSize(var1, var2);
            }

            @Override
            public void onSurfaceDestroyed() {
                mFilterManager.release();
            }

            @Override
            public int onDrawFrame(int var1, int var2, int var3, float[] var4) {
                mGLTextureId = mFilterManager.drawFrame(var1, null, var2, var3);
                return mGLTextureId;
            }
        });

    }

    /**
     * 截帧
     *
     * @param isHolder
     */
    public void captureFrame(boolean isHolder) {
        this.isHolder = isHolder;
        if (mFilterManager != null) {
            mFilterManager.setIsCapture(true);
        }
        // ZBStreamingClient.getInstance().captureFrame(0, 0, mCapturedCallback);
    }

    /**
     * 获取gps经纬度
     */
    private void initLocation() {
        if (mLocationOption == null) {
            //初始化定位

            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();

            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            mLocationClient = new AMapLocationClient(ZhiboApplication.getContext());
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
            //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
            mLocationClient.setLocationListener(this);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 位置发生改变时调用
                // 获取经纬度并且把把正确经纬度转换成火星坐标
                mLocation = aMapLocation.getLatitude() + "," + aMapLocation.getLongitude();
                System.out.println("location = " + aMapLocation.getLatitude());
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                com.zhiyicx.common.utils.log.LogUtils.d("AmapError" + "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    /**
     * 获得过滤管理器
     *
     * @return
     */
    public FilterManager getFilterManager() {
        return mFilterManager;
    }

    /**
     * 分享朋友圈
     *
     * @param publishLiveActivity
     */
    public void shareMoment(final ZBLPublishLiveActivity publishLiveActivity) {

        if (mSharePolicy.getShareContent() == null || mSharePolicy.getShareContent().getImage() == null) {
            mSharePolicy.shareMoment(publishLiveActivity, this);

        } else {
            Glide.with(UiUtils.getContext()).load(mSharePolicy.getShareContent().getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mSharePolicy.getShareContent().setBitmap(resource);
                    mSharePolicy.shareMoment(publishLiveActivity, PublishPresenter.this);

                }
            });
        }
    }

    /**
     * 分享微信
     *
     * @param publishLiveActivity
     */
    public void shareWechat(final ZBLPublishLiveActivity publishLiveActivity) {

        if (mSharePolicy.getShareContent() == null || mSharePolicy.getShareContent().getImage() == null) {
            mSharePolicy.shareWechat(publishLiveActivity, this);

        } else {
            Glide.with(UiUtils.getContext()).load(mSharePolicy.getShareContent().getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mSharePolicy.getShareContent().setBitmap(resource);
                    mSharePolicy.shareWechat(publishLiveActivity, PublishPresenter.this);

                }
            });
        }
    }

    /**
     * 分享微博
     *
     * @param publishLiveActivity
     */
    public void shareWeibo(final ZBLPublishLiveActivity publishLiveActivity) {

        if (mSharePolicy.getShareContent() == null || mSharePolicy.getShareContent().getImage() == null) {
            mSharePolicy.shareWeibo(publishLiveActivity, this);

        } else {
            Glide.with(UiUtils.getContext()).load(mSharePolicy.getShareContent().getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mSharePolicy.getShareContent().setBitmap(resource);
                    mSharePolicy.shareWeibo(publishLiveActivity, PublishPresenter.this);

                }
            });
        }
    }

    /**
     * 分享qq
     *
     * @param publishLiveActivity
     */
    public void shareQQ(final ZBLPublishLiveActivity publishLiveActivity) {

        if (mSharePolicy.getShareContent() == null || mSharePolicy.getShareContent().getImage() == null) {
            mSharePolicy.shareQQ(publishLiveActivity, this);

        } else {
            Glide.with(UiUtils.getContext()).load(mSharePolicy.getShareContent().getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mSharePolicy.getShareContent().setBitmap(resource);
                    mSharePolicy.shareQQ(publishLiveActivity, PublishPresenter.this);

                }
            });
        }
    }

    /**
     * 分享qq空间
     *
     * @param publishLiveActivity
     */
    public void shareZone(final ZBLPublishLiveActivity publishLiveActivity) {
        if (mSharePolicy.getShareContent() == null || mSharePolicy.getShareContent().getImage() == null) {
            mSharePolicy.shareZone(publishLiveActivity, this);

        } else {
            Glide.with(UiUtils.getContext()).load(mSharePolicy.getShareContent().getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mSharePolicy.getShareContent().setBitmap(resource);
                    mSharePolicy.shareZone(publishLiveActivity, PublishPresenter.this);

                }
            });
        }
    }


    /**
     * 关闭流
     */
    public void close() {
        mRootView.isSelfClose(true);

        //如果开始直播才调用服务器的关闭流接口
        ZBEndStreamJson endStream = mRootView.getEndStream();
        if (isException) {
            endStream.isException = true;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("endStream", endStream);
        bundle.putSerializable("presenter", ZhiboApplication.getUserInfo());

        if (isStreaming) {
            close(bundle);
        } else if (mRootView != null) {
            mRootView.killMyself();//杀死自己
        }
    }

    /**
     * 关闭流
     */
    @org.simple.eventbus.Subscriber(tag = "close_stearm")
    public void close(Bundle bundle) {
        final ZBEndStreamJson json = (ZBEndStreamJson) bundle.getSerializable("endStream");

        ZBStreamingClient.getInstance().closePlay(new OnCloseStatusListener() {
            @Override
            public void onSuccess(ZBEndStreamJson endStreamJson) {
//                if (json != null)
//                    launchEndStreamActivity(json);
//                else
//                    launchEndStreamActivity(endStreamJson);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
//                if(json != null)
//                launchEndStreamActivity(json);
            }

            @Override
            public void onFial(String code, String message) {
                LogUtils.warnInfo(TAG, message);
            }
        });
        launchEndStreamActivity(json, (UserInfo) bundle.getSerializable("presenter"));
        if (mRootView != null) {
            mRootView.killMyself();//杀死自己
        }
    }

    /**
     * 发送数据到结束直播页面
     *
     * @param endStreamJson
     */
    private void launchEndStreamActivity(ZBEndStreamJson endStreamJson, UserInfo userInfo) {
        Intent intent = new Intent(UiUtils.getContext(), ZBLEndStreamingActivity.class);
        intent.putExtra("isAudience", false);//是否为观众
        intent.putExtra("isException", endStreamJson.isException);//是异常结束
        Bundle bundle = new Bundle();
        bundle.putSerializable("income", endStreamJson.data);
        bundle.putSerializable("presenter", userInfo);
        intent.putExtras(bundle);
        mRootView.launchEndStreamActivity(intent);
    }

    /**
     * 剪切成功后处理数据
     */
    public void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            mRootView.closeCameraFragment();//用户确定剪切后,关闭相册fragment
            try {
                //从intent中取出地址
                mCropfile = new File(new URI(result.getParcelableExtra(MediaStore.EXTRA_OUTPUT).toString()));
            } catch (Exception e) {
                e.printStackTrace();
                //出错使用默认地址
                mCropfile = new File(DataHelper.getCacheFile(UiUtils.getContext()), DataHelper.CROP_CACHE_NAME);
            }
            int degree = DrawableProvider.getBitmapDegree(mCropfile.getAbsolutePath());
            //将地址转换成Bitmap
            Bitmap bitmap = new BitmapDrawable(mCropfile.getAbsolutePath()).getBitmap();
            //改变bitmap大小   如果图片旋转过就旋转后改变大小
            Bitmap resizeBmp = DrawableProvider.getReSizeBitmap(degree == 0 ? bitmap :
                    DrawableProvider.rotateBitmapByDegree(bitmap, degree), COVER_WIDTH, COVER_HEIGHT);
            if (mModel.compressBitmap(mCropfile, resizeBmp)) {
                mRootView.setCoverPhoto(new BitmapDrawable(resizeBmp));//设置剪切后的图片到封面展示
            } else {
                mRootView.showMessage(UiUtils.getString("str_select_failure"));
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            UiUtils.makeText(Crop.getError(result).getMessage());
        } else if (resultCode == Activity.RESULT_CANCELED) {
            mRootView.clearCameraFragmentImg();//清除cameraFragment里面的截帧图片
        }
    }

    /**
     * 开始准备直播
     */
    public void StartStream() {
        String title = "";//标题为可选参数
        if (!TextUtils.isEmpty(mRootView.getTitel())) {
            title = mRootView.getTitel();
            if (ZhiboApplication.filter != null) {
                title = ZhiboApplication.filter.replaceSensitiveWord(title, SensitivewordFilter.minMatchTYpe, ZBLApi.sZBApiConfig.filter_word_conf
                        .filter_word_replace);
            } else {
                ZhiboApplication.initFilterWord();
                mRootView.showMessage(UiUtils.getString("str_network_error_action"));
                return;
            }

        }
        if (!DeviceUtils.isWifiOpen(UiUtils.getContext())) {//提示用户
            UiUtils.SnackbarTextWithLong(UiUtils.getString(R.string.str_not_wifi_prompt));
        }

        ZBStreamingClient.getInstance().startPlay(title, mLocation, mCropfile, new OnLiveStartPlayListener() {
            @Override
            public void onStartPre() {
//                showLoading();
                mRootView.setSelectEnable(false);//请求开始直播时不可点击选择封面
            }

            @Override
            public void onStartReady() {

            }

            @Override
            public void onStartSuccess() {
                hideLoading();
                isStreaming = true;
                EventBus.getDefault().post("1", "remove_all");//清除屏幕按钮
            }

            @Override
            public void onStartFail() {
                hideLoading();
                isStreaming = false;
                EventBus.getDefault().post("连接服务器失败!", "warn_user");//连接失败提示用户
                mRootView.setButtonText("网络不稳定");
            }
        });

    }

    /**
     * 获取礼物配置信息
     */
    public void getGiftConfig() {
        showLoading();
        ZBInitConfigManager.getGiftConfig(new OnGiftConfigCallback() {
            @Override
            public void onSuccess(List<ZBGift> data) {
                ZBLApi.sZBApiConfig.gift_list = data;
                downloadGiftImage();
                StartStream();
            }

            @Override
            public void onFail(String code, String message) {
                hideLoading();
                mRootView.setButtonText(message);
            }

            @Override
            public void onError(Throwable throwable) {
                hideLoading();
                mRootView.setButtonText("网络不稳定");
            }
        });
    }

    /**
     * 预先下载礼物图片
     */
    public void downloadGiftImage() {
        if (ZBLApi.sZBApiConfig != null && ZBLApi.sZBApiConfig.gift_list != null && ZBLApi.sZBApiConfig.gift_list.size() > 0) {
            for (final ZBGift value : ZBLApi.sZBApiConfig.gift_list) {

                if (TextUtils.isEmpty(DataHelper.getStringSF(value.image, UiUtils.getContext()))) {
                    mModel.downloadFile(value.image).
                            subscribeOn(Schedulers.io()).
                            observeOn(Schedulers.io())
                            .subscribe(new Action1<ResponseBody>() {
                                @Override
                                public void call(final ResponseBody responseBody) {

                                    boolean isDownload = UiUtils.writeResponseBodyToDisk(responseBody, value.image);
                                    if (isDownload) {
                                        DataHelper.SetStringSF(value.image, value.image, UiUtils.getContext());
                                    }


                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            });
                }
            }
        }


    }

    private void hideLoading() {
        mRootView.hideLoadding(false);//隐藏loading
        mRootView.setButtonEnable(true);
    }

    private void showLoading() {
        mRootView.showLoadding(false);//显示loading
        mRootView.setButtonEnable(false);//初始化时开始按钮不能点击
    }


    /**
     * 转换摄像头
     */
    public void switchCamera() {
        if (ZBStreamingClient.getInstance() == null) {
            return;
        }
        LogUtils.warnInfo(TAG, "switchCamera");
        ZBStreamingClient.getInstance().switchCamera();//转换摄像头
    }

    public void reconnect() {
        ZBStreamingClient.getInstance().reconnect();
    }

    /**
     * 创建直播间
     *
     * @param intent
     */
    public void createLiveRoom(Intent intent) {
        try {
            //水印
            WatermarkSetting watermarkSetting = getWaterMarkSetting();
            ZBStreamingClient.getInstance().initConfig(
                    mRootView.getCameraPreViewAFL(),
                    mRootView.getCameraPreViewSFV(), null);
        } catch (JSONException e) {
            e.printStackTrace();
            mRootView.setWarnMessage("创建失败");
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            mRootView.setWarnMessage("校验流失败");
        }
        initListener();//初始化监听器

    }

    /**
     * 配置水印
     *
     * @return
     */
    private WatermarkSetting getWaterMarkSetting() {
        WatermarkSetting watermarkSetting = new WatermarkSetting(UiUtils.getContext()); // 100 为 alpha 值
        watermarkSetting.setResourceId(R.mipmap.logo);
        watermarkSetting.setAlpha(100);
        watermarkSetting.setSize(WatermarkSetting.WATERMARK_SIZE.SMALL);
        watermarkSetting.setLocation(WatermarkSetting.WATERMARK_LOCATION.NORTH_EAST);
        watermarkSetting.setCustomPosition(0.8f, 0.5f);
        return watermarkSetting;
    }


    /**
     * 清除按钮
     */
    @Subscriber(tag = "remove_all", mode = ThreadMode.MAIN)
    public void removeAll(String s) {
        hideLoading();
        DeviceUtils.hideSoftKeyboard(UiUtils.getContext(), mRootView.getCameraPreViewAFL());//隐藏软键盘
        mRootView.removeALL();//隐藏开始直播之前所有的布局
        mRootView.showCore();//显示直播页核心页面


    }


    @Subscriber(tag = "warn_user", mode = ThreadMode.MAIN)
    public void WarnUser(String s) {
        hideLoading();
        mRootView.setSelectEnable(true);//请求开始直播时结束可点击选择封面
        mRootView.showMessage(s);
    }

    @Subscriber(tag = "net_change_not_wifi", mode = ThreadMode.MAIN)
    public void notWifiOpen(boolean b) {//非wifi情况,提示用户
        UiUtils.makeText(UiUtils.getString(R.string.str_not_wifi_prompt));
    }


    @Override
    public void onDestroy() {
        if (mCropfile != null) {
            mCropfile = null;
        }
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.onDestroy();
        }
        if (mFilterManager != null) {
            mFilterManager.release();
        }
        ZBStreamingClient.getInstance().onDestroy();
        unSubscribe(glsSubscrebtion);
        super.onDestroy();
        if (mSharePolicy != null) {
            mSharePolicy.setOnShareCallbackListener(null);
        }
    }

    public void onResume() {
        ZBStreamingClient.getInstance().onResume();
    }


    public void onPause() {
        /*直播过程中分享面板弹出不暂停直播 7.4.2017 lei13*/
        if (!mSharePolicy.isShowing()) {
            ZBStreamingClient.getInstance().onPause();
        }
    }

    public void isBackGround(boolean isBackGround) {
        this.isBackGround = isBackGround;
    }

    public boolean isBackGround() {
        return this.isBackGround;
    }


    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showMessage(UiUtils.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showMessage(UiUtils.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showMessage(UiUtils.getString(R.string.share_cancel));

    }
}

