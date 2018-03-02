package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.rank.RankData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2018/03/01/14:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ContributionListPresenter extends AppBasePresenter<ContributionListContract.View> implements ContributionListContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public ContributionListPresenter(ContributionListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getContributionRank(maxId, TSListFragment.DEFAULT_PAGE_SIZE, mRootView.getType())
                .subscribe(new BaseSubscribeForV2<List<ContributionData>>() {
                    @Override
                    protected void onSuccess(List<ContributionData> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, isLoadMore);

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ContributionData> data, boolean isLoadMore) {
        return false;
    }
}
