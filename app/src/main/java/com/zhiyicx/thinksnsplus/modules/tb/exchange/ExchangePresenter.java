package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.tbcandy.CandyBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskContainerBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public class ExchangePresenter extends AppBasePresenter<ExchangeContract.View> implements ExchangeContract.Presenter{

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public ExchangePresenter(ExchangeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getCandy() {
        Subscription subscribe = mUserInfoRepository.getCandy(mRootView.getCurrentCandy().getId())
                .subscribe(new BaseSubscribeForV2<CandyBean>() {
                    @Override
                    protected void onSuccess(CandyBean data) {
                        mRootView.getCandySuccess(data);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void orderCandy(int tbmark, int candy_id) {
        Subscription subscribe = mUserInfoRepository.orderCandy(tbmark, candy_id)
                .subscribe(new BaseSubscribeForV2<CandyBean>() {
                    @Override
                    protected void onSuccess(CandyBean data) {
                        //mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        //mRootView.onResponseError(throwable);
                    }
                });
        addSubscrebe(subscribe);
    }
}
