package com.zhiyicx.thinksnsplus.modules.live.mine;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsPresenter;

public class MineLiveActivity extends TSActivity<SettingsPresenter, MineLiveFragment> {

    @Override
    protected void componentInject() {
    }

    @Override
    protected MineLiveFragment getFragment() {
        return MineLiveFragment.newInstance(getIntent().getExtras());
    }
}
