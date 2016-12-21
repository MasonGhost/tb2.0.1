package com.zhiyicx.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhiyicx.common.mvp.BasePresenter;

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

public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected BaseApplication mApplication;
    @Inject
    protected P mPresenter;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BaseApplication) getApplication();
        mApplication.getActivityList().add(this);
        // 如果要使用eventbus请将此方法返回 true
        if (useEventBus()) {
            EventBus.getDefault().register(this);// 注册到事件主线
        }
        setContentView(getLayoutId());
        // 绑定到butterknife
        mUnbinder = ButterKnife.bind(this);
        ComponentInject();// 依赖注入
        initView();
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (BaseActivity.class) {
            mApplication.getActivityList().remove(this);
        }
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 依赖注入的入口
     */
    protected abstract void ComponentInject();

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * view 初始化
     */
    protected void initView() {

    }

    /**
     * 数据初始化
     */
    protected void initData() {

    }


}
