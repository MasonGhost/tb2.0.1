package com.zhiyicx.thinksnsplus.modules.home.message;

import android.os.Handler;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    public void requestNetData(int maxId, boolean isLoadMore) {
        final List<MessageItemBean> data = new ArrayList<>();
        Conversation likeMessage = new Conversation();
        likeMessage.setLast_message_text("一叶之秋、晴天色"
                + mContext.getString(R.string.like_me));
        UserInfoBean userinfo = new UserInfoBean();
        userinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&" +
                "imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
        likeMessage.setLast_message_time(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            MessageItemBean test = new MessageItemBean();
            UserInfoBean testUserinfo = new UserInfoBean();
            testUserinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com" +
                    "%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
            testUserinfo.setName("颤三");
            testUserinfo.setUser_id((long) (10 + i));
            test.setUserInfo(testUserinfo);
            Message testMessage = new Message();
            testMessage.setTxt("一叶之秋、晴天色" + i
                    + mContext.getString(R.string.like_me));
            testMessage.setCreate_time(System.currentTimeMillis());
            test.setConversation(likeMessage);
            test.setUnReadMessageNums((int) (Math.random() * 10));
            data.add(test);
        }
        mRootView.hideLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mRootView.onNetResponseSuccess(data, false);

                mRootView.showMessage("网络不加。。。。。");
            }
        }, 500);
    }

    /**
     * 没有加载更多，一次全部取出
     *
     * @param maxId
     * @param isLoadMore 加载状态
     * @return
     */
    @Override
    public List<MessageItemBean> requestCacheData(int maxId, boolean isLoadMore) {
//         List<MessageItemBean> data = new ArrayList<>();
//        Conversation likeMessage = new Conversation();
//        likeMessage.setLast_message_text("一叶之秋、晴天色"
//                + mContext.getString(R.string.like_me));
//        UserInfoBean userinfo = new UserInfoBean();
//        userinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&" +
//                "imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
//        likeMessage.setLast_message_time(System.currentTimeMillis());
//        for (int i = 0; i < 5; i++) {
//            MessageItemBean test = new MessageItemBean();
//            UserInfoBean testUserinfo = new UserInfoBean();
//            testUserinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&imgtype=0&src=http%3A%2F%2Fimg17.3lian.com" +
//                    "%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
//            testUserinfo.setName("颤三");
//            testUserinfo.setUser_id((long) (10 + i));
//            test.setUserInfo(testUserinfo);
//            Message testMessage = new Message();
//            testMessage.setTxt("一叶之秋、晴天色" + i
//                    + mContext.getString(R.string.like_me));
//            testMessage.setCreate_time(System.currentTimeMillis());
//            test.setConversation(likeMessage);
//            test.setUnReadMessageNums((int) (Math.random() * 10));
//            data.add(test);
//        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return mChatRepository.getConversionListData(mAuthRepository.getAuthBean().getUser_id());

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

    @Override
    public void createChat() {
        final String uids = mAuthRepository.getAuthBean().getUser_id() + ",4";
        final String pair = mAuthRepository.getAuthBean().getUser_id() + "&4";// "pair":null,   // type=0时此项为两个uid：min_uid&max_uid
        mChatRepository.createConveration(ChatType.CHAT_TYPE_PRIVATE, "七夜和超超", "", uids)
                .subscribe(new BaseSubscribe<Conversation>() {
                    @Override
                    protected void onSuccess(Conversation data) {
                        data.setUsids(uids);
                        data.setPair(pair);
                        mChatRepository.insertOrUpdateConversation(data);
                        mRootView.showMessage("创建对话成功");
                    }

                    @Override
                    protected void onFailure(String message) {
                        LogUtils.d(message);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e("error", throwable);
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
    }

    /*******************************************
     * IM 相关
     *********************************************/


    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGERECEIVED)
    private void onMessageReceived(Message message) {
        if (!(ActivityHandler.getInstance().currentActivity() instanceof HomeActivity)) {
            return;
        }
        LogUtils.d(TAG,"------onMessageReceived------->"+message);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_ONMESSAGEACKRECEIVED)
    private void onMessageACKReceived(Message message) {
        if (!(ActivityHandler.getInstance().currentActivity() instanceof HomeActivity)) {
            return;
        }
        LogUtils.d(TAG,"-------onMessageACKReceived------------>"+message);
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