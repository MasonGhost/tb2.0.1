package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 悬赏页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class QA_RewardActivity extends TSActivity<QA$RewardPresenter, QA$RewardFragment>{

    @Override
    protected QA$RewardFragment getFragment() {
        return QA$RewardFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerQA$RewardComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qA$RewardPresenterModule(new QA$RewardPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
