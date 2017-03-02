package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailContract.Repository,
        DynamicDetailContract.View> implements DynamicDetailContract.Presenter {
    @Inject
    public DynamicDetailPresenter(DynamicDetailContract.Repository repository, DynamicDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<DynamicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return false;
    }

    @Override
    public void getDynamicDigList(Long feed_id, Integer max_id) {
        Subscription subscription = mRepository.getDynamicDigList(feed_id, max_id)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(ApiConfig.DEFAULT_MAX_RETRY_COUNT, 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                       mRootView.setDigHeadIcon(data);
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }
}
