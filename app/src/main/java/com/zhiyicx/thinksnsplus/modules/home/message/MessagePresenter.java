package com.zhiyicx.thinksnsplus.modules.home.message;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.AuthData;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.source.local.JpushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
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

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    JpushMessageBeanGreenDaoImpl mJpushMessageBeanGreenDao;

    private MessageItemBean mItemBeanComment;
    private MessageItemBean mItemBeanLike;

    @Inject
    public MessagePresenter(MessageContract.Repository repository, MessageContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
        handleFlushMessage();
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
        mRootView.updateLikeItemData(mItemBeanLike);
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

        List<JpushMessageBean> mCommentJpushMessageBeen = mJpushMessageBeanGreenDao.getCommentJpushMessage();

        if (mItemBeanComment == null) {
            mItemBeanComment = new MessageItemBean();
            Conversation commentMessage = new Conversation();
            Message message = new Message();
            commentMessage.setLast_message(message);
            mItemBeanComment.setConversation(commentMessage);
        }
        for (JpushMessageBean jpushMessageBean : mCommentJpushMessageBeen) {
            if (jpushMessageBean.getCreat_time() > mItemBeanComment.getConversation().getLast_message_time()) {
                mItemBeanComment.getConversation().setLast_message_time(jpushMessageBean.getCreat_time());
            }
            if (!jpushMessageBean.isRead()) {
                mItemBeanComment.setUnReadMessageNums(mItemBeanComment.getUnReadMessageNums() + 1);
            }
            // TODO: 2017/4/12 添加用户信息
        }
        mItemBeanComment.getConversation().getLast_message().setTxt("还没有人"
                + mContext.getString(R.string.comment_me));


        return mItemBeanComment;
    }

    @Override
    public MessageItemBean updateLikeItemData() {
        List<JpushMessageBean> mDigJpushMessageBeen = mJpushMessageBeanGreenDao.getDigJpushMessage();

        if (mItemBeanLike == null) {
            mItemBeanLike = new MessageItemBean();
            Conversation likeConversation = new Conversation();
            Message message = new Message();
            likeConversation.setLast_message(message);
            mItemBeanLike.setConversation(likeConversation);
        }
        for (JpushMessageBean jpushMessageBean : mDigJpushMessageBeen) {
            if (jpushMessageBean.getCreat_time() > mItemBeanLike.getConversation().getLast_message_time()) {
                mItemBeanLike.getConversation().setLast_message_time(jpushMessageBean.getCreat_time());
            }
            if (!jpushMessageBean.isRead()) {
                mItemBeanLike.setUnReadMessageNums(mItemBeanLike.getUnReadMessageNums() + 1);
            }
            // TODO: 2017/4/12 添加用户信息
        }

        mItemBeanLike.getConversation().getLast_message().setTxt("还没有人"
                + mContext.getString(R.string.like_me));

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

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_AUTHSUCESSED)
    private void onAuthSuccessed(AuthData authData) {
//        mRootView.showSnackSuccessMessage("IM 聊天加载成功");
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
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM: // 推送携带的消息  {"seq":36,"msg_type":0,"cid":1,"mid":338248648800337924,"type":"im","uid":20} IM 消息通过IM接口 同步，故不需要对 推送消息做处理

                break;
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_FEED:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_CHANNEL:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_MUSIC:
            case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_NEWS:
            default:
                switch (jpushMessageBean.getAction()) {
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_COMMENT:
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_ACTION_DIGG:
                    default:
                        // 服务器同步未读评论和点赞消息
                        handleFlushMessage();
                        break;
                }
                break;

        }
    }

    /**
     * 处理 获取用户收到的最新消息
     * @return
     */
    private Observable<BaseJson<List<FlushMessages>>> handleFlushMessage() {
//        return mUserInfoRepository.getMyFlushMessage();
        return null;
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
        EventBus.getDefault().post(isShowMessgeTip, EventBusTagConfig.EVENT_IM_SETMESSAGETIPVISABLE);
    }

}