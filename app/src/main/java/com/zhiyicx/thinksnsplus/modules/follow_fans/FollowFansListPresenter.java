package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FollowFansListPresenter extends AppBasePresenter<
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
    public FollowFansListPresenter(
            FollowFansListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void requestNetData(final Long maxId, final boolean isLoadMore, final long userId, final int pageType) {
        this.mUserId = userId;
        this.mPageType = pageType;
        Observable<List<UserInfoBean>> observable = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            observable = mUserInfoRepository.getFollowListFromNet(userId, maxId.intValue());
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            observable = mUserInfoRepository.getFansListFromNet(userId, maxId.intValue());
        }
        Subscription subscription = observable
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
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
    public List<UserInfoBean> requestCacheData(Long maxId, boolean isLoadMore, long userId, int pageType) {
///        取消缓存，因为需要首先显示最新关注用户
//        List<UserInfoBean> followFansBeanList = null;
//        if(userId!= AppApplication.getmCurrentLoginAuth().getUser_id()){
//            return null;
//        }
//        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
//            followFansBeanList = mUserInfoBeanGreenDao.getFollowingUserInfo(maxId.intValue());
//        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
//            followFansBeanList = mUserInfoBeanGreenDao.getFollowerUserInfo( maxId.intValue());
//        }
//        mRootView.onCacheResponseSuccess(followFansBeanList, isLoadMore);
//
//        return followFansBeanList;
        mRootView.onCacheResponseSuccess(null, isLoadMore);
        return new ArrayList<>();


    }

    @Override
    public void followUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);

    }

    @Override
    public void cancleFollowUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);
    }

    @Override
    public void cleanNewFans() {
        mFlushMessageBeanGreenDao.readMessageByKey(NotificationConfig.NOTIFICATION_KEY_FOLLOWS);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW)
    public void upDateFollowState(UserInfoBean followFansBean) {
        // 注意当前操作是在另一个页面订阅：如果我在粉丝页面点击关注按钮，当前订阅是在关注页面：
        List<UserInfoBean> followFansBeanList = mRootView.getListDatas();
        Iterator<UserInfoBean> iterator = followFansBeanList.iterator();
        int position = 0;
        while (iterator.hasNext()) {
            UserInfoBean userInfoBean = iterator.next();
            // 如果粉丝（关注）列表中存在同样的用户，更新它
            if (userInfoBean.getUser_id() == followFansBean.getUser_id()) {
                // 更新内存数据
                userInfoBean.setFollower(followFansBean.isFollower());
                userInfoBean.setFollowing(followFansBean.isFollowing());
                mRootView.upDateFollowFansState(position);
                break;
            }
            // 遍历到最后一条数据，仍然不存在该用户，并且，当前订阅页面是关注页面，需要添加item
            else if (position == followFansBeanList.size() - 1 && mRootView.getPageType() == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE &&
                    followFansBean.isFollower()) {
                followFansBeanList.add(0, followFansBean);
                mRootView.upDateFollowFansState();
            }
            position++;
        }
    }

}
