package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_PAGE_MAX_ID;

/**
 * @Describe doc  {@see https://slimkit.github.io/plus-docs/v2/package-feed/reward}
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public class RewardListPresenter extends AppBasePresenter<RewardListContract.Repository, RewardListContract.View> implements RewardListContract
        .Presenter {


    @Inject
    public RewardListPresenter(RewardListContract.Repository repository, RewardListContract.View rootView) {
        super(repository, rootView);
    }

    /**
     * 默认排序 是 amount
     *
     * @param maxId      当前获取到数据的最大 id
     * @param isLoadMore 加载状态
     */
    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Integer since = null;
        if (!DEFAULT_PAGE_MAX_ID.equals(maxId)) {
            since = maxId.intValue();
        }
        switch (mRootView.getCurrentType()) {
            case INFO:
                getRewardUsers(mRepository.rewardInfoList(mRootView.getSourceId(), TSListFragment.DEFAULT_ONE_PAGE_SIZE, since, null,
                        null)
                        , isLoadMore);
                break;

            case DYNAMIC:
                getRewardUsers(mRepository.rewardDynamicList(mRootView.getSourceId(), TSListFragment.DEFAULT_ONE_PAGE_SIZE, since, null, null)
                        , isLoadMore);
                break;
            case QA_ANSWER:
                getRewardUsers(mRepository.rewardQAList(mRootView.getSourceId(), TSListFragment.DEFAULT_ONE_PAGE_SIZE, since, null)
                        , isLoadMore);
            default:

        }

    }

    private void getRewardUsers(Observable<List<RewardsListBean>> observable, final boolean isLoadMore) {
        Subscription subscription = observable.subscribe(new BaseSubscribeForV2<List<RewardsListBean>>() {
            @Override
            protected void onSuccess(List<RewardsListBean> data) {
                mRootView.onNetResponseSuccess(data, isLoadMore);
            }

            @Override
            protected void onFailure(String message, int code) {
                mRootView.showMessage(message);
            }

            @Override
            protected void onException(Throwable throwable) {
                mRootView.onResponseError(throwable, isLoadMore);
            }
        });
        addSubscrebe(subscription);
    }


    @Override
    public List<RewardsListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return mRootView.getCacheData();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RewardsListBean> data, boolean isLoadMore) {
        return true;
    }


}

