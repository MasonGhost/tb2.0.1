package com.zhiyicx.zhibolibrary.presenter;

import com.google.gson.Gson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.StarExchangeModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.GoldService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.Config;
import com.zhiyicx.zhibolibrary.model.entity.StarExchangeList;
import com.zhiyicx.zhibolibrary.model.entity.TradeOrder;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.view.StarExchangeView;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBApiToken;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/26.
 */
@ActivityScope
public class StarExchangePresenter extends BasePresenter<StarExchangeModel, StarExchangeView> {
    private Subscription mPreSubscription;
    private Subscription mExchangeSubscription;
    private String mToken = "";
    private Subscription mOrderSubscription;
    private Subscription mConfigSubscription;


    @Inject
    public StarExchangePresenter(StarExchangeModel model, StarExchangeView rootView) {
        super(model, rootView);
    }

    /**
     * 获得token
     */
    public void obtainToken() {
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
        final String token = UiUtils.MD5encode("" + currentTime + GoldService.EXCHANGE_TYPE_ZAN + ZhiboApplication.userInfo.uid);
        LogUtils.warnInfo(TAG, token + "---" + currentTime + "----" + GoldService.EXCHANGE_TYPE_ZAN + "------" + ZhiboApplication.userInfo.uid);

        //得到口令
        mPreSubscription = mModel.getPreToken(token
                , hexTime
                , ZhiboApplication.userInfo.uid
                , GoldService.EXCHANGE_TYPE_ZAN
                , null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<ZBApiToken>>() {
                    @Override
                    public void call(BaseJson<ZBApiToken> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mToken = json.data.pre_token;
                        } else {
                            LogUtils.debugInfo(TAG, json.message + "-----obtain");
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    public void exchange(final int costStar) {
        mExchangeSubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                LogUtils.warnInfo(TAG, mToken);
                mOrderSubscription = getOrderObservable(subscriber, costStar, null)
                        .subscribe(new Action1<BaseJson<UserInfo>>() {
                            @Override
                            public void call(BaseJson<UserInfo> json) {
                                LogUtils.debugInfo(TAG, "Exchange---------");
                                if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                                    updateUserCount(json.data);//存储最新的用户统计信息到内存中
                                    LogUtils.debugInfo(TAG, "Exchange_success");
                                    mRootView.showMessage(UiUtils.getString(R.string.str_exchange_success));
                                } else {
                                    mRootView.showMessage(json.message);
                                }
                                mRootView.hideLoading();
                                subscriber.onCompleted();//结束
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                mRootView.showMessage(UiUtils.getString("str_net_erro"));
                                mRootView.hideLoading();
                                LogUtils.warnInfo(TAG, "root_Erro-------");
                            }
                        });
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(final Observable<? extends Throwable> observable) {
                LogUtils.debugInfo(TAG, "retry_when--------");
                return observable.flatMap(new Func1<Throwable, Observable<BaseJson<ZBApiToken>>>() {
                    @Override
                    public Observable<BaseJson<ZBApiToken>> call(Throwable throwable) {
                        LogUtils.debugInfo(TAG, "-----retry_start------");
                        if (throwable instanceof IllegalArgumentException) {
                            int currentTime = (int) (System.currentTimeMillis() / 1000);
                            String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
                            String token = UiUtils.MD5encode("" + currentTime + GoldService.EXCHANGE_TYPE_ZAN + ZhiboApplication.userInfo.uid);
                            return mModel.getPreToken(token
                                    , hexTime
                                    , ZhiboApplication.userInfo.uid
                                    , GoldService.EXCHANGE_TYPE_ZAN
                                    , null
                                 );
                        }
                        return Observable.error(throwable);//继续抛给onerro

                    }
                }).flatMap(new Func1<BaseJson<ZBApiToken>, Observable<?>>() {
                    @Override
                    public Observable<?> call(BaseJson<ZBApiToken> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mToken = json.data.pre_token;//请求成功返回空让后面执行逻辑
                            return Observable.create(new Observable.OnSubscribe<Long>() {
                                @Override
                                public void call(Subscriber<? super Long> subscriber) {
                                    subscriber.onNext(1L);//调用onnext重新订阅
                                    LogUtils.debugInfo(TAG, "reSubscribe-------");
                                }
                            });
                        }
                        mRootView.showMessage(json.message);
                        mRootView.hideLoading();
                        return null;
                    }
                });
            }
        }, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.warnInfo(TAG, "OnSubscribe-------");
                        mRootView.showLoading();
                    }
                })
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                        LogUtils.warnInfo(TAG, "finally-------");
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LogUtils.warnInfo(TAG, "root_done");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(UiUtils.getString("str_net_erro"));
                        mRootView.hideLoading();
                        LogUtils.warnInfo(TAG, "root_Erro-------");
                    }
                });
    }

    private Observable<BaseJson<UserInfo>> getOrderObservable(final Subscriber<? super String> subscriber, int costStar, String giftCode) {
        return mModel.createOrder(//得到口令则创建订单
                mToken
                , costStar
                , giftCode
                , null
               )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<BaseJson<TradeOrder>, Observable<BaseJson<UserInfo>>>() {
                    @Override
                    public Observable<BaseJson<UserInfo>> call(BaseJson<TradeOrder> json) {
                        if (!json.code.equals(ZBLApi.REQUEST_SUCESS)) {//请求失败
                            LogUtils.debugInfo(TAG, json.message + "-----create1");
                            if (json.code.equals(ZBLApi.REQUEST_EXCHANGE_PROTOKEN_INVALID)||json.code.equals(ZBLApi.REQUEST_EXCHANGE_PROTOKEN_INVALID2)) {//如果是口令无效的错误则抛错,重新拉取口令
                                subscriber.onError(new IllegalArgumentException());//调用retry
                            } else {
                                mRootView.showMessage(json.message);
                            }
                            return null;
                        }
                        LogUtils.debugInfo(TAG, "-----create2");
                        return mModel.getOrderStatus(json.data.trade_order//成功后再次查询订单状态
                               );
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取配置信息
     */
    public void getConfig() {
        String localConfig = "{\"zan_list\":[{\"zan\":100,\"gold\":20},{\"zan\":200,\"gold\":40},{\"zan\":500,\"gold\":100},{\"zan\":1000,\"gold\":200},{\"zan\":2000,\"gold\":400},{\"zan\":5000,\"gold\":1000}],\"pay_list\":{\"ios\":[{\"money\":1,\"gold\":1,\"product_id\":\"zhibo.product01\"},{\"money\":6,\"gold\":10,\"product_id\":\"zhibo.product02\"},{\"money\":12,\"gold\":30,\"product_id\":\"zhibo.product03\"},{\"money\":30,\"gold\":110,\"product_id\":\"zhibo.product04\"},{\"money\":50,\"gold\":300,\"product_id\":\"zhibo.product05\"},{\"money\":108,\"gold\":1000,\"product_id\":\"zhibo.product06\"},{\"money\":188,\"gold\":2500,\"product_id\":\"zhibo.product07\"}],\"android\":[{\"money\":1,\"gold\":1},{\"money\":5,\"gold\":10},{\"money\":10,\"gold\":30},{\"money\":30,\"gold\":120},{\"money\":50,\"gold\":250},{\"money\":100,\"gold\":600},{\"money\":200,\"gold\":2000}]},\"cash_list\":[{\"money\":1,\"gold\":10},{\"money\":5,\"gold\":50},{\"money\":10,\"gold\":100},{\"money\":20,\"gold\":200},{\"money\":50,\"gold\":500},{\"money\":100,\"gold\":1000}]}";

        //服務器暫不支持获取，先本地配置

        ZBLApi.EXCHANGE_TYPE_LIST = new Gson().fromJson(localConfig, StarExchangeList.class);
        DataHelper.saveDeviceData(DataHelper.CONFIG_NAME, ZBLApi.EXCHANGE_TYPE_LIST, UiUtils.getContext());//保存到本地
        mRootView.initRegular();
//        final int currentTime = (int) (System.currentTimeMillis() / 1000);
//        final String hexTime = Integer.toHexString(currentTime);//十六进制时间戳
//        String token = UiUtils.MD5encode(currentTime + hexTime);//当前时间戳+16进制时间戳，MD5
//
//        mConfigSubscription = mModel.getConfig(hexTime, token, "").observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseJson<Config>>() {
//                    @Override
//                    public void call(BaseJson<Config> json) {
//                        if (json == null) return;
//                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
//                            DataHelper.saveDeviceData(DataHelper.CONFIG_NAME, json.data, UiUtils.getContext());//保存到本地
//                            mRootView.initRegular();
//                        } else {//请求失败
//                            LogUtils.warnInfo("erroCode:" + json.code + "," + json.message);
//                            mRootView.showMessage(json.message);
//                        }
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                        //提示用户
//                        mRootView.showMessage(UiUtils.getString("str_network_error_action"));
//                    }
//                });

    }


    private void updateUserCount(UserInfo data) {
        ZhiboApplication.userInfo.gold = data.gold;
        ZhiboApplication.userInfo.follow_count = data.follow_count;
        ZhiboApplication.userInfo.fans_count = data.fans_count;
        ZhiboApplication.userInfo.zan_count = data.zan_count;
        mRootView.updatedGold();//刷新金币信息
        mRootView.updateStar();//刷新赞的信息
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mPreSubscription);
        unSubscribe(mExchangeSubscription);
        unSubscribe(mOrderSubscription);
        unSubscribe(mConfigSubscription);
    }
}
