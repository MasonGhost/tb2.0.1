package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

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
public class DynamicCommentTopPresenter extends AppBasePresenter<DynamicCommentTopContract.Repository, DynamicCommentTopContract.View>
        implements DynamicCommentTopContract.Presenter {

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Inject
    public DynamicCommentTopPresenter(DynamicCommentTopContract.Repository repository, DynamicCommentTopContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void topDynamicComment(long feed_id, long comment_id, int amount, int day) {

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

        Subscription subscription = mRepository.stickTop(feed_id, comment_id, amount, day)
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
    public float getBalance() {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(authBean.getUser_id());
            int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
            try {
                return (float) walletBean.getBalance() * (ratio / MONEY_UNIT);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
}
