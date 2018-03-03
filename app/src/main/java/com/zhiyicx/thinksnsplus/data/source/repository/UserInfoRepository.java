package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.DefaultUserInfoConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.beans.request.BindAccountRequstBean;
import com.zhiyicx.thinksnsplus.data.beans.request.DeleteUserPhoneOrEmailRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.ThirdAccountBindRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.request.UpdateUserPhoneOrEmailRequestBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskRewardRuleBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IUserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.modules.tb.rank.RankData;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe 用户信息相关的model层实现
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */

public class UserInfoRepository implements IUserInfoRepository {
    public static final int DEFAULT_MAX_USER_GET_NUM_ONCE = 50;
    public static final int DEFAULT_MAX_USER_GET_BY_PHONE_NUM_ONCE = 100;

    private UserInfoClient mUserInfoClient;
    private FollowFansClient mFollowFansClient;
    private LoginClient mLoginClient;
    private RegisterClient mRegisterClient;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    AuthRepository mAuthRepository;
    @Inject
    Application mContext;
    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public UserInfoRepository(ServiceManager serviceManager) {
        mUserInfoClient = serviceManager.getUserInfoClient();
        mFollowFansClient = serviceManager.getFollowFansClient();
        mLoginClient = serviceManager.getLoginClient();
        mRegisterClient = serviceManager.getRegisterClient();
    }


    @Override
    public Observable<AuthBean> registerByPhone(String phone, String name, String vertifyCode, String password) {
        return mRegisterClient.register(phone, null, name, password, RegisterClient.REGITER_TYPE_SMS, vertifyCode, "personal")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(authBean -> {
                    mAuthRepository.saveAuthBean(authBean);
                    return getCurrentLoginUserInfo()
                            .map(userInfoBean -> {
                                authBean.setUser(userInfoBean);
                                authBean.setUser_id(userInfoBean.getUser_id());
                                return authBean;
                            });
                });
    }

    @Override
    public Observable<AuthBean> registerByEmail(String email, String name, String vertifyCode, String password) {
        return mRegisterClient.register(null, email, name, password, RegisterClient.REGITER_TYPE_EMAIL, vertifyCode, "personal")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(authBean -> {
                    mAuthRepository.saveAuthBean(authBean);
                    return getCurrentLoginUserInfo()
                            .map(userInfoBean -> {
                                authBean.setUser(userInfoBean);
                                authBean.setUser_id(userInfoBean.getUser_id());
                                return authBean;
                            });
                });
    }

