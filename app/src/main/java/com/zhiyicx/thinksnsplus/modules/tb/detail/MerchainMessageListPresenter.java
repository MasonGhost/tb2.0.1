package com.zhiyicx.thinksnsplus.modules.tb.detail;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage.MerchianMassageBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/23
 * @Contact master.jungle68@gmail.com
 */
public class MerchainMessageListPresenter extends AppBasePresenter<MerchainMessageListContract.View> implements MerchainMessageListContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MerchainMessageListPresenter(MerchainMessageListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getContributionRank((long)TSListFragment.DEFAULT_PAGE_SIZE,maxId.intValue(), "")
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
    public boolean insertOrUpdateData(@NotNull List< MerchianMassageBean.DataBean> data, boolean isLoadMore) {
        return false;
    }
}
