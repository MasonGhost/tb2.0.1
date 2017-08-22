package com.zhiyicx.thinksnsplus.modules.settings.account;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountManagementPresenter extends BasePresenter<AccountManagementContract.Repository, AccountManagementContract.View>
        implements AccountManagementContract.Presenter{

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    public AccountManagementPresenter(AccountManagementContract.Repository repository,
                                      AccountManagementContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getBindSocialAcounts() {
        Subscription subscribe = mUserInfoRepository.getBindThirds()
                .subscribe(new BaseSubscribeForV2<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> data) {
                        mRootView.updateBindStatus(data,mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));

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
        addSubscrebe(subscribe);
    }
}
