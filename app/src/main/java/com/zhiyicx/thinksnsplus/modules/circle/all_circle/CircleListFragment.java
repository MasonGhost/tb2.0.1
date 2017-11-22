package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @date 17/11/22 13:42
 * @email Jliuer@aliyun.com
 * @description
 */
public class CircleListFragment extends TSListFragment<CircleListContract.Presenter, GroupInfoBean> implements CircleListContract.View {

    public static final String CIRCLE_TYPE = "circle_type";

    @Inject
    CircleListPresenter mCircleListPresenter;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    public static CircleListFragment newInstance(String type) {
        CircleListFragment circleListFragment = new CircleListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CIRCLE_TYPE, type);
        circleListFragment.setArguments(bundle);
        return circleListFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        for (int i = 0; i < 12; i++) {
            GroupInfoBean groupInfoBean = new GroupInfoBean();
            groupInfoBean.setId(1);
            groupInfoBean.setTitle("我加入");
            groupInfoBean.setIntro("查看更多");
            mListDatas.add(groupInfoBean);
        }
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem());
        return adapter;
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
