package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe 扫码的presenter
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class ScanCodePresenter extends AppBasePresenter< ScanCodeContract.View>
        implements ScanCodeContract.Presenter{

    @Inject
    public ScanCodePresenter(ScanCodeContract.View rootView) {
        super( rootView);
    }


}
