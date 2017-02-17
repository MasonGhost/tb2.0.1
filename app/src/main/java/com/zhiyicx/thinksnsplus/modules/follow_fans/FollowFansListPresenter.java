package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDao;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

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
    public void requestNetData(int maxId, final boolean isLoadMore, int userId, boolean isFollowed) {
        Observable<BaseJson<List<FollowFansBean>>> observable = null;
        if (isFollowed) {
            observable = mRepository.getFollowListFromNet(userId, maxId);
        } else {
            observable = mRepository.getFansListFromNet(userId, maxId);
        }
        Subscription subscription = observable
                .subscribe(new Action1<BaseJson<List<FollowFansBean>>>() {
                    @Override
                    public void call(BaseJson<List<FollowFansBean>> listBaseJson) {
                        mRootView.onNetResponseSuccess(listBaseJson.getData(), isLoadMore);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<FollowFansBean> requestCacheData(int maxId, boolean isLoadMore, int userId, boolean isFollowed) {
        List<FollowFansBean> followFansBeanList = null;
        if (isFollowed) {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFollower(userId);
        } else {
            followFansBeanList = mFollowFansBeanGreenDao.getSomeOneFans(userId);
        }
        return followFansBeanList;
    }

    @Override
    public void followUser(long userId) {
        BackgroundRequestTaskBean backgroundRequestTaskBean=new BackgroundRequestTaskBean();
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void cancleFollowUser(long userId) {

    }
}
