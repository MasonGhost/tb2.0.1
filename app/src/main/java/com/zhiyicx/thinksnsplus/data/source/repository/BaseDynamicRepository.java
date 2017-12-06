package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CollectGroupDyanmciListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
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

    @Override
    public Observable<BaseJsonV2<Object>> sendDynamicV2(SendDynamicDataBeanV2 dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson()
                .toJson(dynamicDetailBean));
        return mDynamicClient.sendDynamicV2(body);
    }


    @Override
    public Observable<List<DynamicDetailBeanV2>> getDynamicListV2(String type, Long after, Long user_id, final boolean isLoadMore,
                                                                  String screen) {
        Observable<DynamicBeanV2> observable;
        // 收藏的动态地址和返回大不一样，真滴难受
        if (DYNAMIC_TYPE_MY_COLLECTION.equals(type)) {
            observable = mDynamicClient.getCollectDynamicListV2(after, user_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                    .flatMap(detailBeanV2 -> {
                        DynamicBeanV2 data = new DynamicBeanV2();
                        data.setFeeds(detailBeanV2);
                        return Observable.just(data);
                    });
        } else {
            observable = mDynamicClient.getDynamicListV2(type, after, user_id, (long) TSListFragment.DEFAULT_PAGE_SIZE, screen);
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
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig
                                .POST_V2, params);
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                                .APP_PATH_DYNAMIC_CLICK_LIKE_FORMAT_V2, feed_id));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig
                                .DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                                .APP_PATH_DYNAMIC_CANCEL_CLICK_LIKE_FORMAT_V2, feed_id));
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
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_DELETE_COMMENT_V2, feed_id,
                comment_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }


    @Override
    public void sendCommentV2(String commentContent, Long feed_id, Long reply_to_user_id, Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", commentContent);
        params.put("comment_mark", comment_mark);
        if (reply_to_user_id > 0) {
            params.put("reply_user", reply_to_user_id);
        }

        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig
                .SEND_DYNAMIC_COMMENT, params);
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
    public void updateOrInsertDynamicV2(List<DynamicDetailBeanV2> dynamicBeens, final String type) {
        Observable.just(dynamicBeens)
                .observeOn(Schedulers.io())
                .subscribe(datas -> {
                    // 清除旧数据
                    mDynamicDetailBeanV2GreenDao.deleteDynamicByType(type);
                    List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
                    List<DynamicDetailBeanV2> result = new ArrayList<>();
                    int size = datas.size();
                    for (int i = 0; i < size; i++) {
                        // 处理关注和热门数据
                        if (datas.get(i).getFeed_mark() != null && datas.get(i).getFeed_mark() != 0) {
                            dealLocalTypeDataV2(datas.get(i));
                            if (datas.get(i).getComments() != null) {
                                dynamicCommentBeen.addAll(datas.get(i).getComments());
                            }
                            result.add(datas.get(i));
                        }
                    }

                    mDynamicDetailBeanV2GreenDao.insertOrReplace(result);
                    mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBeen);
                }, throwable -> throwable.printStackTrace());
    }


    private void dealLocalTypeDataV2(DynamicDetailBeanV2 dynamicBeanTmp) {
        DynamicDetailBeanV2 localDynamicBean = mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(dynamicBeanTmp
                .getFeed_mark());
        if (localDynamicBean != null) {
            if ((dynamicBeanTmp.getHot_creat_time() == null || dynamicBeanTmp.getHot_creat_time() == 0) &&
                    localDynamicBean.getHot_creat_time() != null && localDynamicBean.getHot_creat_time() != 0) {
                dynamicBeanTmp.setHot_creat_time(localDynamicBean.getHot_creat_time());
            }
            if (localDynamicBean.getIsFollowed()) {
                dynamicBeanTmp.setIsFollowed(localDynamicBean.getIsFollowed());
            }
        }
    }

    @Override
    public Observable<List<DynamicDigListBean>> getDynamicDigListV2(Long feed_id, Long
            max_id) {
        return mDynamicClient.getDynamicDigListV2(feed_id, max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .observeOn(Schedulers.io())
                .flatMap(dynamicDigListBeanList -> {
                    // 获取点赞的用户id列表
                    if (!dynamicDigListBeanList.isEmpty()) {
                        List<Object> userids = new ArrayList<>();
                        for (int i = 0; i < dynamicDigListBeanList.size(); i++) {
                            DynamicDigListBean dynamicDigListBean = dynamicDigListBeanList.get(i);
                            userids.add(dynamicDigListBean.getUser_id());
                            userids.add(dynamicDigListBean.getTarget_user());
                        }
                        // 通过用户id列表请求用户信息和用户关注状态
                        return mUserInfoRepository.getUserInfo(userids)
                                .map(listBaseJson -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : listBaseJson) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(),
                                                userInfoBean);
                                    }
                                    for (DynamicDigListBean dynamicDigListBean : dynamicDigListBeanList) {
                                        dynamicDigListBean.setDiggUserInfo(userInfoBeanSparseArray.get
                                                (dynamicDigListBean.getUser_id().intValue()));
                                        dynamicDigListBean.setTargetUserInfo(userInfoBeanSparseArray.get
                                                (dynamicDigListBean.getTarget_user().intValue()));
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                    return dynamicDigListBeanList;
                                });
                    } else {
                        // 返回期待以外的数据，比如状态为false，或者数据为空，发射空数据
                        return Observable.just(dynamicDigListBeanList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
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
                .observeOn(Schedulers.io())
                .flatMap(listBaseJson -> {

                    final List<Object> user_ids = new ArrayList<>();
                    if (listBaseJson.getComments() != null && listBaseJson.getComments().size() > 1) {
                        Collections.sort(listBaseJson.getComments(), new TimeStringSortClass());
                    }
                    for (DynamicCommentBean dynamicCommentBean : listBaseJson.getPinneds()) {
                        dynamicCommentBean.setPinned(true);
                    }
                    listBaseJson.getPinneds().addAll(listBaseJson.getComments());
                    for (DynamicCommentBean dynamicCommentBean : listBaseJson.getPinneds()) {
                        user_ids.add(dynamicCommentBean.getUser_id());
                        user_ids.add(dynamicCommentBean.getReply_to_user_id());
                        dynamicCommentBean.setFeed_mark(feed_mark);
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(listBaseJson.getPinneds());
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userinfobeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userinfobeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (int i = 0; i < listBaseJson.getPinneds().size(); i++) {
                                    listBaseJson.getPinneds().get(i).setCommentUser(userInfoBeanSparseArray.get(
                                            (int) listBaseJson.getPinneds().get(i).getUser_id()));
                                    if (listBaseJson.getPinneds().get(i).getReply_to_user_id() == 0) {
                                        // 如果
                                        // reply_user_id = 0 回复动态
                                        UserInfoBean userInfoBean = new UserInfoBean();
                                        userInfoBean.setUser_id(0L);
                                        listBaseJson.getPinneds().get(i).setReplyUser(userInfoBean);
                                    } else {
                                        listBaseJson.getPinneds().get(i).setReplyUser(userInfoBeanSparseArray.get
                                                ((int) listBaseJson.getPinneds().get(i).getReply_to_user_id()));
                                    }
                                }
                                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);

                                return listBaseJson.getPinneds();
                            });

                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
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


    protected Observable<List<GroupDynamicListBean>> dealWithGroupDynamicList(Observable<List<GroupDynamicListBean>>
                                                                                      observable,
                                                                              final String type, final boolean
                                                                                      isLoadMore) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(groupDynamicList -> {
                    final List<Object> user_ids = new ArrayList<>();
                    for (GroupDynamicListBean groupDynamicListBean : groupDynamicList) {
                        user_ids.add(groupDynamicListBean.getUser_id());
                        if (groupDynamicListBean.getNew_comments() == null || groupDynamicListBean
                                .getNew_comments().isEmpty()) {
                            continue;
                        }
                        for (GroupDynamicCommentListBean commentListBean : groupDynamicListBean.getNew_comments()) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_to_user_id());
                        }
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(groupDynamicList);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userinfobeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userinfobeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (GroupDynamicListBean dynamicBean : groupDynamicList) {
                                    dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get(dynamicBean
                                            .getUser_id().intValue()));
                                    if (dynamicBean.getNew_comments() == null || dynamicBean.getNew_comments()
                                            .isEmpty()) {
                                        continue;
                                    }
                                    for (int i = 0; i < dynamicBean.getNew_comments().size(); i++) {
                                        UserInfoBean tmpUserinfo = userInfoBeanSparseArray.get((int) dynamicBean
                                                .getNew_comments().get(i).getUser_id());
                                        if (tmpUserinfo != null) {
                                            dynamicBean.getNew_comments().get(i).setCommentUser(tmpUserinfo);
                                        }
                                        if (dynamicBean.getNew_comments().get(i).getReply_to_user_id() == 0) {
                                            // 如果 reply_user_id = 0 回复动态
                                            UserInfoBean userInfoBean = new UserInfoBean();
                                            userInfoBean.setUser_id(0L);
                                            dynamicBean.getNew_comments().get(i).setReplyUser(userInfoBean);
                                        } else {
                                            if (userInfoBeanSparseArray.get((int) dynamicBean.getNew_comments()
                                                    .get(i).getReply_to_user_id()) != null) {
                                                dynamicBean.getNew_comments().get(i).setReplyUser
                                                        (userInfoBeanSparseArray.get((int) dynamicBean
                                                                .getNew_comments().get(i).getReply_to_user_id()));
                                            }
                                        }
                                    }

                                }
                                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                return groupDynamicList;
                            });
                });

    }

    protected Observable<List<GroupDynamicListBean>> dealWithGroupCollectDynamicList
            (Observable<List<CollectGroupDyanmciListBean>> observable,
             final String type, final boolean
                     isLoadMore) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(groupDynamicList -> {
                    final List<Object> user_ids = new ArrayList<>();
                    List<GroupDynamicListBean> result = new ArrayList<>();
                    for (CollectGroupDyanmciListBean groupDynamicListBean : groupDynamicList) {
                        user_ids.add(groupDynamicListBean.getPost().getUser_id());
                        groupDynamicListBean.getPost().setId((long) groupDynamicListBean.getPost_id());
                        result.add(groupDynamicListBean.getPost());
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(result);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userinfobeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userinfobeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (CollectGroupDyanmciListBean dynamicBean : groupDynamicList) {
                                    dynamicBean.getPost().setId((long) dynamicBean.getPost_id());
                                    dynamicBean.getPost().setUserInfoBean(userInfoBeanSparseArray.get(dynamicBean
                                            .getPost().getUser_id().intValue()));
                                    if (dynamicBean.getPost().getNew_comments() == null || dynamicBean.getPost()
                                            .getNew_comments().isEmpty()) {
                                        continue;
                                    }
                                    for (int i = 0; i < dynamicBean.getPost().getNew_comments().size(); i++) {
                                        UserInfoBean tmpUserinfo = userInfoBeanSparseArray.get((int) dynamicBean
                                                .getPost().getNew_comments().get(i).getUser_id());
                                        if (tmpUserinfo != null) {
                                            dynamicBean.getPost().getNew_comments().get(i).setCommentUser
                                                    (tmpUserinfo);
                                        }
                                        if (dynamicBean.getPost().getNew_comments().get(i).getReply_to_user_id()
                                                == 0) { // 如果 reply_user_id = 0 回复动态
                                            UserInfoBean userInfoBean = new UserInfoBean();
                                            userInfoBean.setUser_id(0L);
                                            dynamicBean.getPost().getNew_comments().get(i).setReplyUser
                                                    (userInfoBean);
                                        } else {
                                            if (userInfoBeanSparseArray.get((int) dynamicBean.getPost()
                                                    .getNew_comments().get(i).getReply_to_user_id()) != null) {
                                                dynamicBean.getPost().getNew_comments().get(i).setReplyUser
                                                        (userInfoBeanSparseArray.get((int) dynamicBean.getPost()
                                                                .getNew_comments().get(i).getReply_to_user_id()));
                                            }
                                        }
                                    }

                                }
                                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                return result;
                            });
                });

    }

    protected Observable<DynamicDetailBeanV2> dealWithDynamic(Observable<DynamicDetailBeanV2> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(dynamicBean -> {
                    final List<Object> user_ids = new ArrayList<>();
                    user_ids.add(dynamicBean.getUser_id());
                    return getDynamicCommentListV2(dynamicBean.getFeed_mark(), dynamicBean.getId(), 0L)
                            .flatMap(dynamicCommentBeen -> {
                                for (DynamicCommentBean dynamicCommentBean : dynamicCommentBeen) {
                                    user_ids.add(dynamicCommentBean.getUser_id());
                                    user_ids.add(dynamicCommentBean.getReply_to_user_id());
                                    // 评论中增加
                                    dynamicCommentBean.setFeed_mark(dynamicBean.getFeed_mark());
                                    // feed_mark \和用户标记
                                    dynamicCommentBean.setFeed_user_id(dynamicBean.getUser_id());
                                }
                                mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(dynamicBean.getFeed_mark())
                                ;// 删除本条动态的本地评论
                                dynamicBean.setComments(dynamicCommentBeen);
                                return Observable.just(dynamicBean);
                            }).flatMap(dynamicDetailBeanV2 -> {
                                if (user_ids.isEmpty()) {
                                    return Observable.just(dynamicBean);
                                }
                                return mUserInfoRepository.getUserInfo(user_ids)
                                        .map(userinfobeans -> {
                                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                                    SparseArray<>();
                                            for (UserInfoBean userInfoBean : userinfobeans) {
                                                userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                                        .intValue(), userInfoBean);
                                            }
                                            dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get
                                                    (dynamicBean.getUser_id().intValue()));

                                            dynamicBean.handleData();
                                            for (int i = 0; i < dynamicBean.getComments().size(); i++) {
                                                dynamicBean.getComments().get(i).setCommentUser
                                                        (userInfoBeanSparseArray.get((int) dynamicBean
                                                                .getComments().get(i).getUser_id()));
                                                if (dynamicBean.getComments().get(i).getReply_to_user_id() ==
                                                        0) {
                                                    // 如果 reply_user_id = 0 回复动态
                                                    UserInfoBean userInfoBean = new UserInfoBean();
                                                    userInfoBean.setUser_id(0L);
                                                    dynamicBean.getComments().get(i).setReplyUser(userInfoBean);
                                                } else {
                                                    dynamicBean.getComments().get(i).setReplyUser
                                                            (userInfoBeanSparseArray.get((int) dynamicBean
                                                                    .getComments().get(i).getReply_to_user_id
                                                                            ()));
                                                }
                                            }
                                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                            return dynamicBean;
                                        });
                            });
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    protected Observable<List<DynamicDetailBeanV2>> dealWithDynamicListV2
            (Observable<DynamicBeanV2> observable, final String type, final boolean isLoadMore) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(dynamicBeanV2 -> {
                    List<DynamicDetailBeanV2> topData = dynamicBeanV2.getPinned();
                    if (topData != null && !topData.isEmpty()) {
                        for (DynamicDetailBeanV2 data : topData) {
                            data.setTop(DynamicDetailBeanV2.TOP_SUCCESS);
                        }
                        if (!type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS) && !type.equals(ApiConfig
                                .DYNAMIC_TYPE_USERS)) {
                            dynamicBeanV2.getFeeds().addAll(0, topData);
                        }
                    }
                    return dynamicBeanV2.getFeeds();
                })
                .flatMap(listBaseJson -> {
                    if (listBaseJson.isEmpty()) {
                        return Observable.just(listBaseJson);
                    }
                    final List<Object> user_ids = new ArrayList<>();
                    // 如果是热门，需要初始化时间
                    if (!isLoadMore && type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {
                        for (int i = listBaseJson.size() - 1; i >= 0; i--) {
                            listBaseJson.get(i).setHot_creat_time(System.currentTimeMillis());
                        }
                    }
                    for (DynamicDetailBeanV2 dynamicBean : listBaseJson) {
                        user_ids.add(dynamicBean.getUser_id());
                        //如果是关注，需要初始化标记
                        if (type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) {
                            dynamicBean.setFollowed(true);
                        }
                        // 热门的 max_id 是通过 hot_creat_time
                        if (type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {
                            // 标识，最新与关注都是通过 feed_id 标识
                            dynamicBean.setMaxId(dynamicBean.getHot_creat_time());
                        } else {
                            dynamicBean.setMaxId(dynamicBean.getId());
                        }
                        for (DynamicCommentBean dynamicCommentBean : dynamicBean.getComments()) {
                            user_ids.add(dynamicCommentBean.getUser_id());
                            user_ids.add(dynamicCommentBean.getReply_to_user_id());
                            // 评论中增加 feed_mark \和用户标记
                            dynamicCommentBean.setFeed_mark(dynamicBean.getFeed_mark());
                            dynamicCommentBean.setFeed_user_id(dynamicBean.getUser_id());
                        }
                        // 删除本条动态的本地评论
                        mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(dynamicBean.getFeed_mark());
                    }

                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userinfobeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                List<DynamicDetailBeanV2> topData = new ArrayList<>();
                                for (UserInfoBean userInfoBean : userinfobeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (DynamicDetailBeanV2 dynamicBean : listBaseJson) {
                                    dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get(dynamicBean
                                            .getUser_id().intValue()));
                                    dynamicBean.handleData();
                                    for (int i = 0; i < dynamicBean.getComments().size(); i++) {
                                        if (userInfoBeanSparseArray.get((int) dynamicBean.getComments().get(i)
                                                .getUser_id()) != null) {
                                            dynamicBean.getComments().get(i).setCommentUser
                                                    (userInfoBeanSparseArray.get((int) dynamicBean.getComments()
                                                            .get(i).getUser_id()));
                                        }
                                        // 如果 reply_user_id = 0 回复动态
                                        if (dynamicBean.getComments().get(i).getReply_to_user_id() == 0) {
                                            UserInfoBean userInfoBean = new UserInfoBean();
                                            userInfoBean.setUser_id(0L);
                                            dynamicBean.getComments().get(i).setReplyUser(userInfoBean);
                                        } else {
                                            dynamicBean.getComments().get(i).setReplyUser(userInfoBeanSparseArray
                                                    .get((int) dynamicBean.getComments().get(i)
                                                            .getReply_to_user_id()));
                                        }
                                    }
                                    if (dynamicBean.getTop() == DynamicDetailBeanV2.TOP_SUCCESS) {
                                        topData.add(dynamicBean);
                                    }
                                }
                                // 置顶只有 热门、最新
                                if (!type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) {
                                    TopDynamicBean topDynamicBean = new TopDynamicBean();
                                    topDynamicBean.setType(type.equals(ApiConfig.DYNAMIC_TYPE_NEW) ? TYPE_NEW :
                                            TYPE_HOT);
                                    topDynamicBean.setTopDynamics(topData);
                                    mTopDynamicBeanGreenDao.insertOrReplace(topDynamicBean);
                                }
                                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                return listBaseJson;
                            });
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
