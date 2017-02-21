package com.zhiyicx.thinksnsplus.modules.chat;

import android.text.TextUtils;
import android.util.SparseArray;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.simple.eventbus.Subscriber;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends BasePresenter<ChatContract.Repository, ChatContract.View> implements ChatContract.Presenter {

    private SparseArray<UserInfoBean> mUserInfoBeanSparseArray = new SparseArray<>();// 把用户信息存入内存，方便下次使用


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
                .subscribe(new Action1<List<ChatItemBean>>() {
                    @Override
                    public void call(List<ChatItemBean> chatItemBeen) {
                        for (ChatItemBean chatItemBean : chatItemBeen) {
                            if (!chatItemBean.getLastMessage().getIs_read()) {
                                // 把消息更新为已经读
                                MessageDao.getInstance(mContext).readMessage(chatItemBean.getLastMessage().getMid());
                            }
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
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Message message = ChatClient.getInstance(mContext).sendTextMsg(text, cid, "");// usid 暂不使用
        message.setCreate_time(System.currentTimeMillis());
        message.setUid(AppApplication.getmCurrentLoginAuth().getUser_id());
        message.setIs_read(true);
        updateMessage(message);
    }

    /**
     * 消息重发
     *
     * @param chatItemBean
     */
    @Override
    public void reSendText(ChatItemBean chatItemBean) {
        chatItemBean.getLastMessage().setCreate_time(System.currentTimeMillis());
        ChatClient.getInstance(mContext).sendMessage( chatItemBean.getLastMessage());
        mRootView.reFreshMessage(chatItemBean);
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
        MessageDao.getInstance(mContext).readMessage(message.getMid());
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
            message.setUid(AppApplication.getmCurrentLoginAuth().getUser_id());
        }
        UserInfoBean userInfoBean = mUserInfoBeanSparseArray.get(message.getUid());
        if (userInfoBean == null) {
            userInfoBean = AppApplication.AppComponentHolder.getAppComponent()
                    .userInfoBeanGreenDao().getSingleDataFromCache((long) message.getUid());
            if (userInfoBean == null) {
                // TODO: 2017/2/21    //网络请求
            }
            mUserInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
        }
        chatItemBean.setUserInfo(userInfoBean);
        mRootView.reFreshMessage(chatItemBean);
        mRootView.smoothScrollToBottom();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONNECTED)
    private void onConnected() {
        mRootView.showMessage("IM 聊天加载成功");
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONDISCONNECT)
    private void onDisconnect(int code, String reason) {
        mRootView.showMessage("IM 聊天断开" + reason);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONERROR)
    private void onError(Exception error) {
        mRootView.showMessage("IM 聊天错误" + error.toString());
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGETIMEOUT)
    private void onMessageTimeout(Message message) {
        mRootView.updateMessageStatus(message);
    }
}
