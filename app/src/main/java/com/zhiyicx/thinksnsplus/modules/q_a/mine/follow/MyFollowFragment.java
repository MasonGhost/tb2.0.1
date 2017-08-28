package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyFollowFragment extends TSListFragment<MyFollowContract.Presenter, BaseListBean>
        implements MyFollowContract.View{

    @Inject
    MyFollowPresenter mFollowPresenter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected void initData() {
        DaggerMyFollowComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myFollowPresenterModule(new MyFollowPresenterModule(this))
                .build()
                .inject(this);
        super.initData();
    }
}
