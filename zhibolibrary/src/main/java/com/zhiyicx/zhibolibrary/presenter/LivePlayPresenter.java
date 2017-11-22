package com.zhiyicx.zhibolibrary.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.LivePlayModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLEndStreamingActivity;
import com.zhiyicx.zhibolibrary.ui.components.MediaController;
import com.zhiyicx.zhibolibrary.ui.view.LivePlayView;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.manage.listener.OnGiftConfigCallback;
import com.zhiyicx.zhibosdk.manage.listener.OnPlayStartListenr;
import com.zhiyicx.zhibosdk.manage.listener.OnVideoPlayCompletionListener;
import com.zhiyicx.zhibosdk.manage.listener.OnVideoStartPlayListener;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBGift;
import com.zhiyicx.zhibosdk.policy.OnReconnetListener;
import com.zhiyicx.zhibosdk.widget.ZBMediaPlayer;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/4/1.
 */
@ActivityScope
public class LivePlayPresenter extends BasePresenter<LivePlayModel, LivePlayView> implements OnShareCallbackListener {

    private SearchResult mData;
    private String mIconUrl;
    public static final long PROMPT_SPAN_TIME = 5 * 1000;

    private UserInfo mUserInfo;
    private boolean isVideo;
    private String mVid;
    private String mSid;
    private boolean isPlay;//是否已经开始播放
    private ZBApiImInfo mZBApiImInfo;
    private Subscription mfollowSubscription;
    private Subscription mUserinfoSubscription;
    private SearchResult[] mRecommendDatas;//推荐直播数据

    @Inject
    public LivePlayPresenter(LivePlayModel model, LivePlayView rootView) {
        super(model, rootView);

    }

    /**
     *
     */
    private void initCallBack() {
        /**
         * 播放开始时回调
         */

        mRootView.getZBplayClient().setOnPlayStartListenr(new OnPlayStartListenr() {
            @Override
            public void onPlayStart() {

                mRootView.setPlaceHolderVisible(false);
                if (!isVideo) {
                    getGiftConfig();
                }
                else {
                    //设置播放控制器
                    mRootView.setMediaController(new MediaController(UiUtils.getContext(), false, false));
                    mRootView.showPresenterInfo();
                }
            }
        });
        mRootView.getZBplayClient().setReconnetListener(new OnReconnetListener() {
            @Override
            public void reconnectStart() {
                mRootView.setPlaceHolderVisible(true);
            }

            @Override
            public void reconnectScuccess() {
                mRootView.setPlaceHolderVisible(false);
                mRootView.hideWarn();
            }

            @Override
            public void reConnentFailure() {
                mRootView.showWarn();
                mRootView.setPlaceHolderVisible(false);
            }
        });

        if (isVideo) {
            mRootView.getZBplayClient().setOnVideoPlayCompletionListener(new OnVideoPlayCompletionListener() {
                @Override
                public void onVideoPlayCompleted() {
                    mRootView.playComplete();
                }
            });
        }

    }

