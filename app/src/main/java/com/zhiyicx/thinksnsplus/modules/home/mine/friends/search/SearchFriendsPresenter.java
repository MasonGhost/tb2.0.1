package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SearchFriendsPresenter extends AppBasePresenter< SearchFriendsContract.View>
        implements SearchFriendsContract.Presenter{
    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    public SearchFriendsPresenter(SearchFriendsContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseFriendsRepository.getUserFriendsList(maxId, mRootView.getKeyWord())
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
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }
}
