package com.zhiyicx.zhibosdk.policy.impl;


import android.content.Context;

import com.zhiyicx.zhibosdk.policy.ReconnetPolicy;
import com.zhiyicx.zhibosdk.utils.DeviceUtils;
import com.zhiyicx.zhibosdk.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by jess on 16/5/26.
 */
public class ReconnectPolicyImpl implements ReconnetPolicy {
    private ReconnectPolicyCallback mCallBack;
    private boolean isReconnect;
    private boolean isReconnecting;
    private Subscription mSubscription;
    private boolean isStop;
    private int mReconnectCount;
    private Context mContext;//ApplicationContext

    public ReconnectPolicyImpl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void shutDown() {
        if (mContext == null) return;
        if (isStop || mCallBack == null || isReconnecting) {//停止后不执行
            return;
        }
        mSubscription = Observable.range(1, 4)
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mReconnectCount = 0;
                        isReconnecting = true;
                        isReconnect = false;
                        mCallBack.reconnectStart();
                    }
                })
                .flatMap(new Func1<Integer, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Integer i) {
                        LogUtils.errroInfo("ReconnectPolicyImpl", "...." + i);
                        switch (i) {
                            case 1:
                                return Observable.timer(1, TimeUnit.SECONDS);
                            case 2:
                                return Observable.timer(4, TimeUnit.SECONDS);
                            case 3:
                                return Observable.timer(7, TimeUnit.SECONDS);
                            case 4:
                                return Observable.timer(10, TimeUnit.SECONDS);
                            default:
                        }
                        return null;
                    }
                })
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        if (DeviceUtils.netIsConnected(mContext) && mReconnectCount < 3) {
                            if (!isReconnect) {//重连失败继续重连
                                isReconnect = mCallBack.onReConnect();
                            } else {//连接成功调最终将执行的方法
                                finalDo();
                            }
                        }
                        return ++mReconnectCount;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer count) {
                        LogUtils.errroInfo("ReconnectPolicyImpl", "subscribe...." + count + "//" + isReconnect);
                        if (count == 4) {//最后一个序列
                            finalDo();//结束重连
                            if (!isReconnect) mCallBack.reConnentFailure();
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 最终会做的事
     */
    public void finalDo() {
        if (isReconnecting) {//只在正在执行重连时调用
            isReconnecting = false;//标记重连结束
            if (mCallBack != null)
                mCallBack.reconnectEnd();
        }
    }


    /**
     * 告诉重连策略重连成功
     */
    @Override
    public void reconnectSuccess() {
        if (isReconnecting) {//重连正在执行的时候
            LogUtils.errroInfo("ReconnectPolicyImpl", "success---------");
            isReconnect = true;
            isReconnecting = false;
        }
    }

    public void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();//保证activity结束时取消所有正在执行的订阅
        }
    }

    @Override
    public void stop() {
        isStop = true;
        LogUtils.errroInfo("ReconnectPolicyImpl", "stop---------");
        unSubscribe(mSubscription);//停止
    }


    @Override
    public void setCallBack(ReconnectPolicyCallback callBack) {
        this.mCallBack = callBack;
    }


}
