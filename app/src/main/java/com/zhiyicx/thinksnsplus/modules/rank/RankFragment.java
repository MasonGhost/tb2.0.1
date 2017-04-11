package com.zhiyicx.thinksnsplus.modules.rank;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * @Describe 我的排行榜
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
public class RankFragment extends TSListFragment<RankContract.Presenter, FollowFansBean> implements RankContract.View {
    // 当前页面是关注页面还是粉丝页面:pageType
    public static final int FANS_FRAGMENT_PAGE = 0;
    public static final int FOLLOW_FRAGMENT_PAGE = 1;

    // 获取页面类型的key
    public static final String PAGE_TYPE = "page_type";
    // 获取用户id，决定这是谁的关注粉丝列表
    public static final String PAGE_DATA = "page_data";

    private int pageType;// 页面类型，由上一个页面决定
    private long userId;// 上一个页面传过来的用户id

    @Override
    protected CommonAdapter<FollowFansBean> getAdapter() {
        return new RankAdapter(getContext(), R.layout.item_follow_fans_list, mListDatas, pageType, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        pageType = getArguments().getInt(PAGE_TYPE, FOLLOW_FRAGMENT_PAGE);
        userId = getArguments().getLong(PAGE_DATA);
        super.initView(rootView);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, userId, pageType);
    }

    @Override
    protected List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, userId, pageType);
    }

    public static RankFragment initFragment(Bundle bundle) {
        RankFragment rankFragment = new RankFragment();
        rankFragment.setArguments(bundle);
        return rankFragment;
    }

    @Override
    public void upDateFollowFansState(int index) {
        refreshData(index);
    }

    @Override
    public void upDateFollowFansState() {
        refreshData();
    }

    @Override
    public int getPageType() {
        return pageType;
    }

}
