package com.zhiyicx.thinksnsplus.modules.rank;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DigBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

/**
 * @Describe 我的排行榜
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
public class RankFragment extends TSListFragment<RankContract.Presenter, DigBean> implements RankContract.View {


    @Override
    protected CommonAdapter<DigBean> getAdapter() {
        return new RankAdapter(getContext(), R.layout.item_mine_dig_list, mListDatas);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dig_ranking);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    public static RankFragment newInstance(Bundle bundle) {
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



}