    private ZBMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new ZBMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            LogUtils.debugInfo(TAG, "onVideoSizeChanged: " + width + "," + height);
        }
    };

    /**
     * 分享
     */
    public void showshare(UserInfo presenterUser, Activity context) {
        UmengSharePolicyImpl sharePolicy = new UmengSharePolicyImpl(context);
        sharePolicy.setOnShareCallbackListener(this);
        sharePolicy.setShareContent(UserInfo.getShareContentByUserInfo(presenterUser));
        sharePolicy.showShare(context);
    }

    /**
     * 设置播放器需要的监听
     */
    public void initListener() {
        mRootView.setListener(
                mOnVideoSizeChangedListener
        );
    }

    @Subscriber(tag = "net_change_not_wifi", mode = ThreadMode.MAIN)
    public void notWifiOpen(boolean b) {//非wifi情况,提示用户
        UiUtils.makeText(UiUtils.getString(R.string.str_not_wifi_prompt));
    }


    public void parseIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        isVideo = bundle.getBoolean("isVideo", false);

        if (isVideo) {//回放
            mUserInfo = (UserInfo) bundle.getSerializable("userInfo");
            mIconUrl = bundle.getString("iconUrl");
            mVid = bundle.getString("vid");
        }
        else {//直播
            mData = (SearchResult) bundle.getSerializable("data");//用户数据
            if (mData == null) {//搜索页面跳转而来
                mUserInfo = (UserInfo) bundle.getSerializable("userInfo");
                mIconUrl = bundle.getString("iconUrl");
                mSid = bundle.getString("sid");
            }
            else {//直播列表页面跳转而来
                if (mData.stream != null) {//直播页面
                    mUserInfo = mData.user;
                    mIconUrl = mData.stream.icon.getOrigin();
                }
            }

        }
        mRootView.setPlaceHolder(mIconUrl);
        showPlay();//开始播放

    }

    /**
     * 开始放映
     */
    public void showPlay() {
        initCallBack();//初始化监听器
        if (isVideo) {//回放
            startVideo(mVid);
        }
        else {//直播
            if (mData == null) {//搜索页面跳转而来
                startPlay(mUserInfo.uid, mSid);
            }
            else {//直播列表页面跳转而来
                startPlay(mData.user.uid, mData.stream.id);
            }
        }
        isPlay = true;
    }


    /**
     * 开始播放
     *
     * @param uid
     * @param streamId
     */
    private void startPlay(final String uid, String streamId) {
        mRootView.getZBplayClient().startLive(mRootView.getZBVideoView(), uid, streamId, new OnVideoStartPlayListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(String code, String message) {
                mRootView.showWarn();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRootView.killMyself();
                    }
                }, 1500);

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                mRootView.showMessage(UiUtils.getString("str_net_erro"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRootView.killMyself();
                    }
                }, 1500);

            }

            @Override
            public void onLiveEnd(String jsonstr, String uid) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonstr);

                    lauchEnd((SearchResult[]) new Gson().fromJson(jsonObject.getJSONObject("data").getString("list"), new TypeToken<SearchResult[]>() {
                    }.getType()), uid, (int) jsonObject.getJSONObject("data").getInt("view_count"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    mRootView.killMyself();
                }

            }
        });


    }

    public void getGiftConfig() {
        ZBInitConfigManager.getGiftConfig(new OnGiftConfigCallback() {
            @Override
            public void onSuccess(List<ZBGift> data) {
                ZBLApi.sZBApiConfig.gift_list = data;
                downloadGiftImage();
                mRootView.showCore();
            }

            @Override
            public void onFail(String code, String message) {
                mRootView.showMessage(message);
            }

            @Override
            public void onError(Throwable throwable) {
                mRootView.showMessage(UiUtils.getString("str_net_erro"));
            }
        });
    }

    /**
     * 预先下载礼物图片
     */
    public void downloadGiftImage() {
        if (ZBLApi.sZBApiConfig != null && ZBLApi.sZBApiConfig.gift_list != null && ZBLApi.sZBApiConfig.gift_list.size() > 0) {
            for (final ZBGift value : ZBLApi.sZBApiConfig.gift_list) {

                if (TextUtils.isEmpty(DataHelper.getStringSF(value.image,UiUtils.getContext()))) {
                    mModel.downloadFile(value.image).
                            subscribeOn(Schedulers.io()).
                            observeOn(Schedulers.io())
                            .subscribe(new Action1<ResponseBody>() {
                                @Override
                                public void call(ResponseBody responseBody) {
                                    boolean isDownload = UiUtils.writeResponseBodyToDisk(responseBody, value.image);
                                    if (isDownload) {
                                        DataHelper.SetStringSF(value.image, value.image, UiUtils.getContext());
                                    }
                                    LogUtils.debugInfo(TAG,"isDownload = " + isDownload);

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


    /**
     * 当前主播的uid
     *
     * @param uid
     */
    public void getRecomList(final String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("stream_uid", uid);
        ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_RECOMME_LIVE_LIST, map)
                .subscribeOn(Schedulers.io())
                .map(new Func1<JsonObject, ApiList>() {
                    @Override
                    public ApiList call(JsonObject jsonObject) {
                        return new Gson().fromJson(jsonObject, ApiList.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiList>() {
                    @Override
                    public void call(ApiList apiList) {
                        if (apiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            lauchEnd(apiList.data, uid, 100);
                        }
                        else {
                            mRootView.showMessage(apiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        lauchEnd(null, uid, 100);
                    }
                });
    }

    /**
     * 跳转到结束页面
     *
     * @param datas
     * @param uid
     */
    private void lauchEnd(SearchResult[] datas, final String uid, final int viewCount) {
        mRecommendDatas = datas;
        /**
         * 通过usid获取用户信息
         */
        String usid = "";
        for (SearchResult searchResult : datas) {
            usid += searchResult.user.usid + ",";
        }

        if (usid.length() > 0) {
            usid = usid.substring(0, usid.length() - 1);
        }
        if (usid.length() > 0) {
            mUserinfoSubscription = mModel.getUsidInfo(usid, "")
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseJson<UserInfo[]>>() {
                        @Override
                        public void call(BaseJson<UserInfo[]> baseJson) {
                            for (int i = 0; i < baseJson.data.length; i++) {
                                mRecommendDatas[i].user = baseJson.data[i];
                            }
                            doEnd(mRecommendDatas, uid, viewCount);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            mRootView.showMessage(UiUtils.getString("str_net_erro"));
                            mRootView.killMyself();
                        }
                    });
        }
        else {
            doEnd(mRecommendDatas, uid, viewCount);
        }
    }

    private void doEnd(SearchResult[] datas, String uid, int viewCount) {
        Intent intent = new Intent(UiUtils.getContext(), ZBLEndStreamingActivity.class);
        intent.putExtra("isAudience", true);//是否为观众
        intent.putExtra("userId", uid);//uid用于关注用户
        Bundle bundle = new Bundle();

        bundle.putSerializable("info", new ApiList(datas));
        bundle.putSerializable("presenter", mUserInfo);
        bundle.putInt("view_count", viewCount);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
        mRootView.killMyself();//关闭本页面
    }


    /**
     * 开始播放回放
     *
     * @param vid video id
     */
    private void startVideo(String vid) {
        mRootView.getZBplayClient().startVedio(mRootView.getZBVideoView(), vid, new OnVideoStartPlayListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(String code, String message) {
                mRootView.showMessage(message);
                mRootView.killMyself();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                mRootView.showMessage(UiUtils.getString("str_net_erro"));
                mRootView.killMyself();
            }

            @Override
            public void onLiveEnd(String jsonstr, String uid) {

            }

        });

    }

    public void follow(String action) {
        //设置关注按钮状态
        mfollowSubscription = mModel.followUser(action, mUserInfo.usid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRootView.setFollowEnable(false);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.setFollowEnable(true);
                    }
                }).subscribe(new Action1<BaseJson<FollowInfo>>() {
                    @Override
                    public void call(BaseJson<FollowInfo> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mRootView.setFollow(UserHomePresenter.isFollow(json.data.is_follow));//设置关注按钮状态
                            mUserInfo.is_follow = json.data.is_follow;
                        }
                        else {
                            mRootView.showMessage(json.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.setFollowEnable(true);
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));
                    }
                });
    }

    public ZBApiImInfo getImInfo() {
        return this.mZBApiImInfo;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    public SearchResult getData() {
        return this.mData;
    }

    public boolean isPlay() {
        return this.isPlay;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mfollowSubscription);
        unSubscribe(mUserinfoSubscription);

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
