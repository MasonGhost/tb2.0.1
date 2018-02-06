package com.zhiyicx.thinksnsplus.modules.wallet.sticktop;


import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.StickTopRepsotory;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_DYNAMIC;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_INFO;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickTopPresenter extends AppBasePresenter<StickTopContract.View>
        implements StickTopContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    StickTopRepsotory mStickTopRepsotory;


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public StickTopPresenter(StickTopContract.View rootView) {
        super(rootView);
    }

    /**
     * 内容置顶
     *
     * @param parentId
     */
    @Override
    public void stickTop(long parentId) {
        if (mRootView.getInputMoney() <= 0) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (parentId < 0) {
            return;
        }

        double amount = mRootView.getInputMoney() * mRootView.getTopDyas();

        Subscription subscription = mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.apply_doing)))
                .flatMap(userInfoBean -> {
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                    if (userInfoBean.getCurrency() != null) {
                        if (userInfoBean.getCurrency().getSum() < amount) {
                            mRootView.goRecharge(MineIntegrationActivity.class);
                            return Observable.error(new RuntimeException(""));
                        }
                    }
                    return mStickTopRepsotory.stickTop(mRootView.getType(), parentId, amount, mRootView.getTopDyas());
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    return null;
                }, () -> null)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        switch (mRootView.getType()) {
                            case TYPE_DYNAMIC:
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_dynamic_success));
                                break;
                            case TYPE_INFO:
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_info_success));
                                break;
                            default:
                                mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_success));

                        }

                        mRootView.topSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                        mRootView.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });

        addSubscrebe(subscription);
    }

    /**
     * 内容所属的评论置顶
     *
     * @param parentId
     * @param childId
     */
    @Override
    public void stickTop(long parentId, long childId) {
        if (mRootView.getInputMoney() != (int) mRootView.getInputMoney()) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        if (parentId < 0) {
            return;
        }
        Subscription subscription = mStickTopRepsotory.stickTop(mRootView.getType(), parentId, childId,
                mRootView.getInputMoney() * mRootView.getTopDyas(),
                mRootView.getTopDyas())
                .doOnSubscribe(() ->
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing))
                )
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_top_success));
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

        Subscription userInfoSub = mUserInfoRepository.getCurrentLoginUserInfo()
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        mRootView.updateBalance(data.getCurrency() != null ? data.getCurrency().getSum() : 0);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackWarningMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(userInfoSub);

        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (authBean != null) {
            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(authBean.getUser_id());
            if (userInfoBean == null || userInfoBean.getCurrency() == null) {
                return 0;
            }
            return userInfoBean.getCurrency().getSum();
        }
        return 0;
    }
}
