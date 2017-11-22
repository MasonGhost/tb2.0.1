package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Jliuer
 * @Date 17/11/22 14:49
 * @Email Jliuer@aliyun.com
 * @Description 
 */
public class CircleDetailActivity extends TSActivity<CircleDetailPresenter, CircleDetailFragment> {

    @Override
    protected CircleDetailFragment getFragment() {
        return new CircleDetailFragment();
    }

    @Override
    protected void componentInject() {
        DaggerCircleDetailComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleDetailPresenterModule(new CircleDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
