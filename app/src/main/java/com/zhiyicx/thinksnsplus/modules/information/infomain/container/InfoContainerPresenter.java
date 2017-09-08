package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    public boolean checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
        if (userInfoBean != null && userInfoBean.getVerified() != null) {
            if (userInfoBean.getVerified().getType() == null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
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
