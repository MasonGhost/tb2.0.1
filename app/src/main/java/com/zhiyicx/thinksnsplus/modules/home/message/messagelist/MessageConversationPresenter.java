package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMMultipleMessagesEvent;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageConversationRepository;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository.MAX_RETRY_COUNTS;
import static com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository.RETRY_DELAY_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MessageConversationPresenter extends AppBasePresenter<MessageConversationContract.View>
        implements MessageConversationContract.Presenter {

    @Inject
    MessageConversationRepository mRepository;

    /**
     * 复制的所有原数据
     */
    private List<MessageItemBeanV2> mCopyConversationList;

    @Inject
    public MessageConversationPresenter(MessageConversationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        getAllConversationV2(isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBeanV2> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void refreshConversationReadMessage() {
        Subscription represhSu = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(s -> {
                    checkBottomMessageTip();
                    return mRootView.getListDatas();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> mRootView.refreshData(), Throwable::printStackTrace);
        addSubscrebe(represhSu);
    }

    @Override
    public void deleteConversation(int position) {
        // 改为环信的删除
        MessageItemBeanV2 messageItemBeanV2 = mRootView.getListDatas().get(position);
        Subscription subscription = Observable.just(messageItemBeanV2)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemBeanV2 -> {
                    mRootView.getListDatas().remove(itemBeanV2);
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
        if (mRootView.getListDatas().get(position).getConversation().getType() == EMConversation.EMConversationType.Chat) {
            chatUserInfoBeans.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
            chatUserInfoBeans.add(getChatUser(mRootView.getListDatas().get(position).getUserInfo()));
        } else {
            if (mRootView.getListDatas().get(position) != null) {
                for (UserInfoBean userInfoBean : mRootView.getListDatas().get(position).getList()) {
                    chatUserInfoBeans.add(getChatUser(userInfoBean));
                }
            }
        }
        return chatUserInfoBeans;
    }

    @Override
    public void searchList(String key) {
        Observable.just(key)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(s -> {
                    List<MessageItemBeanV2> newList = new ArrayList<>();
                    for (MessageItemBeanV2 itemBeanV2 : mCopyConversationList) {
                        String name = "";
                        if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat) {
                            if (itemBeanV2.getUserInfo() != null) {
                                name = itemBeanV2.getUserInfo().getName();
                            }
                        } else {
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(itemBeanV2.getEmKey());
                            if (group != null) {
                                name = group.getGroupName();
                            }
                        }
                        if (name.contains(s)) {
                            newList.add(itemBeanV2);
                        }
                    }
                    return newList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mRootView.onNetResponseSuccess(list, false));
    }

    private ChatUserInfoBean getChatUser(UserInfoBean userInfoBean) {
        ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
        chatUserInfoBean.setUser_id(userInfoBean.getUser_id());
        chatUserInfoBean.setAvatar(userInfoBean.getAvatar());
        chatUserInfoBean.setName(userInfoBean.getName());
        chatUserInfoBean.setSex(userInfoBean.getSex());
        if (userInfoBean.getVerified() != null) {
            ChatVerifiedBean verifiedBean = new ChatVerifiedBean();
            verifiedBean.setDescription(userInfoBean.getVerified().getDescription());
            verifiedBean.setIcon(userInfoBean.getVerified().getIcon());
            verifiedBean.setStatus(userInfoBean.getVerified().getStatus());
            verifiedBean.setType(userInfoBean.getVerified().getType());
            chatUserInfoBean.setVerified(verifiedBean);
        }
        return chatUserInfoBean;
    }

    /**
     * 获取环信的所有会话列表
     *
     * @param isLoadMore 是否加载更多
     */
    private void getAllConversationV2(boolean isLoadMore) {
        // 已连接才去获取
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            Subscription subscribe = mRepository.getConversationList((int) AppApplication.getMyUserIdWithdefault())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscribeForV2<List<MessageItemBeanV2>>() {
                        @Override
                        protected void onSuccess(List<MessageItemBeanV2> data) {
                            if (mCopyConversationList == null) {
                                mCopyConversationList = new ArrayList<>();
                            }
                            mCopyConversationList = data;
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                            mRootView.hideStickyMessage();
                            checkBottomMessageTip();
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showStickyMessage(message);
                            mRootView.onResponseError(null,false);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.showStickyMessage(mContext.getString(R.string.chat_unconnected));
                            mRootView.onResponseError(throwable,false);


                        }
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
                    for (MessageItemBeanV2 messageItemBean : mRootView.getListDatas()) {
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
                    Fragment containerFragment = mRootView.getCurrentFragment().getParentFragment();
                    if (containerFragment != null && containerFragment instanceof MessageContainerFragment) {
                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(isShowMessageTip, 1);
                    }
                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);


    }

    /**
     * 请求 群聊天会话信息列表  成功的回调
     *
     * @param bundle
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_GET_GROUP_INFO)
    public void getGroupList(Bundle bundle) {
        if (bundle != null && bundle.containsKey(EventBusTagConfig.EVENT_IM_GET_GROUP_INFO)) {
            List<ChatGroupBean> list = bundle.getParcelableArrayList(EventBusTagConfig.EVENT_IM_GET_GROUP_INFO);
            if (list == null || list.isEmpty()) {
                return;
            }
            List<MessageItemBeanV2> messageItemBeanList = new ArrayList<>();
            for (ChatGroupBean chatGroupBean : list) {
                // 如果列表已经有  那么就不再追加
                boolean canAdded = true;
                for (MessageItemBeanV2 exitItem : mRootView.getListDatas()) {
                    if (exitItem.getConversation().conversationId().equals(chatGroupBean.getId())) {
                        exitItem.setEmKey(chatGroupBean.getId());
                        exitItem.setList(chatGroupBean.getAffiliations());
                        exitItem.setConversation(EMClient.getInstance().chatManager().getConversation(chatGroupBean.getId()));
                        exitItem.setChatGroupBean(chatGroupBean);
                        canAdded = false;
                        break;
                    }
                }
                if (canAdded) {
                    MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                    itemBeanV2.setEmKey(chatGroupBean.getId());
                    itemBeanV2.setList(chatGroupBean.getAffiliations());
                    itemBeanV2.setConversation(EMClient.getInstance().chatManager().getConversation(chatGroupBean.getId()));
                    itemBeanV2.setChatGroupBean(chatGroupBean);
                    messageItemBeanList.add(itemBeanV2);
                }
            }
            if (!messageItemBeanList.isEmpty()) {
                mRootView.getListDatas().addAll(messageItemBeanList);
                if (mCopyConversationList == null) {
                    mCopyConversationList = new ArrayList<>();
                }
                mCopyConversationList = mRootView.getListDatas();
            }
        }
        mRootView.refreshData();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 收到聊天消息
     *
     * @param messagesEvent 聊天类容
     */
    @Subscriber(mode = ThreadMode.MAIN)
    public void onMessageReceived(TSEMMultipleMessagesEvent messagesEvent) {
        if (messagesEvent.getMessages() == null || messagesEvent.getMessages().isEmpty()) {
            return;
        }
        List<EMMessage> list = messagesEvent.getMessages();
        Subscription subscribe = Observable.just(list)
                .subscribeOn(Schedulers.io())
                .flatMap(messageList -> {
                    LogUtils.d("Cathy", "MessagePresenter onMessageReceived -----");
                    int size = mRootView.getListDatas().size();
                    // 对话是否存在
                    // 用来装新的会话item
                    List<MessageItemBeanV2> messageItemBeanV2List = new ArrayList<>();
                    for (EMMessage emMessage : messageList) {



                        // 用收到的聊天的item的会话id去本地取出会话
                        EMConversation conversationNew = EMClient.getInstance().chatManager().getConversation(emMessage.conversationId());
                        if (conversationNew != null) {
                            // 会话已经存在
                            for (int i = 0; i < size; i++) {
                                // 检测列表中是否已经存在了
                                EMConversation conversationOld = mRootView.getListDatas().get(i).getConversation();
                                if (conversationOld.conversationId().equals(conversationNew.conversationId())) {
                                    // 直接替换会话
                                    MessageItemBeanV2 itemBeanV2 = mRootView.getListDatas().get(i);
                                    itemBeanV2.setConversation(conversationNew);
                                    messageItemBeanV2List.add(itemBeanV2);
                                    break;
                                } else if (i == size - 1) {
                                    // 循环到最后一条，仍然没有会话，那则证明是需要新增一条到会话列表
                                    LogUtils.d("msg::" + "newMsg");
                                    MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                                    itemBeanV2.setConversation(conversationNew);
                                    itemBeanV2.setEmKey(conversationNew.conversationId());
                                    messageItemBeanV2List.add(itemBeanV2);
                                }
                            }
                        } else {
                            // 居然不存在 exm？？？
                            // 那还能怎么办呢，当然新来一条的，不过这种情况目前没有遇到过的样子呢(*╹▽╹*)
                            EMConversation conversation =
                                    EMClient.getInstance().chatManager().getConversation(emMessage.getFrom(), EMConversation.EMConversationType
                                            .Chat, true);
                            conversation.insertMessage(emMessage);
                            MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                            itemBeanV2.setConversation(conversation);
                            itemBeanV2.setEmKey(emMessage.conversationId());
                            messageItemBeanV2List.add(itemBeanV2);
                        }
                    }
                    return mRepository.completeEmConversation(messageItemBeanV2List)
                            .map(list12 -> list12);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<MessageItemBeanV2>>() {
                    @Override
                    protected void onSuccess(List<MessageItemBeanV2> data) {
                        for (MessageItemBeanV2 messageItemBeanV2 : data) {
                            // 移除原来的
                            if (mRootView.getListDatas().indexOf(messageItemBeanV2) != -1) {
                                mRootView.getListDatas().remove(messageItemBeanV2);
                            }
                        }
                        // 加载到第一条
                        mRootView.getListDatas().addAll(0, data);
                        mRootView.refreshData();
                        // 小红点是否要显示
                        checkBottomMessageTip();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscribe);
    }

    /**
     * 删除群
     */
    @Override
    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_DELETE_QUIT)
    public void deleteGroup(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        MessageItemBeanV2 deleteItem = null;
        for (MessageItemBeanV2 messageItemBeanV2 : mRootView.getListDatas()) {
            if (messageItemBeanV2.getConversation().conversationId().equals(id)) {
                deleteItem = messageItemBeanV2;
                break;
            }
        }
        if (deleteItem != null) {
            mRootView.getListDatas().remove(deleteItem);
            mRootView.refreshData();
        }
    }

    /**
     * 更新群信息
     */
    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_INFO)
    public void updateGroup(ChatGroupBean chatGroupBean) {
        for (MessageItemBeanV2 itemBeanV2 : mRootView.getListDatas()) {
            if (itemBeanV2.getEmKey().equals(chatGroupBean.getId())) {
                itemBeanV2.setChatGroupBean(chatGroupBean);
            }
        }
        mRootView.refreshData();
    }
}
