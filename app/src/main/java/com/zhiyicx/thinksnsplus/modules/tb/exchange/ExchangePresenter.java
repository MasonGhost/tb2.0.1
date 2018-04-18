package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

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
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RechargeSuccessBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getUserInfo() {

    }
}
