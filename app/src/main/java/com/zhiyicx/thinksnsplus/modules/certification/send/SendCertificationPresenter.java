package com.zhiyicx.thinksnsplus.modules.certification.send;


import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;


import org.simple.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SendCertificationPresenter extends BasePresenter<SendCertificationContract.View>
        implements SendCertificationContract.Presenter {

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public SendCertificationPresenter(SendCertificationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void sendCertification(SendCertificationBean bean) {
        mRootView.updateSendState(true, false, mContext.getString(R.string.send_certification_ing));
        checkUpdateData(bean);
        Subscription subscription = mUserInfoRepository.sendCertification(bean)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.updateSendState(false, false, message);
                    }


                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        // 组合数据 通知我的页面刷新
                        UserCertificationInfo info = new UserCertificationInfo();
                        info.setId(new Date().getTime());
                        info.setStatus(0);
                        info.setCertification_name(bean.getType());
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EventBusTagConfig.EVENT_SEND_CERTIFICATION_SUCCESS, info);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_SEND_CERTIFICATION_SUCCESS);
                        // 更新状态
                        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache
                                (AppApplication.getmCurrentLoginAuth().getUser_id());
                        if (userInfoBean != null) {
                            if (userInfoBean.getVerified() != null) {
                                userInfoBean.getVerified().setStatus(0);
                            } else {
                                VerifiedBean verifiedBean = new VerifiedBean();
                                verifiedBean.setStatus(0);
                                userInfoBean.setVerified(verifiedBean);
                            }
                        }
                        mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
                        String message = "";
                        if (data.getMessage() != null && data.getMessage().size() > 0) {
                            message = data.getMessage().get(0);
                        }
                        mRootView.updateSendState(false, true, message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public SendCertificationBean checkUpdateData(SendCertificationBean bean) {
        UserCertificationInfo userCertificationInfo = mUserCertificationInfoGreenDao.getInfoByUserId();
        if (userCertificationInfo != null) {
            bean.setUpdate(true);
            // 公司信息 如果新的申请是企业 才去验证是否修改
            if (bean.getType().equals(SendCertificationBean.ORG)) {
                if (!TextUtils.isEmpty(userCertificationInfo.getData().getOrg_name())
                        && userCertificationInfo.getData().getOrg_name().equals(bean.getOrg_name())) {
                    bean.setOrg_name("");
                }
                if (!TextUtils.isEmpty(userCertificationInfo.getData().getOrg_address())
                        && userCertificationInfo.getData().getOrg_address().equals(bean.getOrg_address())) {
                    bean.setOrg_address("");
                }
            }
            // 类型是否改变
            if (userCertificationInfo.getCertification_name().equals(bean.getType())) {
                bean.setType("");
            }
            // 描述是否改变
            if (userCertificationInfo.getData().getDesc().equals(bean.getDesc())) {
                bean.setDesc("");
            }
            // 用户名是否改变
            if (userCertificationInfo.getData().getName().equals(bean.getName())) {
                bean.setName("");
            }
            // 身份在是否改变
            if (userCertificationInfo.getData().getNumber().equals(bean.getNumber())) {
                bean.setNumber("");
            }
            // 电话是否改变
            if (userCertificationInfo.getData().getPhone().equals(bean.getPhone())) {
                bean.setPhone("");
            }
        } else {
            bean.setUpdate(false);
        }
        return bean;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
