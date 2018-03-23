package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.Window;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 打赏页
 * @Author Jungle68
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
public class RewardActivity extends TSActivity<RewardPresenter, RewardFragment> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //注意这里要有这句话，不然弹出的布局不是理想中的。
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

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
    protected void initData() {
        super.initData();
        this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected RewardFragment getFragment() {
        return RewardFragment.newInstance(getIntent().getExtras());
    }

}
