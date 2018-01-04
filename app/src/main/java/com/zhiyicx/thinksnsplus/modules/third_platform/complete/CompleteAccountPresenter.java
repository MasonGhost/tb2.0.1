package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
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
public class CompleteAccountPresenter extends AppBasePresenter<CompleteAccountContract.View>
        implements CompleteAccountContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    AuthRepository mAuthRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public CompleteAccountPresenter(
            CompleteAccountContract.View rootView) {
        super(rootView);
    }

    @Override
    public void checkName(ThridInfoBean thridInfoBean, String name) {
        if (checkUsername(name)) {
            return;
        }
        check(thridInfoBean, name);
    }

    @Override
    public void thridRegister(ThridInfoBean thridInfoBean, String name) {
        if (checkUsername(name)) {
            return;
        }
        register(thridInfoBean, name, true);
    }

    private void check(final ThridInfoBean thridInfoBean, final String name) {
        Subscription subscribe = mUserInfoRepository.checkUserOrRegisterUser(thridInfoBean.getProvider(), thridInfoBean.getAccess_token(), name, true)
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        mRootView.checkNameSuccess(thridInfoBean, name);
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

    private void register(final ThridInfoBean thridInfoBean, final String name, boolean isCheck) {
        Subscription subscribe = mUserInfoRepository.checkUserOrRegisterUser(thridInfoBean.getProvider(), thridInfoBean.getAccess_token(), name,
                isCheck)
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        if (isCheck) {
                            register(thridInfoBean, name, false);
                        } else { // register success
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

    /**
     * 检查用户名是否小于最小长度,不能以数字开头
     *
     * @param name
     * @return
     */
    private boolean checkUsername(String name) {
        if (!RegexUtils.isUsernameLength(name, mContext.getResources().getInteger(R.integer.username_min_length), mContext.getResources()
                .getInteger(R.integer.username_max_length))) {
            mRootView.showErrorTips(mContext.getString(R.string.username_toast_hint));
            return true;
        }
        if (RegexUtils.isUsernameNoNumberStart(name)) {// 数字开头
            mRootView.showErrorTips(mContext.getString(R.string.username_toast_not_number_start_hint));
            return true;
        }
        if (!RegexUtils.isUsername(name)) {// 用户名只能包含数字、字母和下划线
            mRootView.showErrorTips(mContext.getString(R.string.username_toast_not_symbol_hint));
            return true;
        }
        return false;
    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig
                .GET_IM_INFO));
    }
}
