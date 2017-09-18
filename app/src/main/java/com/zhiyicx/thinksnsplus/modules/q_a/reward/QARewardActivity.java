package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 悬赏页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class QARewardActivity extends TSActivity<QARewardPresenter, QARewardFragment>{

    @Override
    protected QARewardFragment getFragment() {
        return QARewardFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerQARewardComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qARewardPresenterModule(new QARewardPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
