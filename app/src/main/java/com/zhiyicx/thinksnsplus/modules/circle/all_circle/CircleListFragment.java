package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @date 17/11/22 13:42
 * @email Jliuer@aliyun.com
 * @description
 */
public class CircleListFragment extends TSListFragment<CircleListContract.Presenter, CircleInfo>
        implements CircleListContract.View, BaseCircleItem.CircleItemItemEvent {

    public static final String CIRCLE_TYPE = "circle_type";

    @Inject
    CircleListPresenter mCircleListPresenter;
    private CircleListItem mCircleListItem;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
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
        mCircleListItem = new CircleListItem(false, mActivity, this, mPresenter);
        adapter.addItemViewDelegate(mCircleListItem);
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
        mCircleListItem.setPresenter(mPresenter);
    }

    @Override
    public void toAllJoinedCircle(CircleInfo circleInfo) {

    }

    @Override
    public void toCircleDetail(CircleInfo circleInfo) {
        if ((CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode())
                || CircleInfo.CirclePayMode.PRIVATE.value.equals(circleInfo.getMode()) && circleInfo.getJoined() == null)) {
            showSnackErrorMessage(getString(R.string.circle_blocked));
        }
        Intent intent = new Intent(getActivity(), CircleDetailActivity.class);
        intent.putExtra(CircleDetailFragment.CIRCLE_ID, circleInfo.getId());
        startActivity(intent);
    }

    @Override
    public void changeRecommend() {

    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {
        mPresenter.dealCircleJoinOrExit(position, circleInfo);
    }
}
