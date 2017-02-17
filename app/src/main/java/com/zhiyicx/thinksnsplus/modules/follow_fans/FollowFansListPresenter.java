package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDao;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

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
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FollowFansListPresenter extends BasePresenter<FollowFansListContract.Repository,
        FollowFansListContract.View> implements FollowFansListContract.Presenter {
    private FollowFansBeanGreenDao mFollowFansBeanGreenDao;

    @Inject
    public FollowFansListPresenter(FollowFansListContract.Repository repository,
                                   FollowFansListContract.View rootView) {
        super(repository, rootView);
        mFollowFansBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent()
                .followFansBeanGreenDao();
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
//        mFollowFansBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void requestNetData(int maxId, final boolean isLoadMore, int userId, int pageType) {
        Observable<BaseJson<List<FollowFansBean>>> observable = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            observable = mRepository.getFollowListFromNet(userId, maxId);
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            observable = mRepository.getFansListFromNet(userId, maxId);
        }
        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<List<FollowFansBean>>>() {
                    @Override
                    public void call(BaseJson<List<FollowFansBean>> listBaseJson) {
                        mRootView.onNetResponseSuccess(listBaseJson.getData(), isLoadMore);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
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
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFollower(userId);
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFans(userId);
        }
        return followFansBeanList;
    }

    @Override
    public void followUser(int index, long followedId) {
        // 后台通知服务器关注
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_FOLLOW_USER + "/" + followedId);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新

    }

    @Override
    public void cancleFollowUser(int index, long followedId) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
        backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER + "/" + followedId);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }
}
