package com.zhiyicx.thinksnsplus.modules.certification.send;


import android.os.Bundle;

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


import org.simple.eventbus.EventBus;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class SendCertificationPresenter extends BasePresenter<SendCertificationContract.Repository, SendCertificationContract.View>
        implements SendCertificationContract.Presenter{

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public SendCertificationPresenter(SendCertificationContract.Repository repository, SendCertificationContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void sendCertification(SendCertificationBean bean) {
        mRootView.updateSendState(true, false, mContext.getString(R.string.send_certification_ing));
        Subscription subscription = mRepository.sendCertification(bean)
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
                        if (userInfoBean != null){
                            if (userInfoBean.getVerified() != null){
                                userInfoBean.getVerified().setStatus(0);
                            } else {
                                VerifiedBean verifiedBean = new VerifiedBean();
                                verifiedBean.setStatus(0);
                                userInfoBean.setVerified(verifiedBean);
                            }
                        }
                        mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
                        String message = "";
                        if (data.getMessage() != null && data.getMessage().size() > 0){
                            message = data.getMessage().get(0);
                        }
                        mRootView.updateSendState(false, true, message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
