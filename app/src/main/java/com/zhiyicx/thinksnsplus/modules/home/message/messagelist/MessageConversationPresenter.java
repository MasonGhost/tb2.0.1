package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

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
        if (mRootView.getRealMessageList().get(position).getConversation().getType() == EMConversation.EMConversationType.Chat){
            chatUserInfoBeans.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
            chatUserInfoBeans.add(getChatUser(mRootView.getRealMessageList().get(position).getUserInfo()));
        } else {
            for (UserInfoBean userInfoBean : mRootView.getRealMessageList().get(position).getList()){
                chatUserInfoBeans.add(getChatUser(userInfoBean));
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
        if (userInfoBean.getVerified() != null){
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
     * @param isLoadMore 是否加载更多
     */
    private void getAllConversationV2(boolean isLoadMore) {
        // 已连接才去获取
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            Subscription subscribe = mRepository.getConversationList((int) AppApplication.getMyUserIdWithdefault())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageItemBeanV2s -> {
                        List<MessageItemBeanV2> singleList = new ArrayList<>();
                        for (MessageItemBeanV2 itemBeanV2 : messageItemBeanV2s){
                            if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat){
                                singleList.add(itemBeanV2);
                            }
                        }
                        if (mCopyConversationList == null){
                            mCopyConversationList = new ArrayList<>();
                        }
                        mCopyConversationList = singleList;
                        mRootView.getMessageListSuccess(singleList);
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

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_GET_GROUP_INFO)
    public void getGroupList(Bundle bundle){
        if (bundle != null && bundle.containsKey(EventBusTagConfig.EVENT_IM_GET_GROUP_INFO)){
            List<ChatGroupBean> list = bundle.getParcelableArrayList(EventBusTagConfig.EVENT_IM_GET_GROUP_INFO);
            if (list == null){
                return;
            }
            List<MessageItemBeanV2> messageItemBeanList = new ArrayList<>();
            for (ChatGroupBean chatGroupBean : list){
                MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                itemBeanV2.setEmKey(chatGroupBean.getId());
                itemBeanV2.setList(chatGroupBean.getAffiliations());
                itemBeanV2.setConversation(EMClient.getInstance().chatManager().getConversation(chatGroupBean.getId()));
                messageItemBeanList.add(itemBeanV2);
            }
            if (!messageItemBeanList.isEmpty()){
                mRootView.getRealMessageList().addAll(messageItemBeanList);
                if (mCopyConversationList == null){
                    mCopyConversationList = new ArrayList<>();
                }
                mCopyConversationList = mRootView.getRealMessageList();
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
     * @param bundle 聊天类容
     */
    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED_V2)
    private void onNewMessageReceived(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        LogUtils.d("Cathy", "MessagePresenter onMessageReceived" + bundle);
        List<EMMessage> list = bundle.getParcelableArrayList(EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED_V2);
        Subscription subscribe = Observable.just(list)
                .observeOn(Schedulers.io())
                .flatMap(messageList -> {
                    LogUtils.d("Cathy", "MessagePresenter onMessageReceived -----");
                    int size = mRootView.getRealMessageList().size();
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
                                EMConversation conversationOld = mRootView.getRealMessageList().get(i).getConversation();
                                if (conversationOld.conversationId().equals(conversationNew.conversationId())) {
                                    // 直接替换会话
                                    MessageItemBeanV2 itemBeanV2 = mRootView.getRealMessageList().get(i);
                                    itemBeanV2.setConversation(conversationNew);
                                    messageItemBeanV2List.add(itemBeanV2);
                                    break;
                                } else if (i == size - 1) {
                                    // 循环到最后一条，仍然没有会话，那则证明是需要新增一条到会话列表
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
                                    EMClient.getInstance().chatManager().getConversation(emMessage.getFrom(), EMConversation.EMConversationType.Chat, true);
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
                .subscribe(list1 -> {
                    for (MessageItemBeanV2 messageItemBeanV2 : list1) {
                        // 移除原来的
                        if (mRootView.getRealMessageList().indexOf(messageItemBeanV2) != -1) {
                            mRootView.getRealMessageList().remove(messageItemBeanV2);
                        }
                    }
                    // 加载到第一条
                    mRootView.getRealMessageList().addAll(0, list1);
                    mRootView.refreshData();
                    // 小红点是否要显示
                    checkBottomMessageTip();
                });
        addSubscrebe(subscribe);
    }
}
