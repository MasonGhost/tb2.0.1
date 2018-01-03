package com.zhiyicx.thinksnsplus.modules.certification.input;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class CertificationInputPresenter extends BasePresenter<CertificationInputContract.View>
        implements CertificationInputContract.Presenter {

    @Inject
    public CertificationInputPresenter(
            CertificationInputContract.View rootView) {
        super(rootView);
    }
}
