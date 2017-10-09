package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CertificationDetailRepository;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoContainerPresenter extends AppBasePresenter<InfoMainContract.Repository
        , InfoMainContract.InfoContainerView> implements InfoMainContract.InfoContainerPresenter {

    @Inject
    InfoTypeBeanGreenDaoImpl mInfoTypeBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    CertificationDetailRepository mCertificationDetailRepository;

    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoDao;

    @Inject
    public InfoContainerPresenter(InfoMainContract.Repository repository,
                                  InfoMainContract.InfoContainerView rootContainerView) {
        super(repository, rootContainerView);
    }

    @Override
    public void getInfoType() {
        Subscription subscription = Observable.just(mInfoTypeBeanGreenDao)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(infoTypeBeanGreenDao -> infoTypeBeanGreenDao.getSingleDataFromCache(1L))
                .flatMap(new Func1<InfoTypeBean, Observable<InfoTypeBean>>() {
                    @Override
                    public Observable<InfoTypeBean> call(InfoTypeBean infoTypeBean) {
                        if (infoTypeBean == null) {
                            return mRepository.getInfoType();
                        }
                        return Observable.just(infoTypeBean);
                    }
                })
                .subscribe(new BaseSubscribeForV2<InfoTypeBean>() {
                    @Override
                    protected void onSuccess(InfoTypeBean data) {
                        for (InfoTypeCatesBean myCates : data.getMy_cates()) {
                            myCates.setIsMyCate(true);
                        }
                        mInfoTypeBeanGreenDao.updateSingleData(data);
                        mRootView.setInfoType(data);
                    }
                });
        addSubscrebe(subscription);

        mRepository.getInfoType().subscribe(data -> {
            for (InfoTypeCatesBean myCates : data.getMy_cates()) {
                myCates.setIsMyCate(true);
            }
            mInfoTypeBeanGreenDao.updateSingleData(data);
            mRootView.setInfoType(data);
        });
    }

    @Override
    public void checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        UserCertificationInfo userCertificationInfo = mUserCertificationInfoDao.getInfoByUserId();

        if (userCertificationInfo != null && userCertificationInfo.getStatus() == 1) {
            mRootView.setUserCertificationInfo(userCertificationInfo);
        } else{
            mCertificationDetailRepository.getCertificationInfo()
                    .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("信息加载中..."))
                    .subscribe(new BaseSubscribeForV2<UserCertificationInfo>() {
                        @Override
                        protected void onSuccess(UserCertificationInfo data) {
                            mUserCertificationInfoDao.saveSingleData(data);
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
                            mRootView.setUserCertificationInfo(data);
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            mRootView.dismissSnackBar();
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
    }

    @Override
    public boolean isNeedPayTip() {
        // 用用户ID加上key来取出值
        return SharePreferenceUtils.getBoolean(mContext,
                String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id())
                        + SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_SEND_INFO,
                true);
    }

    @Override
    public void savePayTip(boolean isNeed) {
        SharePreferenceUtils.saveBoolean(mContext,
                String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id())
                        + SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_SEND_INFO,
                false);
    }

}
