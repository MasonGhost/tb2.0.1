package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SelectDynamicTypePresenter extends AppBasePresenter<SelectDynamicTypeContract.View>
        implements SelectDynamicTypeContract.Presenter{

    @Inject
    UserInfoRepository mCertificationDetailRepository;

    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoDao;

    @Inject
    public SelectDynamicTypePresenter( SelectDynamicTypeContract.View rootView) {
        super( rootView);
    }

    @Override
    public void checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        Observable.zip(mSystemRepository.getBootstrappersInfo(), mCertificationDetailRepository.getCertificationInfo(),
                (systemConfigBean, userCertificationInfo1) -> {
                    Map data = new HashMap();
                    data.put("systemConfigBean", systemConfigBean);
                    data.put("userCertificationInfo", userCertificationInfo1);
                    return data;
                })
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("信息加载中..."))
                .doAfterTerminate(() -> mRootView.dismissSnackBar())
                .subscribe(new BaseSubscribeForV2<Map>() {
                    @Override
                    protected void onSuccess(Map zipData) {
                        UserCertificationInfo data = (UserCertificationInfo) zipData.get("userCertificationInfo");
                        SystemConfigBean systemConfigBean = (SystemConfigBean) zipData.get("systemConfigBean");
                        mSystemRepository.saveComponentStatus(systemConfigBean, mContext);
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
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackSuccessMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
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
