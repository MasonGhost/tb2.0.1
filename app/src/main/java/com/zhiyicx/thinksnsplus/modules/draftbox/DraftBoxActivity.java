package com.zhiyicx.thinksnsplus.modules.draftbox;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class DraftBoxActivity extends TSActivity<DraftBoxPresenter, DraftContainerFragment> {

    @Override
    protected DraftContainerFragment getFragment() {
        return new DraftContainerFragment();
    }

    @Override
    protected void componentInject() {

    }
}
