package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.local.DigRankBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.rank.RankContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public class RewardListPresenter extends AppBasePresenter<RewardListContract.Repository, RewardListContract.View> implements RewardListContract.Presenter {


    @Inject
    public RewardListPresenter(RewardListContract.Repository repository, RewardListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        switch (mRootView.getCurrentType()){
            case INFO:
                getInfoRewardUsers(isLoadMore);
                break;

            case DYNAMIC:

                break;

            default:


        }

    }

    private void getInfoRewardUsers(final boolean isLoadMore) {
        Subscription subscription = mRepository.rewardsInfoList(mRootView.getSourceId(), TSListFragment.DEFAULT_ONE_PAGE_SIZE,mRootView.getPage(),null,null)
                .subscribe(new BaseSubscribeForV2<List<RewardsListBean>>() {
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
                       mRootView.onResponseError(throwable,isLoadMore);
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

