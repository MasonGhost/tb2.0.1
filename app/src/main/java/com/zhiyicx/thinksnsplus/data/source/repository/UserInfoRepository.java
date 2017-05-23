package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.cache.CacheImp;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
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

    /**
     * 获取 地区列表
     *
     * @return
     */
    @Override
    public Observable<ArrayList<AreaBean>> getAreaList() {
        return Observable.create(new Observable.OnSubscribe<ArrayList<AreaBean>>() {
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
    }

    /**
     * 修改用户信息
     *
     * @param userInfos 用户需要修改的信息，通过 hashMap 传递，key 表示请求字段，value 表示修改的值
     * @return
     */
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
    public Observable<BaseJson<List<UserInfoBean>>> getUserInfo(List<Object> user_ids) {
        //V1
//        HashMap<String, Object> datas = new HashMap<>();
//        datas.put("user_ids", user_ids);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(datas));
//        return mUserInfoClient.getUserInfo(body)
//                .subscribeOn(Schedulers.io()).
//                        observeOn(AndroidSchedulers.mainThread());

        // V2
        String userids = user_ids.toString();
        userids = userids.replace("[", "");
        userids = userids.replace("]", "");
        return mUserInfoClient.getUserInfoV2(userids)
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<UserInfoBean>, BaseJson<List<UserInfoBean>>>() {
                    @Override
                    public BaseJson<List<UserInfoBean>> call(List<UserInfoBean> userInfoBeen) {
                        BaseJson<List<UserInfoBean>> baseJson = new BaseJson<List<UserInfoBean>>();
                        baseJson.setStatus(true);
                        baseJson.setData(userInfoBeen);
                        return baseJson;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 获取用户信息,先从本地获取，本地没有再从网络 获取
     *
     * @param user_id 用户 id
     * @return
     */
    @Override
    public Observable<BaseJson<UserInfoBean>> getLocalUserInfoBeforeNet(long user_id) {
        final BaseJson<UserInfoBean> beanBaseJson = new BaseJson<>();
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(user_id);
        if (userInfoBean != null) {
            beanBaseJson.setStatus(true);
            beanBaseJson.setData(userInfoBean);
            return Observable.just(beanBaseJson);
        }
        List<Object> user_ids = new ArrayList<>();
        user_ids.add(user_id);

        //V1
//        HashMap<String, Object> datas = new HashMap<>();
//        datas.put("user_ids", user_ids);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(datas));
//        return mUserInfoClient.getUserInfo(body)
//                .subscribeOn(Schedulers.io()).
//                        observeOn(AndroidSchedulers.mainThread())
//                .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<UserInfoBean>>() {
//                    @Override
//                    public BaseJson<UserInfoBean> call(BaseJson<List<UserInfoBean>> listBaseJson) {
//                        beanBaseJson.setCode(listBaseJson.getCode());
//                        beanBaseJson.setMessage(listBaseJson.getMessage());
//                        beanBaseJson.setStatus(listBaseJson.isStatus());
//                        if (listBaseJson.isStatus()) {
//                            beanBaseJson.setData(listBaseJson.getData().get(0));
//                        }
//                        return beanBaseJson;
//                    }
//                });

        // V2
        String userids = user_ids.toString();
        userids = userids.replace("[", "");
        userids = userids.replace("]", "");

        return mUserInfoClient.getUserInfoV2(userids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<UserInfoBean>, BaseJson<UserInfoBean>>() {
                    @Override
                    public BaseJson<UserInfoBean> call(List<UserInfoBean> listBaseJson) {
                        beanBaseJson.setStatus(true);
                        beanBaseJson.setData(listBaseJson.get(0));
                        return beanBaseJson;
                    }
                });

    }

    /**
     * 获取用户关注状态
     *
     * @param user_ids
     * @return
     */
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

    /**
     * 关注操作
     *
     * @param followFansBean
     */
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

    /**
     * 获取点赞排行榜
     *
     * @param page
     * @return
     */
    @Override
    public Observable<BaseJson<List<DigRankBean>>> getDidRankList(int page) {
        return mUserInfoClient.getDigRankList(page, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DigRankBean>>, Observable<BaseJson<List<DigRankBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DigRankBean>>> call(final BaseJson<List<DigRankBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                            List<Object> userIds = new ArrayList();
                            for (DigRankBean digRankBean : listBaseJson.getData()) {
                                userIds.add(digRankBean.getUser_id());
                            }
                            return getUserInfo(userIds)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<DigRankBean>>>() {
                                        @Override
                                        public BaseJson<List<DigRankBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                for (DigRankBean digRankBean : listBaseJson.getData()) {
                                                    digRankBean.setDigUserInfo(userInfoBeanSparseArray.get(digRankBean.getUser_id().intValue()));
                                                }
                                                AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao().insertOrReplace(userinfobeans.getData());
                                            } else {
                                                listBaseJson.setStatus(userinfobeans.isStatus());
                                                listBaseJson.setCode(userinfobeans.getCode());
                                                listBaseJson.setMessage(userinfobeans.getMessage());
                                            }
                                            return listBaseJson;
                                        }
                                    });
                        }
                        return Observable.just(listBaseJson);
                    }
                });
    }

    /**
     * 获取我收到的赞的列表
     *
     * @param max_id
     * @return
     */
    @Override
    public Observable<BaseJson<List<DigedBean>>> getMyDiggs(int max_id) {
        return mUserInfoClient.getMyDiggs(max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DigedBean>>, Observable<BaseJson<List<DigedBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DigedBean>>> call(final BaseJson<List<DigedBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                            List<Object> userIds = new ArrayList();
                            for (DigedBean digedBean : listBaseJson.getData()) {
                                userIds.add(digedBean.getUser_id());
                                userIds.add(digedBean.getTo_user_id());
                            }
                            return getUserInfo(userIds)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<DigedBean>>>() {
                                        @Override
                                        public BaseJson<List<DigedBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                for (DigedBean digedBean : listBaseJson.getData()) {
                                                    digedBean.setDigUserInfo(userInfoBeanSparseArray.get(digedBean.getUser_id().intValue()));
                                                    digedBean.setDigedUserInfo(userInfoBeanSparseArray.get(digedBean.getTo_user_id().intValue()));
                                                }
                                                AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao().insertOrReplace(userinfobeans.getData());
                                            } else {
                                                listBaseJson.setStatus(userinfobeans.isStatus());
                                                listBaseJson.setCode(userinfobeans.getCode());
                                                listBaseJson.setMessage(userinfobeans.getMessage());
                                            }
                                            return listBaseJson;
                                        }
                                    });
                        }
                        return Observable.just(listBaseJson);
                    }
                });
    }

    /**
     * 获取我收到的评论列表
     *
     * @param max_id
     * @return
     */
    @Override
    public Observable<BaseJson<List<CommentedBean>>> getMyComments(int max_id) {
        return mUserInfoClient.getMyComments(max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<CommentedBean>>, Observable<BaseJson<List<CommentedBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<CommentedBean>>> call(final BaseJson<List<CommentedBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                            List<Object> userIds = new ArrayList();
                            for (CommentedBean commentedBean : listBaseJson.getData()) {
                                userIds.add(commentedBean.getUser_id());
                                userIds.add(commentedBean.getTo_user_id());
                                userIds.add(commentedBean.getReply_to_user_id());
                            }
                            return getUserInfo(userIds)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<CommentedBean>>>() {
                                        @Override
                                        public BaseJson<List<CommentedBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                for (CommentedBean commentedBean : listBaseJson.getData()) {
                                                    commentedBean.setCommentUserInfo(userInfoBeanSparseArray.get(commentedBean.getUser_id().intValue()));
                                                    commentedBean.setSourceUserInfo(userInfoBeanSparseArray.get(commentedBean.getTo_user_id().intValue()));
                                                    if (commentedBean.getReply_to_user_id() == 0) { // 用于占位
                                                        UserInfoBean userinfo = new UserInfoBean();
                                                        userinfo.setUser_id(0L);
                                                        commentedBean.setReplyUserInfo(userinfo);
                                                    } else {
                                                        commentedBean.setReplyUserInfo(userInfoBeanSparseArray.get((int) commentedBean.getReply_to_user_id().intValue()));
                                                    }
                                                }
                                                AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao().insertOrReplace(userinfobeans.getData());
                                            } else {
                                                listBaseJson.setStatus(userinfobeans.isStatus());
                                                listBaseJson.setCode(userinfobeans.getCode());
                                                listBaseJson.setMessage(userinfobeans.getMessage());
                                            }
                                            return listBaseJson;
                                        }
                                    });
                        }
                        return Observable.just(listBaseJson);
                    }
                });
    }

    /**
     * 获取用户收到的最新消息
     *
     * @param time 零时区的秒级时间戳
     * @param key  查询关键字 默认查询全部 多个以逗号隔开 可选参数有 diggs comments follows
     * @return
     */
    @Override
    public Observable<BaseJson<List<FlushMessages>>> getMyFlushMessage(long time, String key) {
        return mUserInfoClient.getMyFlushMessages(time, key).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<FlushMessages>>, Observable<BaseJson<List<FlushMessages>>>>() {
                    @Override
                    public Observable<BaseJson<List<FlushMessages>>> call(final BaseJson<List<FlushMessages>> listBaseJson) {
                        if (listBaseJson.isStatus() && !listBaseJson.getData().isEmpty()) {
                            List<Object> userIdstmp = new ArrayList();
                            for (FlushMessages flushMessages : listBaseJson.getData()) {
                                userIdstmp.addAll(flushMessages.getUids());
                            }
                            if (userIdstmp.isEmpty()) {
                                return Observable.just(listBaseJson);
                            }
                            return getUserInfo(userIdstmp)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<FlushMessages>>>() {
                                        @Override
                                        public BaseJson<List<FlushMessages>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao().insertOrReplace(userinfobeans.getData());
                                            } else {
                                                listBaseJson.setStatus(userinfobeans.isStatus());
                                                listBaseJson.setCode(userinfobeans.getCode());
                                                listBaseJson.setMessage(userinfobeans.getMessage());
                                            }
                                            return listBaseJson;
                                        }
                                    });
                        }
                        return Observable.just(listBaseJson);
                    }
                });
    }

}
