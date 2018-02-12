package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic;


import android.os.Bundle;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;

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
public class DynamicTopPresenter extends AppBasePresenter<DynamicTopContract.View>
        implements DynamicTopContract.Presenter {

    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public DynamicTopPresenter(DynamicTopContract.View rootView) {
        super(rootView);
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
        Subscription subscription = mBaseDynamicRepository.stickTop(feed_id, mRootView.getInputMoney() * mRootView
                .getTopDyas(), mRootView.getTopDyas())
                .doOnSubscribe(() ->
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing))
                )
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Integer>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Integer> data) {
                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mDynamicDetailBeanV2GreenDao.getDynamicByFeedId(feed_id);
                        dynamicDetailBeanV2.setTop(DynamicDetailBeanV2.TOP_REVIEW);
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicDetailBeanV2);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.dynamic_list_top_dynamic_success));
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(DYNAMIC_DETAIL_DATA, dynamicDetailBeanV2);
                        bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
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

    /**
     * @return 积分余额
     */
    @Override
    public long getBalance() {
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
