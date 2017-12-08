package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyCodePresenter extends AppBasePresenter<MyCodeContract.Repository, MyCodeContract.View>
        implements MyCodeContract.Presenter{

    @Inject
    public MyCodePresenter(MyCodeContract.Repository repository, MyCodeContract.View rootView) {
        super(repository, rootView);
    }
}
