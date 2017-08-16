package com.zhiyicx.thinksnsplus.modules.home;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.appprocess.BackgroundUtil;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
class HomePresenter extends BasePresenter<HomeContract.Repository, HomeContract.View> implements HomeContract.Presenter, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    @Inject
    AuthRepository mAuthRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

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
            ChatClient.getInstance(mContext).setImMsgReceveListener(this);
            ChatClient.getInstance(mContext).setImStatusListener(this);
            ChatClient.getInstance(mContext).setImTimeoutListener(this);
        }
    }

    /*******************************************
     * 聊天相关回调
     *********************************************/

    @Override
    public void onMessageReceived(final Message message) {
        setMessageTipVisable(true);
        EventBus.getDefault().post(message, EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED);
        if (!BackgroundUtil.getAppIsForegroundStatus()) {   // 应用在后台
            mUserInfoRepository.getLocalUserInfoBeforeNet(message.getUid())
                    .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                        @Override
                        protected void onSuccess(UserInfoBean data) {
                            JpushMessageBean jpushMessageBean = new JpushMessageBean();
                            jpushMessageBean.setType(JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM);
                            jpushMessageBean.setMessage(data.getName() + ":" + message.getTxt());
                            jpushMessageBean.setNofity(false);
                            NotificationUtil.showNotifyMessage(mContext, jpushMessageBean);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                        }
                    });
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE)
    public void setMessageTipVisable(boolean isShow) {
        mRootView.setMessageTipVisable(isShow);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MINE_TIP_VISABLE)
    public void setMineTipVisable(boolean isShow) {
        mRootView.setMineTipVisable(isShow);
    }

    @Override
    public void onMessageACKReceived(Message message) {
        EventBus.getDefault().post(message, EventBusTagConfig.EVENT_IM_ONMESSAGEACKRECEIVED);
    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {

    }

    @Override
    public void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer) {

    }

    @Override
    public void onConversationMCACKReceived(List<Conversation> conversations) {

    }

    @Override
    public void synchronousInitiaMessage(int limit) {

    }

    @Override
    public void onAuthSuccess(AuthData authData) {
        EventBus.getDefault().post(authData, EventBusTagConfig.EVENT_IM_AUTHSUCESSED);
        synIMMessage(authData);
    }

    /**
     * IM 消息同步
     *
     * @param authData
     */
    private void synIMMessage(AuthData authData) {
        if (authData.getSeqs() != null) {
            Observable.from(authData.getSeqs()) // 消息同步
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(seqsBean -> {
                        Message message = MessageDao.getInstance(mContext).getLastMessageByCid(seqsBean.getCid());
                        if (message != null && message.getSeq() < seqsBean.getSeq()) {
                            ZBIMClient.getInstance().syncAsc(message.getCid(), message.getSeq(), seqsBean.getSeq(), (int) System.currentTimeMillis());
                        }
                    }, throwable -> throwable.printStackTrace());
        }
    }

    @Override
    public void onConnected() {
        EventBus.getDefault().post("", EventBusTagConfig.EVENT_IM_ONCONNECTED);
    }

    @Override
    public void onDisconnect(int code, String reason) {
        EventBus.getDefault().post(code, EventBusTagConfig.EVENT_IM_ONDISCONNECT);
    }

    @Override
    public void onError(Exception error) {
        EventBus.getDefault().post(error, EventBusTagConfig.EVENT_IM_ONERROR);
    }

    @Override
    public void onMessageTimeout(Message message) {
        EventBus.getDefault().post(message, EventBusTagConfig.EVENT_IM_ONMESSAGETIMEOUT);
    }

    @Override
    public void onConversationJoinTimeout(int roomId) {

    }

    @Override
    public void onConversationLeaveTimeout(int roomId) {

    }

    @Override
    public void onConversationMcTimeout(List<Integer> roomIds) {

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
     *
     * @param isClick
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_CHECK_IN_CLICK)
    public void checkInClick(boolean isClick) {
       CheckInBean checkInBean= mRootView.getCheckInData();
        if(checkInBean!=null) {
            mRootView.showCheckInPop(checkInBean);
        }else {
            getCheckInInfo();
        }
    }

    @Override
    public void checkIn() {
       Subscription subscription= mUserInfoRepository.checkIn()
        .subscribe(new BaseSubscribeForV2<Object>() {
            @Override
            protected void onSuccess(Object data) {
                mRootView.showSnackSuccessMessage("签到成功");
                getCheckInInfo();
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getCheckInInfo() {

        Subscription subscription= mUserInfoRepository.getCheckInInfo()
                .subscribe(new BaseSubscribeForV2<CheckInBean>() {
                    @Override
                    protected void onSuccess(CheckInBean data) {
                        mRootView.showCheckInPop(data);
                    }
                });
        addSubscrebe(subscription);

    }

}