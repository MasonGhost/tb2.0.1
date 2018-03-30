package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Jliuer
 * @Date 2017/11/21/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerPresenter extends AppBasePresenter< AllCircleContainerContract.View>
        implements AllCircleContainerContract.Presenter {

    @Inject
    protected CircleTypeBeanGreenDaoImpl mCircleTypeBeanGreenDao;
    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoDao;
    @Inject
    UserInfoRepository mCertificationDetailRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    BaseCircleRepository mBaseCircleRepository;


    @Inject
    public AllCircleContainerPresenter(AllCircleContainerContract.View rootView) {
        super( rootView);
    }

    @Override
    public void getCategroiesList(int limit, int offet) {
        Subscription subscription = mBaseCircleRepository.getCategroiesList(limit, offet)
                .subscribe(new BaseSubscribeForV2<List<CircleTypeBean>>() {

                    @Override
                    protected void onSuccess(List<CircleTypeBean> data) {
                        // 创建默认推荐数据，服务器不反会，测试又要要，让我们自己加
                        CircleTypeBean circleTypeBean=new CircleTypeBean();
                        circleTypeBean.setId(Long.valueOf(InfoContainerFragment.RECOMMEND_INFO));
                        circleTypeBean.setName(mContext.getString(R.string.info_recommend));
                        data.add(circleTypeBean);
                        mRootView.setCategroiesList(data);
                        mCircleTypeBeanGreenDao.saveMultiData(data);
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
        addSubscrebe(subscription);
    }

    @Override
    public List<CircleTypeBean> getCircleTypesFormLocal() {
        return mCircleTypeBeanGreenDao.getMultiDataFromCache();
    }

    /**
     * 检查认证状态信息
     */
    @Override
    public void checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        UserCertificationInfo userCertificationInfo = mUserCertificationInfoDao.getInfoByUserId();
        if (getSystemConfigBean() != null && getSystemConfigBean().getCircleGroup() != null && userCertificationInfo != null &&
                userCertificationInfo.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value) {
            mRootView.setUserCertificationInfo(userCertificationInfo);
            return;
        }

        Subscription subscribe = Observable.zip(mSystemRepository.getBootstrappersInfo(), mCertificationDetailRepository.getCertificationInfo(),
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
        addSubscrebe(subscribe);
    }
}
