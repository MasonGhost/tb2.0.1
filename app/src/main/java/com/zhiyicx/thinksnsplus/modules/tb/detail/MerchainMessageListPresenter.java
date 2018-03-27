package com.zhiyicx.thinksnsplus.modules.tb.detail;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage.MerchianMassageBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/23
 * @Contact master.jungle68@gmail.com
 */
public class MerchainMessageListPresenter extends AppBasePresenter<MerchainMessageListContract.View> implements MerchainMessageListContract
        .Presenter {

    private int feedAfter;
    private int newsAfter;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MerchainMessageListPresenter(MerchainMessageListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getMerchianMessages((long) TSListFragment.DEFAULT_PAGE_SIZE, mRootView.getOriginId(),
                feedAfter, newsAfter)
                .map(merchianMassageBean -> {
                    feedAfter = merchianMassageBean.getFeedMin();
                    newsAfter = merchianMassageBean.getNewsMin();
                    Collections.sort(merchianMassageBean.getData(), (o1, o2) -> o1.getCreated_at().compareTo(o2.getCreated_at()));
                    List<MerchianMassageBean.DataBean> tmps = new ArrayList<>();
                    tmps.addAll(merchianMassageBean.getData());
                    tmps.addAll(mRootView.getListDatas());
                    return tmps;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<MerchianMassageBean.DataBean>>() {
                    @Override
                    protected void onSuccess(List<MerchianMassageBean.DataBean> data) {
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
    public boolean insertOrUpdateData(@NotNull List<MerchianMassageBean.DataBean> data, boolean isLoadMore) {
        return false;
    }
}
