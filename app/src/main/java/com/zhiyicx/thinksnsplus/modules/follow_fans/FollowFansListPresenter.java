package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FollowFansListPresenter extends AppBasePresenter<FollowFansListContract.Repository,
        FollowFansListContract.View> implements FollowFansListContract.Presenter {

    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    private int mPageType;
    private long mUserId;

    @Inject
    public FollowFansListPresenter(FollowFansListContract.Repository repository,
                                   FollowFansListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<FollowFansBean> data, boolean isLoadMore) {
        mFollowFansBeanGreenDao.deleteDataByType(mPageType, mUserId);
        mFollowFansBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void requestNetData(final Long maxId, final boolean isLoadMore, final long userId, final int pageType) {
        this.mUserId = userId;
        this.mPageType = pageType;
        Observable<BaseJson<List<FollowFansBean>>> observable = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            observable = mRepository.getFollowListFromNet(userId, maxId.intValue());
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            observable = mRepository.getFansListFromNet(userId, maxId.intValue());
        }
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<List<FollowFansBean>>() {
                    @Override
                    protected void onSuccess(List<FollowFansBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        Throwable throwable = new Throwable(message);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, throwable.getMessage());
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore, long userId, int pageType) {
        List<FollowFansBean> followFansBeanList = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFollower((int) userId, maxId.intValue());
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFans((int) userId, maxId.intValue());
        }
        return followFansBeanList;
    }

    @Override
    public void followUser(int index, FollowFansBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);

    }

    @Override
    public void cancleFollowUser(int index, FollowFansBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);
    }

    @Override
    public void cleanNewFans() {
        mFlushMessageBeanGreenDao.readMessageByKey(ApiConfig.NOTIFICATION_KEY_FOLLOWS);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW)
    public void upDateFollowState(FollowFansBean followFansBean) {
        // 注意当前操作是在另一个页面订阅：如果我在粉丝页面点击关注按钮，当前订阅是在关注页面：
        List<FollowFansBean> followFansBeanList = mRootView.getFollowListData();
        Iterator<FollowFansBean> iterator = followFansBeanList.iterator();
        int position = 0;
        while (iterator.hasNext()) {
            FollowFansBean oldFollowList = iterator.next();
            // 如果粉丝（关注）列表中存在同样的用户，更新它
            if (oldFollowList.getTargetUserId() == followFansBean.getTargetUserId()) {
                // 更新内存数据
                oldFollowList.setOrigin_follow_status(followFansBean.getOrigin_follow_status());
                mRootView.upDateFollowFansState(position);
                break;
            }
            // 遍历到最后一条数据，仍然不存在该用户，并且，当前订阅页面是关注页面，需要添加item
            else if (position == followFansBeanList.size() - 1 && mRootView.getPageType() == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
                followFansBeanList.add(0, followFansBean);
                mRootView.upDateFollowFansState();
            }
            position++;
        }
    }

}
