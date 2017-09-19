package com.zhiyicx.zhibosdk.manage;

import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.zhiyicx.zhibosdk.ZBSmartLiveSDK;
import com.zhiyicx.zhibosdk.di.component.DaggerZBCloudApiClientComponent;
import com.zhiyicx.zhibosdk.di.module.ZBCloudApiModule;
import com.zhiyicx.zhibosdk.manage.listener.ZBCloudApiCallback;
import com.zhiyicx.zhibosdk.manage.soupport.CloudApiSupport;
import com.zhiyicx.zhibosdk.model.CloudApiModel;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.api.ZBContants;
import com.zhiyicx.zhibosdk.model.entity.ZBUserAuth;
import com.zhiyicx.zhibosdk.utils.CommonUtils;
import com.zhiyicx.zhibosdk.utils.LogUtils;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/7/21.
 * com.zhiyicx.zhibosdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBCloudApiClient implements CloudApiSupport {

    @Inject
    CloudApiModel mCloudApiModel;
    private volatile static ZBCloudApiClient sZBCloudApiClient;

    private ZBCloudApiClient() {
        initDagger();
    }


    private void initDagger() {
        DaggerZBCloudApiClientComponent
                .builder()
                .clientComponent(ZBSmartLiveSDK.getClientComponent())
                .zBCloudApiModule(new ZBCloudApiModule())
                .build()
                .inject(this);

    }

    public static ZBCloudApiClient getInstance() {

        if (sZBCloudApiClient == null) {
            synchronized (ZBCloudApiClient.class) {
                if (sZBCloudApiClient == null) {
                    sZBCloudApiClient = new ZBCloudApiClient();
                }
            }
        }
        return sZBCloudApiClient;
    }

    /**
     * 获取智播云数据by  NoramalCallback
     *
     * @param api
     * @param map
     * @param callback
     */
    @Override
    public void sendZBCloudApiRequest(String api, Map<String, Object> map, final ZBCloudApiCallback callback) {

        doApiRequst(api, map, callback);
    }

//    /**
//     * 获取智播云数据by ZBCloudApiTCallback 泛型
//     *
//     * @param api
//     * @param map
//     * @param callback
//     */
//    public void sendCloudApiRequest(String api, Map<String, Object> map, final ZBCloudApiTCallback<T> callback) {
//        doApiRequst(api, map, callback);
//    }

    /**
     * 获取智播云数据by  rxJava
     * @param api
     * @param map
     * @return
     */
    @Override
    public Observable<JsonObject> sendCloudApiRequestForRx(String api, Map<String, Object> map) {
        return getJsonObjectObservable(api, map);
    }

    @Nullable
    private Observable<JsonObject> getJsonObjectObservable(String api, Map<String, Object> map) {
        if (map == null)
            throw new IllegalArgumentException("无效的参数集");

        ZBUserAuth userAuth = ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret()));
        if (userAuth == null) {
            LogUtils.errroInfo("ticket验证失败！");
            return null;
        }
        map.put("api", api);
        map.put("ak", userAuth.getAk());
        return mCloudApiModel.sendCloudApiRequest(map).subscribeOn(Schedulers.io());
    }


    private void doApiRequst(String api, Map<String, Object> map, final ZBCloudApiCallback callback) {
        getJsonObjectObservable(api, map)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject response) {
                        if (callback != null && response != null)
                            callback.onResponse(response.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null)
                            callback.onError(throwable);
                    }
                });
    }

//    private void doApiRequst(String api, Map<String, Object> map, final ZBCloudApiTCallback<T> callback) {
//        if (map == null)
//            throw new IllegalArgumentException("无效的参数集");
//
//        ZBUserAuth userAuth = ZBContants.getmUserAuth(CommonUtils.MD5encode(ZBApi.getContantsSeckret()));
//        if (userAuth == null) {
//            LogUtils.errroInfo("ticket验证失败！");
//            return;
//        }
//        map.put("api", api);
//        map.put("ak", userAuth.getAk());
//        mCloudApiModel.sendCloudApiRequest(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<JsonObject>() {
//                    @Override
//                    public void call(JsonObject response) {
//                        if (callback != null && response != null) {
//                            ZBBaseJson<T> resut = new ZBBaseJson<T>();
//                            try {
//                                JSONObject tmp = new JSONObject(response.toString());
//                                resut.code = tmp.getString("code");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            resut.data = new Gson().fromJson(response, new TypeToken<T>() {
//                            }.getType());
//                            callback.onResponse(resut);
//
//                        }
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        if (callback != null)
//                            callback.onError(throwable);
//                    }
//                });
//    }


}
