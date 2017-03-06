package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public class DigListFragment extends TSListFragment<DigListContract.Presenter, DynamicDigListBean> {

    private List<DynamicDigListBean> mDatas = new ArrayList<>();

    @Override
    protected MultiItemTypeAdapter<DynamicDigListBean> getAdapter() {
        mDatas.add(new DynamicDigListBean());
        mDatas.add(new DynamicDigListBean());
        mDatas.add(new DynamicDigListBean());
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
}
