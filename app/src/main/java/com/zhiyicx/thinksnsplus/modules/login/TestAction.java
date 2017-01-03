package com.zhiyicx.thinksnsplus.modules.login;

import android.support.test.espresso.idling.CountingIdlingResource;

import com.zhiyicx.common.base.BaseJson;

import rx.functions.Action1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/3
 * @contact email:450127106@qq.com
 */

public abstract class TestAction<T> implements Action1<T> {
    private CountingIdlingResource mIdlingResource;

    public TestAction(CountingIdlingResource idlingResource) {
        mIdlingResource = idlingResource;
    }

    public TestAction() {
    }

    @Override
    public void call(T t) {
        if (mIdlingResource != null) {
            mIdlingResource.decrement();
        }
        testCall(t);
    }

    abstract void testCall(T t);

}
