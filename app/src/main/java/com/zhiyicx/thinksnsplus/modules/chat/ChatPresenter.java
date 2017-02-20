package com.zhiyicx.thinksnsplus.modules.chat;

import android.text.TextUtils;
import android.util.SparseArray;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
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

import static com.zhiyicx.imsdk.db.base.BaseDao.TIME_DEFAULT_ADD;

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
        List<ChatItemBean> data = mRepository.getChatListData(cid, creat_time);
        Collections.reverse(data);
        System.out.println("creat_time = " + TimeUtils.getStandardTimeWithYeay(creat_time));
        for (ChatItemBean chatItemBean : data) {
            System.out.println("chatItemBean = " + TimeUtils.getStandardTimeWithYeay(chatItemBean.getLastMessage().create_time));
        }
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
        Message message = ChatClient.getInstance(mContext).sendTextMsg(text, cid, "", 0);
        message.setCreate_time(System.currentTimeMillis());
        message.setUid(AppApplication.getmCurrentLoginAuth().getUser_id());
        onMessageReceived(message);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message message) {
        LogUtils.d(TAG, "------onMessageReceived------->" + message);
        if (message.getMid() > 0) {
            message.setCreate_time((message.mid >> 23) + TIME_DEFAULT_ADD); //  消息的MID，`(mid >> 23) + 1451577600000` 为毫秒时间戳
        }
        updateMessage(message);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGEACKRECEIVED)
    private void onMessageACKReceived(Message message) {
        LogUtils.d(TAG, "------onMessageACKReceived------->" + message);
//        updateMessage(message);
    }

    private void updateMessage(Message message) {
        ChatItemBean chatItemBean = new ChatItemBean();
        chatItemBean.setLastMessage(message);
        UserInfoBean userInfoBean = mUserInfoBeanSparseArray.get(message.getUid());
        if (userInfoBean == null) {
            userInfoBean = AppApplication.AppComponentHolder.getAppComponent()
                    .userInfoBeanGreenDao().getSingleDataFromCache((long) message.getUid());

            if (userInfoBean == null) {
                //网络请求
            } else {
                mUserInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
            }
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
//        mRootView.showMessage("IM 聊天超时" + message);
    }
}
