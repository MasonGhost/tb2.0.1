package com.zhiyicx.zhibolibrary.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.camerafilters.base.FilterManager;
import com.soundcloud.android.crop.Crop;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerPublishComponent;
import com.zhiyicx.zhibolibrary.di.module.PublishModule;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.PublishPresenter;
import com.zhiyicx.zhibolibrary.ui.Transformation.GaussianBlurTrasform;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.view.CameraView;
import com.zhiyicx.zhibolibrary.ui.view.FocusIndicatorRotateLayout;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreParentView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.ui.view.PublishView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.DeviceUtils;
import com.zhiyicx.zhibolibrary.util.FastBlur;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.widget.ZBAspectFrameLayout;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 主播开启直播页
 * Created by zhiyicx on 2016/3/23.
 */
public class ZBLPublishLiveActivity extends ZBLBaseActivity implements PublishView, PublishCoreParentView, View.OnClickListener {
    public static final String KEY_APIIMINFO = "APIIMINFO";
    protected GLSurfaceView mCameraPreViewSFV;
    protected View focusIndicator;
    protected FocusIndicatorRotateLayout focusIndicatorRotateLayout;
    protected ZBAspectFrameLayout mCameraPreViewAFL;
    protected ImageView ivHolder;
    protected FrameLayout flCore;
    protected FrameLayout flCamera;
    protected ProgressBar mLoadingPB;
    protected EditText mTitleET;
    protected ImageView mSelectBT;
    protected ImageView mCoverImg;
    protected TextView tvChangeCover;
    protected RelativeLayout rlPublishSelect;
    protected ImageButton ibPublishCoreWeixin;
    protected ImageButton ibPublishCoreFriend;
    protected ImageButton ibPublishCoreQq;
    protected ImageButton ibPublishCoreSina;
    protected ImageButton ibPublishCoreZone;
    protected LinearLayout llShare;
    protected Button mStartlBT;
    protected ImageView mBottomLoadingPB;
    protected RelativeLayout mActionRootRL;
    protected ImageView mPromptTV;
    protected ImageButton mCloseBT;
    @Inject
    PublishPresenter mPresenter;
    @Inject
    PublishCoreView mPublishCoreView;
    @Inject
    CameraView mCameraView;

    private String[] mActions = new String[]{"拍照", "相册"};
    //    private AlertDialog.Builder mDialogBuilder;
    private static int RESULT_CAPTURE_IMAGE = 0;
    private AlertDialog.Builder mWarnBuilder;
    private AlertDialog mWarnDialog;
    private File sdcardTempFile;
    private AlertDialog mOptionDialog;
    private boolean isCameraOpen;
    private Subscription mSubscription;
    private boolean isSelfClose;
    private AlertDialog mWIFIDialog;


