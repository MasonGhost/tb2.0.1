package com.zhiyicx.thinksnsplus.modules.information.infomain;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.DaggerInfoContainerComponent;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerPresenter;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerPresenterModule;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯
 */
public class InfoActivity extends TSActivity<InfoContainerPresenter, InfoContainerFragment> {

    @Override
    protected InfoContainerFragment getFragment() {
        if (mContanierFragment == null) {
            mContanierFragment = new InfoContainerFragment();
        }
        return mContanierFragment;
    }

    @Override
    protected void componentInject() {
        DaggerInfoContainerComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoContainerPresenterModule(
                        new InfoContainerPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        mContanierFragment = getFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mContanierFragment,
                R.id.fl_fragment_container);
    }

}
