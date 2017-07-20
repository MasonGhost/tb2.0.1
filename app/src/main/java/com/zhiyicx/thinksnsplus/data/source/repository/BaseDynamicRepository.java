package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.TopDynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.TimeStringSortClass;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.DYNAMIC_TYPE_MY_COLLECTION;
import static com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean.TYPE_HOT;
import static com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean.TYPE_NEW;


/**
 * @Describe 动态数据处理基类
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public class BaseDynamicRepository implements IDynamicReppsitory {

    protected DynamicClient mDynamicClient;

    @Inject
    protected UserInfoRepository mUserInfoRepository;
    @Inject
    protected Application mContext;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    TopDynamicBeanGreenDaoImpl mTopDynamicBeanGreenDao;

    @Inject
    public BaseDynamicRepository(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
    }

    /**
     * publish dynamic
     *
     * @param dynamicDetailBean dynamic content
     * @return
     */
    @Override
    public Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean, int dynamicBelong, long channel_id) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        switch (dynamicBelong) {
            case SendDynamicDataBean.MORMAL_DYNAMIC:
                return mDynamicClient.sendDynamic(body);
            case SendDynamicDataBean.CHANNEL_DYNAMIC:
                return mDynamicClient.sendDynamicToChannel(channel_id, body);
            default:
                return mDynamicClient.sendDynamic(body);
        }
    }

    @Override
    public Observable<BaseJsonV2<Object>> sendDynamicV2(SendDynamicDataBeanV2 dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        return mDynamicClient.sendDynamicV2(body);
    }

    @Override
    public Observable<BaseJsonV2<Object>> sendGroupDynamic(GroupSendDynamicDataBean dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        return mDynamicClient.sendGroupDynamic(dynamicDetailBean.getGroup_id(), body);
    }

    /**
     * get dynamic list
     *
     * @param type       "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param max_id     用来翻页的记录id(对应数据体里的feed_id ,最新和关注选填)
     * @param page       页码 热门选填
     * @param feeds_id
     * @param isLoadMore 是否是刷新
     * @return
     */
    @Override
    public Observable<BaseJson<List<DynamicBean>>> getDynamicList(final String type, Long max_id, int page, String feeds_id, final boolean isLoadMore) {
        feeds_id = feeds_id.replace("[", "");
        feeds_id = feeds_id.replace("]", "");
        return dealWithDynamicList(mDynamicClient.getDynamicList(type, max_id,
                Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page, feeds_id), type, isLoadMore);
    }

    @Override
    public Observable<List<DynamicDetailBeanV2>> getDynamicListV2(String type, Long after, Long user_id, final boolean isLoadMore) {
        Observable<DynamicBeanV2> observable;
        if (type.equals(DYNAMIC_TYPE_MY_COLLECTION)) {// 收藏的动态地址和返回大不一样，真滴难受
            observable = mDynamicClient.getCollectDynamicListV2(after, user_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE))
                    .flatMap(new Func1<List<DynamicDetailBeanV2>, Observable<DynamicBeanV2>>() {
                        @Override
                        public Observable<DynamicBeanV2> call(List<DynamicDetailBeanV2> detailBeanV2) {
                            DynamicBeanV2 data = new DynamicBeanV2();
                            data.setFeeds(detailBeanV2);
                            return Observable.just(data);
                        }
                    });
        } else {
            observable = mDynamicClient.getDynamicListV2(type, after, user_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE));
        }
        return dealWithDynamicListV2(observable, type, isLoadMore);
    }

    /**
     * 处理喜欢操作
     *
     * @param feed_id
     * @return
     */
    @Override
    public void handleLike(boolean isLiked, final Long feed_id) {

        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("feed_id", feed_id);
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_CLICK_LIKE_FORMAT_V2, feed_id));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_CANCEL_CLICK_LIKE_FORMAT_V2, feed_id));
                    }

                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    /**
     * 删除动态
     *
     * @param feed_id
     */
    @Override
    public void deleteDynamic(Long feed_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", feed_id);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DELETE_DYNAMIC, feed_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteCommentV2(Long feed_id, Long comment_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", feed_id);
        params.put("comment_id", comment_id);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_DELETE_COMMENT_V2, feed_id, comment_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    /**
     * 删除评论
     *
     * @param feed_id
     * @param comment_id
     */
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
    public void sendCommentV2(String commentContent, Long feed_id, Long reply_to_user_id, Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", commentContent);
        params.put("comment_mark", comment_mark);
        if (reply_to_user_id > 0) {
            params.put("reply_to_user_id", reply_to_user_id);
        }

        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_SEND_COMMENT_V2, feed_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public Observable<DynamicCommentToll> setDynamicCommentToll(Long feed_id, int amout) {
        return mDynamicClient.setDynamicCommentToll(feed_id, amout)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void updateOrInsertDynamic(List<DynamicBean> dynamicBeens, final String type) {

        Observable.just(dynamicBeens)
                .observeOn(Schedulers.io())
                .subscribe(datas -> {
                    mDynamicBeanGreenDao.deleteDynamicByType(type); // 清除旧数据
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
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void updateOrInsertDynamicV2(List<DynamicDetailBeanV2> dynamicBeens, final String type) {
        Observable.just(dynamicBeens)
                .observeOn(Schedulers.io())
                .subscribe(datas -> {
                    mDynamicDetailBeanV2GreenDao.deleteDynamicByType(type); // 清除旧数据
                    List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
                    List<DynamicDetailBeanV2> result = new ArrayList<>();

                    for (DynamicDetailBeanV2 dynamicBeanTmp : datas) {
                        // 处理关注和热门数据
                        if (dynamicBeanTmp.getFeed_mark() != null && dynamicBeanTmp.getFeed_mark() != 0) {
                            dealLocalTypeDataV2(dynamicBeanTmp);
                            dynamicCommentBeen.addAll(dynamicBeanTmp.getComments());
                            result.add(dynamicBeanTmp);
                        }
                    }
                    mDynamicDetailBeanV2GreenDao.insertOrReplace(result);
                    mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBeen);
                }, throwable -> throwable.printStackTrace());
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

    private void dealLocalTypeDataV2(DynamicDetailBeanV2 dynamicBeanTmp) {
        DynamicDetailBeanV2 localDynamicBean = mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(dynamicBeanTmp.getFeed_mark());
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
        return mDynamicClient.getDynamicDigList(feed_id, max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .flatMap(new Func1<BaseJson<List<DynamicDigListBean>>, Observable<BaseJson<List<FollowFansBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<FollowFansBean>>> call(BaseJson<List<DynamicDigListBean>> listBaseJson) {
                        final List<DynamicDigListBean> dynamicDigListBeanList = listBaseJson.getData();
                        // 获取点赞的用户id列表
                        // 服务器返回数据
                        if (listBaseJson.isStatus() && dynamicDigListBeanList != null && !dynamicDigListBeanList.isEmpty()) {
                            List<Object> targetUserIds = new ArrayList<>();
                            String userIdString = "";
                            for (int i = 0; i < dynamicDigListBeanList.size(); i++) {
                                DynamicDigListBean dynamicDigListBean = dynamicDigListBeanList.get(i);
                                targetUserIds.add(dynamicDigListBean.getUser_id());
                                if (i == 0) {
                                    userIdString = dynamicDigListBean.getUser_id() + "";
                                } else {
                                    userIdString += ConstantConfig.SPLIT_SMBOL + dynamicDigListBean.getUser_id();
                                }
                            }
                            // 通过用户id列表请求用户信息和用户关注状态
                            return Observable.combineLatest(mUserInfoRepository.getUserFollowState(userIdString),
                                    mUserInfoRepository.getUserInfo(targetUserIds), (listBaseJson1, listBaseJson2) -> {

                                        List<UserInfoBean> userInfoList = listBaseJson2.getData();
                                        // 没有获取到用户信息，但依然显示列表信息，有这个必要吗
                                        if (listBaseJson2.isStatus() && userInfoList != null && !userInfoList.isEmpty()) {
                                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                            for (UserInfoBean userInfoBean : listBaseJson2.getData()) {
                                                userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                            }
                                            List<FollowFansBean> followFansBeanList = listBaseJson1.getData();
                                            if (listBaseJson1.isStatus() && followFansBeanList != null && !followFansBeanList.isEmpty()) {
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
                                        return listBaseJson1;
                                    });
                        }
                        // 返回期待以外的数据，比如状态为false，或者数据为空，发射空数据
                        BaseJson<List<FollowFansBean>> baseJsonUserInfoList = new BaseJson<>();
                        baseJsonUserInfoList.setData(new ArrayList<>());
                        baseJsonUserInfoList.setStatus(listBaseJson.isStatus());
                        baseJsonUserInfoList.setMessage(listBaseJson.getMessage());
                        return Observable.just(baseJsonUserInfoList);
                    }
                });
    }

    @Override
    public Observable<List<FollowFansBean>> getDynamicDigListV2(Long feed_id, Long
            max_id) {
        return mDynamicClient.getDynamicDigListV2(feed_id, max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .flatMap(new Func1<List<DynamicDigListBean>, Observable<List<FollowFansBean>>>() {
                    @Override
                    public Observable<List<FollowFansBean>> call(List<DynamicDigListBean> dynamicDigListBeanList) {
                        // 获取点赞的用户id列表
                        // 服务器返回数据
                        if (dynamicDigListBeanList != null && !dynamicDigListBeanList.isEmpty()) {
                            List<Object> targetUserIds = new ArrayList<>();
                            String userIdString = "";
                            for (int i = 0; i < dynamicDigListBeanList.size(); i++) {
                                DynamicDigListBean dynamicDigListBean = dynamicDigListBeanList.get(i);
                                targetUserIds.add(dynamicDigListBean.getUser_id());
                                if (i == 0) {
                                    userIdString = dynamicDigListBean.getUser_id() + "";
                                } else {
                                    userIdString += ConstantConfig.SPLIT_SMBOL + dynamicDigListBean.getUser_id();
                                }
                            }
                            // 通过用户id列表请求用户信息和用户关注状态
                            return Observable.combineLatest(mUserInfoRepository.getUserFollowState(userIdString),
                                    mUserInfoRepository.getUserInfo(targetUserIds), (listBaseJson, listBaseJson2) -> {

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
                                        return listBaseJson.getData();
                                    });
                        } else {
                            // 返回期待以外的数据，比如状态为false，或者数据为空，发射空数据
                            return Observable.just(new ArrayList<>());
                        }
                    }
                });
    }

    /**
     * V2
     *
     * @param feed_mark dyanmic feed mark
     * @param feed_id   dyanmic detail id
     * @param after     max_id
     * @return
     */
    @Override
    public Observable<List<DynamicCommentBean>> getDynamicCommentListV2(
            final Long feed_mark, Long feed_id, Long after) {
        return mDynamicClient.getDynamicCommentListV2(feed_id, after, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<DynamicCommentBeanV2, Observable<List<DynamicCommentBean>>>() {
                    @Override
                    public Observable<List<DynamicCommentBean>> call(final DynamicCommentBeanV2 listBaseJson) {

                        final List<Object> user_ids = new ArrayList<>();
                        if (listBaseJson.getComments() != null && listBaseJson.getComments().size() > 1) {
                            Collections.sort(listBaseJson.getComments(), new TimeStringSortClass());
                        }
                        listBaseJson.getPinned().addAll(listBaseJson.getComments());
                        for (DynamicCommentBean dynamicCommentBean : listBaseJson.getPinned()) {
                            user_ids.add(dynamicCommentBean.getUser_id());
                            user_ids.add(dynamicCommentBean.getReply_to_user_id());
                            dynamicCommentBean.setFeed_mark(feed_mark);
                        }
                        if (user_ids.isEmpty()) {
                            return Observable.just(listBaseJson.getPinned());
                        }
                        return mUserInfoRepository.getUserInfo(user_ids)
                                .map(userinfobeans -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (int i = 0; i < listBaseJson.getPinned().size(); i++) {
                                        listBaseJson.getPinned().get(i).setCommentUser(userInfoBeanSparseArray.get((int) listBaseJson.getPinned().get(i).getUser_id()));
                                        if (listBaseJson.getPinned().get(i).getReply_to_user_id() == 0) { // 如果 reply_user_id = 0 回复动态
                                            UserInfoBean userInfoBean = new UserInfoBean();
                                            userInfoBean.setUser_id(0L);
                                            listBaseJson.getPinned().get(i).setReplyUser(userInfoBean);
                                        } else {
                                            listBaseJson.getPinned().get(i).setReplyUser(userInfoBeanSparseArray.get((int) listBaseJson.getPinned().get(i).getReply_to_user_id()));
                                        }
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());

                                    return listBaseJson.getPinned();
                                });

                    }
                });
    }


    /**
     * @param comment_ids 评论id 以逗号隔开或者数组形式传入
     * @return
     */
    @Override
    public Observable<BaseJson<List<DynamicCommentBean>>> getDynamicCommentListByCommentIds(
            String comment_ids) {
        comment_ids = comment_ids.replace("[", "");
        comment_ids = comment_ids.replace("]", "");
        return mDynamicClient.getDynamicCommentListByCommentsId(comment_ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DynamicCommentBean>>, Observable<BaseJson<List<DynamicCommentBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DynamicCommentBean>>> call(final BaseJson<List<DynamicCommentBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && listBaseJson.getData() != null && !listBaseJson.getData().isEmpty()) {
                            final List<Object> user_ids = new ArrayList<>();
                            for (DynamicCommentBean dynamicCommentBean : listBaseJson.getData()) {
                                user_ids.add(dynamicCommentBean.getUser_id());
                                user_ids.add(dynamicCommentBean.getReply_to_user_id());
//                                dynamicCommentBean.setFeed_mark(feed_mark);
                            }
                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(userinfobeans -> {
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
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                        } else {
                                            listBaseJson.setStatus(userinfobeans.isStatus());
                                            listBaseJson.setCode(userinfobeans.getCode());
                                            listBaseJson.setMessage(userinfobeans.getMessage());
                                        }
                                        return listBaseJson;
                                    });
                        } else {
                            return Observable.just(listBaseJson);
                        }

                    }

                });
    }

    /**
     * 增加动态浏览量
     *
     * @param feed_id 动态的唯一 id
     * @return
     */
    @Override
    public void handleDynamicViewCount(Long feed_id) {
        mDynamicClient.handleDynamicViewCount(feed_id)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        LogUtils.d(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, "handleDynamicViewCount");
                    }
                });
    }

    /**
     * 获取动态详情 V2
     *
     * @param feed_id 动态id
     * @return
     */
    @Override
    public Observable<DynamicDetailBeanV2> getDynamicDetailBeanV2(Long feed_id) {
        return dealWithDynamic(mDynamicClient.getDynamicDetailBeanV2(feed_id));
    }

    protected Observable<BaseJson<List<DynamicBean>>> dealWithDynamicList(Observable<BaseJson<List<DynamicBean>>> observable,
                                                                          final String type, final boolean isLoadMore) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DynamicBean>>, Observable<BaseJson<List<DynamicBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DynamicBean>>> call(final BaseJson<List<DynamicBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && listBaseJson.getData() != null && !listBaseJson.getData().isEmpty()) {
                            final List<Object> user_ids = new ArrayList<>();
                            if (!isLoadMore && type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {// 如果是热门，需要初始化时间
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
                                mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(dynamicBean.getFeed_mark());// 删除本条动态的本地评论
                            }

                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(userinfobeans -> {
                                        if (userinfobeans.isStatus()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                            for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                            }
                                            for (DynamicBean dynamicBean : listBaseJson.getData()) {
                                                dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get((int) dynamicBean.getUser_id()));
                                                for (int i = 0; i < dynamicBean.getComments().size(); i++) {
                                                    UserInfoBean tmpUserinfo = userInfoBeanSparseArray.get((int) dynamicBean.getComments().get(i).getUser_id());
                                                    if (tmpUserinfo != null) {
                                                        dynamicBean.getComments().get(i).setCommentUser(tmpUserinfo);
                                                    }
                                                    if (dynamicBean.getComments().get(i).getReply_to_user_id() == 0) { // 如果 reply_user_id = 0 回复动态
                                                        UserInfoBean userInfoBean = new UserInfoBean();
                                                        userInfoBean.setUser_id(0L);
                                                        dynamicBean.getComments().get(i).setReplyUser(userInfoBean);
                                                    } else {
                                                        if (userInfoBeanSparseArray.get((int) dynamicBean.getComments().get(i).getReply_to_user_id()) != null) {
                                                            dynamicBean.getComments().get(i).setReplyUser(userInfoBeanSparseArray.get((int) dynamicBean.getComments().get(i).getReply_to_user_id()));
                                                        }

                                                    }
                                                }

                                            }
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                        } else {
                                            listBaseJson.setStatus(userinfobeans.isStatus());
                                            listBaseJson.setCode(userinfobeans.getCode());
                                            listBaseJson.setMessage(userinfobeans.getMessage());
                                        }
                                        return listBaseJson;
                                    });
                        } else {
                            return Observable.just(listBaseJson);
                        }

                    }
                });
    }

    protected Observable<DynamicDetailBeanV2> dealWithDynamic(Observable<DynamicDetailBeanV2> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<DynamicDetailBeanV2, Observable<DynamicDetailBeanV2>>() {
                    @Override
                    public Observable<DynamicDetailBeanV2> call(final DynamicDetailBeanV2 dynamicBean) {

                        final List<Object> user_ids = new ArrayList<>();
                        user_ids.add(dynamicBean.getUser_id());
                        return getDynamicCommentListV2(dynamicBean.getFeed_mark(), dynamicBean.getId(), 0L)
                                .flatMap(new Func1<List<DynamicCommentBean>, Observable<DynamicDetailBeanV2>>() {
                                    @Override
                                    public Observable<DynamicDetailBeanV2> call(List<DynamicCommentBean> dynamicCommentBeen) {
                                        for (DynamicCommentBean dynamicCommentBean : dynamicCommentBeen) {
                                            user_ids.add(dynamicCommentBean.getUser_id());
                                            user_ids.add(dynamicCommentBean.getReply_to_user_id());
                                            dynamicCommentBean.setFeed_mark(dynamicBean.getFeed_mark()); // 评论中增加 feed_mark \和用户标记
                                            dynamicCommentBean.setFeed_user_id(dynamicBean.getUser_id());
                                        }
                                        mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(dynamicBean.getFeed_mark());// 删除本条动态的本地评论
                                        dynamicBean.setComments(dynamicCommentBeen);
                                        return Observable.just(dynamicBean);
                                    }
                                }).flatMap(new Func1<DynamicDetailBeanV2, Observable<DynamicDetailBeanV2>>() {
                                    @Override
                                    public Observable<DynamicDetailBeanV2> call(DynamicDetailBeanV2 dynamicDetailBeanV2) {
                                        if (user_ids.isEmpty()) {
                                            return Observable.just(dynamicBean);
                                        }
                                        return mUserInfoRepository.getUserInfo(user_ids)
                                                .map(userinfobeans -> {
                                                    if (userinfobeans.isStatus()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                        for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                        }
                                                        dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get(dynamicBean.getUser_id().intValue()));
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
                                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                                    }
                                                    return dynamicBean;
                                                });
                                    }
                                });
                    }
                });
    }

    protected Observable<List<DynamicDetailBeanV2>> dealWithDynamicListV2
            (Observable<DynamicBeanV2> observable, final String type, final boolean isLoadMore) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(dynamicBeanV2 -> {
                    List<DynamicDetailBeanV2> topData = dynamicBeanV2.getPinned();
                    if (topData != null && !topData.isEmpty()) {
                        for (DynamicDetailBeanV2 data : topData) {
                            data.setTop(DynamicDetailBeanV2.TOP_SUCCESS);
                        }
                        if (!type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) {
                            dynamicBeanV2.getFeeds().addAll(0, topData);
                        }
                    }
                    return dynamicBeanV2.getFeeds();
                })
                .flatMap(new Func1<List<DynamicDetailBeanV2>, Observable<List<DynamicDetailBeanV2>>>() {
                    @Override
                    public Observable<List<DynamicDetailBeanV2>> call(final List<DynamicDetailBeanV2> listBaseJson) {
                        if (listBaseJson.isEmpty()) {
                            return Observable.just(listBaseJson);
                        }
                        final List<Object> user_ids = new ArrayList<>();
                        if (!isLoadMore && type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {// 如果是热门，需要初始化时间
                            for (int i = listBaseJson.size() - 1; i >= 0; i--) {
                                listBaseJson.get(i).setHot_creat_time(System.currentTimeMillis());
                            }
                        }
                        for (DynamicDetailBeanV2 dynamicBean : listBaseJson) {
                            user_ids.add(dynamicBean.getUser_id());
                            if (type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) { //如果是关注，需要初始化标记
                                dynamicBean.setFollowed(true);
                            }
                            if (type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) { // 热门的 max_id 是通过 hot_creat_time 标识，最新与关注都是通过 feed_id 标识
                                dynamicBean.setMaxId(dynamicBean.getHot_creat_time());
                            } else {
                                dynamicBean.setMaxId(dynamicBean.getId());
                            }
                            for (DynamicCommentBean dynamicCommentBean : dynamicBean.getComments()) {
                                user_ids.add(dynamicCommentBean.getUser_id());
                                user_ids.add(dynamicCommentBean.getReply_to_user_id());
                                dynamicCommentBean.setFeed_mark(dynamicBean.getFeed_mark()); // 评论中增加 feed_mark \和用户标记
                                dynamicCommentBean.setFeed_user_id(dynamicBean.getUser_id());
                            }
                            mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(dynamicBean.getFeed_mark());// 删除本条动态的本地评论
                        }

                        return mUserInfoRepository.getUserInfo(user_ids)
                                .map(userinfobeans -> {
                                    if (userinfobeans.isStatus()) { // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        List<DynamicDetailBeanV2> topData = new ArrayList<>();
                                        for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (DynamicDetailBeanV2 dynamicBean : listBaseJson) {
                                            dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get(dynamicBean.getUser_id().intValue()));
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
                                            if (dynamicBean.getTop() == DynamicDetailBeanV2.TOP_SUCCESS) {
                                                topData.add(dynamicBean);
                                            }
                                        }
                                        if (!type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) {// 置顶只有 热门、最新
                                            TopDynamicBean topDynamicBean = new TopDynamicBean();
                                            topDynamicBean.setType(type.equals(ApiConfig.DYNAMIC_TYPE_NEW) ? TYPE_NEW : TYPE_HOT);
                                            topDynamicBean.setTopDynamics(topData);
                                            mTopDynamicBeanGreenDao.insertOrReplace(topDynamicBean);
                                        }
                                        mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                                    }
                                    return listBaseJson;
                                });
                    }
                });
    }
}
