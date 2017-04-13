package com.zhiyicx.thinksnsplus.modules.collect;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.base.TSListFragment;

/**
 * @author LiuChao
 * @describe 收藏列表
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectListActivity extends TSActivity<CollectListPresenter, CollectListFragment> {
    @Override
    protected CollectListFragment getFragment() {
        return CollectListFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        // 如果CollectListFragment需要Presenter逻辑，就创建dagger
    }

}
