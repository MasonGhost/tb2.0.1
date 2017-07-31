package com.zhiyicx.thinksnsplus.modules.channel.list;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
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
public class ChannelListPresenter extends AppBasePresenter<ChannelListContract.Repository, ChannelListContract.View>
        implements ChannelListContract.Presenter {
    @Inject
    ChannelSubscripBeanGreenDaoImpl mChannelSubscripBeanGreenDao;
    @Inject
    ChannelInfoBeanGreenDaoImpl mChannelInfoBeanGreenDao;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;

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
        Observable<List<GroupInfoBean>> observable = null;
        switch (pageType) {
            case ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST:
                if (istourist()) {
                    mRootView.gotoAllChannel();
                    // 如果没有登陆，那么直接显示没有数据
                    mRootView.onNetResponseSuccess(null, isLoadMore);
                    return;
                }
                observable = mRepository.getUserJoinedGroupList(maxId);
                break;
            case ChannelListViewPagerFragment.PAGE_ALL_CHANNEL_LIST:
                observable = mRepository.getAllGroupList(maxId);
                break;
            default:
        }
        dealWithGroupNetData(maxId, isLoadMore, pageType, observable);
    }

    @Override
    public List<GroupInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        int pageType = mRootView.getPageType();
        List<GroupInfoBean> groupInfoBeanList = null;
        switch (pageType) {
            case ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST:
                groupInfoBeanList = mGroupInfoBeanGreenDao.getUserJoinedGroup();
                break;
            case ChannelListViewPagerFragment.PAGE_ALL_CHANNEL_LIST:
                groupInfoBeanList = mGroupInfoBeanGreenDao.getMultiDataFromCache();
                break;
            default:
        }
        return groupInfoBeanList;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupInfoBean> data, boolean isLoadMore) {
        // 在repository中进行了清空表，和添加数据的操作
        return true;
    }

    private void dealWithChannelNetData(Long maxId, final boolean isLoadMore, Observable<BaseJson<List<ChannelSubscripBean>>> observable) {
        Subscription subscription = observable.subscribe(new BaseSubscribe<List<ChannelSubscripBean>>() {
            @Override
            protected void onSuccess(List<ChannelSubscripBean> data) {
//                mRootView.onNetResponseSuccess(data, isLoadMore);
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

    private void dealWithGroupNetData(Long maxId, final boolean isLoadMore, int type, Observable<List<GroupInfoBean>> observable) {
        Subscription subscription = observable
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<GroupInfoBean>>() {
                    @Override
                    protected void onSuccess(List<GroupInfoBean> data) {
                        // 自己的圈子，手动设置为已加入
                        if (type == ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST) {
                            for (GroupInfoBean groupInfoBean : data) {
                                groupInfoBean.setIs_member(1);
                            }
                        }
                        mGroupInfoBeanGreenDao.saveMultiData(data);
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        Throwable throwable = new Throwable(message);
                        mRootView.onResponseError(throwable, isLoadMore);
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
    public void handleChannelSubscrib(int position, ChannelSubscripBean channelSubscripBean) {

        mRepository.handleSubscribChannel(channelSubscripBean);
        EventBus.getDefault().post(channelSubscripBean, EventBusTagConfig.EVENT_CHANNEL_SUBSCRIB);
    }

    @Override
    public List<SystemConfigBean.Advert> getAdvert() {
        List<SystemConfigBean.Advert> imageAdvert = new ArrayList<>();
        for (SystemConfigBean.Advert advert : mSystemRepository.getBootstrappersInfoFromLocal().getAdverts()) {
            if (advert.getImageAdvert() != null) {
                imageAdvert.add(advert);
            }
        }
        return imageAdvert;
    }

    @Override
    public void handleGroupJoin(int position, GroupInfoBean groupInfoBean) {
        boolean isJoined = groupInfoBean.getIs_member() == 1;
        if (isJoined) {
            // 已经订阅，变为未订阅
            groupInfoBean.setMembers_count(groupInfoBean.getMembers_count() - 1);// 订阅数-1
        } else {
            // 未订阅，变为已订阅
            groupInfoBean.setMembers_count(groupInfoBean.getMembers_count() + 1);// 订阅数+1
        }
        // 更改数据源，切换订阅状态
        groupInfoBean.setIs_member(isJoined ? 0 : 1);
        mGroupInfoBeanGreenDao.updateSingleData(groupInfoBean);
        mRepository.handleGroupJoin(groupInfoBean);
        EventBus.getDefault().post(groupInfoBean, EventBusTagConfig.EVENT_GROUP_JOIN);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_CHANNEL_SUBSCRIB)
    public void uploadChannelSubscribState(GroupInfoBean channelSubscripBean) {
        List<GroupInfoBean> currentPageData = mRootView.getGroupList();
        int position = currentPageData.indexOf(channelSubscripBean);
        LogUtils.i("uploadChannelSubscribState page " + mRootView.getPageType() + "  position " + position);
        // 如果当前列表存在这样的数据，刷新该数据
        if (position > -1) {
            //更新item的状态
            GroupInfoBean currentItem = currentPageData.get(position);
            int newFollowCount = channelSubscripBean.getMembers_count();
            currentItem.setMembers_count(newFollowCount);
            currentItem.setIs_member(channelSubscripBean.getIs_member());
            mRootView.refreshData(position);
        } else {
            // 如果不存在就添加近列表
            currentPageData.add(0, channelSubscripBean);
            mRootView.refreshData();
        }
    }
}
