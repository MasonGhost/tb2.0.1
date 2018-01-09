package com.zhiyicx.thinksnsplus.base;

import rx.Subscriber;

/**
 * @Author Jliuer
 * @Date 2018/01/09/11:17
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class EmptySubscribe<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T o) {

    }
}
