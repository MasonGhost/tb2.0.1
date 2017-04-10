package com.zhiyicx.thinksnsplus.modules.channel;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class ChannelListPresenter extends BasePresenter<ChannelListContract.Repository, ChannelListContract.View>
        implements ChannelListContract.Presenter {
    @Inject
    ChannelSubscripBeanGreenDaoImpl mChannelSubscripBeanGreenDao;

    @Inject
    public ChannelListPresenter(ChannelListContract.Repository repository, ChannelListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        int pageType = mRootView.getPageType();
        Observable<BaseJson<List<ChannelSubscripBean>>> observable = null;
        switch (pageType) {
            case ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST:
                observable = mRepository.getMySubscribChannelList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                break;
            case ChannelListViewPagerFragment.PAGE_ALL_CHANNEL_LIST:
                observable = mRepository.getAllChannelList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                break;
            default:
        }
        dealWithChannelNetData(maxId, isLoadMore, observable);

    }

    @Override
    public List<ChannelSubscripBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        int pageType = mRootView.getPageType();
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        List<ChannelSubscripBean> channelSubscripBeanList = null;
        switch (pageType) {
            case ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST:
                channelSubscripBeanList = mChannelSubscripBeanGreenDao.getSomeOneSubscribChannelList(authBean.getUser_id());
                break;
            case ChannelListViewPagerFragment.PAGE_ALL_CHANNEL_LIST:
                channelSubscripBeanList = mChannelSubscripBeanGreenDao.getAllChannelList(authBean.getUser_id());
                break;
            default:
        }
        return channelSubscripBeanList;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ChannelSubscripBean> data) {
        return false;
    }

    private void dealWithChannelNetData(Long maxId, final boolean isLoadMore, Observable<BaseJson<List<ChannelSubscripBean>>> observable) {
        Subscription subscription = observable.subscribe(new BaseSubscribe<List<ChannelSubscripBean>>() {
            @Override
            protected void onSuccess(List<ChannelSubscripBean> data) {
                mRootView.onNetResponseSuccess(data, isLoadMore);
            }

            @Override
            protected void onFailure(String message, int code) {
                Throwable throwable = new Throwable(message);
                mRootView.onResponseError(throwable, isLoadMore);
            }

            @Override
            protected void onException(Throwable throwable) {
                mRootView.onResponseError(throwable, isLoadMore);
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void handleChannelSubscrib() {

    }
}
