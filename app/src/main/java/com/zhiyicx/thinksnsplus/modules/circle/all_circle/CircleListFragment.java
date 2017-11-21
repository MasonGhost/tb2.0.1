package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListFragment extends TSListFragment<CircleListContract.Presenter, GroupInfoBean> implements CircleListContract.View {

    @Inject
    CircleListPresenter mCircleListPresenter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected void initData() {
        DaggerCircleListComponent.
                builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleListPresenterModule(new CircleListPresenterModule(this))
                .build().inject(this);
        super.initData();
    }
}
