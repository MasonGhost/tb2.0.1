package com.zhiyicx.thinksnsplus.modules.information.indetails;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/03/07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsFragment extends TSListFragment {

    @BindView(R.id.tv_top_tip_text)
    TextView mTvTopTipText;
    @BindView(R.id.fl_top_tip_container)
    FrameLayout mFlTopTipContainer;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refreshlayout)
    SwipeToLoadLayout mRefreshlayout;
    @BindView(R.id.dd_dynamic_tool)
    DynamicDetailMenuView mDdDynamicTool;

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_details);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_detail;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        return null;
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }
}
