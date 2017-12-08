package com.zhiyicx.zhibolibrary.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerLivePlayComponent;
import com.zhiyicx.zhibolibrary.di.module.LivePlayModule;
import com.zhiyicx.zhibolibrary.model.entity.Icon;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.LivePlayPresenter;
import com.zhiyicx.zhibolibrary.presenter.UserHomePresenter;
import com.zhiyicx.zhibolibrary.ui.Transformation.GaussianBlurTrasform;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.FllowButtonView;
import com.zhiyicx.zhibolibrary.ui.components.MediaController;
import com.zhiyicx.zhibolibrary.ui.components.popup.CustomPopupWindow;
import com.zhiyicx.zhibolibrary.ui.view.LivePlayView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreParentView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.util.DeviceUtils;
import com.zhiyicx.zhibolibrary.util.FastBlur;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBPlayClient;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.widget.ZBMediaPlayer;
import com.zhiyicx.zhibosdk.widget.ZBVideoView;
import com.zhiyicx.zhibosdk.widget.soupport.ZBMediaController;
import com.zhy.autolayout.AutoLinearLayout;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 用户观看直播
 * Created by zhiyicx on 2016/4/1.
 */
public class ZBLLivePlayActivity extends ZBLBaseActivity implements LivePlayView, PublishCoreParentView, View.OnClickListener {

    protected ZBVideoView mVideoPlayer;
    protected ImageView mPlaceHolder;
    protected ImageView mPromptTV;
    protected ImageView ivLivePlayReplay;
    protected RelativeLayout mReplayCoverRL;
    protected ImageView ivPresenterHeadpic;
    protected ImageView ivPresenteVerified;
    protected TextView tvPresenterName;
    protected TextView tvPresenterEnglishname;
    protected AutoLinearLayout rlPublishCorePresenterInfo;
    protected ImageButton btLivePlayClose;
    @Inject
    LivePlayPresenter mPresenter;
    @Inject
    PublishCoreView mPublishCoreView;
    @Inject
    ZBLBaseFragment mPagerFragment;


    private ZBPlayClient mZBPlayClient = ZBPlayClient.getInstance();
    private AlertDialog.Builder mExitDialog;
    private AlertDialog mOptionDialog;

    private UserInfo presenterUser;//主播信息
    private ZBMediaController mediaController;
    private Subscription mSubscription;
    private View mUserInfoPopView = null;
    private CustomPopupWindow userInfoPop = null;//用户信息框\主播信息框

