package com.zhiyicx.thinksnsplus.modules.channel.list;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
    ChannelInfoBeanGreenDaoImpl mChannelInfoBeanGreenDao;

    @Inject
    public ChannelListPresenter(ChannelListContract.Repository repository, ChannelListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
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
        // 在repository中进行了清空表，和添加数据的操作
        return true;
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
    public void handleChannelSubscrib(int position, ChannelSubscripBean channelSubscripBean) {
        mRepository.handleSubscribChannel(channelSubscripBean);
        EventBus.getDefault().post(channelSubscripBean, EventBusTagConfig.EVENT_CHANNEL_SUBSCRIB);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_CHANNEL_SUBSCRIB)
    public void uploadChannelSubscribState(ChannelSubscripBean channelSubscripBean) {
        List<ChannelSubscripBean> currentPageData = mRootView.getChannelListData();
        int position = currentPageData.indexOf(channelSubscripBean);
        LogUtils.i("uploadChannelSubscribState page " + mRootView.getPageType() + "  position " + position);
        // 如果当前列表存在这样的数据，刷新该数据
        if (position > -1) {
            //更新item的状态
            ChannelSubscripBean currentItem = currentPageData.get(position);
            currentItem.setChannelSubscriped(channelSubscripBean.getChannelSubscriped());
            mRootView.refreshData(position);
        } else {
            // 如果不存在就添加近列表
            currentPageData.add(0, channelSubscripBean);
            mRootView.refreshData();
        }
    }
}
