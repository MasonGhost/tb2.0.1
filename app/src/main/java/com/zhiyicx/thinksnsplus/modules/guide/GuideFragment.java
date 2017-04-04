package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;

public class GuideFragment extends TSFragment<GuideContract.Presenter> implements GuideContract.View {

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
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
    public void startActivity(Class aClass) {
        startActivity(new Intent(getActivity(), aClass));
        getActivity().finish();
    }
}
