package com.zhiyicx.thinksnsplus.modules.chat;

import android.text.TextUtils;
import android.util.SparseArray;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_ONCONVERSATIONCRATED;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends BasePresenter<ChatContract.Repository, ChatContract.View> implements ChatContract.Presenter {

    private SparseArray<UserInfoBean> mUserInfoBeanSparseArray = new SparseArray<>();// 把用户信息存入内存，方便下次使用
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public ChatPresenter(ChatContract.Repository repository, ChatContract.View rootView) {
        super(repository, rootView);
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
        final List<ChatItemBean> data = mRepository.getChatListData(cid, creat_time);
        Collections.reverse(data);
        Observable.just(data)
                .observeOn(Schedulers.io())
                .subscribe(chatItemBeen -> {
                    for (ChatItemBean chatItemBean : chatItemBeen) {
                        if (!chatItemBean.getLastMessage().getIs_read()) {
                            // 把消息更新为已经读
                            MessageDao.getInstance(mContext).readMessage(chatItemBean.getLastMessage().getMid());
                        }
                    }
                });
        mRootView.hideLoading();
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
        Message message = ChatClient.getInstance(mContext).sendTextMsg(text, cid, "");// usid 暂不使用
        message.setUid(AppApplication.getmCurrentLoginAuth() != null ? (int) AppApplication.getmCurrentLoginAuth().getUser_id() : 0);// 更新
        if (!ZBIMClient.getInstance().isLogin()) { // IM 没有连接成功
            message.setSend_status(MessageStatus.SEND_FAIL);
        }
        message.setIs_read(true); // 更新
        MessageDao.getInstance(mContext).insertOrUpdateMessage(message); // 更新
        updateMessage(message);
    }

    /**
     * 消息重发
     *
     * @param chatItemBean
     */
    @Override
    public void reSendText(ChatItemBean chatItemBean) {
        if (ZBIMClient.getInstance().isLogin()) {
            ChatClient.getInstance(mContext).sendMessage(chatItemBean.getLastMessage());
            mRootView.refreshData();
        } else {
            ZBIMClient.getInstance().reConnect();
        }
    }

    @Override
    public void createChat(final UserInfoBean userInfoBean, final String text) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        final String uids = AppApplication.getmCurrentLoginAuth().getUser_id() + "," + userInfoBean.getUser_id();
        final String pair = AppApplication.getmCurrentLoginAuth().getUser_id() + "&" + userInfoBean.getUser_id();// "pair":null,   // type=0时此项为两个uid：min_uid&max_uid
        mRepository.createConveration(ChatType.CHAT_TYPE_PRIVATE, "", "", uids)
                .subscribe(new BaseSubscribe<Conversation>() {
                    @Override
                    protected void onSuccess(Conversation data) {
                        data.setIm_uid((int) AppApplication.getmCurrentLoginAuth().getUser_id());
                        data.setUsids(uids);
                        data.setPair(pair);
                        mRepository.insertOrUpdateConversation(data);
                        mRootView.updateConversation(data);
                        if (!TextUtils.isEmpty(text)) {
                            sendTextMessage(text, data.getCid());
                        }
                        MessageItemBean messageItemBean = new MessageItemBean();
                        messageItemBean.setConversation(data);
                        messageItemBean.setUserInfo(userInfoBean);
                        EventBus.getDefault().post(messageItemBean, EVENT_IM_ONCONVERSATIONCRATED);// 通知会话列表

                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackWarningMessage(mContext.getString(R.string.im_not_work));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
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
     *
     * @param message
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message message) {
        LogUtils.d(TAG, "------onMessageReceived------->" + message);
        if (message.cid != mRootView.getCurrentChatCid()) {// 丢弃非当前房间的消息
            return;
        }
        updateMessage(message);
        // 把消息更新为已经读
        Observable.just(message)
                .observeOn(Schedulers.io())
                .subscribe(message1 -> MessageDao.getInstance(mContext).readMessage(message1.getMid()));

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
            message.setUid(AppApplication.getmCurrentLoginAuth() != null ? (int) AppApplication.getmCurrentLoginAuth().getUser_id() : 0);
        }
        UserInfoBean userInfoBean = mUserInfoBeanSparseArray.get(message.getUid());
        if (userInfoBean == null) {
            userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) message.getUid());
            mUserInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
        }
        chatItemBean.setUserInfo(userInfoBean);
        mRootView.reFreshMessage(chatItemBean);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_AUTHSUCESSED)
    private void onAuthSuccessed(AuthData authData) {
//        mRootView.showSnackSuccessMessage("IM 聊天加载成功");
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONNECTED)
    private void onConnected() {
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