    @Override
    public Observable<AuthBean> loginV2(final String account, final String password) {
       /* if(cacheImp==null){
            cacheImp = new CacheImp<>(new AuthBeanGreenDaoImpl(context));
        }
        return cacheImp.load(1483098241l, new NetWorkCache<AuthBean>() {
            @Override
            public Observable<BaseJson<AuthBean>> get(Long key) {
            }
        });*/
        return mLoginClient.loginV2(account, password);
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
            return Observable.zip(getUserBaseJsonObservable(user_ids.subList(0, DEFAULT_MAX_USER_GET_NUM_ONCE)), getUserBaseJsonObservable(user_ids
                    .subList(DEFAULT_MAX_USER_GET_NUM_ONCE, user_ids.size())), (listBaseJson, listBaseJson2) -> {
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
        // 添加默认删除用户的信息
        return getUserInfoByIds(userids)
                .observeOn(Schedulers.io())
                .map(users -> {
                    mUserInfoBeanGreenDao.insertOrReplace(users);
                    List<String> containerUsers = new ArrayList<>();
                    for (Object user_id : user_ids) {
                        try {
                            containerUsers.add(String.valueOf(user_id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    for (UserInfoBean user : users) {
                        try {
                            if (containerUsers.contains(String.valueOf(user.getUser_id()))) {
                                containerUsers.remove(String.valueOf(user.getUser_id()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    for (String userId : containerUsers) {
                        if (TextUtils.isEmpty(userId)) {
                            continue;
                        }
                        try {
                            users.add(DefaultUserInfoConfig.getDefaultDeletUserInfo(mContext, Long.parseLong(userId)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return users;
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
        if (currentUserinfoBean != null) {//如果请求的数据中有当前登录的用户信息，直接拿取本地用户信息
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

    /**
     * @param user_ids Get multiple designated users, multiple IDs using , split.
     * @param name     Used to retrieve users whose username contains name.
     * @param since    The integer ID of the last User that you've seen.
     * @param order    Sorting. Enum: asc, desc
     * @param limit    List user limit, minimum 1 max 50.
     * @return
     */
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

            return Observable.just(userInfoBean)
                    .observeOn(AndroidSchedulers.mainThread());
        }
        List<Object> user_ids = new ArrayList<>();
        user_ids.add(user_id);
        return getUserInfo(user_ids)
                .observeOn(Schedulers.io())
                .map(datas -> {
                    if (datas.isEmpty()) {
                        return null;
                    }
                    return datas.get(0);
                })
                .observeOn(AndroidSchedulers.mainThread());

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
            backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_FOLLOW_USER_FORMART, followFansBean.getUser_id
                    ()));
            followFansBean.getExtra().setFollowings_count(followFansBean.getExtra().getFollowings_count() + 1);

        } else {
            // 已关注，取消关注
            followFansBean.setFollower(false);
            EventBus.getDefault().post(followFansBean, EventBusTagConfig.EVENT_FOLLOW_AND_CANCEL_FOLLOW);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_CANCEL_FOLLOW_USER_FORMART, followFansBean
                    .getUser_id()));
            if (followFansBean.getExtra().getFollowings_count() > 0) {
                followFansBean.getExtra().setFollowings_count(followFansBean.getExtra().getFollowings_count() - 1);
            }

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
                .observeOn(Schedulers.io())
                .flatMap(data -> {
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
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
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
                .observeOn(Schedulers.io())
                .flatMap(data -> {
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
                                            commentedBean.setReplyUserInfo(userInfoBeanSparseArray.get(commentedBean.getReply_user()
                                                    .intValue()));
                                        }
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                }
                                return data;
                            });
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
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

    @Override
    public Observable<List<UserInfoBean>> getRecommendUserInfo() {
        return mUserInfoClient.getRecommendUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param phones 单次最多 100 条
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getUsersByPhone(ArrayList<String> phones) {
        if (phones == null || phones.isEmpty()) {
            return Observable.just(new ArrayList<>());
        }
        if (phones.size() > DEFAULT_MAX_USER_GET_BY_PHONE_NUM_ONCE) {
            List<Observable<List<UserInfoBean>>> requests = new ArrayList<>();
            int i = 0;
            int end;
            int size = phones.size();
            while (i < size) {
                if (i + DEFAULT_MAX_USER_GET_BY_PHONE_NUM_ONCE >= size) {
                    end = size;
                } else {
                    end = i + DEFAULT_MAX_USER_GET_BY_PHONE_NUM_ONCE;
                }
                requests.add(getListObservable(phones.subList(i, end)));
                i += DEFAULT_MAX_USER_GET_BY_PHONE_NUM_ONCE;
            }
            return Observable.zip(requests, args -> {
                List<UserInfoBean> data = new ArrayList<>();
                for (Object arg : args) {
                    data.addAll((Collection<? extends UserInfoBean>) arg);
                }
                return data;
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
                .observeOn(Schedulers.io())
                .flatMap(nearbyBeen -> {
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
                }).map(nearbyBeen -> {
                    List<NearbyBean> result = new ArrayList<>();
                    for (NearbyBean nearbyBean : nearbyBeen) {
                        if (nearbyBean.getUser() != null) {
                            result.add(nearbyBean);
                        }

                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
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

    /**
     * @return 认证信息
     */
    @Override
    public Observable<UserCertificationInfo> getCertificationInfo() {
        return mUserInfoClient.getUserCertificationInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 提交认证信息
     *
     * @param bean
     * @return
     */
    @Override
    public Observable<BaseJsonV2<Object>> sendCertification(SendCertificationBean bean) {
        List<ImageBean> photos = bean.getPicList();
        // 有图片需要上传时：先处理图片上传任务，成功后，获取任务id，发布动态
        // 先处理图片上传，图片上传成功后，在进行动态发布
        List<Observable<BaseJson<Integer>>> upLoadPics = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            ImageBean imageBean = photos.get(i);
            String filePath = imageBean.getImgUrl();
            int photoWidth = (int) imageBean.getWidth();
            int photoHeight = (int) imageBean.getHeight();
            String photoMimeType = imageBean.getImgMimeType();
            upLoadPics.add(mUpLoadRepository.upLoadSingleFileV2(filePath, photoMimeType, true, photoWidth, photoHeight));
        }
        return Observable.zip(upLoadPics, (FuncN<Object>) args -> {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                BaseJson<Integer> baseJson = (BaseJson<Integer>) args[i];
                if (baseJson.isStatus()) {
                    if (i == 0 && bean.getFiles() != null) {
                        bean.getFiles().clear();
                    }
                    bean.getFiles().add(baseJson.getData());
                } else {
                    throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                }
            }
            return integers;
        }).map(integers -> {
//            bean.setPicList(null);
            return bean;
        }).flatMap(new Func1<SendCertificationBean, Observable<BaseJsonV2<Object>>>() {
            @Override
            public Observable<BaseJsonV2<Object>> call(SendCertificationBean bean) {
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(bean));
                LogUtils.d("Cathy", new Gson().toJson(bean));
                if (bean.isUpdate()) {
                    return mUserInfoClient.updateUserCertificationInfo(body);
                } else {
                    return mUserInfoClient.sendUserCertificationInfo(body);
                }
            }
        });
    }


    @Override
    public Observable<List<UserInfoBean>> getFollowListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFollowsList(userId, maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .observeOn(Schedulers.io())
                .map(userInfoBeen -> {
                    // 保存用户信息
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                    return userInfoBeen;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserInfoBean>> getFansListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFansList(userId, maxId, TSListFragment.DEFAULT_PAGE_SIZE)
                .observeOn(Schedulers.io())
                .map(userInfoBeen -> {
                    // 保存用户信息
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                    return userInfoBeen;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<Object> followUser(long userId) {
        return mFollowFansClient.followUser(userId);
    }

    @Override
    public Observable<Object> cancleFollowUser(long userId) {
        return mFollowFansClient.cancelFollowUser(userId);
    }


    /*******************************************  TB  *********************************************/

    @Override
    public Observable<List<RankData>> getTBRank(Long limit, int size) {
        return mUserInfoClient.getTBRank(limit, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param limit limit	int	条目数
     * @param size  offset	int	翻页标示
     * @param type  type	string	默认: all, all-累计贡献排行 day-日贡献排行
     * @return
     */
    @Override
    public Observable<List<ContributionData>> getContributionRank(Long limit, int size, String type) {
        return mUserInfoClient.getContributionRank(limit, size, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 非必须, 快讯分享需传:feed
     */
    public enum SHARETYPEENUM {
        FEED("feed");

        public String value;

        SHARETYPEENUM(String value) {
            this.value = value;
        }
    }

    /**
     * 分享统计
     *
     * @param type
     * @param id
     * @return
     */
    @Override
    public Observable<BaseJsonV2> shareCount(String type, String id) {
        return mUserInfoClient.shareCount(type, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return 钱包历史记录
     */
    @Override
    public Observable<List<RechargeSuccessBean>> getBillList(int after, String action) {
        return dealRechargeList(mUserInfoClient.getRechargeSuccessList(TSListFragment.DEFAULT_PAGE_SIZE, after, action));
    }

    @Override
    public Observable<List<RechargeSuccessBean>> dealRechargeList(Observable<List<RechargeSuccessBean>> data) {
        return data.observeOn(Schedulers.io())
                .flatMap(rechargeListBeen -> {
                    final List<Object> user_ids = new ArrayList<>();
                    for (RechargeSuccessBean rechargeSuccessBean : rechargeListBeen) {
                        if (rechargeSuccessBean.getChannel().equals("user") || rechargeSuccessBean.getChannel().equals("system")) //
                        // @see{https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/wallet/charge.md}
                        {
                            user_ids.add(rechargeSuccessBean.getAccount());
                            try {
                                rechargeSuccessBean.setUser_id(Long.valueOf(rechargeSuccessBean.getAccount()));
                            } catch (Exception e) {
                            }
                        } else {
                            user_ids.add(rechargeSuccessBean.getUser_id());
                        }

                    }
                    return getUserInfo(user_ids).map(userinfobeans -> rechargeListBeen);
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * @return 获取任务信息
     */
    @Override
    public Observable<TBTaskContainerBean> getTaskInfo() {
        return mUserInfoClient.getTaskInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<TBTaskRewardRuleBean> getTaskRewardRule() {
        return mUserInfoClient.getTaskRewardRule()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
