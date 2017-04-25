package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationPresenter extends BasePresenter<SystemConversationContract.Repository, SystemConversationContract.View> implements SystemConversationContract.Presenter {

    @Inject
    public SystemConversationPresenter(SystemConversationContract.Repository repository, SystemConversationContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void sendTextMessage(String text) {
        if (TextUtils.isEmpty(text)) {
            mRootView.showSnackWarningMessage("消息不能为空");
            return;
        }
        mRepository.systemFeedback(text)
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    /**
     * 获取网络数据
     *
     * @param maxId      当前获取到数据的最大 id
     * @param isLoadMore true 加载更多，false 刷新
     */
    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription systemconversationsSub = mRepository.getSystemConversations(maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .map(new Func1<BaseJson<List<SystemConversationBean>>, BaseJson<List<ChatItemBean>>>() {
                    @Override
                    public BaseJson<List<ChatItemBean>> call(BaseJson<List<SystemConversationBean>> listBaseJson) {
                        BaseJson<List<ChatItemBean>> chatData = new BaseJson<List<ChatItemBean>>();
                        chatData.setCode(listBaseJson.getCode());
                        chatData.setMessage(listBaseJson.getMessage());
                        chatData.setStatus(listBaseJson.isStatus());
                        if (listBaseJson.isStatus()) {
                            chatData.setData(packageChatItemBean(listBaseJson.getData()));
                        }
                        return chatData;
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new BaseSubscribe<List<ChatItemBean>>() {
                    @Override
                    protected void onSuccess(List<ChatItemBean> data) {
                        mRootView.updateData(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(systemconversationsSub);
    }

    /**
     * 获取本地数据
     *
     * @param max_Id
     * @return
     */
    @Override
    public void requestCacheData(long max_Id) {
        Subscription cacheSub = Observable.just(mRepository.requestCacheData(max_Id))
                .map(new Func1<List<SystemConversationBean>, List<ChatItemBean>>() {
                    @Override
                    public List<ChatItemBean> call(List<SystemConversationBean> systemConversationBeen) {
                        return packageChatItemBean(systemConversationBeen);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ChatItemBean>>() {
                    @Override
                    public void call(List<ChatItemBean> chatItemBeen) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        addSubscrebe(cacheSub);
    }

    @NonNull
    private List<ChatItemBean> packageChatItemBean(List<SystemConversationBean> systemConversationBeen) {
        List<ChatItemBean> datas = new ArrayList<>();
        for (SystemConversationBean systemConversationBean : systemConversationBeen) {
            ChatItemBean chatItemBean = new ChatItemBean();
            Message message = new Message();
            message.setId(systemConversationBean.getId().intValue());
            message.setTxt(systemConversationBean.getContent());
            message.setCreate_time(TimeUtils.string2MillisDefaultLocal(systemConversationBean.getCreated_at()));
            message.setSend_status(MessageStatus.SEND_SUCCESS);
            message.setUid(systemConversationBean.getUser_id().intValue());
            chatItemBean.setLastMessage(message);
            chatItemBean.setUserInfo(systemConversationBean.getUserInfo());
            datas.add(chatItemBean);
        }
        return datas;
    }
}
