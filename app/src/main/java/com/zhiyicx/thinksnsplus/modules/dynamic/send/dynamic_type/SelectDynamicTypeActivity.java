package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeFragment.SEND_OPTION;

public class SelectDynamicTypeActivity extends TSActivity<SelectDynamicTypePresenter, SelectDynamicTypeFragment> {

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected SelectDynamicTypeFragment getFragment() {
        return SelectDynamicTypeFragment.getInstance(getIntent().getBundleExtra(SEND_OPTION));
    }

    @Override
    protected void componentInject() {
        DaggerSelectDynamicTypeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .selectDynamicTypePresenterModule(new SelectDynamicTypePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

}
