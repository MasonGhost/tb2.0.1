package com.zhiyicx.thinksnsplus.modules.tb.rank;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RankListPresenter extends AppBasePresenter<RankListContract.View> implements RankListContract.Presenter {
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public RankListPresenter(RankListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getTBRank(maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribe(new BaseSubscribeForV2<List<RankData>>() {
                    @Override
                    protected void onSuccess(List<RankData> data) {
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
    public boolean insertOrUpdateData(@NotNull List<RankData> data, boolean isLoadMore) {
        return false;
    }
}
