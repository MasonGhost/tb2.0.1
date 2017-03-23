package com.zhiyicx.thinksnsplus.modules.home.message;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
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
        mRepository.getMessageList(AppApplication.getmCurrentLoginAuth().getUser_id())
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
                    protected void onFailure(String message) {
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
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data) {
        return mChatRepository.insertOrUpdateMessageItemBean(data);
    }

    @Override
    public MessageItemBean updateCommnetItemData() {
        if (mItemBeanComment == null) {
            mItemBeanComment = new MessageItemBean();
            Conversation commentMessage = new Conversation();
            commentMessage.setLast_message_text("还没有人"
                    + mContext.getString(R.string.comment_me));
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
            Conversation likeMessage = new Conversation();
            likeMessage.setLast_message_text("还没有人"
                    + mContext.getString(R.string.like_me));
            likeMessage.setLast_message_time(System.currentTimeMillis() / 1000);
            mItemBeanLike.setConversation(likeMessage);
            mItemBeanLike.setUnReadMessageNums(Math.round(0));
        }
        return mItemBeanLike;
    }

    /**
     * 刷新是否显示底部红点
     * 刷新当条item 的未读数
     *
     * @param positon                当条数据位置
     * @param currentMessageItemBean 当条数据
     * @param data                   所有数据
     */
    @Override
    public void refreshLastClicikPostion(int positon, MessageItemBean currentMessageItemBean, List<MessageItemBean> data) {

        // 刷新当条item 的未读数
        Message message = MessageDao.getInstance(mContext).getLastMessageByCid(currentMessageItemBean.getConversation().getCid());
        if (message != null) {
            currentMessageItemBean.getConversation().setLast_message_time(message.getCreate_time());
            currentMessageItemBean.getConversation().setLast_message_text(message.getTxt());
        }
        currentMessageItemBean.setUnReadMessageNums(0);
        mRootView.refreshLastClicikPostion(positon, currentMessageItemBean);

        // 是否显示底部红点
        boolean isShowMessgeTip = false;
        for (MessageItemBean messageItemBean : data) {
            if (messageItemBean.getUnReadMessageNums() > 0) {
                isShowMessgeTip = true;
                break;
            }
        }
        EventBus.getDefault().post(isShowMessgeTip, EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED);

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
        mRootView.refreshMessageUnreadNum(message);

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
    private void onMessageTimeout(Message message) {

    }

}