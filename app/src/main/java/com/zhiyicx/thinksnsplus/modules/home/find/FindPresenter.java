package com.zhiyicx.thinksnsplus.modules.home.find;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Administrator on 2018/4/18.
 */
@FragmentScoped
public class FindPresenter extends AppBasePresenter<FindContract.View> implements FindContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public FindPresenter(FindContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getCandyList()
                .subscribe(new BaseSubscribeForV2<List<CandyBean>>() {
                    @Override
                    protected void onSuccess(List<CandyBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
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
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CandyBean> data, boolean isLoadMore) {
        return false;
    }
}
