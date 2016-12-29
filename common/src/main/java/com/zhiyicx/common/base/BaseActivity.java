package com.zhiyicx.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhiyicx.common.base.i.IBaseActivity;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe Activity 基类
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity implements IBaseActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected BaseApplication mApplication;
    @Inject
    protected P mPresenter;
    private Unbinder mUnbinder;
    protected LayoutInflater mLayoutInflater;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BaseApplication) getApplication();
        mApplication.getActivityList().add(this);
        mLayoutInflater = LayoutInflater.from(this);
        // 如果要使用 eventbus 请将此方法返回 true
        if (useEventBus()) {
            EventBus.getDefault().register(this);// 注册到事件主线
        }
        setContentView(getLayoutId());
        // 绑定到 butterknife
        mUnbinder = ButterKnife.bind(this);
        initView();
        componentInject();// 依赖注入
        initData();

    }

    /**
     * 子类获取contentView
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (BaseActivity.class) {
            mApplication.getActivityList().remove(this);
        }
        if (mPresenter != null) mPresenter.onDestroy();// 释放资源
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().unregister(this);
    }

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 依赖注入的入口
     */
    protected abstract void componentInject();

    /**
     * view 初始化
     */
    protected abstract void initView();

    /**
     * 数据初始化
     */
    protected abstract void initData();


}
