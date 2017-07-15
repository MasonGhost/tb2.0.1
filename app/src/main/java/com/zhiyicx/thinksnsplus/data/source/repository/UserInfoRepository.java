package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
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

import rx.Observable;
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

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    Application mContext;

    @Inject
    public UserInfoRepository(ServiceManager serviceManager, Application application) {
        mUserInfoClient = serviceManager.getUserInfoClient();
    }

    /**
     * 获取 地区列表
     *
     * @return
     */
    @Override
    public Observable<ArrayList<AreaBean>> getAreaList() {
        return Observable.create((Observable.OnSubscribe<ArrayList<AreaBean>>) subscriber -> {
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
        return getBatchSpecifiedUserInfo(userids)
                .subscribeOn(Schedulers.io())
                .map(userInfoBeen -> {
                    BaseJson<List<UserInfoBean>> baseJson = new BaseJson<>();
                    baseJson.setStatus(true);
                    baseJson.setData(userInfoBeen);
                    return baseJson;
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 获取当前登录用户信息 V2
     *
     * @return
     */
    @Override
    public Observable<UserInfoBean> getCurrentLoginUserInfo() {
        return mUserInfoClient.getCurrentLoginUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取指定用户信息  其中 following、follower 是可选参数，null 代表不传，验证用户我是否关注以及是否关注我的用户 id ，默认为当前登陆用户。V2
     *
     * @param userId          the specified user id
     * @param followingUserId following user id
     * @param followerUserId  follow user id
     * @return
     */
    @Override
    public Observable<UserInfoBean> getSpecifiedUserInfo(long userId, Long followingUserId, Long followerUserId) {
        return mUserInfoClient.getSpecifiedUserInfo(userId, followingUserId, followerUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 批量获取指定用户的用户信息 V2
     *
     * @param user_ids user 可以是一个值，或者多个值，多个值的时候用英文半角 , 分割。
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getBatchSpecifiedUserInfo(String user_ids) {
        return mUserInfoClient.getBatchSpecifiedUserInfo(user_ids)
                .subscribeOn(Schedulers.io())
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

        return mUserInfoClient.getBatchSpecifiedUserInfo(userids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(listBaseJson -> {
                    beanBaseJson.setStatus(true);
                    beanBaseJson.setData(listBaseJson.get(0));
                    return beanBaseJson;
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
                .map(listBaseJson -> {
                    if (listBaseJson.isStatus()) {
                        for (FollowFansBean followFansBean : listBaseJson.getData()) {
                            followFansBean.setOriginUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
                            followFansBean.setOrigintargetUser("");
                        }
                    }
                    return listBaseJson;
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
                                    .map(userinfobeans -> {
                                        if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                            for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                            }
                                            for (DigRankBean digRankBean : listBaseJson.getData()) {
                                                digRankBean.setDigUserInfo(userInfoBeanSparseArray.get(digRankBean.getUser_id().intValue()));
                                            }
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                        } else {
                                            listBaseJson.setStatus(userinfobeans.isStatus());
                                            listBaseJson.setCode(userinfobeans.getCode());
                                            listBaseJson.setMessage(userinfobeans.getMessage());
                                        }
                                        return listBaseJson;
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
    public Observable<List<DigedBean>> getMyDiggs(int max_id) {
        return mUserInfoClient.getMyDiggs(max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<DigedBean>, Observable<List<DigedBean>>>() {
                    @Override
                    public Observable<List<DigedBean>> call(final List<DigedBean> data) {
                        List<Object> userIds = new ArrayList();
                        for (DigedBean digedBean : data) {
                            userIds.add(digedBean.getUser_id());
                            userIds.add(digedBean.getTarget_user());
                        }
                        return getUserInfo(userIds)
                                .map(userinfobeans -> {
                                    if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (DigedBean digedBean : data) {
                                            digedBean.setDigUserInfo(userInfoBeanSparseArray.get(digedBean.getUser_id().intValue()));
                                            digedBean.setDigedUserInfo(userInfoBeanSparseArray.get(digedBean.getTarget_user().intValue()));
                                        }
                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                    }
                                    return data;
                                });
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
    public Observable<List<CommentedBean>> getMyComments(int max_id) {
        return mUserInfoClient.getMyComments(max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<CommentedBean>, Observable<List<CommentedBean>>>() {
                    @Override
                    public Observable<List<CommentedBean>> call(final List<CommentedBean> data) {
                        List<Object> userIds = new ArrayList();
                        for (CommentedBean commentedBean : data) {
                            userIds.add(commentedBean.getUser_id());
                            userIds.add(commentedBean.getTarget_user());
                            userIds.add(commentedBean.getReply_user());
                        }
                        return getUserInfo(userIds)
                                .map(userinfobeans -> {
                                    if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (CommentedBean commentedBean : data) {
                                            commentedBean.setCommentUserInfo(userInfoBeanSparseArray.get(commentedBean.getUser_id().intValue()));
                                            commentedBean.setSourceUserInfo(userInfoBeanSparseArray.get(commentedBean.getTarget_user().intValue()));
                                            if (commentedBean.getReply_user() == 0) { // 用于占位
                                                UserInfoBean userinfo = new UserInfoBean();
                                                userinfo.setUser_id(0L);
                                                commentedBean.setReplyUserInfo(userinfo);
                                            } else {
                                                commentedBean.setReplyUserInfo(userInfoBeanSparseArray.get((int) commentedBean.getReply_user().intValue()));
                                            }
                                        }
                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                    }
                                    return data;
                                });
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
                                    .map(userinfobeans -> {
                                        if (userinfobeans.isStatus() && !userinfobeans.getData().isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                        } else {
                                            listBaseJson.setStatus(userinfobeans.isStatus());
                                            listBaseJson.setCode(userinfobeans.getCode());
                                            listBaseJson.setMessage(userinfobeans.getMessage());
                                        }
                                        return listBaseJson;
                                    });
                        }
                        return Observable.just(listBaseJson);
                    }
                });
    }

}
