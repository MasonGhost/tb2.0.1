package com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.baseproject.base.SystemConfigBean;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class ChooseBindPresenter extends AppBasePresenter<ChooseBindContract.Repository, ChooseBindContract.View>
        implements ChooseBindContract.Presenter{

    @Inject
    public ChooseBindPresenter(ChooseBindContract.Repository repository, ChooseBindContract.View rootView) {
        super(repository, rootView);
    }
}
