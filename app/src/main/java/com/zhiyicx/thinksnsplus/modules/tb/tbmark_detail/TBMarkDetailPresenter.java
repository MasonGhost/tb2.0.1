package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

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

public class TBMarkDetailPresenter extends AppBasePresenter<TBMarkDetailContract.View> implements TBMarkDetailContract.Presenter{

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public TBMarkDetailPresenter(TBMarkDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mUserInfoRepository.getBillList(maxId.intValue(), mRootView.getBillType())
                .subscribe(new BaseSubscribeForV2<List<RechargeSuccessBean>>() {
                    @Override
                    protected void onSuccess(List<RechargeSuccessBean> data) {
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
    public boolean insertOrUpdateData(@NotNull List<RechargeSuccessBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getUserInfo() {
        Subscription subscribe = mUserInfoRepository.getLocalUserInfoBeforeNet(AppApplication.getMyUserIdWithdefault())
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        if (data.getWallet() == null) {
                            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getMyUserIdWithdefault());
                            if (walletBean != null) {
                                data.setWallet(walletBean);
                            }
                        }
                        mRootView.updateUserInfo(data);
                    }
                });
        addSubscrebe(subscribe);
    }
}
