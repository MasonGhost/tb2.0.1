package com.zhiyicx.thinksnsplus.modules.gallery;

import android.os.Bundle;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
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
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_UPDATE_TOLL;

/**
 * @Author Jliuer
 * @Date 2017/06/29/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class GalleryPresenter extends AppBasePresenter<GalleryConstract.View> implements GalleryConstract.Presenter {


    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    @Inject
    public GalleryPresenter(GalleryConstract.View rootView) {
        super(rootView);
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

        handleIntegrationBlance((long) amount)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(o -> mCommentRepository.paykNote(note))
                .flatMap(stringBaseJsonV2 -> Observable.just(stringBaseJsonV2))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.hideCenterLoading();
                        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getmCurrentLoginAuth().getUser_id
                                ());
                        walletBean.setBalance(walletBean.getBalance() - amount);
                        mWalletBeanGreenDao.insertOrReplace(walletBean);
                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mDynamicDetailBeanV2GreenDao.getDynamicByFeedId(feed_id);
                        dynamicDetailBeanV2.getImages().get(imagePosition).setPaid(true);
                        mRootView.getCurrentImageBean().getToll().setPaid(true);
                        mRootView.reLoadImage();
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicDetailBeanV2);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(DYNAMIC_UPDATE_TOLL, true);
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
                        if (isIntegrationBalanceCheck(throwable)) {
                            return;
                        }
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
