package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupManagerBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.ChannelSubscripBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import retrofit2.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

//    @Inject
//    private GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;

    @Inject
    public BaseChannelRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mChannelClient = serviceManager.getChannelClient();
    }

    @Override
    public void handleSubscribChannel(ChannelSubscripBean channelSubscripBean) {
        // 发送订阅后台处理任务
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        ChannelInfoBean channelInfoBean = channelSubscripBean.getChannelInfoBean();
        if (channelSubscripBean.getChannelSubscriped()) {
            // 已经订阅，变为未订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            channelInfoBean.setFollow_count(channelInfoBean.getFollow_count() - 1);// 订阅数-1
        } else {
            // 未订阅，变为已订阅
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
            channelInfoBean.setFollow_count(channelInfoBean.getFollow_count() + 1);// 订阅数+1
        }
        // 设置请求路径
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_SUBSCRIB_CHANNEL_S, channelSubscripBean.getId() + ""));
        // 启动后台任务
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 更改数据源，切换订阅状态
        channelSubscripBean.setChannelSubscriped(!channelSubscripBean.getChannelSubscriped());
        // 更新数据库
        mChannelSubscripBeanGreenDao.insertOrReplace(channelSubscripBean);
    }

    @Override
    public Observable<BaseJsonV2<Object>> handleSubscribGroupByFragment(GroupInfoBean channelSubscripBean) {
        return mChannelClient.joinGroup(channelSubscripBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJson<List<ChannelSubscripBean>>> getChannelList(@Path("type") final String type, final long userId) {
        // 将获取到的ChannelInfoBean类型的频道列表，通过map转换成ChannelSubscripBean类型的数据
        return mChannelClient.getChannelList(type)
                .map(listBaseJson -> {
                    BaseJson<List<ChannelSubscripBean>> channelSubscripBeanBaseJson = new BaseJson<>();
                    channelSubscripBeanBaseJson.setCode(listBaseJson.getCode());
                    channelSubscripBeanBaseJson.setMessage(listBaseJson.getMessage());
                    channelSubscripBeanBaseJson.setStatus(listBaseJson.isStatus());
                    if (listBaseJson.isStatus() || listBaseJson.getCode() == 0) {
                        List<ChannelInfoBean> channelInfoBeanList = listBaseJson.getData();
                        List<ChannelSubscripBean> channelSubscripBeanList = new ArrayList<>();
                        if (channelInfoBeanList != null) {
                            for (ChannelInfoBean channelInfoBean : channelInfoBeanList) {
                                ChannelSubscripBean channelSubscripBean = new ChannelSubscripBean();
                                channelSubscripBean.setId(channelInfoBean.getId());// 设置频道id
                                channelSubscripBean.setChannelInfoBean(channelInfoBean);// 设置频道信息
                                // 如果是获取我订阅的频道，设置订阅状态为1
                                if (type == ApiConfig.CHANNEL_TYPE_MY_SUBSCRIB_CHANNEL) {
                                    channelInfoBean.setFollow_status(1);
                                }
                                channelSubscripBean.setChannelSubscriped(channelInfoBean.getFollow_status() == 0 ? false : true);// 设置订阅状态
                                channelSubscripBean.setUserId(userId);// 设置请求的用户id
                                channelSubscripBean.setUserIdAndIdforUnique("");// 添加唯一约束，防止数据重复
                                channelSubscripBeanList.add(channelSubscripBean);
                            }
                        }
                        channelSubscripBeanBaseJson.setData(channelSubscripBeanList);
                        mChannelInfoBeanGreenDao.insertOrReplace(channelInfoBeanList);
                        mChannelSubscripBeanGreenDao.clearTable();
                        mChannelSubscripBeanGreenDao.insertOrReplace(channelSubscripBeanList);

                    } else {
                        channelSubscripBeanBaseJson.setData(null);
                    }
                    return channelSubscripBeanBaseJson;
                });
    }

    @Override
    public Observable<List<GroupDynamicListBean>> getDynamicListFromGroup(long group_id, long max_id) {
        return dealWithGroupDynamicList(mChannelClient.getDynamicListFromGroup(group_id, TSListFragment.DEFAULT_PAGE_SIZE, max_id), "", false);
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
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<GroupInfoBean>, Observable<List<GroupInfoBean>>>() {
                    @Override
                    public Observable<List<GroupInfoBean>> call(List<GroupInfoBean> groupInfoBeen) {
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
                                    for (UserInfoBean userInfoBean : listBaseJson.getData()) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (int i = 0; i < groupInfoBeen.size(); i++) {
                                        if (groupInfoBeen.get(i).getManagers() != null) {
                                            for (int j = 0; j < groupInfoBeen.get(i).getManagers().size(); j++) {
                                                groupInfoBeen.get(i).getManagers().get(j).setUserInfoBean(
                                                        userInfoBeanSparseArray.get((int) groupInfoBeen.get(i).getManagers().get(j).getUser_id()));
                                            }
                                            for (int m = 0; m < groupInfoBeen.get(i).getMembers().size(); m++) {
                                                groupInfoBeen.get(i).getMembers().get(m).setUserInfoBean(
                                                        userInfoBeanSparseArray.get((int) groupInfoBeen.get(i).getMembers().get(m).getUser_id()));
                                            }
                                        }
                                    }
                                    return groupInfoBeen;
                                });
                    }
                });
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
    public void sendGroupComment(String commentContent,Long group_id, Long feed_id, Long reply_to_user_id,Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("content", commentContent);
        params.put("comment_mark", feed_id);
        params.put("reply_to_user_id", reply_to_user_id);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_GROUP_DYNAMIC_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_COMMENT_GROUP_DYNAMIC_FORMAT,group_id, feed_id));
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
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<GroupDynamicCommentListBean>, Observable<List<GroupDynamicCommentListBean>>>() {
                    @Override
                    public Observable<List<GroupDynamicCommentListBean>> call(List<GroupDynamicCommentListBean> groupDynamicCommentListBeen) {
                        List<Object> user_ids = new ArrayList<>();
                        if (groupDynamicCommentListBeen != null) {
                            for (GroupDynamicCommentListBean groupDynamicCommentListBean : groupDynamicCommentListBeen) {
                                user_ids.add(groupDynamicCommentListBean.getUser_id());
                                user_ids.add(groupDynamicCommentListBean.getReply_to_user_id());
                            }
                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(listBaseJson -> {
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : listBaseJson.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (int i = 0; i < groupDynamicCommentListBeen.size(); i++) {
                                            groupDynamicCommentListBeen.get(i).setCommentUser(
                                                    userInfoBeanSparseArray.get((int) groupDynamicCommentListBeen.get(i).getUser_id()));
                                            groupDynamicCommentListBeen.get(i).setReplyUser(
                                                    userInfoBeanSparseArray.get((int) groupDynamicCommentListBeen.get(i).getReply_to_user_id()));
                                        }
                                        return groupDynamicCommentListBeen;
                                    });
                        } else {
                            return Observable.just(new ArrayList<>());
                        }
                    }
                });
    }

    @Override
    public Observable<List<GroupDynamicLikeListBean>> getGroupDynamicDigList(long group_id, long dynamic_id, long max_id) {
        return mChannelClient.getDigList(group_id, dynamic_id, TSListFragment.DEFAULT_PAGE_SIZE, max_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<GroupDynamicLikeListBean>, Observable<List<GroupDynamicLikeListBean>>>() {
                    @Override
                    public Observable<List<GroupDynamicLikeListBean>> call(List<GroupDynamicLikeListBean> followFansBeen) {
                        List<Object> user_ids = new ArrayList<>();
                        if (followFansBeen != null) {
                            for (GroupDynamicLikeListBean followFansBean : followFansBeen) {
                                user_ids.add(followFansBean.getId());
                            }
                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(listBaseJson -> {
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : listBaseJson.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        for (int i = 0; i < followFansBeen.size(); i++) {
                                            followFansBeen.get(i).setMUserInfoBean(userInfoBeanSparseArray.get(Integer.parseInt(String.valueOf(followFansBeen.get(i).getId()))));
                                        }
                                        return followFansBeen;
                                    });
                        } else {
                            return Observable.just(new ArrayList<>());
                        }
                    }
                });
    }

    @Override
    public Observable<GroupDynamicListBean> getGroupDynamicDetail(long group_id, long dynamic_id) {
        return mChannelClient.getGroupDynamicDetail(group_id, dynamic_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<GroupDynamicListBean, Observable<GroupDynamicListBean>>() {
                    @Override
                    public Observable<GroupDynamicListBean> call(GroupDynamicListBean groupDynamicListBean) {
                        List<Object> user_ids = new ArrayList<>();
                        if (groupDynamicListBean != null){
                            user_ids.add(groupDynamicListBean.getUser_id());
                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(listBaseJson -> {
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                        for (UserInfoBean userInfoBean : listBaseJson.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                        }
                                        groupDynamicListBean.setUserInfoBean(
                                                userInfoBeanSparseArray.get(Integer.parseInt(String.valueOf(groupDynamicListBean.getUser_id()))));
                                        return groupDynamicListBean;
                                    });
                        }
                        return null;
                    }
                });
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
                                String.format(ApiConfig.APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC_S, String.valueOf(group_id), String.valueOf(dynamic_id)));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC_S, String.valueOf(group_id), String.valueOf(dynamic_id)));
                    }
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void handelCollect(boolean isCollected, long group_id, long dynamic_id) {
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
}
