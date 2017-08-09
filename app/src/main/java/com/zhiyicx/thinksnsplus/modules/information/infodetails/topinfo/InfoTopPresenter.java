package com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo;


import android.os.Bundle;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoTopPresenter extends AppBasePresenter<InfoTopContract.Repository, InfoTopContract.View>
        implements InfoTopContract.Presenter {

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    @Inject
    SystemRepository mSystemRepository;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public InfoTopPresenter(InfoTopContract.Repository repository, InfoTopContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void stickTop(long news_id) {
        if (mRootView.getInputMoney() != (int) mRootView.getInputMoney()) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (news_id < 0) {
            return;
        }
        Subscription subscription = mRepository.stickTop(news_id, PayConfig.realCurrencyYuan2Fen(mRootView.getInputMoney()*mRootView.getTopDyas()), mRootView.getTopDyas())
                .doOnSubscribe(() ->
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing))
                )
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_dynamic_success));
                        Bundle bundle = new Bundle();
                        mRootView.topSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public double getBalance() {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(authBean.getUser_id());
            if (walletBean == null) {
                return 0;
            }
            int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
            return PayConfig.realCurrencyFen2Yuan(walletBean.getBalance());
        }
        return 0;
    }
}
