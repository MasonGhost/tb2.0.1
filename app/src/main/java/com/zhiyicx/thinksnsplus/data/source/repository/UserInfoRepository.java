package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.simple.eventbus.EventBus;

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
import rx.functions.Func1;
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
    private UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    private FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    private DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    private Context mContext;

    @Inject
    public UserInfoRepository(ServiceManager serviceManager, Application application) {
        this.mContext = application;
        mUserInfoClient = serviceManager.getUserInfoClient();
        mCommonClient = serviceManager.getCommonClient();
        mFollowFansBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().followFansBeanGreenDao();
        mDynamicBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicBeanGreenDao();
        mUserInfoBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao();
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

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(String user_ids) {
        return mUserInfoClient.getUserFollowState(user_ids)
                .map(new Func1<BaseJson<List<FollowFansBean>>, BaseJson<List<FollowFansBean>>>() {
                    @Override
                    public BaseJson<List<FollowFansBean>> call(BaseJson<List<FollowFansBean>> listBaseJson) {
                        if (listBaseJson.isStatus()) {
                            for (FollowFansBean followFansBean : listBaseJson.getData()) {
                                followFansBean.setOriginUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
                                followFansBean.setOrigintargetUser("");
                            }
                        }
                        return listBaseJson;
                    }
                });
    }

    @Override
    public void handleFollow(FollowFansBean followFansBean) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        UserInfoBean mineUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()));
        if (followFansBean.getOrigin_follow_status() == FollowFansBean.UNFOLLOWED_STATE) {
            // 当前未关注，进行关注
            followFansBean.setOrigin_follow_status(FollowFansBean.IFOLLOWED_STATE);
            EventBus.getDefault().post(followFansBean, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
            backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_FOLLOW_USER);
            if (!TextUtils.isEmpty(mineUserInfo.getFollowing_count())) {
                mineUserInfo.setFollowing_count(String.valueOf(Integer.valueOf(mineUserInfo.getFollowing_count()) + 1));
            } else {
                mineUserInfo.setFollowing_count(String.valueOf(1));
            }

        } else {
            // 已关注，取消关注
            followFansBean.setOrigin_follow_status(FollowFansBean.UNFOLLOWED_STATE);
            EventBus.getDefault().post(followFansBean, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER);
            try {
                mineUserInfo.setFollowing_count(String.valueOf(Integer.valueOf(mineUserInfo.getFollowing_count()) - 1));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getTargetUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库,关注状态
        mFollowFansBeanGreenDao.insertOrReplace(followFansBean);
        mUserInfoBeanGreenDao.insertOrReplace(mineUserInfo);
        // 更新动态关注状态
        mDynamicBeanGreenDao.updateFollowStateByUserId(followFansBean.getTargetUserId(), followFansBean.getOrigin_follow_status() != FollowFansBean.UNFOLLOWED_STATE);

    }


}