    @Override
    protected void initView() {
//        UiUtils.statuInScreen(this);//全屏，并且沉侵式状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,//防止屏幕黑屏
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.zb_activity_publish_live);
        //初始化Dialog
        InitDialog();
        mCameraPreViewSFV = (GLSurfaceView) findViewById(R.id.cameraPreview_surfaceView);
        focusIndicator = (View) findViewById(R.id.focus_indicator);
        focusIndicatorRotateLayout = (FocusIndicatorRotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
        mCameraPreViewAFL = (ZBAspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        ivHolder = (ImageView) findViewById(R.id.iv_publish_holder);
        flCore = (FrameLayout) findViewById(R.id.fl_core);
        flCamera = (FrameLayout) findViewById(R.id.fl_camera);
        mLoadingPB = (ProgressBar) findViewById(R.id.pb_publish_small_loading);
        mTitleET = (EditText) findViewById(R.id.et_publish_title);
        mSelectBT = (ImageView) findViewById(R.id.bt_publish_select);
        mSelectBT.setOnClickListener(ZBLPublishLiveActivity.this);
        mCoverImg = (ImageView) findViewById(R.id.iv_publish_cover_image);
        tvChangeCover = (TextView) findViewById(R.id.tv_change_cover);
        rlPublishSelect = (RelativeLayout) findViewById(R.id.rl_publish_select);
        ibPublishCoreWeixin = (ImageButton) findViewById(R.id.ib_publish_core_weixin);
        ibPublishCoreWeixin.setOnClickListener(ZBLPublishLiveActivity.this);
        ibPublishCoreFriend = (ImageButton) findViewById(R.id.ib_publish_core_friend);
        ibPublishCoreFriend.setOnClickListener(ZBLPublishLiveActivity.this);
        ibPublishCoreQq = (ImageButton) findViewById(R.id.ib_publish_core_qq);
        ibPublishCoreQq.setOnClickListener(ZBLPublishLiveActivity.this);
        ibPublishCoreSina = (ImageButton) findViewById(R.id.ib_publish_core_sina);
        ibPublishCoreSina.setOnClickListener(ZBLPublishLiveActivity.this);
        ibPublishCoreZone = (ImageButton) findViewById(R.id.ib_publish_core_zone);
        ibPublishCoreZone.setOnClickListener(ZBLPublishLiveActivity.this);
        llShare = (LinearLayout) findViewById(R.id.ll_share);
        mStartlBT = (Button) findViewById(R.id.bt_publish_start);
        mStartlBT.setOnClickListener(ZBLPublishLiveActivity.this);
        mBottomLoadingPB = (ImageView) findViewById(R.id.pb_publish_loading);
        mActionRootRL = (RelativeLayout) findViewById(R.id.rl_action_root);
        mPromptTV = (ImageView) findViewById(R.id.tv_live_play_prompt);
        mCloseBT = (ImageButton) findViewById(R.id.bt_publish_close);
        mCloseBT.setOnClickListener(ZBLPublishLiveActivity.this);
    }

    @Override
    protected void initData() {
        DaggerPublishComponent//注入
                .builder()
                .publishModule(new PublishModule(this))
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .build()
                .inject(this);
        mPresenter.createLiveRoom(getIntent());

    }


    @Override
    public void onBackPressed() {
        try {
            if (isCameraOpen) {
                closeCameraFragment();
            }
            else {
                showWarnDialog();//退出动画
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭摄像头的fragemnt
     */
    @Override
    public void closeCameraFragment() {
        getSupportFragmentManager().popBackStack();//将当前fragment弹出
        restoreAll();//恢复直播间其他组件
        mCloseBT.setVisibility(View.VISIBLE);//显示关闭直播间的按钮
        isCameraOpen = false;
    }

    /**
     * 打开摄像头的fragemnt
     */
    @Override
    public void openCameraFragment() {
        removeALL();//移除直播间其他组件
        showCamera();
        mCloseBT.setVisibility(View.GONE);//隐藏关闭直播间的按钮
    }


    /**
     * @param isSmall
     */
    @Override
    public void showLoadding(boolean isSmall) {
        if (isSmall) {//顶部的loading
            mCloseBT.setVisibility(View.GONE);
            mLoadingPB.setVisibility(View.VISIBLE);
        }
        else {//底部的loading

            mBottomLoadingPB.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mBottomLoadingPB.getDrawable()).start();
        }
    }

    @Override
    public void hideLoadding(boolean isSmall) {
        if (isSmall) {//顶部的loading
            mCloseBT.setVisibility(View.VISIBLE);
            mLoadingPB.setVisibility(View.GONE);
        }
        else {//底部的loading
            mBottomLoadingPB.setVisibility(View.GONE);
            ((AnimationDrawable) mBottomLoadingPB.getDrawable()).stop();
        }
    }

    @Override
    public void launchLivaActivity() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void setButtonEnable(boolean isEnable) {
        mStartlBT.setEnabled(isEnable);
    }

    @Override
    public void setButtonText(String text) {
        mStartlBT.setText(text);
    }

    @Override
    public void killMyself() {
        finish();
        Anim.startActivityFromTop(this);//退出动画
    }

    @Override
    public String getTitel() {
        return mTitleET.getText().toString();
    }

    @Override
    public void setCoverPhoto(Drawable drawable) {
        mSelectBT.setBackgroundDrawable(drawable);
        tvChangeCover.setVisibility(View.VISIBLE);
        mCoverImg.setVisibility(View.GONE);//隐藏cover上的图片
    }

    @Override
    public void launchEndStreamActivity(Intent intent) {
        startActivity(intent);
        Anim.startActivityFromBottom(this);
    }

    @Override
    public ZBAspectFrameLayout getCameraPreViewAFL() {
        return mCameraPreViewAFL;
    }

    @Override
    public GLSurfaceView getCameraPreViewSFV() {
        return mCameraPreViewSFV;
    }

    @Override
    public void removeALL() {
        mActionRootRL.setVisibility(View.GONE);//隐藏输入直播间信息的组件
    }

    @Override
    public void restoreAll() {
        mActionRootRL.setVisibility(View.VISIBLE);//恢复输入直播间信息的组件
    }

    @Override
    public void showCore() {
        if (!mPublishCoreView.getCoreFragment().isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_core, mPublishCoreView.getCoreFragment());
            transaction.commit();
        }
    }

    @Override
    public void showCamera() {
        if (!mCameraView.getFragment().isAdded()) {
            isCameraOpen = true;//标记摄像头已打开
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_camera, mCameraView.getFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void captureFrame() {
        mPresenter.captureFrame(false);
    }

    /**
     * 传入截帧的bitmap
     *
     * @param bm
     */
    @Override
    public void setCaptureBitmap(Bitmap bm) {
        mCameraView.setCaptureBitmap(bm);
    }

    @Override
    public void setLoadingCaptureBitap(Bitmap bm) {
        if (bm != null) {
            ivHolder.setImageBitmap(FastBlur.blurBitmap(bm, ivHolder.getWidth(), ivHolder.getHeight()));
        } else {
            UiUtils.glideWrap(ZhiboApplication.getUserInfo().avatar.origin).transform(new GaussianBlurTrasform(this)).into(ivHolder);
        }
    }

    @Override
    public void setWarnMessage(String warn) {
        mOptionDialog.setMessage(warn);
    }


    @Override
    public void showWarn() {
        if (mPresenter.isStreaming && !mOptionDialog.isShowing()) {
            mOptionDialog.show();
        }
    }

    @Override
    public void hideWarn() {
        if (mPresenter.isStreaming && mOptionDialog.isShowing()) {
            mOptionDialog.dismiss();
        }
    }

    @Override
    public ZBEndStreamJson getEndStream() {
        return mPublishCoreView.getEndStream();
    }

    private void showWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("当前处于移动网络,是否继续?");
        builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.getGiftConfig();//先获取礼物配置，再开始直播
                if (mWIFIDialog != null) {
                    mWIFIDialog.dismiss();
                }
            }
        });
        builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mWIFIDialog != null) {
                    mWIFIDialog.dismiss();
                }
            }
        });
        mWIFIDialog = builder.create();
        mWIFIDialog.setCanceledOnTouchOutside(false);
        mWIFIDialog.show();
    }

    /**
     * 显示警告对话框
     */
    private void showWarnDialog() {
        if (mWarnDialog == null) {
            mWarnDialog = mWarnBuilder.create();
        }
        if (!mWarnDialog.isShowing()) {
            mWarnDialog.show();
        }
    }


    /**
     * 初始化用户选择头像的dialog
     */
    private void InitDialog() {
//        mDialogBuilder = new AlertDialog.Builder(this);
//        mDialogBuilder.setTitle("选择封面");
//        mDialogBuilder.setItems(mActions, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (i == 0) {
//                    //拍照
//                    launchCamera();
//                }
//                else if (i == 1) {
//                    //相册
//                    launchAlbum();
//                }
//            }
//        });

        mWarnBuilder = new AlertDialog.Builder(this);
        UiUtils.getExitDialog(mWarnBuilder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.close();//关闭流
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setNegativeButton("重连", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.reconnect();
            }
        });
        builder.setPositiveButton("断开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.close();//关闭流
            }
        });
        mOptionDialog = builder.create();
        mOptionDialog.setCanceledOnTouchOutside(false);

    }

    /**
     * 打开相册
     */
    @Override
    public void launchAlbum() {
        Crop.pickImage(ZBLPublishLiveActivity.this);
    }

    @Override
    public void clearCameraFragmentImg() {
        if (mCameraView != null) {
            mCameraView.clearImage();
        }
    }

    @Override
    public void setPlaceHolderVisible(final boolean isVisiable) {
        //                            UiUtils.glideWrap(BaseApplication.getUserInfo().avatar.origin).transform(new GaussianBlurTrasform(ZBLPublishLiveActivity.this)).into(ivHolder);
        mSubscription = Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mPromptTV.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
                        ivHolder.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
                        if (isVisiable) {
                            mPresenter.captureFrame(isVisiable);
                            ((AnimationDrawable) mPromptTV.getDrawable()).start();
//                            UiUtils.glideWrap(BaseApplication.getUserInfo().avatar.origin).transform(new GaussianBlurTrasform(ZBLPublishLiveActivity.this)).into(ivHolder);
                        }
                        else {
                            ((AnimationDrawable) mPromptTV.getDrawable()).stop();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void setSelectEnable(boolean isEnable) {
        mSelectBT.setEnabled(isEnable);
    }

    /**
     * 开启摄像头
     */
    @Override
    public void launchCamera() {
        sdcardTempFile = new File(DataHelper.getCacheFile(getApplicationContext()), DataHelper.CAMERA_CAPTURE_TEMP_PICTURE);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));


        ZBLPublishLiveActivity.this.startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK && data != null) {//从相册选择照片成功
            beginCrop(data.getData());
        }
        else if (requestCode == RESULT_CAPTURE_IMAGE && resultCode == RESULT_OK && sdcardTempFile != null) {//拍照成功
            beginCrop(Uri.fromFile(sdcardTempFile));
        }
        else if (requestCode == Crop.REQUEST_CROP) {//剪切成功后处理数据
            mPresenter.handleCrop(resultCode, data);
        }
    }

    /**
     * 开始剪切
     *
     * @param source
     */
    @Override
    public void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(DataHelper.getCacheFile(getApplicationContext()), DataHelper.CROP_CACHE_NAME));//剪切成功后保存图片的地址
        Crop.of(source, destination).asSquare().start(this);//跳转到剪切页面
    }

    @Override
    protected void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
        }
        if (mWarnDialog != null) {
            if (mWarnDialog.isShowing()) {
                mWarnDialog.dismiss();
            }
            mWarnDialog = null;
        }

        if (mWarnBuilder != null) {
            mWarnBuilder = null;
        }
        mPresenter.onDestroy();//销毁
        DeviceUtils.fixInputMethodManagerLeak(getApplicationContext());
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
//        showLoadding(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public ZBApiImInfo getImInfo() {
        ZBApiImInfo apiImInf;
        try {
            apiImInf = (ZBApiImInfo) getIntent().getExtras().get(KEY_APIIMINFO);
        } catch (Exception e) {
            apiImInf = new ZBApiImInfo();
        }

        return apiImInf;
    }

    @Override
    public SearchResult getData() {
        return null;
    }

    @Override
    public UserInfo getUserInfo() {
        return ZhiboApplication.getUserInfo();
    }

    @Override
    public void switchCamera() {
        mPresenter.switchCamera();
    }

    @Override
    public ZBLBaseFragment getPublishCoreview() {
        return mPublishCoreView.getCoreFragment();
    }

    @Override
    public boolean isSelfClose() {
        return isSelfClose;
    }

    @Override
    public void isSelfClose(boolean isClose) {
        this.isSelfClose = isClose;
    }

    /**
     * 获得过滤管理器
     *
     * @return
     */
    @Override
    public FilterManager getFilterManager() {
        return mPresenter.getFilterManager();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.isBackGround(false);//软件是否在后台
    }


    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.isBackGround(true);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_publish_select) {
            //显示dialog
//                mDialogBuilder.show();
            DeviceUtils.hideSoftKeyboard(getApplicationContext(), mTitleET);
            openCameraFragment();//打开用于采集照片的fragment
        }
        else if (view.getId() == R.id.ib_publish_core_weixin) {
            mPresenter.shareWechat(ZBLPublishLiveActivity.this);
        }
        else if (view.getId() == R.id.ib_publish_core_friend) {
            mPresenter.shareMoment(ZBLPublishLiveActivity.this);
        }
        else if (view.getId() == R.id.ib_publish_core_qq) {
            mPresenter.shareQQ(ZBLPublishLiveActivity.this);
        }
        else if (view.getId() == R.id.ib_publish_core_sina) {
            mPresenter.shareWeibo(ZBLPublishLiveActivity.this);
        }
        else if (view.getId() == R.id.ib_publish_core_zone) {
            mPresenter.shareZone(ZBLPublishLiveActivity.this);
        }
        else if (view.getId() == R.id.bt_publish_start) {
            //检测是否是wifi
            if (DeviceUtils.isWifiConnected(getApplicationContext())) {
                mPresenter.getGiftConfig();//先获取礼物配置，再开始直播
            } else {
                showWifiDialog();
            }
        }
        else if (view.getId() == R.id.bt_publish_close) {
            showWarnDialog();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}


