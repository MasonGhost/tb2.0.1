package com.zhiyicx.thinksnsplus.modules.home;

import android.text.TextUtils;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMMultipleMessagesEvent;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.appprocess.BackgroundUtil;
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
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletConfigBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

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
class HomePresenter extends AppBasePresenter<HomeContract.View> implements HomeContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    WalletConfigBeanGreenDaoImpl mWalletConfigBeanGreenDao;

    @Inject
    BaseMessageRepository mBaseMessageRepository;

    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;

    @Inject
    public HomePresenter(HomeContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void initIM() {
        if (isLogin()) {
            mAuthRepository.loginIM();
        }
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
        TSEMHyphenate.getInstance().replease();
        ChatClient.getInstance(mContext).onDestroy();
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onMessageReceived(TSEMMultipleMessagesEvent messagesEvent) {
        List<EMMessage> list = messagesEvent.getMessages();
        setMessageTipVisable(true);
        // 应用在后台，则推送通知
        if (!BackgroundUtil.getAppIsForegroundStatus()) {
            // 手动创建聊天item，从数据库取出用户信息
            List<ChatItemBean> chatItemBeans = new ArrayList<>();
            for (EMMessage message : list) {
                ChatItemBean chatItemBean = new ChatItemBean();
                chatItemBean.setMessage(message);

                boolean isUserJoin, isUserExit;

                isUserJoin = TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));
                isUserExit = TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"));

                if (isUserExit || isUserJoin) {
                    // 只有群聊中才会有 成员 加入or退出的消息
                    updateChatGroupMemberCount(message.conversationId(), 1, isUserJoin);
                }

                // admin  消息 ，我们后台的发，显示的时候不要名字，只要内容，所以 new UserInfoBean("");搞了个名字是""的用户信息。
                if ("admin".equals(message.getFrom())) {
                    chatItemBean.setUserInfo(new UserInfoBean(""));
                } else {
                    try {
                        chatItemBean.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache
                                (Long.parseLong(message.getFrom())));
                    } catch (NumberFormatException ignore) {
                        chatItemBean.setUserInfo(new UserInfoBean(""));
                    }
                }
                chatItemBeans.add(chatItemBean);
            }
            // 遍历返回的信息，如果有用户信息为空的 证明数据库中没有此用户，从服务器取用户信息
            for (ChatItemBean chatItemBean : chatItemBeans) {
                Observable.just(chatItemBean)
                        .flatMap(chatItemBean1 -> {
                            if (chatItemBean1.getUserInfo() == null) {
                                List<ChatItemBean> chatItemBeanList = new ArrayList<>();
                                chatItemBeanList.add(chatItemBean1);
                                return mBaseMessageRepository.completeUserInfo(chatItemBeanList)
                                        .map(list1 -> list1.get(0));
                            }
                            return Observable.just(chatItemBean1);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(chatItemBean12 -> {
                            JpushMessageBean jpushMessageBean = new JpushMessageBean();
                            jpushMessageBean.setType(JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM);
                            jpushMessageBean.setExtras(chatItemBean12.getMessage().getChatType().name());
                            String content = chatItemBean12.getMessage().getBody().toString();
                            // 目前只有单聊，别的还没定
                            if (chatItemBean12.getMessage().getBody() instanceof EMTextMessageBody) {
                                content = ((EMTextMessageBody) chatItemBean12.getMessage().getBody()).getMessage();
                            }

                            if (TextUtils.isEmpty(chatItemBean12.getUserInfo().getName())) {
                                content = chatItemBean12.getUserInfo().getName() + content;
                            } else {
                                content = chatItemBean12.getUserInfo().getName() + ":" + content;
                            }
                            jpushMessageBean.setMessage(content);
                            jpushMessageBean.setNofity(false);
                            NotificationUtil.showChatNotifyMessage(mContext, jpushMessageBean, chatItemBean12.getUserInfo());
                        });
            }
        }
    }

    /**
     * @param id 群 id
     * @param count 变动 数量
     * @param add 是否 加法
     * @return
     */
    @Override
    public boolean updateChatGroupMemberCount(String id, int count,boolean add) {
        return mChatGroupBeanGreenDao.updateChatGroupMemberCount(id, count,add);
    }
}