package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class BindOldAccountPresenter extends BasePresenter<BindOldAccountContract.Repository, BindOldAccountContract.View>
        implements BindOldAccountContract.Presenter{

    @Inject
    public BindOldAccountPresenter(BindOldAccountContract.Repository repository,
                                   BindOldAccountContract.View rootView) {
        super(repository, rootView);
    }
}
