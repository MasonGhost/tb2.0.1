package com.zhiyicx.thinksnsplus.modules.certification.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class CertificationDetailPresenter extends BasePresenter<CertificationDetailContract.Repository, CertificationDetailContract.View>
        implements CertificationDetailContract.Presenter{

    @Inject
    public CertificationDetailPresenter(CertificationDetailContract.Repository repository,
                                        CertificationDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void getCertificationInfo() {
        Subscription subscription = mRepository.getCertificationInfo()
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<UserCertificationInfo>() {

                    @Override
                    protected void onSuccess(UserCertificationInfo data) {
                        mRootView.setCertificationInfo(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }
                });
        addSubscrebe(subscription);
    }
}
