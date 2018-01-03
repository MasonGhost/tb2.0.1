package com.zhiyicx.thinksnsplus.modules.circle.create.types;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.container.AllCircleContainerPresenter;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.container.AllCircleContainerPresenterModule;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.container.DaggerAllCircleContainerComponent;

/**
 * @author Jliuer
 * @Date 2017/11/28/14:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleTyepsActivity extends TSActivity<AllCircleContainerPresenter, CircleTypesFragment> {
    @Override
    protected CircleTypesFragment getFragment() {
        return CircleTypesFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerAllCircleContainerComponent
                .builder()
                .allCircleContainerPresenterModule(new AllCircleContainerPresenterModule(mContanierFragment))
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .build().inject(this);
    }
}