    @Override
    protected void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, //防止屏幕黑屏
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.zb_activity_live_play);
        initDialog();//初始化dialog
        mVideoPlayer = (ZBVideoView) findViewById(R.id.plvv_live_play_player);
        mPlaceHolder = (ImageView) findViewById(R.id.iv_live_play_place_holder);
        mPromptTV = (ImageView) findViewById(R.id.tv_live_play_prompt);
        ivLivePlayReplay = (ImageView) findViewById(R.id.iv_live_play_replay);
        ivLivePlayReplay.setOnClickListener(ZBLLivePlayActivity.this);
        mReplayCoverRL = (RelativeLayout) findViewById(R.id.rl_live_play_replay_recover);
        ivPresenterHeadpic = (ImageView) findViewById(R.id.iv_presenter_headpic);
        ivPresenteVerified = (ImageView) findViewById(R.id.iv_presente_verified1);
        tvPresenterName = (TextView) findViewById(R.id.tv_presenter_name);
        tvPresenterEnglishname = (TextView) findViewById(R.id.tv_presenter_englishname);
        rlPublishCorePresenterInfo = (AutoLinearLayout) findViewById(R.id.rl_publish_core_presenter_info);
        rlPublishCorePresenterInfo.setOnClickListener(ZBLLivePlayActivity.this);
        btLivePlayClose = (ImageButton) findViewById(R.id.bt_live_play_close);
        btLivePlayClose.setOnClickListener(ZBLLivePlayActivity.this);
    }


    @Override
    protected void initData() {
        DaggerLivePlayComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .livePlayModule(new LivePlayModule(this))
                .build()
                .inject(this);


        setPlaceHolderVisible(true);//设置进入是时的模糊效果和加载动画
        mPresenter.parseIntent(getIntent());
        start();
    }

    /**
     * 开始播放视频
     */
    private void start() {
        initPlayer();//初始化播放器
        SearchResult data = getData();//是充直播页面进来
        if (data == null) {
            presenterUser = getUserInfo();//重搜索页面进入
            if (presenterUser == null) {
                presenterUser = new UserInfo(new Icon());
            }
        } else {
            presenterUser = new UserInfo();
            presenterUser = data.user;
            presenterUser.location = data.stream.location;
        }
    }

    /**
     * 显示主播头像和名字
     */
    @Override
    public void showPresenterInfo() {
        rlPublishCorePresenterInfo.setVisibility(View.VISIBLE);
        UiUtils.glideDisplayWithTrasform(presenterUser.avatar != null && presenterUser.avatar.origin != null ? presenterUser.avatar.origin : "",
                ivPresenterHeadpic, new GlideCircleTrasform(UiUtils.getContext()));
        tvPresenterName.setText(presenterUser.uname);
        if (!TextUtils.isEmpty(presenterUser.location)) {
            tvPresenterEnglishname.setText(presenterUser.location);
        } else {
            tvPresenterEnglishname.setText("火星");
        }
        ivPresenteVerified.setVisibility(presenterUser.is_verified == 1 ? View.VISIBLE : View.GONE);
        mediaController.show();//更新播放按钮图片状态
    }


    private void initPlayer() {

        mPresenter.initListener();//设置播放器需要的监听
    }

    private void initDialog() {
        mExitDialog = new AlertDialog.Builder(this);//退出提示
        UiUtils.getExitDialog(mExitDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                killMyself();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setNegativeButton(getString(R.string.str_continue_wait), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mPresenter.isPlay())//开始播放了
                {
                    mZBPlayClient.reconnect();
                } else//还没开始播放
                {
                    mPresenter.showPlay();
                }
            }
        });
        builder.setPositiveButton(getString(R.string.str_retire), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                killMyself();
            }
        });
        mOptionDialog = builder.create();
        mOptionDialog.setMessage(getString(R.string.str_disconnect));
        mOptionDialog.setCanceledOnTouchOutside(false);

    }


    @Override
    public void onBackPressed() {
        killMyself();
    }


    /**
     * 显示主播个人信息
     */
    private void showPresenterInfo(final UserInfo userInfotmp) {

        if (userInfotmp == null) {
            return;
        }
        if (mUserInfoPopView == null) {
            mUserInfoPopView = LayoutInflater.from(this).inflate(R.layout.zb_pop_userinfo, null);
        }
        try {
            userInfoPop = CustomPopupWindow.newInstance(mUserInfoPopView, null, new CustomPopupWindow.CustomPopupWindowListener() {
                @Override
                public void initPopupView(View contentView) {
                    ((TextView) contentView.findViewById(R.id.tv_userinfo_name)).setText(userInfotmp.uname);
                    ((TextView) contentView.findViewById(R.id.tv_userinfo_city)).setText(TextUtils.isEmpty(userInfotmp.location) ? getString(R
                            .string.str_default_location) : userInfotmp.location);
                    UiUtils.glideDisplayWithTrasform(userInfotmp.avatar.getOrigin()
                            , (ImageView) contentView.findViewById(R.id.iv_userinfo_item_icon)
                            , new GlideCircleTrasform(getApplicationContext()));
                    contentView.findViewById(R.id.iv_userinfo_item_verified)
                            .setVisibility(userInfotmp.is_verified == 1 ? View.VISIBLE : View.GONE);
                    ((TextView) contentView.findViewById(R.id.tv_userinfo_intro)).setText(
                            TextUtils.isEmpty(userInfotmp.intro) ? getString(R.string.str_default_intro) : userInfotmp.intro
                    );
                    ((TextView) contentView.findViewById(R.id.tv_userinfo_zan_nums)).setText(userInfotmp.zan_remain + "");
                    ((TextView) contentView.findViewById(R.id.tv_userinfo_fans)).setText(userInfotmp.fans_count + "");
                    ((TextView) contentView.findViewById(R.id.tv_userinfo_attention)).setText(userInfotmp.follow_count + "");
                    contentView.findViewById(R.id.bt_userinfo_talk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 16/5/25 聊天
                        }
                    });
                    final FllowButtonView btAttention = (FllowButtonView) contentView.findViewById(R.id.bt_userinfo_attention);
                    if (userInfotmp.usid.equals(ZhiboApplication.getUserInfo().usid)) {
                        btAttention.setEnabled(false);
                        btAttention.setNameLeftDrawable(null);
                        btAttention.setName(getString(R.string.str_cant_follow_self));
                        btAttention.setBackgroundResource(R.drawable.shape_follow_button_enable);
                    } else {
                        btAttention.setEnabled(true);
                        btAttention.setVisibility(View.VISIBLE);
                        btAttention.setNameSize(14);
                        btAttention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO: 16/5/25 关注
                                mPresenter.follow(UserHomePresenter.isFollow(!(userInfotmp.is_follow == 1)));
                                userInfotmp.is_follow = userInfotmp.is_follow == 1 ? 2 : 1;

                            }
                        });
                        setAtteionStatus(userInfotmp.is_follow == 1, btAttention);
                    }

                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置关注按钮的状态
     *
     * @param isFollow
     * @param btAttention
     */
    private void setAtteionStatus(boolean isFollow, FllowButtonView btAttention) {
        if (isFollow) {//是否关注
            btAttention.setName(getString(R.string.str_already_follow));
            btAttention.setNameLeftDrawable(R.mipmap.ico_added_gz);
            btAttention.setBackgroundResource(R.drawable.shape_follow_button_enable);
        } else {
            btAttention.setName(getString(R.string.str_follow));
            btAttention.setNameLeftDrawable(null);
            btAttention.setBackgroundResource(R.drawable.shape_blue_solid);
        }
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
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        if (mPublishCoreView == null) {
            return;
        }
        mPublishCoreView.hidekeyboard();//隐藏软键盘
        if (mediaController != null) {
            mediaController.setEnabled(false);//回收
        }
        finish();
        this.overridePendingTransition(R.anim.animate_null, R.anim.slide_out_from_right);//动画
    }

    @Override
    public ZBVideoView getZBVideoView() {
        return mVideoPlayer;
    }

    @Override
    public ZBPlayClient getZBplayClient() {
        return mZBPlayClient;
    }


    @Override
    public void showCore() {
        if (!mPagerFragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_live_core, mPagerFragment);
            transaction.commit();
            mPagerFragment.showCompleted();
        }

    }

    public static boolean isLiveStreaming(String url) {
        if (url.startsWith("rtmp://")
                || (url.startsWith("http://") && url.endsWith(".m3u8"))
                || (url.startsWith("http://") && url.endsWith(".flv"))) {
            return true;
        }
        return false;
    }

    @Override
    public void setPlaceHolder(String url) {
        Bitmap bitmap = BitmapFactory.decodeResource(UiUtils.getResources()
                , R.mipmap.pic_photo_340);
        if (TextUtils.isEmpty(url)) {
//            setPlaceHolderVisible(false);
            mPlaceHolder.setImageBitmap(FastBlur.blurBitmap(bitmap, UiUtils.getScreenWidth()
                    , UiUtils.getScreenHeidth()));
        } else {
            UiUtils.glideWrap(url)
                    .placeholder(new BitmapDrawable(FastBlur.blurBitmap(bitmap, UiUtils.getScreenWidth()
                            , UiUtils.getScreenHeidth())))
                    .transform(new GaussianBlurTrasform(UiUtils.getContext()))
                    .into(mPlaceHolder);

        }
    }


    @Override
    public void setListener(ZBMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {

        mVideoPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);

    }

    @Override
    public void setPlaceHolderVisible(final boolean isVisible) {
        mSubscription = Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mPlaceHolder.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                        mPromptTV.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                        if (isVisible) {
                            ((AnimationDrawable) mPromptTV.getDrawable()).start();
                        } else {
                            ((AnimationDrawable) mPromptTV.getDrawable()).stop();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

//        tvTip.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 回放结束
     */
    @Override
    public void playComplete() {
        mVideoPlayer.seekTo(0);
        setCoverVisable(true);//显示蒙层
        mediaController.show();
        ((MediaController) mediaController).updatePausePlay(false);//更新播放按钮图片状态
    }

    @Override
    public void hideWarn() {
        if (mOptionDialog.isShowing()) {
            mOptionDialog.dismiss();
        }
        LogUtils.warnInfo(TAG, "hide....");
    }

    @Override
    public void setCoverVisable(boolean isVisable) {
        mReplayCoverRL.setVisibility(isVisable ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否开始播放
     *
     * @return
     */
    @Override
    public boolean isPlaying() {
        return mVideoPlayer.isPlaying();
    }

    @Override
    public void setFollowEnable(boolean isEnable) {
        rlPublishCorePresenterInfo.setEnabled(isEnable);
    }

    @Override
    public void setFollow(boolean isFollow) {
        presenterUser.is_follow = Integer.valueOf(UserHomePresenter.isFollow(isFollow));
        if (userInfoPop != null && userInfoPop.isShowing()) {
            FllowButtonView btAttention = (FllowButtonView) mUserInfoPopView.findViewById(R.id.bt_userinfo_attention);
            btAttention.setNameSize(14);
            setAtteionStatus(isFollow, btAttention);
            TextView tv_follows = (TextView) mUserInfoPopView.findViewById(R.id.tv_userinfo_fans);
            try {
                if (isFollow) {
                    tv_follows.setText(Integer.valueOf(tv_follows.getText().toString()) + 1 + "");

                } else {

                    tv_follows.setText(Integer.valueOf(tv_follows.getText().toString()) - 1 + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            showPresenterInfo(presenterUser);
        }
    }

    @Override
    public void setMediaController(ZBMediaController mediaController) {
        this.mediaController = mediaController;
        ((MediaController) mediaController).setOnShareClickListener(mOnShareClickListener);
        ((MediaController) mediaController).setOnShownListener(mOnShownListener);
        ((MediaController) mediaController).setOnHiddenListener(mOnHiddenListener);
        ((MediaController) mediaController).setOnPlayClickListener(mOnPlayClickListener);
        mVideoPlayer.setMediaController(mediaController);
    }

    /**
     * 点击分享
     */
    private MediaController.OnShareClickListener mOnShareClickListener = new MediaController.OnShareClickListener() {
        @Override
        public void onShareClick(View v) {

            mPresenter.showshare(presenterUser, ZBLLivePlayActivity.this);
        }
    };
    /**
     * 控制条显示
     */
    private MediaController.OnShownListener mOnShownListener = new MediaController.OnShownListener() {
        @Override
        public void onShown() {
            btLivePlayClose.setVisibility(View.VISIBLE);
        }
    };
    /**
     * 控制条隐藏
     */
    private MediaController.OnHiddenListener mOnHiddenListener = new MediaController.OnHiddenListener() {
        @Override
        public void onHidden() {
            btLivePlayClose.setVisibility(View.INVISIBLE);
        }
    };

    private MediaController.OnPlayClickListener mOnPlayClickListener = new MediaController.OnPlayClickListener() {
        @Override
        public void onPlayClick(View v) {
            setCoverVisable(false);//隐藏蒙层
        }
    };


    @Override
    public void setWarnMessage(String warn) {
        mOptionDialog.setMessage(warn);
    }

    @Override
    public void showWarn() {
        try {
            if (!mOptionDialog.isShowing()) {
                mOptionDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        mZBPlayClient.onResume();
        if (mediaController != null) {
            ((MediaController) mediaController).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.pause();
        mZBPlayClient.onPause();
        if (mediaController != null) {
            ((MediaController) mediaController).pausePlayer();
        }

    }

    @Override
    protected void onDestroy() {
        mZBPlayClient.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
        }
        mediaController = null;
        if (mOptionDialog != null) {
            if (mOptionDialog.isShowing()) {
                mOptionDialog.dismiss();
            }
            mOptionDialog = null;
        }


        mPresenter.onDestroy();
        DeviceUtils.fixInputMethodManagerLeak(getApplicationContext());
        super.onDestroy();
    }

    @Override
    public ZBApiImInfo getImInfo() {
        return mPresenter.getImInfo();
    }

    @Override
    public SearchResult getData() {
        return mPresenter.getData();
    }

    @Override
    public UserInfo getUserInfo() {
        return mPresenter.getUserInfo();
    }

    @Override
    public void switchCamera() {

    }

    @Override
    public ZBLBaseFragment getPublishCoreview() {
        return mPublishCoreView.getCoreFragment();
    }

    @Override
    public boolean isSelfClose() {
        return false;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_live_play_replay) {
            if (mZBPlayClient != null) {
                mZBPlayClient.reStartConnect();//播放视频
                setCoverVisable(false);//隐藏蒙层
            }
        } else if (view.getId() == R.id.rl_publish_core_presenter_info) {
            /**
             * 点击主播头像
             */
            showPresenterInfo(presenterUser);
        } else if (view.getId() == R.id.bt_live_play_close) {
            killMyself();
        }
    }
}
