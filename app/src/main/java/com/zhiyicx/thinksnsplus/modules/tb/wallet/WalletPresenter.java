package com.zhiyicx.thinksnsplus.modules.tb.wallet;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2018/02/28/19:22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WalletPresenter extends AppBasePresenter<WalletContract.View> implements WalletContract.Presenter {
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public WalletPresenter(WalletContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<WalletData> data, boolean isLoadMore) {
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
