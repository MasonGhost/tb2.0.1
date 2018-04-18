package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description 糖果兑换页面
 */

public class ExchangeActivity extends TSActivity<ExchangePresenter, ExchangeFragment> {

    @Override
    protected void componentInject() {
        DaggerExchangeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .exchangePresenterModule(new ExchangePresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected ExchangeFragment getFragment() {
        return ExchangeFragment.initFragment(getIntent().getExtras());
    }
}
