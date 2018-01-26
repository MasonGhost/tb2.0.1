package com.zhiyicx.thinksnsplus.modules.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_ONCONVERSATIONCRATED;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends AppBasePresenter<ChatContract.View> implements ChatContract.Presenter {

    private SparseArray<UserInfoBean> mUserInfoBeanSparseArray = new SparseArray<>();// 把用户信息存入内存，方便下次使用
    @Inject
    SystemRepository mSystemRepository;

    @Inject
    ChatRepository mChatRepository;

    @Inject
    public ChatPresenter(ChatContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void getUserInfo(long user_id) {

    }

    @Override
    public List<ChatItemBean> getHistoryMessages(int cid, long creat_time) {
        final List<ChatItemBean> data = mChatRepository.getChatListData(cid, creat_time);
        Collections.reverse(data);
        Subscription subscribe = Observable.just(data)
                .observeOn(Schedulers.io())
                .subscribe(chatItemBeen -> {
                    for (ChatItemBean chatItemBean : chatItemBeen) {
                        if (!chatItemBean.getLastMessage().getIs_read()) {
                            // 把消息更新为已经读
                            MessageDao.getInstance(mContext).readMessage(chatItemBean.getLastMessage().getMid());
                        }
                    }
                });
        addSubscrebe(subscribe);
        mRootView.hideLoading();
        return data;
    }

    @Override
    public List<ChatItemBean> getHistoryMessagesV2(String id, int pageSize, boolean isNeedScrollToBottom) {
        List<ChatItemBean> data = mChatRepository.getChatListDataV2(mRootView.getMessItemBean(), id, pageSize);
        Subscription subscribe = mChatRepository.completeUserInfo(data)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(list -> {
                    // 未读的消息发送已读回执
                    for (ChatItemBean chatItemBean : list) {
                        EMMessage message = chatItemBean.getMessage();
                        // 收到的消息，自己发送的消息不处理
                        if (!String.valueOf(AppApplication.getMyUserIdWithdefault()).equals(message.getFrom()) && !message.isUnread()) {
                            try {
                                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mRootView.hideLoading();
                    mRootView.getHistoryMessageSuccess(list, isNeedScrollToBottom);
                });
        addSubscrebe(subscribe);
        return data;
    }

    /*******************************************
     * IM 相关
     *********************************************/
    /**
     * 发送文本消息
     *
     * @param text 文本内容
     * @param cid  对话 id
     */
    @Override
    public void sendTextMessage(String text, int cid) {
        // usid 暂不使用
        Message message = ChatClient.getInstance(mContext).sendTextMsg(text, cid, "");
        // 更新
        message.setUid(AppApplication.getmCurrentLoginAuth() != null ? (int) AppApplication.getMyUserIdWithdefault() : 0);
        // IM 没有连接成功
        if (!ZBIMClient.getInstance().isLogin()) {
            message.setSend_status(MessageStatus.SEND_FAIL);
        }
        // 更新
        message.setIs_read(true);
        MessageDao.getInstance(mContext).insertOrUpdateMessage(message);
        updateMessage(message);
    }

    @Override
    public void sendTextMessageV2(String content, String userId) {
        // 环信的发送消息
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, userId);
        //如果是群聊，设置chattype，默认是单聊
//        if (chatType == CHATTYPE_GROUP){
//            message.setChatType(ChatType.GroupChat);
//        }
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        updateMessageV2(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 发送成功 需要刷新页面
                LogUtils.d("Cathy", "发送成功" + message.getBody().toString());
                if (mRootView.getListDatas().isEmpty()||mRootView.getListDatas().size()==1) {
                    Observable.just("")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                mRootView.scrollToBottom();
                            });
                }
            }

            @Override
            public void onError(int code, String error) {
                LogUtils.d("Cathy", "sendTextMessageV2 onError// " + error + code);
                // 这个错误也太多了 先随便写点吧_(:з」∠)_  具体的查看官方API EMError
                switch (code) {
                    case EMError.SERVER_BUSY:
                        // 服务器繁忙
                        break;
                    case EMError.NETWORK_ERROR:
                        // 网络异常
                        break;
                    case EMError.SERVER_NOT_REACHABLE:
                        // 无法访问到服务器
                        break;
                    default:
                }
                Observable.just("")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> mRootView.refreshData());
            }

            @Override
            public void onProgress(int progress, String status) {
                LogUtils.d("Cathy", "sendTextMessageV2 progress// " + progress);
            }
        });
    }

    /**
     * 消息重发
     */
    @Override
    public void reSendText(ChatItemBean chatItemBean) {
//        if (ZBIMClient.getInstance().isLogin()) {
//            ChatClient.getInstance(mContext).sendMessage(chatItemBean.getLastMessage());
//            mRootView.refreshData();
//        } else {
//            ZBIMClient.getInstance().reConnect();
//        }
        // 改为环信的
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(chatItemBean.getMessage());
    }

    @Override
    public void createChat(final UserInfoBean userInfoBean, final String text) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        // 替换为环信的创建会话
        MessageItemBeanV2 itemBeanV2 = mRootView.getMessItemBean();
        Subscription subscription = Observable.just(itemBeanV2)
                .map(messageItemBeanV2 -> {
                    // 创建会话的 conversation 要传入用户名 ts+采用用户Id作为用户名，聊天类型 单聊
                    EMConversation conversation =
                            EMClient.getInstance().chatManager().getConversation(itemBeanV2.getEmKey(), EMConversation.EMConversationType.Chat, true);
                    if (!TextUtils.isEmpty(text)) {
                        // 发送信息的时候 如果没有会话信息，则创建一个
                    }
                    mRootView.getMessItemBean().setConversation(conversation);
                    mRootView.updateConversation(conversation);
                    return messageItemBeanV2;
                })
                .subscribe(messageItemBeanV2 -> {
                    if (messageItemBeanV2.getConversation() != null) {
                        // 通知会话列表
                        EventBus.getDefault().post(messageItemBeanV2, EVENT_IM_ONCONVERSATIONCRATED);
                    } else {
                        mRootView.showSnackWarningMessage(mContext.getString(R.string.im_not_work));
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 检测 ts helper 是否是当前用户
     */
    @Override
    public String checkTShelper(long user_id) {
        return mSystemRepository.checkTShelper(user_id);
    }

    /**
     * 收到消息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED_V2)
    private void onMessageReceived(Bundle bundle) {
        //收到消息
        if (bundle == null) {
            return;
        }
        List<EMMessage> list = bundle.getParcelableArrayList(EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED_V2);
        LogUtils.d("Cathy", " 收到消息 :" + list);
        if (list != null && !list.isEmpty()) {
            for (EMMessage message : list) {
                if (message.conversationId().equals(mRootView.getMessItemBean().getEmKey())) {
                    // 这才是本聊天组的消息哦
                    updateMessageV2(message);
                }
            }
        }
        // 设置消息已读
        mRootView.getMessItemBean().getConversation().markAllMessagesAsRead();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGEACKRECEIVED)
    private void onMessageACKReceived(Message message) {
        LogUtils.d(TAG, "------onMessageACKReceived------->" + message);
        mRootView.updateMessageStatus(message);
    }

    private void updateMessage(Message message) {
        ChatItemBean chatItemBean = new ChatItemBean();
        chatItemBean.setLastMessage(message);
        if (message.getUid() == 0) {// 如果没有 uid, 则表明是当前用户发的消息
            message.setUid(AppApplication.getmCurrentLoginAuth() != null ? (int) AppApplication.getMyUserIdWithdefault() : 0);
        }
        UserInfoBean userInfoBean = mUserInfoBeanSparseArray.get(message.getUid());
        if (userInfoBean == null) {
            userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) message.getUid());
            mUserInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
        }
        chatItemBean.setUserInfo(userInfoBean);
        mRootView.reFreshMessage(chatItemBean);
    }

    /**
     * 更新消息 基于环信
     *
     * @param message 消息体
     */
    private void updateMessageV2(EMMessage message) {
        ChatItemBean chatItemBean = new ChatItemBean();
        chatItemBean.setMessage(message);
        // 消息的来源与当前用户不一致，则证明非当前用户
        String currentUser = String.valueOf(AppApplication.getmCurrentLoginAuth() != null ? (int) AppApplication.getMyUserIdWithdefault() : 0);
        if (!message.getFrom().equals(currentUser)) {
            // 当前这个版本还没有群聊呢，要快速出版本，暂时不考虑群聊的情况，后面需要根据来源查找用户信息
            currentUser = message.getFrom();
        }
        chatItemBean.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(currentUser)));
        // 如果没有用户信息则去完善用户信息，之后如果有群聊也可以直接这样，
        // 但是有个问题，如果用户信息更新了，数据库没有更新，那么是不会更新的
        Subscription subscription = Observable.just(chatItemBean)
                .flatMap(chatItemBean12 -> {
                    if (chatItemBean12.getUserInfo() == null) {
                        List<ChatItemBean> chatItemBeans = new ArrayList<>();
                        chatItemBeans.add(chatItemBean12);
                        return mChatRepository.completeUserInfo(chatItemBeans)
                                .map(list -> list.get(0));
                    }
                    return Observable.just(chatItemBean12);
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatItemBean1 -> mRootView.reFreshMessage(chatItemBean1));
        addSubscrebe(subscription);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_AUTHSUCESSED)
    private void onAuthSuccessed(AuthData authData) {
//        mRootView.showSnackSuccessMessage("IM 聊天加载成功");
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONNECTED)
    private void onConnected(String content) {
//        mRootView.showSnackSuccessMessage("IM 聊天加载成功");
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONDISCONNECT)
    private void onDisconnect(int code, String reason) {
//        mRootView.showSnackSuccessMessage("IM 聊天断开" + reason);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONERROR)
    private void onError(Exception error) {
        LogUtils.d(" 超时   message = " + error);
//        mRootView.showSnackSuccessMessage("IM 聊天错误" + error.toString());
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGETIMEOUT)
    private void onMessageTimeout(Message message) {
        LogUtils.d(" 超时   message = " + message);
        mRootView.updateMessageStatus(message);
    }
}
