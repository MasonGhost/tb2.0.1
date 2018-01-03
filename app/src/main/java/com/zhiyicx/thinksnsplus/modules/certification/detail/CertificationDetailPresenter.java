package com.zhiyicx.thinksnsplus.modules.certification.detail;

import android.os.Bundle;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class CertificationDetailPresenter extends BasePresenter<CertificationDetailContract.View>
        implements CertificationDetailContract.Presenter {

    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public CertificationDetailPresenter(
            CertificationDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getCertificationInfo() {
        Subscription subscription = mUserInfoRepository.getCertificationInfo()
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<UserCertificationInfo>() {

                    @Override
                    protected void onSuccess(UserCertificationInfo data) {
                        mRootView.setCertificationInfo(data);
                        mUserCertificationInfoGreenDao.insertOrReplace(data);
                        // 更新状态
                        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache
                                (AppApplication.getmCurrentLoginAuth().getUser_id());
                        if (userInfoBean != null) {
                            if (userInfoBean.getVerified() != null) {
                                userInfoBean.getVerified().setStatus((int) data.getStatus());
                            } else {
                                VerifiedBean verifiedBean = new VerifiedBean();
                                verifiedBean.setStatus((int) data.getStatus());
                                userInfoBean.setVerified(verifiedBean);
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS, data);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS);
                        mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }
                });
        addSubscrebe(subscription);
    }
}
