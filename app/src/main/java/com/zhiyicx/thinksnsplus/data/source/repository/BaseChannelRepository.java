package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupManagerBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class BaseChannelRepository extends BaseDynamicRepository implements IBaseChannelRepository {

    protected ChannelClient mChannelClient;
    @Inject
    protected UserInfoRepository mUserInfoRepository;
    @Inject
    protected ChannelSubscripBeanGreenDaoImpl mChannelSubscripBeanGreenDao;
    @Inject
    protected ChannelInfoBeanGreenDaoImpl mChannelInfoBeanGreenDao;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

//    @Inject
//    private GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;

    @Inject
    public BaseChannelRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mChannelClient = serviceManager.getChannelClient();
    }


    @Override
    public Observable<BaseJsonV2<Object>> handleSubscribGroupByFragment(GroupInfoBean channelSubscripBean) {
        return mChannelClient.joinGroup(channelSubscripBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<GroupDynamicListBean>> getDynamicListFromGroup(long group_id, long max_id) {
        return dealWithGroupDynamicList(mChannelClient.getDynamicListFromGroup(group_id, TSListFragment.DEFAULT_PAGE_SIZE, max_id), "", false);
    }

    @Override
    public Observable<List<GroupDynamicListBean>> getMyCollectGroupDynamicList(long group_id, long max_id) {
        return dealWithGroupCollectDynamicList(mChannelClient.getMyCollectGroupDynamicList(TSListFragment.DEFAULT_PAGE_SIZE, max_id), "", false);
    }

    @Override
    public Observable<List<GroupInfoBean>> getGroupList(int type, long max_id) {
        Observable<List<GroupInfoBean>> observable;
        if (type == 0) {
            observable = mChannelClient.getAllGroupList(TSListFragment.DEFAULT_PAGE_SIZE, max_id);
        } else {
            observable = mChannelClient.getUserJoinedGroupList(TSListFragment.DEFAULT_PAGE_SIZE, max_id);
        }
        return observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(groupInfoBeen -> {
                    if (groupInfoBeen.isEmpty()) {
                        return Observable.just(groupInfoBeen);
                    }
                    List<Object> user_ids = new ArrayList<>();
                    for (GroupInfoBean groupInfoBean : groupInfoBeen) {
                        for (GroupManagerBean groupManagerBean : groupInfoBean.getManagers()) {
                            user_ids.add(groupManagerBean.getUser_id());
                        }
                        for (GroupManagerBean groupManagerBean : groupInfoBean.getMembers()) {
                            user_ids.add(groupManagerBean.getUser_id());
                        }
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(listBaseJson -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : listBaseJson) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                for (int i = 0; i < groupInfoBeen.size(); i++) {
                                    if (groupInfoBeen.get(i).getManagers() != null) {
                                        for (int j = 0; j < groupInfoBeen.get(i).getManagers().size(); j++) {
                                            UserInfoBean userInfoBean = userInfoBeanSparseArray.get((int) groupInfoBeen.get(i).getManagers()
                                                    .get(j).getUser_id());
                                            if (userInfoBean != null) {
                                                groupInfoBeen.get(i).getManagers().get(j).setUserInfoBean(userInfoBean);
                                            }
                                        }
                                        for (int m = 0; m < groupInfoBeen.get(i).getMembers().size(); m++) {
                                            UserInfoBean userInfoBean = userInfoBeanSparseArray.get((int) groupInfoBeen.get(i).getMembers().get
                                                    (m).getUser_id());
                                            if (userInfoBean != null) {
                                                groupInfoBeen.get(i).getMembers().get(m).setUserInfoBean(userInfoBean);
                                            }
                                        }
                                    }
                                }
                                return groupInfoBeen;
                            });
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @Override
    public void handleGroupJoin(GroupInfoBean groupInfoBean) {
        // 发送订阅后台处理任务
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        boolean isJoined = groupInfoBean.getIs_member() == 1;
        if (isJoined) {
            // 已经订阅，变为未订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST_V2);
        } else {
            // 未订阅，变为已订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE_V2);
        }
        // 设置请求路径
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_JOIN_GROUP_S, String.valueOf(groupInfoBean.getId())));
        // 启动后台任务
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void sendGroupComment(String commentContent, Long group_id, Long feed_id, Long reply_to_user_id, Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", commentContent);
        params.put("group_post_comment_mark", comment_mark);
        if (reply_to_user_id != 0) {
            params.put("reply_user", reply_to_user_id);
        }

        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_GROUP_DYNAMIC_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_COMMENT_GROUP_DYNAMIC_FORMAT, group_id, feed_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteGroupComment(long group_id, long feed_id, long comment_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DELETE_GROUP_DYNAMIC_COMMENT_FORMAT, group_id, feed_id, comment_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteGroupDynamic(long group_id, long feed_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DELETE_GROUP_DYNAMIC_FORMAT, group_id, feed_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public Observable<BaseJsonV2<Object>> sendGroupDynamic(GroupSendDynamicDataBean dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        return mChannelClient.sendGroupDynamic(dynamicDetailBean.getGroup_id(), body);
    }

    @Override
    public Observable<List<GroupDynamicCommentListBean>> getGroupDynamicCommentList(long group_id, long dynamic_id, long max_id) {
        return mChannelClient.getGroupDynamicCommentList(group_id, dynamic_id, TSListFragment.DEFAULT_PAGE_SIZE, max_id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(groupDynamicCommentListBeen -> {
                    List<Object> user_ids = new ArrayList<>();
                    if (!groupDynamicCommentListBeen.isEmpty()) {
                        for (GroupDynamicCommentListBean groupDynamicCommentListBean : groupDynamicCommentListBeen) {
                            user_ids.add(groupDynamicCommentListBean.getUser_id());
                            user_ids.add(groupDynamicCommentListBean.getReply_to_user_id());
                        }
                        return mUserInfoRepository.getUserInfo(user_ids)
                                .map(listBaseJson -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : listBaseJson) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (int i = 0; i < groupDynamicCommentListBeen.size(); i++) {
                                        // 发布评论用户
                                        groupDynamicCommentListBeen.get(i).setCommentUser(
                                                userInfoBeanSparseArray.get((int) groupDynamicCommentListBeen.get(i).getUser_id()));
                                        // 回复用户
                                        if (groupDynamicCommentListBeen.get(i).getReply_to_user_id() != 0) {
                                            groupDynamicCommentListBeen.get(i).setReplyUser(
                                                    userInfoBeanSparseArray.get((int) groupDynamicCommentListBeen.get(i).getReply_to_user_id()));
                                        } else {
                                            UserInfoBean userInfoBean = new UserInfoBean();
                                            userInfoBean.setUser_id(0L);
                                            groupDynamicCommentListBeen.get(i).setReplyUser(userInfoBean);
                                        }
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                    return groupDynamicCommentListBeen;
                                });
                    } else {
                        return Observable.just(groupDynamicCommentListBeen);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<List<DynamicDigListBean>> getGroupDynamicDigList(long group_id, long dynamic_id, long max_id) {
        return mChannelClient.getDigList(group_id, dynamic_id, TSListFragment.DEFAULT_PAGE_SIZE, max_id)
                .observeOn(Schedulers.io())
                .flatMap((Func1<List<DynamicDigListBean>, Observable<List<DynamicDigListBean>>>) groupDynamicLikeListBeen -> {
                    List<Object> user_ids = new ArrayList<>();
                    if (!groupDynamicLikeListBeen.isEmpty()) {
                        for (int i = 0; i < groupDynamicLikeListBeen.size(); i++) {
                            DynamicDigListBean likeListBean = groupDynamicLikeListBeen.get(i);
                            if (likeListBean.getUser_id() != null && likeListBean.getUser_id() != 0) {
                                user_ids.add(likeListBean.getUser_id());
                            }
                            if (likeListBean.getTarget_user() != null && likeListBean.getTarget_user() != 0) {
                                user_ids.add(likeListBean.getTarget_user());
                            }
                        }
                        // 通过用户id列表请求用户信息和用户关注状态
                        return mUserInfoRepository.getUserInfo(user_ids)
                                .map(listBaseJson -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : listBaseJson) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (DynamicDigListBean dynamicDigListBean : groupDynamicLikeListBeen) {
                                        if (dynamicDigListBean.getUser_id() != null && dynamicDigListBean.getUser_id() != 0) {
                                            dynamicDigListBean.setDiggUserInfo(userInfoBeanSparseArray.get(dynamicDigListBean.getUser_id()
                                                    .intValue()));
                                        }
                                        if (dynamicDigListBean.getTarget_user() != null && dynamicDigListBean.getTarget_user() != 0) {
                                            dynamicDigListBean.setTargetUserInfo(userInfoBeanSparseArray.get(dynamicDigListBean.getTarget_user
                                                    ().intValue()));
                                        }
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                    return groupDynamicLikeListBeen;
                                });
                    } else {
                        return Observable.just(new ArrayList<>());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @Override
    public Observable<GroupDynamicListBean> getGroupDynamicDetail(long group_id, long dynamic_id) {
        return mChannelClient.getGroupDynamicDetail(group_id, dynamic_id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(groupDynamicListBean -> {
                    List<Object> user_ids = new ArrayList<>();
                    if (groupDynamicListBean != null) {
                        user_ids.add(groupDynamicListBean.getUser_id());
                        return mUserInfoRepository.getUserInfo(user_ids)
                                .map(listBaseJson -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : listBaseJson) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    groupDynamicListBean.setUserInfoBean(
                                            userInfoBeanSparseArray.get(Integer.parseInt(String.valueOf(groupDynamicListBean.getUser_id()))));
                                    return groupDynamicListBean;
                                });
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @Override
    public void handleLike(boolean isLiked, long group_id, long dynamic_id) {
        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("feed_id", feed_id);
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC_S, String.valueOf(group_id), String.valueOf
                                        (dynamic_id)));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC_S, String.valueOf(group_id), String.valueOf
                                        (dynamic_id)));
                    }
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void handleCollect(boolean isCollected, long group_id, long dynamic_id) {
        Observable.just(isCollected)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("feed_id", feed_id);
                    // 后台处理
                    if (!aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_COLLECT_GROUP_DYNAMIC_S, String.valueOf(group_id), String.valueOf(dynamic_id)));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_COLLECT_GROUP_DYNAMIC_S, String.valueOf(group_id), String.valueOf(dynamic_id)));
                    }
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }


    @Override
    public Observable<List<GroupInfoBean>> getAllGroupList(long max_id) {
        return getGroupList(0, max_id);
    }

    @Override
    public Observable<List<GroupInfoBean>> getUserJoinedGroupList(long max_id) {
        return getGroupList(1, max_id);
    }
}
