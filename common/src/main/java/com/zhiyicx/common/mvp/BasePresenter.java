package com.zhiyicx.common.mvp;

import android.app.Application;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BasePresenter<R, V extends IBaseView> implements IBasePresenter {
    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeSubscription mCompositeSubscription;

    protected R mRepository;
    protected V mRootView;
    @Inject
    protected Application mContext;

    public BasePresenter(R repository, V rootView) {
        this.mRepository = repository;
        this.mRootView = rootView;
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
    }

    public BasePresenter() {
    }


    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onDestroy() {
        unSubscribe();
    }

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
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
