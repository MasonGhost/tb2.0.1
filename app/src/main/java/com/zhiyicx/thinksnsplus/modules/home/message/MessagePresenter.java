package com.zhiyicx.thinksnsplus.modules.home.message;

import com.google.gson.Gson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessagePresenter extends BasePresenter<MessageContract.Repository, MessageContract.View> implements MessageContract.Presenter {
    @Inject
    ChatContract.Repository mChatRepository;

    @Inject
    AuthRepository mAuthRepository;
    private MessageItemBean mItemBeanComment;
    private MessageItemBean mItemBeanLike;

    @Inject
    public MessagePresenter(MessageContract.Repository repository, MessageContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (AppApplication.getmCurrentLoginAuth() == null)
            return;
        mRepository.getConversationList(AppApplication.getmCurrentLoginAuth().getUser_id())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new BaseSubscribe<List<MessageItemBean>>() {
                    @Override
                    protected void onSuccess(final List<MessageItemBean> data) {
                        mRootView.onNetResponseSuccess(data, false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getResources().getString(R.string.err_net_not_work));
                    }
                });
    }

    /**
     * 没有加载更多，一次全部取出
     *
     * @param maxId
     * @param isLoadMore 加载状态
     * @return
     */
    @Override
    public List<MessageItemBean> requestCacheData(Long maxId, boolean isLoadMore) {
        if (mAuthRepository.getAuthBean() == null) {
            return new ArrayList<>();
        }
        return mChatRepository.getConversionListData(mAuthRepository.getAuthBean().getUser_id());
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data, boolean isLoadMore) {
        return mChatRepository.insertOrUpdateMessageItemBean(data);
    }

    @Override
    public MessageItemBean updateCommnetItemData() {
        if (mItemBeanComment == null) {
            mItemBeanComment = new MessageItemBean();
            Conversation commentMessage = new Conversation();
            Message message = new Message();
            message.setTxt("还没有人"
                    + mContext.getString(R.string.comment_me));
            commentMessage.setLast_message(message);
            commentMessage.setLast_message_time(System.currentTimeMillis() / 1000);
            mItemBeanComment.setConversation(commentMessage);
            mItemBeanComment.setUnReadMessageNums(Math.round(0));
        }
        return mItemBeanComment;
    }

    @Override
    public MessageItemBean updateLikeItemData() {
        if (mItemBeanLike == null) {
            mItemBeanLike = new MessageItemBean();
            Conversation likeConversation = new Conversation();
            Message message = new Message();
            message.setTxt("还没有人"
                    + mContext.getString(R.string.like_me));
            likeConversation.setLast_message(message);
            likeConversation.setLast_message_time(System.currentTimeMillis() / 1000);
            mItemBeanLike.setConversation(likeConversation);
            mItemBeanLike.setUnReadMessageNums(Math.round(0));
        }
        return mItemBeanLike;
    }

    /**
     * 刷新是否显示底部红点
     * 刷新当条item 的未读数
     *
     * @param positon 当条数据位置
     */
    @Override
    public void refreshLastClicikPostion(int positon) {

        // 刷新当条item 的未读数
        Message message = MessageDao.getInstance(mContext).getLastMessageByCid(mRootView.getListDatas().get(positon).getConversation().getCid());
        if (message != null) {
            mRootView.getListDatas().get(positon).getConversation().setLast_message_time(message.getCreate_time());
            mRootView.getListDatas().get(positon).getConversation().setLast_message(message);
        }
        mRootView.getListDatas().get(positon).setUnReadMessageNums(0);

        mRootView.refreshData(); // 刷新加上 header
        checkBottomMessageTip();


    }

    @Override
    public void deletConversation(MessageItemBean messageItemBean) {
        ConversationDao.getInstance(mContext).delConversation(messageItemBean.getConversation().getCid(), messageItemBean.getConversation().getType());
    }

    @Override
    public void getSingleConversation(int cid) {
        mRepository.getSingleConversation(cid)
                .subscribe(new BaseSubscribe<MessageItemBean>() {
                    @Override
                    protected void onSuccess(MessageItemBean data) {
                        if (mRootView.getListDatas().size() == 0) {
                            mRootView.getListDatas().add(data);
                        } else {
                            mRootView.getListDatas().set(0, data);// 置顶新消息
                        }
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    /*******************************************
     * IM 相关
     *********************************************/

    /**
     * 收到聊天消息
     *
     * @param message 聊天类容
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message message) {
        int size = mRootView.getListDatas().size();
        boolean isHasConversion = false; // 对话是否存在
        for (int i = 0; i < size; i++) {
            if (mRootView.getListDatas().get(i).getConversation().getCid() == message.getCid()) {
                mRootView.getListDatas().get(i).setUnReadMessageNums(mRootView.getListDatas().get(i).getUnReadMessageNums() + 1);
                mRootView.getListDatas().get(i).getConversation().setLast_message(message);
                mRootView.getListDatas().get(i).getConversation().setLast_message_time(message.getCreate_time());
                if (i != 0) {
                    Collections.swap(mRootView.getListDatas(), i, 0);
                }
                mRootView.refreshData(); // 加上 header 的位置
                isHasConversion = true;
                break;
            }
        }
        if (!isHasConversion) { // 不存在本地对话，直接服务器获取
            getSingleConversation(message.getCid());
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
        mRootView.hideStickyMessage();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONDISCONNECT)
    private void onDisconnect(int code, String reason) {
        mRootView.showStickyMessage(reason);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONERROR)
    private void onError(Exception error) {
        mRootView.showMessage(error.toString());
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGETIMEOUT)
    private void onMessaeTimeout(Message message) {

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONCONVERSATIONCRATED)
    private void onConversationCreated(MessageItemBean messageItemBean) {
        mRootView.getListDatas().add(0, messageItemBean);
        mRootView.refreshData();
    }

    /**
     * 推送相关
     *
     * @param jpushMessageBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST)
    private void onJpushMessageRecieved(JpushMessageBean jpushMessageBean) {

        switch (jpushMessageBean.getType()) {
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM: // 推送携带的消息  {"seq":36,"msg_type":0,"cid":1,"mid":338248648800337924,"type":"im","uid":20}
                Message message = new Gson().fromJson(jpushMessageBean.getExtras(), Message.class);
                try {
                    JSONObject jsonObject = new JSONObject(jpushMessageBean.getExtras());
                    message.setType(jsonObject.getInt("msg_type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                message.setCreate_time(message.getMid() >> 23 + MessageDao.TIME_DEFAULT_ADD);
                message.setTxt(jpushMessageBean.getMessage());
                MessageDao.getInstance(mContext).insertOrUpdateMessage(message);
                onMessageReceived(message);

                break;
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_FEED:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_CHANNEL:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_MUSIC:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_NEWS:
            default:
                switch (jpushMessageBean.getAction()) {
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_COMMENT:

                        break;
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_DIGG:

                        break;
                    default:
                        break;
                }
                break;

        }

    }

    /**
     * 检测底部小红点是否需要显示
     */
    private void checkBottomMessageTip() {
        // 是否显示底部红点
        boolean isShowMessgeTip = false;
        for (MessageItemBean messageItemBean : mRootView.getListDatas()) {
            if (messageItemBean.getUnReadMessageNums() > 0) {
                isShowMessgeTip = true;
                break;
            }
        }
        EventBus.getDefault().post(isShowMessgeTip, EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED);
    }

}