package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
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
public class FollowFansListPresenter extends BasePresenter<FollowFansListContract.Repository,
        FollowFansListContract.View> implements FollowFansListContract.Presenter {
    private FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;

    @Inject
    public FollowFansListPresenter(FollowFansListContract.Repository repository,
                                   FollowFansListContract.View rootView) {
        super(repository, rootView);
        mFollowFansBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent()
                .followFansBeanGreenDao();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(int maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(int maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<FollowFansBean> data) {
        mFollowFansBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void requestNetData(final int maxId, final boolean isLoadMore, final int userId, final int pageType) {
        Observable<BaseJson<List<FollowFansBean>>> observable = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            observable = mRepository.getFollowListFromNet(userId, maxId);
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            observable = mRepository.getFansListFromNet(userId, maxId);
        }
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<List<FollowFansBean>>() {
                    @Override
                    protected void onSuccess(List<FollowFansBean> data) {
                        insertOrUpdateData(data);// 保存到数据库
                        // 多表连查，获取用户信息
                        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
                            data = mFollowFansBeanGreenDao.getSomeOneFollower(userId,maxId);
                        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
                            data = mFollowFansBeanGreenDao.getSomeOneFans(userId,maxId);
                        }
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                        // 处理用户信息缺失
                        dealWithUserInfo(pageType, data);
                    }

                    @Override
                    protected void onFailure(String message) {
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
    public List<FollowFansBean> requestCacheData(int maxId, boolean isLoadMore, int userId, int pageType) {
        List<FollowFansBean> followFansBeanList = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFollower(userId,maxId);
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFans(userId,maxId);
        }
        dealWithUserInfo(pageType, followFansBeanList);
        return followFansBeanList;
    }

    @Override
    public void followUser(int index, FollowFansBean followFansBean) {
        // 后台通知服务器关注
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_FOLLOW_USER);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getFollowedUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        int followState = mFollowFansBeanGreenDao.setStateToFollowed(followFansBean);
        followFansBean.setFollowState(followState);
        mRootView.upDateFollowFansState(index, followState);

    }

    @Override
    public void cancleFollowUser(int index, FollowFansBean followFansBean) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getFollowedUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        int followState = mFollowFansBeanGreenDao.setStateToUnFollowed(followFansBean);
        followFansBean.setFollowState(followState);
        mRootView.upDateFollowFansState(index, followState);
    }

    /**
     * 在后台任务获取到最新的用户信息后，更新界面的用户信息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USERINFO_UPDATE)
    public void upDateUserInfo(List<UserInfoBean> userInfoBeanList) {
        mRootView.upDateUserInfo(userInfoBeanList);
    }

    // 当数据库获取用户信息为空时，需要尝试从网络拉去信息
    private void dealWithUserInfo(int pageType, List<FollowFansBean> followFansBeanList) {
        List<Integer> userIdList = new ArrayList<>();
        // 统一处理获取用户信息
        for (FollowFansBean followFansBean : followFansBeanList) {
            UserInfoBean userInfoBean = null;
            if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
               /* userInfoBean = followFansBean.getFllowedUser();
                if (userInfoBean == null) {
                    userIdList.add((int) followFansBean.getFollowedUserId());
                }*/
                userIdList.add((int) followFansBean.getFollowedUserId());
            } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
               /* userInfoBean = followFansBean.getUser();
                if (userInfoBean == null) {
                    userIdList.add((int) followFansBean.getUserId());
                }*/
                userIdList.add((int) followFansBean.getUserId());
            }

        }
        if (!userIdList.isEmpty()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user_id", userIdList);
            BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setParams(hashMap);
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.GET_USER_INFO);
            BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        }
    }
}
