package com.zhiyicx.thinksnsplus.modules.third_platform.complete;

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
public class CompleteAccountPresenter extends BasePresenter<CompleteAccountContract.Repository, CompleteAccountContract.View>
        implements CompleteAccountContract.Presenter{

    @Inject
    public CompleteAccountPresenter(CompleteAccountContract.Repository repository,
                                    CompleteAccountContract.View rootView) {
        super(repository, rootView);
    }
}
