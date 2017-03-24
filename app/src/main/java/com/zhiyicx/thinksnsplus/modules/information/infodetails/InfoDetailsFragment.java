package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.refresh.RefreshFooterView;
import com.zhiyicx.baseproject.widget.refresh.RefreshHeaderView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForContent;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForDig;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/03/08
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsFragment extends TSListFragment {
    @BindView(R.id.tv_top_tip_text)
    TextView mTvTopTipText;
    @BindView(R.id.fl_top_tip_container)
    FrameLayout mFlTopTipContainer;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    RefreshFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.refreshlayout)
    SwipeToLoadLayout mRefreshlayout;
    @BindView(R.id.dd_dynamic_tool)
    DynamicDetailMenuView mDdDynamicTool;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_detail;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter<DynamicBean> adapter = new MultiItemTypeAdapter<>(getContext(),
                mListDatas);
        adapter.addItemViewDelegate(new DynamicDetailItemForContent());
        adapter.addItemViewDelegate(new DynamicDetailItemForDig());
        return adapter;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mDdDynamicTool.setButtonText(new int[]{R.string.info_collect,R.string.comment,
                                                R.string.share,R.string.more});
        mDdDynamicTool.setImageNormalResourceIds(new int[]{R.mipmap.detail_ico_good_uncollect,
        R.mipmap.home_ico_comment_normal,R.mipmap.detail_ico_share_normal,R.mipmap.home_ico_more});

        mDdDynamicTool.setImageCheckedResourceIds(new int[]{R.mipmap.detail_ico_collect,
                R.mipmap.home_ico_comment_normal,R.mipmap.detail_ico_share_normal,R.mipmap.home_ico_more});
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_details);
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

    @Override
    protected List requestCacheData(Long maxId, boolean isLoadMore) {
        return new ArrayList();
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
    }

    @Override
    public void onRefresh() {
        mRefreshlayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        mRefreshlayout.setLoadingMore(false);
    }
}
