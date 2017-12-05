package com.zhiyicx.zhibolibrary.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.old.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.PublishCoreModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLEndStreamingActivity;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.entity.MenuEntity;
import com.zhiyicx.zhibolibrary.ui.holder.LiveChatTextListHolder;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.util.SensitivewordFilter;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;
import com.zhiyicx.zhibosdk.manage.ZBPlayClient;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.manage.listener.OnIMMessageTimeOutListener;
import com.zhiyicx.zhibosdk.manage.listener.OnImListener;
import com.zhiyicx.zhibosdk.manage.listener.OnImStatusListener;
import com.zhiyicx.zhibosdk.model.entity.ZBGift;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/5/11.
 */
@ActivityScope
public class PublishCorePresenter extends BasePresenter<PublishCoreModel, PublishCoreView> implements OnImListener, OnImStatusListener,
        OnIMMessageTimeOutListener, OnShareCallbackListener {
    private Subscription mSubscription;
    private Subscription mGiftRank;
    private Subscription mUserInfoSubscription;
    private Subscription mUserinfoSubscription;
    private Subscription mRecomListSubscription;
    private UmengSharePolicyImpl mSharePolicy;
    private BaseJson<SearchResult[]> mApiList;//礼物排行榜数据
    private boolean isJoinedChatRoom = true;//主播是否进入聊天室
    private SearchResult[] mRecommendDatas;//推荐直播数据


    @Inject
    public PublishCorePresenter(PublishCoreModel model, PublishCoreView rootView) {
        super(model, rootView);
        this.mSharePolicy = new UmengSharePolicyImpl(rootView.getCurrentActivity());
        mSharePolicy.setOnShareCallbackListener(this);
        setIMListioner();
        initSensitiveWordFilter();
    }

    /**
     * 不同端设置不同的IM消息监听
     */
    private void setIMListioner() {
        if (mRootView.isPresenter()) {
            ZBStreamingClient.getInstance().setOnImListener(this);
            ZBStreamingClient.getInstance().setOnImStatusListener(this);
            ZBStreamingClient.getInstance().setOnIMMessageTimeOutListener(this);
        } else {
            ZBPlayClient.getInstance().setOnImListener(this);
            ZBPlayClient.getInstance().setOnImStatusListener(this);
            ZBPlayClient.getInstance().setOnIMMessageTimeOutListener(this);
        }
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
     * 当前主播的uid
     *
     * @param uid
     */
    public void getRecomList(final String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("stream_uid", uid);

        mRecomListSubscription = ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_RECOMME_LIVE_LIST, map)
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
                            try {
                                lauchEnd(apiList.data, uid, mRootView.getChatRoomDataCount().getReviewCount());
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                            }
                        } else {
                            mRootView.showMessage(apiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        try {
                            lauchEnd(new SearchResult[]{}, uid, mRootView.getChatRoomDataCount().getReviewCount());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                    }
                });
    }


    /**
     * 获取赠送排行榜数据
     *
     * @param isMore
     * @param mPage
     * @param usid
     */
    public void getList(final boolean isMore, int mPage, String usid) {
        if (mGiftRank != null && !mGiftRank.isUnsubscribed()) {
            mGiftRank.unsubscribe();
        }
        mGiftRank = mModel.getGiftRank(
                usid + "", mPage)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<BaseJson<SearchResult[]>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(BaseJson<SearchResult[]> apiList) {
                        mApiList = apiList;
                        if (apiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            /**
                             * 通过usid获取用户信息
                             */
                            StringBuilder usid = new StringBuilder();
                            for (SearchResult searchResult : mApiList.data) {
                                usid.append(searchResult.user.usid).append(",");
                            }
                            if (usid.length() > 0) {
                                return mModel.getUsidInfo(usid.toString(), "")
                                        .map(new Func1<BaseJson<UserInfo[]>, Boolean>() {
                                            @Override
                                            public Boolean call(BaseJson<UserInfo[]> baseJson) {
                                                for (int i = 0; i < baseJson.data.length; i++) {
                                                    baseJson.data[i].setGold(mApiList.data[i].user.getGold());
                                                    mApiList.data[i].user = baseJson.data[i];
                                                }
                                                return true;
                                            }
                                        });
                            }
                        }
                        return Observable.just(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isSuccess) {
                        if (isSuccess) {
                            mRootView.giftRankrefresh(mApiList, isMore);//刷新数据
                        } else {
                            mRootView.showMessage(mApiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                    }
                });

    }

    /**
     * 通过uid获取用户信息
     *
     * @param usid
     */
    public void getUserInfo(String usid) {
        mUserInfoSubscription = mModel.getUsidInfo(usid, null)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRootView.setClickable(false, 0);
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.setClickable(true, 0);
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new Action1<BaseJson<UserInfo[]>>() {
                    @Override
                    public void call(BaseJson<UserInfo[]> apiList) {
                        if (apiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            if (apiList.data.length > 0) {
                                mRootView.updatePresenterInfo(apiList.data[0]);
                            }
                        } else {
                            mRootView.showMessage(apiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        mRootView.setClickable(true, 0);
                        mRootView.hideLoading();
                    }
                });


    }

    /**
     * 增加一条直播规则
     */
    public void initZhiboRules() {
        if (ZBLApi.sZBApiConfig.stream_notice != null && ZBLApi.sZBApiConfig.stream_notice.size() > 0) {
            sendTipmsg(ZBLApi.sZBApiConfig.stream_notice.get(0).text);
        } else {
            sendTipmsg(UiUtils.getString(R.string.str_chat_rule));
        }
    }


    /**
     * 公告信息
     *
     * @param text
     */
    public void sendTipmsg(String text) {
        UserMessage usermesage = new UserMessage(new UserInfo(), new Message());
        usermesage.msg.type = MessageType.MESSAGE_TYPE_TIP;
        usermesage.msg.txt = text;
        mRootView.addChat(usermesage);

    }

    /**
     * 发送信息
     *
     * @param text
     */
    public void sendTextmsg(String text) {
        if (ZhiboApplication.filter != null) {
            text = ZhiboApplication.filter.replaceSensitiveWord(text, SensitivewordFilter.minMatchTYpe, ZBLApi.sZBApiConfig.filter_word_conf
                    .filter_word_replace);
        } else {
            ZhiboApplication.initFilterWord();
            mRootView.showMessage(UiUtils.getString("str_network_error_action"));
            return;
        }
        if (mRootView.isPresenter()) {
            mModel.sendTextMsg(text, true);
        } else {
            mModel.sendTextMsg(text, false);
        }

    }

    /**
     * 送礼物
     *
     * @param gift
     */
    public void sendGiftMessage(Map gift, String gift_code, String count, final OnCommonCallbackListener l) {

        mModel.sendGiftMessage(gift, gift_code, count, l);
    }

    /**
     * 关注
     */
    public void sendAttention() {

        mModel.sendAttention();
    }

    /**
     * 送zan
     *
     * @param type
     */
    public void sendZan(int type) {
        mModel.sendZan(type);
    }


    /**
     * 通过usid获取用户信息
     *
     * @param message
     * @param field
     */
    public void getLocalUserInfo(final Message message, String field) {
        if (message == null || message.ext == null || ZhiboApplication.getUserInfo() == null) {
            return;
        }
        mSubscription = mModel.getUsidInfo(message.ext.ZBUSID, field)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Action1<BaseJson<UserInfo[]>>() {
                    @Override
                    public void call(BaseJson<UserInfo[]> users) {
                        if (users.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mRootView.saveAndAddChat(users, message);
                        } else {
                            mRootView.showMessage(users.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户

                    }
                });

    }

    /**
     * 主播设置禁言
     *
     * @param usid
     * @param time
     */
    public void imDisable(String usid, int time) {
        ZBStreamingClient.getInstance().imDisable(usid, time, new OnCommonCallbackListener() {
            @Override
            public void onSuccess() {
                mRootView.showMessage(UiUtils.getString("str_banned_success"));
                mRootView.dimissImDisablePop();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
            }

            @Override
            public void onFail(String code, String message) {
                mRootView.showMessage(message);
            }
        });


    }


    /**
     * 禁言
     *
     * @param gag
     */
    private void disableSendMsg(long gag) {
        if (gag == 0) {//永久
            mRootView.disableSendMsgEver();
        } else if (gag > 0) {//时段
            mRootView.disableSendMsgSomeTime(gag);

        }
    }


    @Subscriber(tag = LiveChatTextListHolder.EVENT_HEADPIC_CLICK, mode = ThreadMode.MAIN)
    public void handleHeadpicClickEvent(int position) {
        mRootView.getClickPresenterInfo(position);
    }

    private void updateUserCount(UserInfo data) {
        ZhiboApplication.getUserInfo().setGold(data.getGold());
        ZhiboApplication.getUserInfo().follow_count = data.follow_count;
        ZhiboApplication.getUserInfo().fans_count = data.fans_count;
        ZhiboApplication.getUserInfo().zan_remain = data.zan_remain;
        mRootView.updatedGold();//刷新金币信息
    }


    /**
     * 跳转到结束页面
     *
     * @param datas
     * @param uid
     * @param viewcount 观看次数
     */
    private void lauchEnd(SearchResult[] datas, final String uid, final int viewcount) throws Exception {

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
                            doEnd(mRecommendDatas, uid, viewcount);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            doEnd(new SearchResult[]{}, uid, viewcount);
                        }
                    });
        } else {
            doEnd(mRecommendDatas, uid, viewcount);
        }
    }

    private void doEnd(SearchResult[] datas, String uid, int viewcount) {
        Intent intent = new Intent(UiUtils.getContext(), ZBLEndStreamingActivity.class);
        intent.putExtra("isAudience", true);//是否为观众
        intent.putExtra("userId", uid);//uid用于关注用户
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", new ApiList(datas));
        bundle.putSerializable("presenter", mRootView.getPresenterInfo());
        bundle.putInt("view_count", viewcount);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
        mRootView.killMyself();
    }

    /**
     * 分享
     */
    public void showshare(UserInfo presenterUser, Context context) {
        mSharePolicy.setShareContent(UserInfo.getShareContentByUserInfo(presenterUser));

        if (mSharePolicy.getShareContent() == null || mSharePolicy.getShareContent().getImage() == null) {
            mSharePolicy.showShare(((Fragment) mRootView).getActivity());

        } else {
            Glide.with(UiUtils.getContext()).load(mSharePolicy.getShareContent().getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mSharePolicy.getShareContent().setBitmap(resource);
                    mSharePolicy.showShare(((Fragment) mRootView).getActivity());
                }
            });
        }
    }

    /**
     * 礼物配置
     *
     * @param context
     * @return
     */
    public List<MenuEntity> getGiftPic(Context context) {
        ArrayList<MenuEntity> list = new ArrayList<>();
        List<ZBGift> gifts = ZBLApi.sZBApiConfig.gift_list;
        for (int i = 0; i < gifts.size(); i++) {
            list.add(new MenuEntity(gifts.get(i).image, gifts.get(i).name, null, gifts.get(i).gold, gifts.get(i).gift_code));
            mRootView.saveGiftConfigCach(gifts.get(i));
        }
        return list;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mSubscription);
        unSubscribe(mGiftRank);
        unSubscribe(mUserInfoSubscription);
        unSubscribe(mRecomListSubscription);
        unSubscribe(mUserinfoSubscription);
        if (mSharePolicy != null) {
            mSharePolicy.setOnShareCallbackListener(null);
            mSharePolicy = null;
        }

    }

    /**
     * 禁言消息
     *
     * @param gag
     */
    @Override
    public void onBanned(long gag) {
        disableSendMsg(gag);
        mRootView.setbanneded(true, gag);

    }

    /**
     * 收到消息
     *
     * @param message
     */
    @Override
    public void onMessageReceived(Message message) {
        LogUtils.v("taglei", "onMessageReceived" + message.type);
        mRootView.handleMessage(message);
        switch (message.type) {
            //文本消息
            case MessageType.MESSAGE_TYPE_TEXT:

                if (!TextUtils.isEmpty(message.txt)) {
                    mRootView.recievedTextMessage(message);
                }
                break;
            case MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE:
                if (null != message.ext.custom) {
                    mRootView.receivedVoteMessage(message);
                }
                break;
            case MessageType.MESSAGE_TYPE_CUSTOM_DISABLE:

                break;
            default:
                break;

        }
    }

    /**
     * 收到礼物消息
     *
     * @param message
     */
    @Override
    public void onGiftReceived(Message message) {
        mRootView.recievedGiftMessage(message);
    }

    /**
     * 收到赞消息
     *
     * @param message
     */
    @Override
    public void onZanReceived(Message message) {

        mRootView.recievedZanMessage(message);
    }

    /**
     * 收到关注消息
     *
     * @param message
     */
    @Override
    public void onAttentionMessageReceived(Message message) {
        mRootView.recievedFllowMessage(message);
    }

    /**
     * 收到有人进入房间广播消息
     *
     * @param usid
     */
    @Override
    public void onSomeBodyJoinMessageReceived(String usid) {

    }

    /**
     * 收到有人离开广播消息
     *
     * @param usid
     */
    @Override
    public void onSomeBodyLeaveMessageReceived(String usid) {

    }

    /**
     * 收到房间信息统计消息
     *
     * @param chatRoomDataCount
     */
    @Override
    public void onChatRoomDataCountReceived(ChatRoomDataCount chatRoomDataCount) {
        mRootView.getChatroomMc(chatRoomDataCount);
    }

    /**
     * 发送消息回执
     *
     * @param message
     */
    @Override
    public void onMessageACK(Message message) {
        if (message.type == MessageType.MESSAGE_TYPE_TEXT) {
            mRootView.addSelfChat(true, message);
        }


    }

    /**
     * 主播结束直播消息
     *
     * @param cid
     */
    @Override
    public void onConvrEnd(int cid) {
        mRootView.convrEnd(cid);
    }

    @Override
    public void onSystemMessageReceived(String message) {
        sendTipmsg(message);
    }

    @Override
    public void onJoinRoomSuccessed() {
    }


    /**
     * 发送消息超时
     *
     * @param message
     */
    @Override
    public void onMessageTimeout(Message message) {
        mRootView.receivedTimeOutMessage(message);
        if (message.type == MessageType.MESSAGE_TYPE_TEXT) {
            sendTipmsg("\"" + message.txt + "\"" + UiUtils.getString("str_im_send_text_timeout"));
        }
    }

    /**
     * IM连接成功消息
     */
    @Override
    public void onConnected() {
        isJoinedChatRoom = true;
        sendTipmsg(UiUtils.getString("str_im_reconnect_successe_tip"));
    }

    /**
     * IM断开消息
     *
     * @param code
     * @param reason
     */
    @Override
    public void onDisconnect(int code, String reason) {
        isJoinedChatRoom = false;
        sendTipmsg(UiUtils.getString("str_im_reconnect_tip"));
    }


    /**
     * 更新本地金币数量（送出礼物后）
     *
     * @param decrice
     */
    public void refreshGoldCountReduce(int decrice) {
        UserInfo info = ZhiboApplication.getUserInfo();
/// 本地处理比例，目前是服务器处理
//        info.setGold(info.getGold() - (int)(ZhiboApplication.getUserInfo().getAppConfigInfoFromLocal()
//                .getWallet_ratio() * decrice / PayConfig.RATIO_UNIT));
        info.setGold(info.getGold() - decrice);
        ZhiboApplication.setUserInfo(info);
    }

    public boolean isJoinedChatRoom() {
        return isJoinedChatRoom;
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
