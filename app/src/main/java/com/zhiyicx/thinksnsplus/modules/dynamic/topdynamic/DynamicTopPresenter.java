package com.zhiyicx.thinksnsplus.modules.dynamic.top;


import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import javax.inject.Inject;

import static com.zhiyicx.baseproject.config.PayConfig.MONEY_UNIT;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicTopPresenter extends AppBasePresenter<DynamicTopContract.Repository, DynamicTopContract.View>
        implements DynamicTopContract.Presenter {

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public DynamicTopPresenter(DynamicTopContract.Repository repository, DynamicTopContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void stickTop(long feed_id) {
        if (mRootView.getInputMoney() != (int) mRootView.getInputMoney()) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (feed_id < 0) {
            return;
        }
        mRepository.stickTop(feed_id, (int) mRootView.getInputMoney(), mRootView.getTopDyas())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
    }

    @Override
    public float getBalance() {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(authBean.getUser_id());
            int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
            return (float) walletBean.getBalance() * (ratio / MONEY_UNIT);
        }
        return 0;
    }
}
