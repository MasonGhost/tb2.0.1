package com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic.DaggerDynamicTopComponent;

import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic.DynamicTopFragment.FEEDID;

public class InfoTopActivity extends TSActivity<InfoTopPresenter, InfoTopFragment> {

    @Override
    protected InfoTopFragment getFragment() {
        return InfoTopFragment.newInstance(getIntent().getLongExtra(FEEDID, -1L));
    }

    @Override
    protected void componentInject() {
        DaggerInfoTopComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoTopPresenterModule(new InfoTopPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
