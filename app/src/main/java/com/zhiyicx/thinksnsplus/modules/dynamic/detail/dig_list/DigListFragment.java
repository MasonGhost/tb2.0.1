package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public class DigListFragment extends TSListFragment<DigListContract.Presenter, FollowFansBean> implements DigListContract.View {

    private List<FollowFansBean> mDatas = new ArrayList<>();

    @Override
    protected MultiItemTypeAdapter<FollowFansBean> getAdapter() {
        mDatas.add(new FollowFansBean());
        mDatas.add(new FollowFansBean());
        mDatas.add(new FollowFansBean());
        return new DigListAdapter(getContext(), R.layout.item_dig_list, mDatas);
    }

    @Override
    public void setPresenter(DigListContract.Presenter presenter) {
        mPresenter = presenter;
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
    protected String setCenterTitle() {
        return getString(R.string.dig_list);
    }

    @Override
    protected void initData() {

    }

    public static DigListFragment initFragment(Bundle bundle) {
        DigListFragment digListFragment = new DigListFragment();
        digListFragment.setArguments(bundle);
        return digListFragment;
    }

    @Override
    public void upDataFollowState(int position) {
      /*  List<FollowFansBean> followFansBeanList = mAdapter.getDatas();
        FollowFansBean followFansBean = followFansBeanList.get(index);
        LogUtils.i("new_state--》" + followState);
        followFansBean.setOrigin_follow_status(followState);
        refreshData(index);*/
    }
}
