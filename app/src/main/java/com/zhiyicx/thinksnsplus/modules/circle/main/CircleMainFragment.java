package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleTypeItem;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:28
 * @Email Jliuer@aliyun.com
 * @Description 圈子首页
 */
public class CircleMainFragment extends TSListFragment<CircleMainContract.Presenter, CircleInfo>
        implements CircleMainContract.View, BaseCircleItem.CircleItemItemEvent {

    public static final int DATALIMIT = 5;

    private CircleMainHeader mCircleMainHeader;

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.group);
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    public static CircleMainFragment newInstance() {
        CircleMainFragment circleMainFragment = new CircleMainFragment();
        return circleMainFragment;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_createcircle;
    }

    @Override
    protected int setRightLeftImg() {
        return R.mipmap.ico_search;
    }

    @Override
    public void updateCircleCount(int count) {
        mActivity.runOnUiThread(() -> mCircleMainHeader.updateCircleCount(count));
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getActivity(), CreateCircleActivity.class));
    }

    @Override
    protected void setRightLeftClick() {
        super.setRightLeftClick();
        startActivity(new Intent(getActivity(), MarkdownActivity.class));
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem(this));
        adapter.addItemViewDelegate(new CircleTypeItem(this));
        return adapter;
    }

    @Override
    protected void initData() {
        mCircleMainHeader = new CircleMainHeader(getActivity(), null, 2341);
        mHeaderAndFooterWrapper.addHeaderView(mCircleMainHeader.getCircleMainHeader());
        super.initData();
        mPresenter.requestNetData(0L, false);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        closeLoadingView();
    }

    @Override
    public void toAllJoinedCircle(CircleInfo groupInfoBean) {

    }

    @Override
    public void changeRecommend() {

    }

    @Override
    public void toCircleDetail(CircleInfo groupInfoBean) {
        startActivity(new Intent(getActivity(), CircleDetailActivity.class));
    }
}
