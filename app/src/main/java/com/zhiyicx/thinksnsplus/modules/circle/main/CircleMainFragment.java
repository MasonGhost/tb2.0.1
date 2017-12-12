package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleTypeItem;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.MyJoinedCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.search.container.CircleSearchContainerActivity;
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
    public static final int TITLEVOUNT = 2;

    private CircleMainHeader mCircleMainHeader;
    private List<CircleInfo> mJoinedCircle;

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
    public List<CircleInfo> getJoinedCircles() {
        return mJoinedCircle;
    }

    @Override
    public void setJoinedCircles(List<CircleInfo> circles) {
        mJoinedCircle = circles;
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
        CreateCircleActivity.startCreateActivity(mActivity);
    }

    @Override
    protected void setRightLeftClick() {
        super.setRightLeftClick();
        startActivity(new Intent(mActivity, CircleSearchContainerActivity.class));
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem(false, mActivity, this,mPresenter));
        adapter.addItemViewDelegate(new CircleTypeItem(this));
        return adapter;
    }

    @Override
    protected void initData() {
        mCircleMainHeader = new CircleMainHeader(mActivity, null, 2341);
        mHeaderAndFooterWrapper.addHeaderView(mCircleMainHeader.getCircleMainHeader());
        super.initData();
        mPresenter.requestNetData(0L, false);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        closeLoadingView();
    }

    /**
     * 查看我加入的
     *
     * @param groupInfoBean
     */
    @Override
    public void toAllJoinedCircle(CircleInfo groupInfoBean) {
        if (mListDatas.size() <= TITLEVOUNT) {
            return;
        }
        Intent intent = new Intent(mActivity, MyJoinedCircleActivity.class);
        startActivity(intent);
    }

    @Override
    public void changeRecommend() {
        if (mListDatas.size() <= TITLEVOUNT) {
            return;
        }
        mPresenter.getRecommendCircle();
    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {
        mPresenter.dealCircleJoinOrExit(position, circleInfo);
    }

    @Override
    public void toCircleDetail(CircleInfo circleInfo) {
        Intent intent = new Intent(mActivity, CircleDetailActivity.class);
        intent.putExtra(CircleDetailFragment.CIRCLE_ID, circleInfo.getId());
        startActivity(intent);
    }
}
