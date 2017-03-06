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
        return false;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<FollowFansBean> data) {
        mFollowFansBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void requestNetData(final Long maxId, final boolean isLoadMore, final int userId, final int pageType) {
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
                        insertOrUpdateData(data);// 保存到数据库
                        mRootView.onNetResponseSuccess(data, isLoadMore);
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
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore, int userId, int pageType) {
        List<FollowFansBean> followFansBeanList = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFollower(userId, maxId.intValue());
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFans(userId, maxId.intValue());
        }
        return followFansBeanList;
    }

    @Override
    public void followUser(int index, FollowFansBean followFansBean) {
        // 更新数据
        followFansBean.setOrigin_follow_status(FollowFansBean.IFOLLOWED_STATE);
        // 后台通知服务器关注
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_FOLLOW_USER);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getTargetUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        mFollowFansBeanGreenDao.insertOrReplace(followFansBean);
        mRootView.upDateFollowFansState(index, FollowFansBean.IFOLLOWED_STATE);

    }

    @Override
    public void cancleFollowUser(int index, FollowFansBean followFansBean) {
        // 更新数据
        followFansBean.setOrigin_follow_status(FollowFansBean.UNFOLLOWED_STATE);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getTargetUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        mFollowFansBeanGreenDao.insertOrReplace(followFansBean);
        mRootView.upDateFollowFansState(index, FollowFansBean.UNFOLLOWED_STATE);
    }
}
