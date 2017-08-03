package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.DaggerRewardComponent;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardPresenter;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardPresenterModule;

/**
 * @Describe 打赏用户列表页
 * @Author Jungle68
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
public class RewardListActivity extends TSActivity<RewardListPresenter, RewardListFragment> {


    @Override
    protected void componentInject() {
        DaggerRewardListComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rewardListPresenterModule(new RewardListPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected RewardListFragment getFragment() {
        return RewardListFragment.newInstance(getIntent().getExtras());
    }

}
