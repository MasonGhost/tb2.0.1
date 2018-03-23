package com.zhiyicx.thinksnsplus.modules.tb.contract;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by lx on 2018/3/23.
 */

public class ContractListPresenter extends AppBasePresenter<ContractListContract.View> implements ContractListContract.Presenter{

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public ContractListPresenter(ContractListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getContract()
                .subscribe(new BaseSubscribeForV2<List<ContractData>>() {
                    @Override
                    protected void onSuccess(List<ContractData> data) {
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
    public boolean insertOrUpdateData(@NotNull List<ContractData> data, boolean isLoadMore) {
        return false;
    }
}
