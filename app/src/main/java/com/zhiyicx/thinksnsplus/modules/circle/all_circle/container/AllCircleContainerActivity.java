package com.zhiyicx.thinksnsplus.modules.circle.all_circle.container;

import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Jliuer
 * @Date 2017/11/21/15:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerActivity extends TSActivity<AllCircleContainerPresenter, AllCircleContainerFragment> {

    @Override
    protected AllCircleContainerFragment getFragment() {
        return new AllCircleContainerFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
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
