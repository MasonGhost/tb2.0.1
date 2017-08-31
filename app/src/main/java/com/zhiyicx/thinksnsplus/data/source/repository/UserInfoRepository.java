package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.request.BindAccountRequstBean;
import com.zhiyicx.thinksnsplus.data.beans.request.DeleteUserPhoneOrEmailRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.ThirdAccountBindRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.UpdateUserPhoneOrEmailRequestBean;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
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
                .observeOn(AndroidSchedulers.mainThread())
                .map(userInfoBean -> {
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                    return userInfoBean;
                });
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
        String currentUserId = String.valueOf(AppApplication.getMyUserIdWithdefault());
        UserInfoBean currentUserinfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (currentUserinfoBean != null) {
            String[] users = user_ids.split(ConstantConfig.SPLIT_SMBOL);
            boolean hasCurrentUser = false;
            StringBuilder stringBuilder = new StringBuilder();
            for (String user : users) {
                if (currentUserId.equals(user)) {
                    hasCurrentUser = true;
                } else {
                    stringBuilder.append(user);
                    stringBuilder.append(ConstantConfig.SPLIT_SMBOL);
                }
            }
            if (stringBuilder.length() > 1) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            user_ids = stringBuilder.toString();

            if (hasCurrentUser) {
                if (TextUtils.isEmpty(user_ids)) {
                    ArrayList<UserInfoBean> datas = new ArrayList<>();
                    datas.add(currentUserinfoBean);
                    return Observable.just(datas);

                }
                return getBatchSpecifiedUserInfo(user_ids)
                        .map(userInfoBeen -> {
                            userInfoBeen.add(currentUserinfoBean);
                            return userInfoBeen;
                        });
            } else {
                return getBatchSpecifiedUserInfo(user_ids);
            }
        } else {
            return getBatchSpecifiedUserInfo(user_ids);
        }
    }

    private Observable<List<UserInfoBean>> getBatchSpecifiedUserInfo(String user_ids) {
        return mUserInfoClient.getBatchSpecifiedUserInfo(user_ids, null, null, null, DEFAULT_MAX_USER_GET_NUM_ONCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserInfoBean>> searchUserInfo(String user_ids, String name, Integer since, String order, Integer limit) {
        return mUserInfoClient.searchUserinfoWithRecommend(limit, since, name)
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
                            digedBean.initDelet();
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
                            commentedBean.initDelet();
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
     * @param phone
     * @param email
     * @param verifiable_code
     * @return
     */
    @Override
    public Observable<Object> updatePhoneOrEmail(String phone, String email, String verifiable_code) {
        return mUserInfoClient.updatePhoneOrEmail(new UpdateUserPhoneOrEmailRequestBean(phone, email, verifiable_code))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param password
     * @param verify_code
     * @return
     */
    @Override
    public Observable<Object> deletePhone(String password, String verify_code) {
        return mUserInfoClient.deletePhone(new DeleteUserPhoneOrEmailRequestBean(password, verify_code))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param password
     * @param verify_code
     * @return
     */
    @Override
    public Observable<Object> deleteEmail(String password, String verify_code) {
        return mUserInfoClient.deleteEmail(new DeleteUserPhoneOrEmailRequestBean(password, verify_code))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getHotUsers(Integer limit, Integer offset) {
        return mUserInfoClient.getHotUsers(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getNewUsers(Integer limit, Integer offset) {
        return mUserInfoClient.getNewUsers(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getUsersRecommentByTag(Integer limit, Integer offset) {
        return mUserInfoClient.getUsersRecommentByTag(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * @param phones 单次最多 100 条
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getUsersByPhone(ArrayList<String> phones) {

        if (phones.size() > 100) {
            return Observable.zip(getListObservable(phones.subList(0, 100)), getListObservable(phones.subList(100, phones.size()))
                    , (userInfoBeen, userInfoBeen2) -> {
                        userInfoBeen.addAll(userInfoBeen2);
                        return userInfoBeen;
                    });

        } else {
            return getListObservable(phones);
        }
    }

    /**
     * @param phones
     * @return
     */
    private Observable<List<UserInfoBean>> getListObservable(List<String> phones) {
        Map<String, List<String>> phonesMap = new HashMap<>();
        phonesMap.put("phones", phones);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(phonesMap));

        return mUserInfoClient.getUsersByPhone(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    @Override
    public Observable<Object> updateUserLocation(double longitude, double latitude) {
        return mUserInfoClient.updateUserLocation(longitude, latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param longitude 当前用户所在位置的纬度
     * @param latitude  当前用户所在位置的经度
     * @param radius    搜索范围，米为单位 [0 - 50000], 默认3000
     * @param limit     默认20， 最大100
     * @param page      分页参数， 默认1，当返回数据小于limit， page达到最大值
     * @return
     */
    @Override
    public Observable<List<NearbyBean>> getNearbyData(double longitude, double latitude, Integer radius, Integer limit, Integer page) {
        return mUserInfoClient.getNearbyData(longitude, latitude, radius, limit, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<NearbyBean>, Observable<List<NearbyBean>>>() {
                    @Override
                    public Observable<List<NearbyBean>> call(List<NearbyBean> nearbyBeen) {
                        List<Object> userIds = new ArrayList();
                        for (NearbyBean nearbyBean : nearbyBeen) {
                            userIds.add(nearbyBean.getUser_id());
                        }
                        return getUserInfo(userIds)
                                .map(userinfobeans -> {
                                    if (!userinfobeans.isEmpty()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (NearbyBean nearbyBean : nearbyBeen) {
                                            try {
                                                if (userInfoBeanSparseArray.get(Integer.parseInt(nearbyBean.getUser_id())) != null) {
                                                    nearbyBean.setUser(userInfoBeanSparseArray.get(Integer.parseInt(nearbyBean.getUser_id())));
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                    }
                                    return nearbyBeen;
                                });
                    }
                }).map(nearbyBeen -> {
                    List<NearbyBean> result = new ArrayList<>();
                    for (NearbyBean nearbyBean : nearbyBeen) {
                        if (nearbyBean.getUser() != null) {
                            result.add(nearbyBean);
                        }

                    }
                    return result;
                });
    }

    /*******************************************  签到  *********************************************/

    /**
     * @return
     */
    @Override
    public Observable<CheckInBean> getCheckInInfo() {
        return mUserInfoClient.getCheckInInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * @return
     */
    @Override
    public Observable<Object> checkIn() {
        return mUserInfoClient.checkIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param offset 数据偏移数，默认为 0。
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getCheckInRanks(Integer offset) {
        return mUserInfoClient.getCheckInRanks(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*******************************************  三方  *********************************************/
    /**
     * 获取已经绑定的三方
     *
     * @return
     */
    @Override
    public Observable<List<String>> getBindThirds() {
        return mUserInfoClient.getBindThirds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 检查绑定并获取用户授权
     *
     * @param provider
     * @param access_token thrid token
     * @return
     */
    @Override
    public Observable<AuthBean> checkThridIsRegitser(String provider, String access_token) {
        return mUserInfoClient.checkThridIsRegitser(provider, access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 检查注册信息或者注册用户
     *
     * @param provider     type qq\weibo\wechat
     * @param access_token 获取的 Provider Access Token。
     * @param name         用户名。
     * @param check        如果是 null 、 false 或 0 则不会进入检查，如果 存在任何转为 bool 为 真 的值，则表示检查注册信息。
     * @return
     */
    @Override
    public Observable<AuthBean> checkUserOrRegisterUser(String provider, String access_token, String name, Boolean check) {
        return mUserInfoClient.checkUserOrRegisterUser(provider, new ThridInfoBean(provider, access_token, name, check))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 已登录账号绑定
     *
     * @param provider
     * @param access_token
     * @return
     */
    @Override
    public Observable<Object> bindWithLogin(String provider, String access_token) {
        return mUserInfoClient.bindWithLogin(provider, new ThirdAccountBindRequestBean(access_token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 输入账号密码绑定
     *
     * @param provider     type qq\weibo\wechat
     * @param access_token 获取的 Provider Access Token。
     * @param login        用户登录名，手机，邮箱
     * @param password     用户密码。
     * @return
     */
    @Override
    public Observable<AuthBean> bindWithInput(String provider, String access_token, String login, String password) {
        return mUserInfoClient.bindWithInput(provider, new BindAccountRequstBean(access_token, login, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消绑定
     *
     * @param provider type qq\weibo\wechat
     * @return
     */
    @Override
    public Observable<Object> cancelBind(String provider) {
        return mUserInfoClient.cancelBind(provider)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
