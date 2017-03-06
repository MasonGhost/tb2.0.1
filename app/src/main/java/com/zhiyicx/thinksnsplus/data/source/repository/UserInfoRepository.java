package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe 用户信息相关的model层实现
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public class UserInfoRepository implements UserInfoContract.Repository {
    private UserInfoClient mUserInfoClient;
    private CommonClient mCommonClient;
    private CacheImp<AuthBean> cacheImp;

    @Inject
    public UserInfoRepository(ServiceManager serviceManager) {
        mUserInfoClient = serviceManager.getUserInfoClient();
        mCommonClient = serviceManager.getCommonClient();
    }

    @Override
    public Observable<ArrayList<AreaBean>> getAreaList() {
        Observable<ArrayList<AreaBean>> observable = Observable.create(new Observable.OnSubscribe<ArrayList<AreaBean>>() {
            @Override
            public void call(Subscriber<? super ArrayList<AreaBean>> subscriber) {
                try {
                    InputStream inputStream = AppApplication.getContext().getAssets().open("area.txt");//读取本地assets数据
                    String jsonString = ConvertUtils.inputStream2String(inputStream, "utf-8");
                    ArrayList<AreaBean> areaBeanList = new Gson().fromJson(jsonString, new TypeToken<ArrayList<AreaBean>>() {
                    }.getType());
                    subscriber.onNext(areaBeanList);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    @Override
    public Observable<BaseJson> changeUserInfo(HashMap<String, String> userInfos) {
        return mUserInfoClient.changeUserInfo(userInfos);
    }

    /**
     * 获取用户信息
     *
     * @param user_ids 用户 id 数组
     * @return
     */
    @Override
    public Observable<BaseJson<List<UserInfoBean>>> getUserInfo(List<Long> user_ids) {
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("user_ids", user_ids);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(datas));
        return mUserInfoClient.getUserInfo(body)
                .subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread());
    }


}
