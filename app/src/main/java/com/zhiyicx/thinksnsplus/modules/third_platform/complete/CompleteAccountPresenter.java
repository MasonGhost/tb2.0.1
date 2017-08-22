package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class CompleteAccountPresenter extends BasePresenter<CompleteAccountContract.Repository, CompleteAccountContract.View>
        implements CompleteAccountContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    AuthRepository mAuthRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public CompleteAccountPresenter(CompleteAccountContract.Repository repository,
                                    CompleteAccountContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void checkName(ThridInfoBean thridInfoBean, String name) {
        checkOrRegister(thridInfoBean, name,true);
    }

    @Override
    public void thridRegister(ThridInfoBean thridInfoBean, String name) {
        checkOrRegister(thridInfoBean, name,false);
    }

    private void checkOrRegister(final ThridInfoBean thridInfoBean, final String name,boolean isCheck) {
        Subscription subscribe = mUserInfoRepository.checkUserOrRegisterUser(thridInfoBean.getProvider(), thridInfoBean.getAccess_token(),name, isCheck)
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        if(isCheck) {
                            mRootView.checkNameSuccess(thridInfoBean, name);
                        }else { // register success
//                            UserInfoBean registerUserInfo = new UserInfoBean();
//                            registerUserInfo.setUser_id(Long.valueOf(data.getUser_id()));
//                            registerUserInfo.setName(name);
//                            registerUserInfo.setPhone(phone);
//                            data.setUser(registerUserInfo);
                            mAuthRepository.saveAuthBean(data);// 保存登录认证信息
                            mUserInfoBeanGreenDao.insertOrReplace(data.getUser());
                            // IM 登录 需要 token ,所以需要先保存登录信息
                            handleIMLogin();
                            mRootView.registerSuccess();
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 登录失败
                        mRootView.showErrorTips(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe);
    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
    }
}
