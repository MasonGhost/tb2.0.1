package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.baseproject.config.PayConfig.MONEY_UNIT;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTopPresenter extends AppBasePresenter< DynamicCommentTopContract.View>
        implements DynamicCommentTopContract.Presenter {

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Inject
    public DynamicCommentTopPresenter(DynamicCommentTopContract.View rootView) {
        super( rootView);
    }

    @Override
    public void topDynamicComment(long feed_id, long comment_id, double amount, int day) {

        if (mRootView.getInputMoney() != (int) mRootView.getInputMoney()) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (feed_id * comment_id < 0) {
            return;
        }
        
        Subscription subscription = mBaseDynamicRepository.commentStickTop(feed_id, comment_id, PayConfig.gameCurrency2RealCurrency(amount*day,getRatio()), day)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
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
            int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
            try {
                return PayConfig.realCurrencyFen2Yuan(walletBean.getBalance());
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
}
