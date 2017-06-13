package com.zhiyicx.thinksnsplus.modules.dynamic.top;


import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import javax.inject.Inject;

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
    public DynamicTopPresenter(DynamicTopContract.Repository repository, DynamicTopContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void stickTop(long feed_id) {
        if (mRootView.getInputMoneyStr().contains(".")) {
            mRootView.initStickTopInstructionsPop();
            return;
        }
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        mRepository.stickTop(feed_id).subscribe(new BaseSubscribeForV2<BaseJson<Integer>>() {
            @Override
            protected void onSuccess(BaseJson<Integer> data) {

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
        try {
            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getMyUserIdWithdefault());
            return (float) userInfoBean.getWallet().getBalance();
        } catch (Exception e) {
            return 0f;
        }
    }
}
