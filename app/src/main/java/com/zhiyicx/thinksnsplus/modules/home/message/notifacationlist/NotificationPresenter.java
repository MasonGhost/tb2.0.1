package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import android.text.TextUtils;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;
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
        StringBuilder notificationIds = new StringBuilder();
        //代表未读
        for (TSPNotificationBean tspNotificationBean:mRootView.getListDatas()){
            if (TextUtils.isEmpty(tspNotificationBean.getRead_at())) {
                notificationIds.append(tspNotificationBean.getId());
                notificationIds.append(",");
                tspNotificationBean.setRead_at(TimeUtils.getCurrenZeroTimeStr());
            }
        }
        // stream() 部分机型并不支持 Java 8 的循环(魅族 M5 NOTE)
//        mRootView.getListDatas().stream().filter(tspNotificationBean -> TextUtils.isEmpty(tspNotificationBean.getRead_at())).forEach(tspNotificationBean -> { //代表未读
//            notificationIds.append(tspNotificationBean.getId());
//            notificationIds.append(",");
//            tspNotificationBean.setRead_at(TimeUtils.getCurrenZeroTimeStr());
//        });
        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_SET_NOTIFICATION_TIP_VISABLE);

        if (TextUtils.isEmpty(notificationIds.toString())) {
            return;
        }
        Subscription subscribe = mMessageRepository.makeNotificationReaded(notificationIds.toString())
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        LogUtils.d("makeNotificationReaded::" + "onSuccess");
                    }
                });
        addSubscrebe(subscribe);
    }
}
