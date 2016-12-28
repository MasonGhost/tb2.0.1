package com.zhiyicx.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe Fragment 基类
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public abstract class BaseFragment<P > extends RxFragment {
    protected final String TAG = this.getClass().getSimpleName();

    protected View mRootView;
    protected Activity mActivity;
    @Inject
    protected P mPresenter;
    private Unbinder mUnbinder;
    protected LayoutInflater mLayoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mRootView = getContentView();
        // 绑定到 butterknife
        mUnbinder = ButterKnife.bind(this, mRootView);
        initView(mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().register(this);// 注册到事件主线
        ComponentInject();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().unregister(this);
    }

    protected abstract View getContentView();

    /**
     * 依赖注入的入口
     */
    protected abstract void ComponentInject();

    protected abstract void initView(View rootView);

    protected abstract void initData();

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 此方法是让外部调用使 fragment 做一些操作的,比如说外部的 activity 想让 fragment 对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传 bundle,里面存一个 what 字段,来区分不同的方法,在 setData
     * 方法中就可以 switch 做不同的操作,这样就可以用统一的入口方法做不同的事,和 message 同理
     * <p>
     * 使用此方法时请注意调用时 fragment 的生命周期,如果调用此 setData 方法时 onActivityCreated
     * 还没执行,setData 里调用 presenter 的方法时,是会报空的,因为 dagger 注入是在 onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调 setData,在内部 onActivityCreated 中
     * 初始化就可以了
     *
     * @param data
     */
    public void setData(Object data) {

    }

    /**
     * 使用此方法时请注意调用时 fragment 的生命周期,如果调用此 setData 方法时 onActivityCreated
     * 还没执行,setData 里调用 presenter 的方法时,是会报空的,因为 dagger 注入是在 onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调 setData,在内部 onActivityCreated 中
     * 初始化就可以了
     */
    public void setData() {

    }

}
