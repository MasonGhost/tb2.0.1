package com.zhiyicx.common.mvp;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import org.simple.eventbus.EventBus;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BasePresenter<M, V extends IBaseView> implements IBasePresenter {
    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeSubscription mCompositeSubscription;

    protected M mModel;
    protected V mRootView;


    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;
        onStart();
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        onStart();
    }

    public BasePresenter() {
        onStart();
    }


    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void onStart() {
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().register(this);// 注册 eventbus
    }

    @Override
    public void onDestroy() {
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().unregister(this);// 解除注册 eventbus
        unSubscribe();// 解除订阅
        this.mModel = null;
        this.mRootView = null;
    }

    @Override
    public void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();// 保证 activity 结束时取消所有正在执行的订阅
        }
    }
    protected void handleError(Throwable throwable) {

    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);// 将所有 subscription 放入,集中处理
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();// 保证 activity 结束时取消所有正在执行的订阅
        }
    }


}
