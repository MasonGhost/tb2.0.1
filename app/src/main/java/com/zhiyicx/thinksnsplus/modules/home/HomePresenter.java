package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.os.Parcelable;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.appprocess.BackgroundUtil;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletConfigBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
class HomePresenter extends AppBasePresenter<HomeContract.Repository, HomeContract.View> implements HomeContract.Presenter, EMConnectionListener, EMMessageListener {
    @Inject
    AuthRepository mAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    WalletConfigBeanGreenDaoImpl mWalletConfigBeanGreenDao;
    @Inject
    ChatRepository mChatRepository;

    @Inject
    public HomePresenter(HomeContract.Repository repository, HomeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void initIM() {
        if (isLogin()) {
            mAuthRepository.loginIM();
            EMClient.getInstance().addConnectionListener(this);
            EMClient.getInstance().chatManager().addMessageListener(this);
        }
        LogUtils.e("-----------" + AppApplication.getMyUserIdWithdefault());
        LogUtils.e("userinfo : " + mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
    }

    /*******************************************
     * 聊天相关回调
     *********************************************/

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE)
    public void setMessageTipVisable(boolean isShow) {
        mRootView.setMessageTipVisable(isShow);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MINE_TIP_VISABLE)
    public void setMineTipVisable(boolean isShow) {
        mRootView.setMineTipVisable(isShow);
    }

    /**
     * IM 消息同步
     *
     * @param authData
     */
    private void synIMMessage(AuthData authData) {
        if (authData.getSeqs() != null) {
            // 消息同步
            Observable.from(authData.getSeqs())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(seqsBean -> {
                        Message message = MessageDao.getInstance(mContext).getLastMessageByCid(seqsBean.getCid());
                        if (message != null && message.getSeq() < seqsBean.getSeq()) {
                            ZBIMClient.getInstance().syncAsc(message.getCid(), message.getSeq(), seqsBean.getSeq(), (int) System.currentTimeMillis());
                        } else {
                            ZBIMClient.getInstance().syncAsc(seqsBean.getCid(), 0, seqsBean.getSeq(), (int) System.currentTimeMillis());
                        }
                    }, Throwable::printStackTrace);
        }
    }

    @Override
    public void onConnected() {
        EventBus.getDefault().post("", EventBusTagConfig.EVENT_IM_ONCONNECTED);
    }

    @Override
    public void onDisconnected(int error) {
        EventBus.getDefault().post(error, EventBusTagConfig.EVENT_IM_ONDISCONNECT);
    }

    @Override
    public boolean isLogin() {
        return mAuthRepository.isLogin();
    }

    @Override
    public boolean handleTouristControl() {
        if (isLogin()) {
            return false;
        } else {
            mRootView.showLoginPop();
            return true;
        }
    }


    /*******************************************  签到  *********************************************/


    /**
     * @param isClick
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_CHECK_IN_CLICK)
    public void checkInClick(boolean isClick) {
        CheckInBean checkInBean = mRootView.getCheckInData();
        if (checkInBean != null) {
            mRootView.showCheckInPop(checkInBean);
        } else {
            mRootView.showCenterLoading(mContext.getString(R.string.loading));
            getCheckInInfo();
        }
    }

    @Override
    public void checkIn() {
        Subscription subscription = mUserInfoRepository.checkIn()
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        getCheckInInfo();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getCheckInInfo() {

        Subscription subscription = mUserInfoRepository.getCheckInInfo()
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideCenterLoading();
                    }
                })
                .subscribe(new BaseSubscribeForV2<CheckInBean>() {
                    @Override
                    protected void onSuccess(CheckInBean data) {
                        mRootView.showCheckInPop(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public double getWalletRatio() {
        return mWalletConfigBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()).getRatio();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(this);
        EMClient.getInstance().chatManager().removeMessageListener(this);
        ChatClient.getInstance(mContext).onDestroy();
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        LogUtils.d("Cathy", " 收到消息 :" + list);
        // 收到消息，更新会话列表
        Observable.just(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageList -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED_V2, (ArrayList<? extends Parcelable>) messageList);
                    EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED_V2);
                    setMessageTipVisable(true);
                });
        // 应用在后台，则推送通知
        if (!BackgroundUtil.getAppIsForegroundStatus()) {
            // 手动创建聊天item，从数据库取出用户信息
            List<ChatItemBean> chatItemBeans = new ArrayList<>();
            for (EMMessage message : list){
                ChatItemBean chatItemBean = new ChatItemBean();
                chatItemBean.setMessage(message);
                chatItemBean.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache
                        (Long.parseLong("admin".equals(message.getFrom()) ? "1" : message.getFrom())));
                chatItemBeans.add(chatItemBean);
            }
            // 遍历返回的信息，如果有用户信息为空的 证明数据库中没有此用户，从服务器取用户信息
            for (ChatItemBean chatItemBean : chatItemBeans){
                Observable.just(chatItemBean)
                        .flatMap(chatItemBean1 -> {
                            if (chatItemBean1.getUserInfo() == null){
                                List<ChatItemBean> chatItemBeanList = new ArrayList<>();
                                chatItemBeanList.add(chatItemBean1);
                                return mChatRepository.completeUserInfo(chatItemBeanList)
                                        .map(list1 -> list1.get(0));
                            }
                            return Observable.just(chatItemBean1);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(chatItemBean12 -> {
                            JpushMessageBean jpushMessageBean = new JpushMessageBean();
                            jpushMessageBean.setType(JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM);
                            String content = chatItemBean12.getMessage().getBody().toString();
                            // 目前只有单聊，别的还没定
                            if (chatItemBean12.getMessage().getBody() instanceof EMTextMessageBody){
                                content = ((EMTextMessageBody) chatItemBean12.getMessage().getBody()).getMessage();
                            }
                            jpushMessageBean.setMessage(chatItemBean12.getUserInfo().getName() + ":" + content);
                            jpushMessageBean.setNofity(false);
                            NotificationUtil.showChatNotifyMessage(mContext, jpushMessageBean, chatItemBean12.getUserInfo());
                        });
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        // 收到透传消息
        LogUtils.d("Cathy", " 收到透传消息 :" + list);
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        // 收到已读回执
        LogUtils.d("Cathy", " 收到已读回执 :" + messages);
    }

    @Override
    public void onMessageDelivered(List<EMMessage> message) {
        // 收到已送达回执
        LogUtils.d("Cathy", " 收到已送达回执 :" + message);
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        // 消息被撤回
        LogUtils.d("Cathy", " 消息被撤回 :" + messages);
    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        // 消息状态变动
        LogUtils.d("Cathy", " 消息状态变动 :" + message + "change : " + change);
    }
}