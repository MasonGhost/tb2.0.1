package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @date 17/11/22 13:42
 * @email Jliuer@aliyun.com
 * @description
 */
public class CircleListFragment extends TSListFragment<CircleListContract.Presenter, CircleInfo> implements CircleListContract.View {

    public static final String CIRCLE_TYPE = "circle_type";

    @Inject
    CircleListPresenter mCircleListPresenter;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return super.isNeedRefreshDataWhenComeIn();
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
    public long getCategoryId() {
        try {
            return Long.parseLong(getArguments().getString(CIRCLE_TYPE));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
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
