package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerActivity extends TSActivity<AllCircleContainerPresenter, AllCircleContainerFragment> {

    @Override
    protected AllCircleContainerFragment getFragment() {
        return null;
    }

    @Override
    protected void componentInject() {
        DaggerAllCircleContainerComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .allCircleContainerPresenterModule(new AllCircleContainerPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
