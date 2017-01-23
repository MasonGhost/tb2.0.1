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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
                    InputStream inputStream = AppApplication.getContext().getAssets().open("testArea.txt");//读取本地assets数据
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
     * @param user_id 用户 id
     * @return
     */
    @Override
    public Observable<BaseJson<UserInfoBean>> getUserInfo(int user_id) {
        return mUserInfoClient.getUserInfo(user_id)
                .subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 上传文件，构造参数
     */
    private List<MultipartBody.Part> upLoadFile(Map<String, String> filePathList, Map<String, String> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单类型
        if (params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder.addFormDataPart(key, params.get(key));//ParamKey.TOKEN 自定义参数key常量类，即参数名
            }
        }
        if (filePathList != null) {
            Set<String> filePathKey = filePathList.keySet();
            for (String fileParam : filePathKey) {
                try {
                    File file = new File(filePathList.get(fileParam));//filePath 图片地址
                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    builder.addFormDataPart(fileParam, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    private HashMap<String, String> parseJSONObject(JSONObject jsonObject) {

        if (jsonObject == null) {
            return null;
        }
        HashMap<String, String> jsonMap = new HashMap<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            try {
                String key = iterator.next();
                jsonMap.put(key, jsonObject.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonMap;
    }

}
