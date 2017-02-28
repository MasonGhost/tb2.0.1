package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public class DynamicDetailFragment extends TSFragment<DynamicDetailContract.Presenter> implements DynamicDetailContract.View {
    @Override
    protected int getBodyLayoutId() {
        return 0;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(DynamicDetailContract.Presenter presenter) {

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

    public static DynamicDetailFragment initFragment(Bundle bundle) {
        DynamicDetailFragment dynamicDetailFragment = new DynamicDetailFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }


}
