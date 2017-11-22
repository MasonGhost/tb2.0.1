package com.zhiyicx.zhibolibrary.ui.common;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jess on 2015/12/8.
 */
public abstract class ZBLBaseFragment extends Fragment {
    protected Activity mActivity;
    protected View mRootView;
    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = initView();
        unbinder = ButterKnife.bind(this, mRootView);//绑定到butterknife
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity =getActivity();
        EventBus.getDefault().register(this);//注册到事件主线
        initData();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    /**
     * fragement加载完成
     */
    public void showCompleted() {

    }

    protected abstract View initView();

    protected abstract void initData();

    protected void initListener() {

    }

    public void setData(Object data) {

    }

    public void setData() {

    }

}
