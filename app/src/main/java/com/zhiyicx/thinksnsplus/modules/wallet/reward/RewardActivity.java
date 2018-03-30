package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 聚合打赏页
 * @Author Jungle68
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
public class RewardActivity extends TSActivity<RewardPresenter, RewardFragment> {


    @Override
    protected void componentInject() {
        DaggerRewardComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rewardPresenterModule(new RewardPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected RewardFragment getFragment() {
        return RewardFragment.newInstance(getIntent().getExtras());
    }

}
