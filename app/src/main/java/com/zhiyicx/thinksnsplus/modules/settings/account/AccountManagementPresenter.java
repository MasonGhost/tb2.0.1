package com.zhiyicx.thinksnsplus.modules.settings.account;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity.BUNDLE_THIRD_INFO;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountManagementPresenter extends BasePresenter<AccountManagementContract.View>
        implements AccountManagementContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    public AccountManagementPresenter(
            AccountManagementContract.View rootView) {
        super(rootView);
    }

    /**
     *
     */
    @Override
    public void getBindSocialAcounts() {
        Subscription subscribe = mUserInfoRepository.getBindThirds()
                .subscribe(new BaseSubscribeForV2<List<String>>() {
                    @Override
                    protected void onSuccess(List<String> data) {
                        mRootView.updateBindStatus(data);

                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe);
    }

    private void updateUserinfo() {
        Subscription subscribe2 = mUserInfoRepository.getCurrentLoginUserInfo()
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mRootView.updateUserinfo(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe2);
    }

    /**
     * @param provider    type
     * @param accessToken accesse token
     * @param isBind      true to bind ,false to unbind
     */
    @Override
    public void bindOrUnbindThirdAccount(String provider, String accessToken, boolean isBind) {
        if (isBind) { // 绑定
            Subscription subscribe = mUserInfoRepository.bindWithLogin(provider, accessToken)
                    .subscribe(new BaseSubscribeForV2<Object>() {
                        @Override
                        protected void onSuccess(Object data) {

                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.bind_success));

                            mRootView.bindThirdSuccess(provider);

                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showSnackErrorMessage(message);
                            SHARE_MEDIA share_media;
                            switch (provider) {
                                case ApiConfig.PROVIDER_QQ:
                                    share_media = SHARE_MEDIA.QQ;
                                    break;
                                case ApiConfig.PROVIDER_WEIBO:
                                    share_media = SHARE_MEDIA.SINA;

                                    break;
                                case ApiConfig.PROVIDER_WECHAT:
                                    share_media = SHARE_MEDIA.WEIXIN;

                                    break;
                                default:
                                    share_media = SHARE_MEDIA.QQ;

                            }
                            mAuthRepository.clearThridAuth(share_media);

                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));

                        }
                    });
            addSubscrebe(subscribe);

        } else {
            mUserInfoRepository.cancelBind(provider)
                    .subscribe(new BaseSubscribeForV2<Object>() {
                        @Override
                        protected void onSuccess(Object data) {
                            SHARE_MEDIA share_media;
                            switch (provider) {
                                case ApiConfig.PROVIDER_QQ:
                                    share_media = SHARE_MEDIA.QQ;
                                    break;
                                case ApiConfig.PROVIDER_WEIBO:
                                    share_media = SHARE_MEDIA.SINA;

                                    break;
                                case ApiConfig.PROVIDER_WECHAT:
                                    share_media = SHARE_MEDIA.WEIXIN;

                                    break;
                                default:
                                    share_media = SHARE_MEDIA.QQ;

                            }
                            mAuthRepository.clearThridAuth(share_media);
                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.unbind_success));

                            mRootView.unBindThirdSuccess(provider);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showSnackErrorMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));

                        }
                    });

        }


    }

    @Override
    public void updaeUserInfo() {
        updateUserinfo();
    }
}
