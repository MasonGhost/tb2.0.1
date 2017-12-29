package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.support.v4.app.Fragment;

import com.hyphenate.chat.EMClient;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MessageConversationPresenter extends AppBasePresenter<MessageConversationContract.Repository, MessageConversationContract.View>
        implements MessageConversationContract.Presenter{

    @Inject
    public MessageConversationPresenter(MessageConversationContract.Repository repository, MessageConversationContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        getAllConversationV2(isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void refreshConversationReadMessage() {

    }

    @Override
    public void deleteConversation(int position) {
        // 改为环信的删除
        MessageItemBeanV2 messageItemBeanV2 = mRootView.getRealMessageList().get(position);
        Subscription subscription = Observable.just(messageItemBeanV2)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemBeanV2 -> {
                    LogUtils.d("Cathy", "deletConversation");
                    mRootView.getRealMessageList().remove(itemBeanV2);
                    mRootView.refreshData();
                    checkBottomMessageTip();
                    EMClient.getInstance().chatManager().deleteConversation(itemBeanV2.getEmKey(), true);
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleFlushMessage() {

    }

    @Override
    public void checkUnreadNotification() {

    }

    /**
     * 获取环信的所有会话列表
     * @param isLoadMore 是否加载更多
     */
    private void getAllConversationV2(boolean isLoadMore) {
        // 已连接才去获取
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            Subscription subscribe = mRepository.getConversationList((int) AppApplication.getMyUserIdWithdefault())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageItemBeanV2s -> {
                        mRootView.getMessageListSuccess(messageItemBeanV2s);
                        mRootView.hideStickyMessage();
                        checkBottomMessageTip();
                    });
            addSubscrebe(subscribe);
        } else {
            mRootView.showStickyMessage(mContext.getString(R.string.chat_unconnected));
            mRootView.hideLoading();
            // 尝试重新登录，在homepresenter接收
            mAuthRepository.loginIM();
        }
    }

    /**
     * 检测底部小红点是否需要显示
     */
    private void checkBottomMessageTip() {
        Subscription subscribe = Observable.just(true)
                .observeOn(Schedulers.io())
                .map(aBoolean -> {
                    // 是否显示底部红点
                    boolean isShowMessageTip = false;
                    for (MessageItemBeanV2 messageItemBean : mRootView.getRealMessageList()) {
                        if (messageItemBean.getConversation().getUnreadMsgCount() > 0) {
                            isShowMessageTip = true;
                            break;
                        } else {
                            isShowMessageTip = false;
                        }
                    }
                    return isShowMessageTip;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isShowMessageTip -> {
//                    Fragment containerFragment = mRootView.getCureenFragment().getParentFragment();
//                    if (containerFragment != null && containerFragment instanceof MessageContainerFragment) {
//                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(mNotificaitonRedDotIsShow, 1);
//                    }
//                    boolean messageContainerRedDotIsShow = isShowMessgeTip || mNotificaitonRedDotIsShow;
//                    EventBus.getDefault().post(messageContainerRedDotIsShow, EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE);

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);


    }
}
