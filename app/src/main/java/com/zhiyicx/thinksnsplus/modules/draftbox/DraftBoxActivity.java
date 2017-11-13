package com.zhiyicx.thinksnsplus.modules.draftbox;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DraftBoxActivity extends TSActivity<DraftBoxPresenter, DraftBoxFragment> {

    @Override
    protected DraftBoxFragment getFragment() {
        return DraftBoxFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerDraftBoxComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .draftBoxPresenterModule(new DraftBoxPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mContanierFragment.updateDate();
    }
}
