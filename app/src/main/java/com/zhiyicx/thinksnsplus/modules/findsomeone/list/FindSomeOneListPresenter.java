package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_HOT;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_NEARBY;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_NEW;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_RECOMMENT;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FindSomeOneListPresenter extends AppBasePresenter<FindSomeOneListContract.Repository,
        FindSomeOneListContract.View> implements FindSomeOneListContract.Presenter {

    public static final int DEFAULT_PAGE_SIZE = 15;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;


    @Inject
    public FindSomeOneListPresenter(FindSomeOneListContract.Repository repository,
                                    FindSomeOneListContract.View rootView) {
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
    public List<UserInfoBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void requestNetData(final Long maxId, final boolean isLoadMore, final int pageType) {
        Observable<List<UserInfoBean>> observable = null;

        switch (pageType) {
            case TYPE_HOT:
                observable = mUserInfoRepository.getHotUsers(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;

            case TYPE_NEW:
                observable = mUserInfoRepository.getNewUsers(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;
            case TYPE_RECOMMENT:
                observable = mUserInfoRepository.getUsersRecommentByTag(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;
            case TYPE_NEARBY:
                observable = mUserInfoRepository.getHotUsers(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;
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
    public void followUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);

    }

    @Override
    public void cancleFollowUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);
    }


}
