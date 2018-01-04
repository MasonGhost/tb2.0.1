package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.support.v4.app.Fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    /**复制的所有原数据*/
    private List<MessageItemBeanV2> mCopyConversationList;

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

    @Override
    public List<ChatUserInfoBean> getChatUserList(int position) {
        List<ChatUserInfoBean> chatUserInfoBeans = new ArrayList<>();
        // 当前用户
        chatUserInfoBeans.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
        chatUserInfoBeans.add(getChatUser(mRootView.getRealMessageList().get(position).getUserInfo()));
        return chatUserInfoBeans;
    }

    @Override
    public void searchList(String key) {
        Observable.just(key)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(s -> {
                    List<MessageItemBeanV2> newList = new ArrayList<>();
                    for (MessageItemBeanV2 itemBeanV2 : mCopyConversationList){
                        String name = "";
                        if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat){
                            if (itemBeanV2.getUserInfo() != null){
                                name = itemBeanV2.getUserInfo().getName();
                            }
                        } else {
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(itemBeanV2.getEmKey());
                            if (group != null){
                                name = group.getGroupName();
                            }
                        }
                        if (name.contains(s)){
                            newList.add(itemBeanV2);
                        }
                    }
                    return newList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mRootView.getMessageListSuccess(list));
    }

    private ChatUserInfoBean getChatUser(UserInfoBean userInfoBean){
        ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
        chatUserInfoBean.setUser_id(userInfoBean.getUser_id());
        chatUserInfoBean.setAvatar(userInfoBean.getAvatar());
        chatUserInfoBean.setName(userInfoBean.getName());
        chatUserInfoBean.setSex(userInfoBean.getSex());
        ChatVerifiedBean verifiedBean = new ChatVerifiedBean();
        verifiedBean.setDescription(userInfoBean.getVerified().getDescription());
        verifiedBean.setIcon(userInfoBean.getVerified().getIcon());
        verifiedBean.setStatus(userInfoBean.getVerified().getStatus());
        verifiedBean.setType(userInfoBean.getVerified().getType());
        chatUserInfoBean.setVerified(verifiedBean);
        return chatUserInfoBean;
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
                        if (mCopyConversationList == null){
                            mCopyConversationList = new ArrayList<>();
                        }
                        mCopyConversationList = messageItemBeanV2s;
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
