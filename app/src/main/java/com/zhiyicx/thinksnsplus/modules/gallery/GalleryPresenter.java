package com.zhiyicx.thinksnsplus.modules.gallery;

import android.os.Bundle;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ICommentRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @Author Jliuer
 * @Date 2017/06/29/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class GalleryPresenter extends BasePresenter<ICommentRepository, GalleryConstract.View> implements GalleryConstract.Presenter {

    @Inject
    CommentRepository mCommentRepository;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    public GalleryPresenter(GalleryConstract.View rootView) {
        super(null, rootView);
    }

    @Override
    public void checkNote(int note) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void payNote(final Long feed_id, final int imagePosition, int note) {

        DynamicDetailBeanV2 dynamicDetail = mDynamicDetailBeanV2GreenDao.getDynamicByFeedId(feed_id);
        double amount = dynamicDetail.getImages().get(imagePosition).getAmount();

        mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(new Func1<UserInfoBean, Observable<BaseJsonV2<String>>>() {
                    @Override
                    public Observable<BaseJsonV2<String>> call(UserInfoBean userInfoBean) {
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        if (userInfoBean.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                            if (userInfoBean.getWallet().getBalance() < amount) {
                                mRootView.goRecharge(WalletActivity.class);
                                return Observable.error(new RuntimeException(""));
                            }
                        }
                        return mCommentRepository.paykNote(note);
                    }
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    return null;
                }, () -> null)
                .flatMap(new Func1<BaseJsonV2<String>, Observable<BaseJsonV2<String>>>() {
                    @Override
                    public Observable<BaseJsonV2<String>> call(BaseJsonV2<String> stringBaseJsonV2) {
                            return Observable.just(stringBaseJsonV2);

                    }
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.hideCenterLoading();
                        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
                        walletBean.setBalance(walletBean.getBalance() - amount);
                        mWalletBeanGreenDao.insertOrReplace(walletBean);
                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mDynamicDetailBeanV2GreenDao.getDynamicByFeedId(feed_id);
                        dynamicDetailBeanV2.getImages().get(imagePosition).setPaid(true);
                        mRootView.getCurrentImageBean().getToll().setPaid(true);
                        mRootView.reLoadImage();
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicDetailBeanV2);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(DYNAMIC_DETAIL_DATA, dynamicDetailBeanV2);
                        bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.hideCenterLoading();
                    }
                });
    }
}
