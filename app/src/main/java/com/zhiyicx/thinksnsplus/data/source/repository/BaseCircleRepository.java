package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.net.UpLoadFile;
import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleCommentZip;
import com.zhiyicx.thinksnsplus.data.beans.circle.CreateCircleBean;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CircleTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/11/21/15:36
 * @Email Jliuer@aliyun.com
 * @Description 圈子请求仓库管理
 */
public class BaseCircleRepository implements IBaseCircleRepository {

    protected CircleClient mCircleClient;

    @Inject
    protected Application mContext;
    @Inject
    protected UserInfoRepository mUserInfoRepository;

    @Inject
    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;
    @Inject
    protected CircleTypeBeanGreenDaoImpl mCircleTypeBeanGreenDao;

    /**
     * 参数 type 默认 1，   1-发布的 2- 已置顶 3-置顶待审
     */
    public enum CircleMinePostType {
        PUBLISH(1),
        HAD_PINNED(2),
        WAIT_PINNED_AUDIT(3),
        SEARCH(4),
        COLLECT(5);

        public int value;

        CircleMinePostType(int value) {
            this.value = value;
        }
    }


    @Inject
    public BaseCircleRepository(ServiceManager serviceManager) {
        mCircleClient = serviceManager.getCircleClient();
    }

    @Override
    public Observable<List<CircleTypeBean>> getCategroiesList(int limit, int offet) {
        return mCircleClient.getCategroiesList(TSListFragment.DEFAULT_PAGE_SIZE, offet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<CircleInfo>> createCircle(CreateCircleBean createCircleBean) {
        Map<String, String> file = new HashMap<>();
        if (createCircleBean.getFilePath() != null) {
            file.put(createCircleBean.getFileName(), createCircleBean.getFilePath());
        }
        return mCircleClient.createCircle(createCircleBean.getCategoryId(), UpLoadFile.upLoadFileAndParams(file, DataDealUitls
                .transBean2MapWithArray(createCircleBean)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<String>> getCircleRule() {
        return mCircleClient.getCircleRule()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<CircleInfo>> updateCircle(CreateCircleBean createCircleBean) {
        Map<String, String> file = new HashMap<>();
        if (createCircleBean.getFilePath() != null) {
            file.put(createCircleBean.getFileName(), createCircleBean.getFilePath());
        }
        return mCircleClient.updateCircle(createCircleBean.getCircleId(), UpLoadFile.upLoadFileAndParams(file, DataDealUitls
                .transBean2MapWithArray(createCircleBean)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<CirclePostListBean>> sendCirclePost(PostPublishBean publishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson()
                .toJson(publishBean));

        return mCircleClient.publishPost(publishBean.getCircle_id(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CircleInfo>> getMyJoinedCircle(int limit, int offet, String type) {
        return mCircleClient.getMyJoinedCircle(limit, offet, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取全部圈
     *
     * @param limit       默认 15 ，数据返回条数 默认为15
     * @param offet       默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword     用于搜索圈子，按圈名搜索
     * @param category_id 圈子分类id
     * @return
     */

    @Override
    public Observable<List<CircleInfo>> getAllCircle(Integer limit, Integer offet, String keyword
            , Integer category_id) {
        return mCircleClient.getAllCircle(limit, offet, keyword, category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Integer>> getCircleCount() {
        return mCircleClient.getCircleCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<RewardsListBean>> getPostRewardList(long post_id, Integer limit, Integer offset, String order, String order_type) {
        return mCircleClient.getPostRewardList(post_id, TSListFragment.DEFAULT_PAGE_SIZE, offset, order, order_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<PostDigListBean>> getPostDigList(long postId, int limit, long offet) {
        return mCircleClient.getPostDigList(postId, TSListFragment.DEFAULT_PAGE_SIZE, offet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(postDigListBeans -> {
                    List<Object> user_ids = new ArrayList<>();
                    for (PostDigListBean digListBean : postDigListBeans) {
                        user_ids.add(digListBean.getUser_id());
                        user_ids.add(digListBean.getTarget_user());
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(postDigListBeans);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(listBaseJson -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : listBaseJson) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (PostDigListBean digListBean : postDigListBeans) {
                                    digListBean.setDiggUserInfo(userInfoBeanSparseArray.get(digListBean.getUser_id().intValue()));
                                    digListBean.setTargetUserInfo(userInfoBeanSparseArray.get(digListBean.getTarget_user().intValue()));
                                }
                                return postDigListBeans;
                            });
                });
    }

    @Override
    public void dealLike(boolean isLiked, long postId) {
        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();

                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_LIKE_POST_FORMAT, String.valueOf(postId)));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_LIKE_POST_FORMAT, String.valueOf(postId)));
                    }
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void dealCollect(boolean isCollected, long postId) {
        Observable.just(isCollected)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
                    // 后台处理
                    if (!aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_COLLECT_POST_FORMAT, String.valueOf(postId)));
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        backgroundRequestTaskBean.setPath(
                                String.format(ApiConfig.APP_PATH_UNCOLLECT_POST_FORMAT, String.valueOf(postId)));
                    }
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void sendPostComment(String commentContent, Long postId, Long replyToUserId, Long commentMark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", commentContent);
        params.put("group_post_comment_mark", commentMark);
        if (replyToUserId != 0) {
            params.put("reply_user", replyToUserId);
        }

        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_CIRCLE_POST_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_COMMENT_POST_FORMAT, postId));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deletePostComment(long postId, long commentId) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DELETE_POST_COMMENT_FORMAT, postId, commentId));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deletePost(long circleId, long postId) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_POST_FORMAT, circleId, postId));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public Observable<BaseJsonV2<Object>> dealCircleJoinOrExit(CircleInfo circleInfo) {

        boolean isJoined = circleInfo.getJoined() != null;

        Observable<BaseJsonV2<Object>> observable;

        if (isJoined) {
            observable = mCircleClient.exitCircle(circleInfo.getId());
        } else {
            observable = mCircleClient.joinCircle(circleInfo.getId());
        }
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean();
//        if (isJoined) {
//            // 已经订阅，变为未订阅
//            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE_V2);
//            backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_PUT_EXIT_CIRCLE_FROMAT, String.valueOf(circleInfo.getId())));
//        } else {
//            // 未订阅，变为已订阅
//            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.PUT);
//            backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_PUT_JOIN_CIRCLE_FORMAT, String.valueOf(circleInfo.getId())));
//        }
//        // 启动后台任务
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
//        return null;
    }

    /**
     * 获取我的帖子列表
     *
     * @param limit
     * @param offet
     * @param type
     * @return
     */
    @Override
    public Observable<List<CirclePostListBean>> getMinePostList(Integer limit, Integer offet, Integer type) {
        return dealWithPostList(mCircleClient.getMinePostList(limit, offet, type).subscribeOn(Schedulers.io()));
    }

    /**
     * @param limit    默认 15 ，数据返回条数 默认为15
     * @param offset   默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword  搜索关键词，模糊匹配圈子名称
     * @param group_id 获取某个圈子下面的全部帖子
     * @return 获取全部帖子
     */
    @Override
    public Observable<List<CirclePostListBean>> getAllePostList(Integer limit, Integer offset, String keyword, Long group_id) {
        return dealWithPostList(mCircleClient.getAllePostList(limit, offset, keyword, group_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param limit  默认 15 ，数据返回条数 默认为15
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @return 用户帖子收藏列表
     */
    @Override
    public Observable<List<CirclePostListBean>> getUserCollectPostList(Integer limit, Integer offset) {
        return dealWithPostList(mCircleClient.getUserCollectPostList(limit, offset))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param circleId
     * @param maxId
     * @param type
     * @return
     */
    @Override
    public Observable<List<CirclePostListBean>> getPostListFromCircle(long circleId, long maxId, String type) {
        return dealWithPostList(mCircleClient.getPostListFromCircle(circleId, TSListFragment.DEFAULT_PAGE_SIZE, (int) maxId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(circlePostBean -> {
                    List<CirclePostListBean> data = circlePostBean.getPinneds();
                    if (data != null) {
                        for (CirclePostListBean postListBean : data) {
                            postListBean.setPinned(true);
                        }
                        data.addAll(circlePostBean.getPosts());
                        return data;
                    } else {
                        return circlePostBean.getPosts();
                    }
                }));
    }

    @Override
    public Observable<BaseJsonV2<Object>> cancleCircleMember(long circleId, long memberId) {
        return mCircleClient.cancleCircleMember(circleId, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> appointCircleManager(long circleId, long memberId) {
        return mCircleClient.appointCircleManager(circleId, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> cancleCircleManager(long circleId, long memberId) {
        return mCircleClient.cancleCircleManager(circleId, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> appointCircleBlackList(long circleId, long memberId) {
        return mCircleClient.appointCircleBlackList(circleId, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> cancleCircleBlackList(long circleId, long memberId) {
        return mCircleClient.cancleCircleBlackList(circleId, memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> setCirclePermissions(long circleId, List<String> permissions) {
        return mCircleClient.setCirclePermissions(circleId, permissions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> stickTopPost(Long postId, int day) {
        return mCircleClient.stickTopPost(postId, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> undoTopPost(Long postId) {
        return mCircleClient.undoTopPost(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void saveCircleType() {
        getCategroiesList(0, 0)
                .subscribe(new BaseSubscribeForV2<List<CircleTypeBean>>() {
                    @Override
                    protected void onSuccess(List<CircleTypeBean> data) {
                        mCircleTypeBeanGreenDao.saveMultiData(data);
                    }
                });
    }

    private Observable<List<CirclePostListBean>> dealWithPostList(Observable<List<CirclePostListBean>> observable) {

        return observable
                .observeOn(Schedulers.io())
                .flatMap(postListBeans -> {
                    final List<Object> user_ids = new ArrayList<>();
                    List<CirclePostCommentBean> comments = new ArrayList<>();
                    for (CirclePostListBean circlePostListBean : postListBeans) {
                        user_ids.add(circlePostListBean.getUser_id());
                        if (circlePostListBean.getComments() == null || circlePostListBean.getComments().isEmpty()) {
                            continue;
                        }
                        comments.addAll(circlePostListBean.getComments());
                        for (CirclePostCommentBean commentListBean : circlePostListBean.getComments()) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_to_user_id());
                        }
                    }
                    mCirclePostCommentBeanGreenDao.saveMultiData(comments);
                    if (user_ids.isEmpty()) {
                        return Observable.just(postListBeans);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userinfobeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userinfobeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (CirclePostListBean circlePostListBean : postListBeans) {
                                    circlePostListBean.setUserInfoBean(userInfoBeanSparseArray.get(circlePostListBean
                                            .getUser_id().intValue()));
                                    if (circlePostListBean.getComments() == null || circlePostListBean.getComments()
                                            .isEmpty()) {
                                        continue;
                                    }
                                    for (int i = 0; i < circlePostListBean.getComments().size(); i++) {
                                        UserInfoBean tmpUserinfo = userInfoBeanSparseArray.get((int) circlePostListBean.getComments()
                                                .get(i).getUser_id());
                                        if (tmpUserinfo != null) {
                                            circlePostListBean.getComments().get(i).setCommentUser(tmpUserinfo);
                                        }
                                        if (circlePostListBean.getComments().get(i).getReply_to_user_id() == 0) {
                                            // 如果 reply_user_id = 0 回复动态
                                            UserInfoBean userInfoBean = new UserInfoBean();
                                            userInfoBean.setUser_id(0L);
                                            circlePostListBean.getComments().get(i).setReplyUser(userInfoBean);
                                        } else {
                                            if (userInfoBeanSparseArray.get((int) circlePostListBean.getComments()
                                                    .get(i).getReply_to_user_id()) != null) {
                                                circlePostListBean.getComments().get(i).setReplyUser
                                                        (userInfoBeanSparseArray.get((int) circlePostListBean.getComments()
                                                                .get(i).getReply_to_user_id()));
                                            }
                                        }
                                    }

                                }
                                return postListBeans;
                            });
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CircleInfo> getCircleInfo(long circleId) {
        return mCircleClient.getCircleInfo(circleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CircleEarningListBean>> getCircleEarningList(Long circleId, Long start, Long end,
                                                                        Long after, Long limit, String type) {

        return mCircleClient.getCircleEarningList(circleId, start, end, after, limit, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CircleReportListBean>> getCircleReportList(Long groupId, Integer status,
                                                                      Integer after, Integer limit, Long start, Long end) {
        return mCircleClient.getCircleReportList(groupId, status, after, limit, start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedCircleReport(Long reportId) {
        return mCircleClient.approvedCircleReport(reportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refuseCircleReport(Long reportId) {
        return mCircleClient.refuseCircleReport(reportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * @param circleId
     * @param after
     * @param limit
     * @param type     默认 all, all-所有, manager-管理员, member-成员, blacklist-黑名单, audit - 带审核
     * @param name     仅仅用于搜索
     * @return
     */
    @Override
    public Observable<List<CircleMembers>> getCircleMemberList(long circleId, int after, int limit, String type, String name) {
        if (TextUtils.isEmpty(name)) {
            name = null;
        }
        return mCircleClient.getCircleMemberList(circleId, limit, after, type, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 转让圈子
     *
     * @param circleId
     * @param userId
     * @return
     */
    @Override
    public Observable<CircleMembers> attornCircle(long circleId, long userId) {
        return mCircleClient.attornCircle(circleId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CirclePostListBean> getPostDetail(long circleId, long postId) {
        return mCircleClient.getPostDetail(circleId, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CirclePostCommentBean>> getPostComments(long postId, int limit, int after) {
        return getPostCommentList(postId, (long) after).flatMap(circleCommentZip -> {
            for (CirclePostCommentBean pinned : circleCommentZip.getPinneds()) {
                pinned.setPinned(true);
            }
            circleCommentZip.getPinneds().addAll(circleCommentZip.getComments());
            return Observable.just(circleCommentZip.getPinneds())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        });
    }

    @Override
    public Observable<CircleCommentZip> getPostCommentList(long postId, Long maxId) {
        return mCircleClient.getPostComments(postId, TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue())
                .subscribeOn(Schedulers.io())
                .flatMap(circleCommentZip -> {
                    final List<Object> user_ids = new ArrayList<>();

                    if (circleCommentZip.getPinneds() != null) {
                        for (CirclePostCommentBean commentListBean : circleCommentZip.getPinneds()) {
                            user_ids.add(commentListBean.getUser_id());
                            commentListBean.setPinned(true);
                            user_ids.add(commentListBean.getReply_to_user_id());
                            user_ids.add(commentListBean.getTo_user_id());
                        }
                    }
                    if (circleCommentZip.getComments() != null) {
                        for (CirclePostCommentBean commentListBean : circleCommentZip.getComments()) {
                            user_ids.add(commentListBean.getUser_id());
                            commentListBean.setPinned(false);
                            user_ids.add(commentListBean.getReply_to_user_id());
                            user_ids.add(commentListBean.getTo_user_id());
                        }
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(circleCommentZip);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userInfoBeanList -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                        SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeanList) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                            .intValue(), userInfoBean);
                                }
                                dealCommentData(circleCommentZip.getPinneds(), userInfoBeanSparseArray);
                                dealCommentData(circleCommentZip.getComments(), userInfoBeanSparseArray);
                                return circleCommentZip;
                            });
                });
    }

    private void dealCommentData(List<CirclePostCommentBean> list, SparseArray<UserInfoBean> userInfoBeanSparseArray) {
        if (list != null) {
            for (CirclePostCommentBean commentListBean : list) {
                commentListBean.setCommentUser(userInfoBeanSparseArray.get((int) commentListBean.getUser_id()));
                if (commentListBean.getReply_to_user_id() == 0) {
                    // reply_user_id = 0 回复动态
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(0L);
                    commentListBean.setReplyUser(userInfoBean);
                } else {
                    commentListBean.setReplyUser(userInfoBeanSparseArray.get((int) commentListBean.getReply_to_user_id()));
                }
            }
        }
    }


    @Override
    public Observable<List<CircleInfo>> getRecommendCircle(int limit, int offet, String type) {
        return mCircleClient.getRecommendCircle(limit, offet, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<List<CircleInfo>> getCircleList(long categoryId, long maxId) {
        return mCircleClient.getCircleList(categoryId, TSListFragment.DEFAULT_PAGE_SIZE, (int) maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void handleFollow(UserInfoBean userInfoBean) {
        mUserInfoRepository.handleFollow(userInfoBean);
    }
}
