package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 专家搜索页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertSearchActivity extends TSActivity<ExpertSearchPresenter, ExpertSearchFragment>{

    public static final String BUNDLE_TOPIC_BEAN = "bundle_topic_bean";
    public static final String BUNDLE_TOPIC_IDS = "bundle_topic_ids";

    @Override
    protected ExpertSearchFragment getFragment() {
        return new ExpertSearchFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerExpertSearchComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .expertSearchPresenterModule(new ExpertSearchPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
