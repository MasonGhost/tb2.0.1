package com.zhiyicx.thinksnsplus.modules.certification.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

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
}
