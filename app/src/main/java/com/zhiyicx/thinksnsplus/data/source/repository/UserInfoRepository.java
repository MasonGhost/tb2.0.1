package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
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
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe 用户信息相关的model层实现
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public class UserInfoRepository implements UserInfoContract.Repository {
    public static final int DEFAULT_MAX_USER_GET_NUM_ONCE = 50;

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
    public Observable<Object> changeUserInfo(HashMap<String, Object> userInfos) {
        return mUserInfoClient.changeUserInfo(userInfos);
    }

    /**
     * 获取用户信息
     *
     * @param user_ids 用户 id 数组
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getUserInfo(List<Object> user_ids) {
        ConvertUtils.removeDuplicate(user_ids); // 去重
        if (user_ids.size() > DEFAULT_MAX_USER_GET_NUM_ONCE) {
            return Observable.zip(getUserBaseJsonObservable(user_ids.subList(0, DEFAULT_MAX_USER_GET_NUM_ONCE)), getUserBaseJsonObservable(user_ids.subList(DEFAULT_MAX_USER_GET_NUM_ONCE, user_ids.size())), (listBaseJson, listBaseJson2) -> {
                listBaseJson.addAll(listBaseJson2);

                return listBaseJson;
            });

        } else {
            return getUserBaseJsonObservable(user_ids);
        }

    }

    private Observable<List<UserInfoBean>> getUserBaseJsonObservable(List<Object> user_ids) {
        if (user_ids.isEmpty()) {
            return Observable.just(new ArrayList<>());
        }

        String userids = user_ids.toString();
        userids = userids.replace("[", "");
        userids = userids.replace("]", "");
        return getUserInfoByIds(userids);
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
    public Observable<List<UserInfoBean>> getUserInfoByIds(String user_ids) {
        return mUserInfoClient.getBatchSpecifiedUserInfo(user_ids, null, null, null, DEFAULT_MAX_USER_GET_NUM_ONCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserInfoBean>> searchUserInfo(String user_ids, String name, Integer since, String order, Integer limit) {
        return mUserInfoClient.getBatchSpecifiedUserInfo(user_ids, name, since, order, limit)
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
    public Observable<UserInfoBean> getLocalUserInfoBeforeNet(long user_id) {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(user_id);
        if (userInfoBean != null) {
            return Observable.just(userInfoBean);
        }
        List<Object> user_ids = new ArrayList<>();
        user_ids.add(user_id);
        return getUserInfo(user_ids)
                .map(datas -> {
                    if (datas.isEmpty()) {
                        return null;
                    }
                    return datas.get(0);
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
    public void handleFollow(UserInfoBean followFansBean) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        if (!followFansBean.isFollower()) {
            // 当前未关注，进行关注
            followFansBean.setFollower(true);
            EventBus.getDefault().post(followFansBean, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.PUT);
            backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_FOLLOW_USER_FORMART, followFansBean.getUser_id()));
            followFansBean.getExtra().setFollowings_count(followFansBean.getExtra().getFollowings_count() + 1);

        } else {
            // 已关注，取消关注
            followFansBean.setFollower(false);
            EventBus.getDefault().post(followFansBean, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_CANCEL_FOLLOW_USER_FORMART, followFansBean.getUser_id()));
            if (followFansBean.getExtra().getFollowings_count() > 0)
                followFansBean.getExtra().setFollowings_count(followFansBean.getExtra().getFollowings_count() - 1);

        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getUser_id() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).
                addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库,关注状态
        mUserInfoBeanGreenDao.insertOrReplace(followFansBean);
        // 更新动态关注状态
        mDynamicBeanGreenDao.updateFollowStateByUserId(followFansBean.getUser_id(), followFansBean.isFollower());

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
                                        if (!userinfobeans.isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                            for (UserInfoBean userInfoBean : userinfobeans) {
                                                userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                            }
                                            for (DigRankBean digRankBean : listBaseJson.getData()) {
                                                digRankBean.setDigUserInfo(userInfoBeanSparseArray.get(digRankBean.getUser_id().intValue()));
                                            }
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
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
                                    if (!userinfobeans.isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (DigedBean digedBean : data) {
                                            digedBean.setDigUserInfo(userInfoBeanSparseArray.get(digedBean.getUser_id().intValue()));
                                            digedBean.setDigedUserInfo(userInfoBeanSparseArray.get(digedBean.getTarget_user().intValue()));
                                        }
                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                    }
                                    Collections.sort(data, (o1, o2) -> (int) (o2.getId() - o1.getId()));
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
                        if (data.isEmpty()) {
                            return Observable.just(data);
                        }
                        List<Object> userIds = new ArrayList();
                        for (CommentedBean commentedBean : data) {
                            userIds.add(commentedBean.getUser_id());
                            userIds.add(commentedBean.getTarget_user());
                            userIds.add(commentedBean.getReply_user());
                        }
                        return getUserInfo(userIds)
                                .map(userinfobeans -> {
                                    if (!userinfobeans.isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans) {
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
                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
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
                                        if (!userinfobeans.isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                        }
                                        return listBaseJson;
                                    });
                        }
                        return Observable.just(listBaseJson);
                    }
                });
    }

    /*******************************************  标签  *********************************************/


    @Override
    public Observable<List<UserTagBean>> getUserTags(long user_id) {
        return mUserInfoClient.getUserTags(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserTagBean>> getCurrentUserTags() {
        return mUserInfoClient.getCurrentUserTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> addTag(long tag_id) {
        return mUserInfoClient.addTag(tag_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> deleteTag(long tag_id) {
        return mUserInfoClient.deleteTag(tag_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*******************************************  找人  *********************************************/
    /**
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getHotUsers(Integer limit, Integer offset) {
        return mUserInfoClient.getHotUsers(limit,offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getNewUsers(Integer limit, Integer offset) {
        return mUserInfoClient.getNewUsers(limit,offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getUsersRecommentByTag(Integer limit, Integer offset) {
        return mUserInfoClient.getUsersRecommentByTag(limit,offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     *
     * @param phones
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getUsersByPhone(ArrayList<Integer> phones) {
        return mUserInfoClient.getUsersByPhone(phones)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
