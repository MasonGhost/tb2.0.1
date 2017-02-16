package com.zhiyicx.thinksnsplus.modules.chat;

import android.text.TextUtils;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends BasePresenter<ChatContract.Repository, ChatContract.View> implements ChatContract.Presenter {


    @Inject
    public ChatPresenter(ChatContract.Repository repository, ChatContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getUserInfo(long user_id) {

    }

    @Override
    public List<ChatItemBean> getHistoryMessages(int cid, long mid) {
        return mRepository.getChatListData(cid, mid);
    }

    /*******************************************
     * IM 相关
     *********************************************/
    /**
     * 发送文本消息
     *
     * @param text
     */
    @Override
    public void sendTextMessage(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message message) {
        if (!(ActivityHandler.getInstance().currentActivity() instanceof HomeActivity)) {
            return;
        }

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGEACKRECEIVED)
    private void onMessageACKReceived(Message message) {
        if (!(ActivityHandler.getInstance().currentActivity() instanceof HomeActivity)) {
            return;
        }
    }


    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONNECTED)
    private void onConnected() {

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONDISCONNECT)
    private void onDisconnect(int code, String reason) {

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONERROR)
    private void onError(Exception error) {

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGETIMEOUT)
    private void onMessageTimeout(Message message) {

    }
}
