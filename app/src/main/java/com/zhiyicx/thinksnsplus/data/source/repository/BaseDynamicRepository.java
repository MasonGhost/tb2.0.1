package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


/**
 * @Describe 动态数据处理基类
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public class BaseDynamicRepository implements IDynamicReppsitory {

    protected DynamicClient mDynamicClient;
    protected UserInfoRepository mUserInfoRepository;
    protected Context mContext;


    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;


    @Inject
    public BaseDynamicRepository(ServiceManager serviceManager, Context context) {
        mContext = context;
        mDynamicClient = serviceManager.getDynamicClient();
        mUserInfoRepository = new UserInfoRepository(serviceManager);
        mDynamicBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicBeanGreenDao();
        mDynamicDetailBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicDetailBeanGreenDao();
        mDynamicCommentBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicCommentBeanGreenDao();
        mDynamicToolBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicToolBeanGreenDao();

    }

    /**
     * publish dynamic
     *
     * @param dynamicDetailBean dynamic content
     * @return
     */
    @Override
    public Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        return mDynamicClient.sendDynamic(body);
    }

    /**
     * get dynamic list
     *
     * @param type   "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param max_id 用来翻页的记录id(对应数据体里的feed_id ,最新和关注选填)
     * @param page   页码 热门选填
     * @return
     */
    @Override
    public Observable<BaseJson<List<DynamicBean>>> getDynamicList(final String type, Long max_id, int page,final boolean isLoadMore) {
        return mDynamicClient.getDynamicList(type, max_id, null, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DynamicBean>>, Observable<BaseJson<List<DynamicBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DynamicBean>>> call(final BaseJson<List<DynamicBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && listBaseJson.getData() != null && !listBaseJson.getData().isEmpty()) {
                            final List<Long> user_ids = new ArrayList<>();
                            if (!isLoadMore&&type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {// 如果是热门，需要初始化时间
                                for (int i = listBaseJson.getData().size() - 1; i >= 0; i--) {
                                    listBaseJson.getData().get(i).setHot_creat_time(System.currentTimeMillis());
                                }
                            }
                            for (DynamicBean dynamicBean : listBaseJson.getData()) {
                                user_ids.add(dynamicBean.getUser_id());
                                // 把详情中的 feed_id 设置到 dynamicBean 中
                                dynamicBean.setFeed_id(dynamicBean.getFeed().getFeed_id());
                                // 把 feed_mark 设置到详情中去
                                dynamicBean.getFeed().setFeed_mark(dynamicBean.getFeed_mark());
                                dynamicBean.getTool().setFeed_mark(dynamicBean.getFeed_mark());
                                if (type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) { //如果是关注，需要初始化标记
                                    dynamicBean.setIsFollowed(true);
                                }
                                if (type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) { // 热门的 max_id 是通过 hot_creat_time 标识，最新与关注都是通过 feed_id 标识
                                    dynamicBean.setMaxId(dynamicBean.getHot_creat_time());
                                } else {
                                    dynamicBean.setMaxId(dynamicBean.getFeed().getFeed_id());
                                }
                                for (DynamicCommentBean dynamicCommentBean : dynamicBean.getComments()) {
                                    user_ids.add(dynamicCommentBean.getUser_id());
                                    user_ids.add(dynamicCommentBean.getReply_to_user_id());
                                    dynamicCommentBean.setFeed_mark(dynamicBean.getFeed_mark()); // 评论中增加 feed_mark \和用户标记
                                    dynamicCommentBean.setFeed_user_id(dynamicBean.getUser_id());
                                }
                            }

                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<DynamicBean>>>() {
                                        @Override
                                        public BaseJson<List<DynamicBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                for (DynamicBean dynamicBean : listBaseJson.getData()) {
                                                    dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get((int) dynamicBean.getUser_id()));
                                                    for (int i = 0; i < dynamicBean.getComments().size(); i++) {
                                                        dynamicBean.getComments().get(i).setCommentUser(userInfoBeanSparseArray.get((int) dynamicBean.getComments().get(i).getUser_id()));
                                                        if (dynamicBean.getComments().get(i).getReply_to_user_id() == 0) { // 如果 reply_user_id = 0 回复动态
                                                            UserInfoBean userInfoBean = new UserInfoBean();
                                                            userInfoBean.setUser_id(0L);
                                                            dynamicBean.getComments().get(i).setReplyUser(userInfoBean);
                                                        } else {
                                                            dynamicBean.getComments().get(i).setReplyUser(userInfoBeanSparseArray.get((int) dynamicBean.getComments().get(i).getReply_to_user_id()));
                                                        }
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
                        } else {
                            return Observable.just(listBaseJson);
                        }

                    }
                });
    }

    /**
     * @param feed_id
     * @return
     */
    @Override
    public void handleLike(boolean isLiked, final Long feed_id) {

        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        BackgroundRequestTaskBean backgroundRequestTaskBean;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("feed_id", feed_id);
                        // 后台处理
                        if (aBoolean) {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST, params);
                        } else {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
                        }
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_HANDLE_LIKE_FORMAT, feed_id));
                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void deleteComment(Long feed_id, Long comment_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", feed_id);
        params.put("comment_id", comment_id);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_DELETE_COMMENT, feed_id, comment_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void sendComment(String commentContent, Long feed_id, Long reply_to_user_id, Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", commentContent);
        params.put("reply_to_user_id", reply_to_user_id);
        params.put("comment_mark", comment_mark);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_SEND_COMMENT, feed_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void updateOrInsertDynamic(List<DynamicBean> dynamicBeens) {

        Observable.just(dynamicBeens)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<DynamicBean>>() {
                    @Override
                    public void call(List<DynamicBean> datas) {
                        List<DynamicDetailBean> dynamicDetailBeen = new ArrayList<>();
                        List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
                        List<DynamicToolBean> dynamicToolBeen = new ArrayList<>();
                        for (DynamicBean dynamicBeanTmp : datas) {
                            // 处理关注和热门数据
                            dealLocalTypeData(dynamicBeanTmp);
                            dynamicDetailBeen.add(dynamicBeanTmp.getFeed());
                            dynamicCommentBeen.addAll(dynamicBeanTmp.getComments());
                            dynamicToolBeen.add(dynamicBeanTmp.getTool());
                        }
                        mDynamicBeanGreenDao.insertOrReplace(datas);
                        mDynamicDetailBeanGreenDao.insertOrReplace(dynamicDetailBeen);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBeen);
                        mDynamicToolBeanGreenDao.insertOrReplace(dynamicToolBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void dealLocalTypeData(DynamicBean dynamicBeanTmp) {
        DynamicBean localDynamicBean = mDynamicBeanGreenDao.getDynamicByFeedMark(dynamicBeanTmp.getFeed_mark());
        if (localDynamicBean != null) {
            if ((dynamicBeanTmp.getHot_creat_time() == null || dynamicBeanTmp.getHot_creat_time() == 0) && localDynamicBean.getHot_creat_time() != null && localDynamicBean.getHot_creat_time() != 0) {
                dynamicBeanTmp.setHot_creat_time(localDynamicBean.getHot_creat_time());
            }
            if (localDynamicBean.getIsFollowed()) {
                dynamicBeanTmp.setIsFollowed(localDynamicBean.getIsFollowed());
            }
        }
    }

    /**
     * @param feed_id
     * @return
     */
    @Override
    public Observable<BaseJson<String>> cancleLikeDynamic(Long feed_id) {
        return mDynamicClient.cancleLikeDynamic(feed_id);
    }

    @Override
    public Observable<BaseJson<Object>> collectDynamic(Long feed_id) {
        return mDynamicClient.collectDynamic(feed_id);
    }

    @Override
    public Observable<BaseJson<Object>> cancleCollectDynamic(Long feed_id) {
        return mDynamicClient.cancleCollectDynamic(feed_id);
    }

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getDynamicDigList(Long feed_id, Long
            max_id) {
        return mDynamicClient.getDynamicDigList(feed_id, max_id)
                .flatMap(new Func1<BaseJson<List<DynamicDigListBean>>, Observable<BaseJson<List<FollowFansBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<FollowFansBean>>> call(BaseJson<List<DynamicDigListBean>> listBaseJson) {
                        final List<DynamicDigListBean> dynamicDigListBeanList = listBaseJson.getData();
                        // 获取点赞的用户id列表
                        // 服务器返回数据
                        if (listBaseJson.isStatus() && dynamicDigListBeanList != null && !dynamicDigListBeanList.isEmpty()) {
                            List<Long> targetUserIds = new ArrayList<Long>();
                            String userIdString = "";
                            for (int i = 0; i < dynamicDigListBeanList.size(); i++) {
                                DynamicDigListBean dynamicDigListBean = dynamicDigListBeanList.get(i);
                                targetUserIds.add(dynamicDigListBean.getUser_id());
                                if (i == 0) {
                                    userIdString = dynamicDigListBean.getUser_id() + "";
                                } else {
                                    userIdString += "," + dynamicDigListBean.getUser_id();
                                }
                            }
                            // 通过用户id列表请求用户信息和用户关注状态
                            return Observable.combineLatest(mUserInfoRepository.getUserFollowState(userIdString),
                                    mUserInfoRepository.getUserInfo(targetUserIds), new Func2<BaseJson<List<FollowFansBean>>
                                            , BaseJson<List<UserInfoBean>>, BaseJson<List<FollowFansBean>>>() {
                                        @Override
                                        public BaseJson<List<FollowFansBean>> call(BaseJson<List<FollowFansBean>> listBaseJson
                                                , BaseJson<List<UserInfoBean>> listBaseJson2) {

                                            List<UserInfoBean> userInfoList = listBaseJson2.getData();
                                            // 没有获取到用户信息，但依然显示列表信息，有这个必要吗
                                            if (listBaseJson2.isStatus() && userInfoList != null && !userInfoList.isEmpty()) {
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : listBaseJson2.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                List<FollowFansBean> followFansBeanList = listBaseJson.getData();
                                                if (listBaseJson.isStatus() && followFansBeanList != null && !followFansBeanList.isEmpty()) {
                                                    // 将用户信息封装到状态列表中
                                                    for (int i = 0; i < followFansBeanList.size(); i++) {
                                                        // 封装好每一个FollowFansBean对象，方便存入数据库
                                                        FollowFansBean followFansBean = followFansBeanList.get(i);
                                                        // 设置点赞列表的maxID到FollowFansBean中,上拉加载
                                                        followFansBean.setId(dynamicDigListBeanList.get(i).getFeed_digg_id());
                                                        // 设置目标用户信息
                                                        followFansBean.setTargetUserInfo(userInfoBeanSparseArray.get((int) followFansBean.getTargetUserId()));
                                                    }
                                                }
                                            }
                                            return listBaseJson;
                                        }
                                    });
                        }
                        // 返回期待以外的数据，比如状态为false，或者数据为空，发射空数据
                        BaseJson<List<FollowFansBean>> baseJsonUserInfoList = new BaseJson<List<FollowFansBean>>();
                        baseJsonUserInfoList.setData(new ArrayList<FollowFansBean>());
                        baseJsonUserInfoList.setStatus(listBaseJson.isStatus());
                        baseJsonUserInfoList.setMessage(listBaseJson.getMessage());
                        return Observable.just(baseJsonUserInfoList);
                    }
                });
    }

    @Override
    public Observable<BaseJson<List<DynamicCommentBean>>> getDynamicCommentList(
            final Long feed_mark, Long feed_id, Long max_id) {
        return mDynamicClient.getDynamicCommentList(feed_id, max_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DynamicCommentBean>>, Observable<BaseJson<List<DynamicCommentBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DynamicCommentBean>>> call(final BaseJson<List<DynamicCommentBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && listBaseJson.getData() != null && !listBaseJson.getData().isEmpty()) {
                            final List<Long> user_ids = new ArrayList<>();
                            for (DynamicCommentBean dynamicCommentBean : listBaseJson.getData()) {
                                user_ids.add(dynamicCommentBean.getUser_id());
                                user_ids.add(dynamicCommentBean.getReply_to_user_id());
                                dynamicCommentBean.setFeed_mark(feed_mark);
                            }
                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<DynamicCommentBean>>>() {
                                        @Override
                                        public BaseJson<List<DynamicCommentBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                for (int i = 0; i < listBaseJson.getData().size(); i++) {
                                                    listBaseJson.getData().get(i).setCommentUser(userInfoBeanSparseArray.get((int) listBaseJson.getData().get(i).getUser_id()));
                                                    if (listBaseJson.getData().get(i).getReply_to_user_id() == 0) { // 如果 reply_user_id = 0 回复动态
                                                        UserInfoBean userInfoBean = new UserInfoBean();
                                                        userInfoBean.setUser_id(0L);
                                                        listBaseJson.getData().get(i).setReplyUser(userInfoBean);
                                                    } else {
                                                        listBaseJson.getData().get(i).setReplyUser(userInfoBeanSparseArray.get((int) listBaseJson.getData().get(i).getReply_to_user_id()));
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
                        } else {
                            return Observable.just(listBaseJson);
                        }

                    }

                });
    }

}
