package com.zhiyicx.thinksnsplus.modules.home.message;

import android.os.Handler;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

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
    public void requestNetData(int maxId, boolean isLoadMore) {
        final List<MessageItemBean> data = new ArrayList<>();
        Message likeMessage = new Message();
        likeMessage.setTxt("一叶之秋、晴天色"
                + mContext.getString(R.string.like_me));
        UserInfoBean userinfo = new UserInfoBean();
        userinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&" +
                "imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
        likeMessage.setCreate_time(System.currentTimeMillis());
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
            test.setLastMessage(likeMessage);
            test.setUnReadMessageNums((int) (Math.random() * 10));
            data.add(test);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mRootView.onNetResponseSuccess(data, false);
                mRootView.hideLoading();
                mRootView.showMessage("网络不加。。。。。");
            }
        }, 2000);
    }

    @Override
    public List<MessageItemBean> requestCacheData(int maxId, boolean isLoadMore) {
        final List<MessageItemBean> data = new ArrayList<>();
        Message likeMessage = new Message();
        likeMessage.setTxt("一叶之秋、晴天色"
                + mContext.getString(R.string.like_me));
        UserInfoBean userinfo = new UserInfoBean();
        userinfo.setUserIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486986007059&di=f53cac87e3dfa7572c8a9f2a06227631&" +
                "imgtype=0&src=http%3A%2F%2Fimg17.3lian.com%2Fd%2Ffile%2F201701%2F20%2F70ac16a3c3336a3bc2fb28c147bf2049.jpg");
        likeMessage.setCreate_time(System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
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
            test.setLastMessage(likeMessage);
            test.setUnReadMessageNums((int) (Math.random() * 10));
            data.add(test);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public MessageItemBean updateCommnetItemData() {
        if (mItemBeanComment == null) {
            mItemBeanComment = new MessageItemBean();
            Message commentMessage = new Message();
            commentMessage.setTxt("默默的小红大家来到江苏高考加分临时价格来看大幅减少了国家法律的世界观浪费时间管理方式的建立各级地方楼市困局"
                    + mContext.getString(R.string.comment_me));
            commentMessage.setCreate_time(System.currentTimeMillis());
            mItemBeanComment.setLastMessage(commentMessage);
            mItemBeanComment.setUnReadMessageNums(Math.round(15));
        }
        return mItemBeanComment;
    }

    @Override
    public MessageItemBean updateLikeItemData() {
        if (mItemBeanLike == null) {
            mItemBeanLike = new MessageItemBean();
            Message likeMessage = new Message();
            likeMessage.setTxt("默默的小红大家来到江苏高考加分临时价格来看大幅减少了国家法律的世界观浪费时间管理方式的建立各级地方楼市困局"
                    + mContext.getString(R.string.comment_me));
            likeMessage.setCreate_time(System.currentTimeMillis());
            mItemBeanLike.setLastMessage(likeMessage);
            mItemBeanLike.setUnReadMessageNums(Math.round(15));
        }
        return mItemBeanLike;
    }

    public void createChat() {
        mChatRepository.createConveration(0, "七夜和超超", "", mAuthRepository.getAuthBean().getUser_id() + ",4")
                .subscribe(new BaseSubscribe<Conversation>() {
                    @Override
                    protected void onSuccess(Conversation data) {

                    }

                    @Override
                    protected void onFailure(String message) {
                        LogUtils.d(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e("error",throwable);
                    }
                });
    }
}