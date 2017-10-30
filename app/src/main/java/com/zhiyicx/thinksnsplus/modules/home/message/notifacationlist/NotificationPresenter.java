package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class NotificationPresenter extends AppBasePresenter<NotificationContract.Repository, NotificationContract.View>
        implements NotificationContract.Presenter {


    @Inject
    MessageRepository mMessageRepository;
    private boolean mIsFirst = true;

    @Inject
    public NotificationPresenter(NotificationContract.Repository repository, NotificationContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getNotificationList(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<TSPNotificationBean>>() {
                    @Override
                    protected void onSuccess(List<TSPNotificationBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
        // 除开第一次自动刷新外，其他的刷新时候需要更新小红点
        if (!mIsFirst && !isLoadMore) {
            readNotification();
        } else {
            mIsFirst = false;
        }

    }

    @Override
    public List<TSPNotificationBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<TSPNotificationBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void readNotification() {

        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_SET_NOTIFICATION_TIP_VISABLE);

        Subscription subscribe = mMessageRepository.makeNotificationAllReaded()
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        LogUtils.d("makeNotificationReaded::" + "onSuccess");
                    }
                });
        addSubscrebe(subscribe);
    }
}
