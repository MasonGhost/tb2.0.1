package com.zhiyicx.thinksnsplus.modules.dynamic.top;


import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
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
    public DynamicTopPresenter(DynamicTopContract.Repository repository, DynamicTopContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void stickTop() {
        if (mRootView.insufficientBalance()) {
            mRootView.gotoRecharge();
            return;
        }
        mRepository.stickTop();
    }

    @Override
    public float getBalance() {
        try {
            return (float) mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getMyUserIdWithdefault()).getWallet().getBalance();
        } catch (Exception e) {
            return 0f;
        }
    }
}
