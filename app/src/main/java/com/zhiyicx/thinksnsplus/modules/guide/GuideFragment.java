package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GuideFragment extends TSFragment<GuideContract.Presenter> implements GuideContract.View {
    public static final int DEFAULT_DELAY_TIME = 1000;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.timer(DEFAULT_DELAY_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mPresenter.checkLogin();
                    }
                });
    }

    @Override
    public void startActivity(Class aClass) {
        startActivity(new Intent(getActivity(), aClass));
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

}
