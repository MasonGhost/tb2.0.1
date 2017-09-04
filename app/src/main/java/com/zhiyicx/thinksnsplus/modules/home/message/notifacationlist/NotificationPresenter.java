package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;

import org.jetbrains.annotations.NotNull;

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
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
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
}
