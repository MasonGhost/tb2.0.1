package com.zhiyicx.thinksnsplus.modules.channel.group_dynamic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupDynamicCommentListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupDynamicListBeanGreenDaoimpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_PAGE_MAX_ID;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */

public class GroupDynamicDetailPresenter extends AppBasePresenter<GroupDynamicDetailContract.Repository,
        GroupDynamicDetailContract.View> implements GroupDynamicDetailContract.Presenter,
        OnShareCallbackListener {
    @Inject
    GroupDynamicListBeanGreenDaoimpl mGroupDynamicListBeanGreenDaoimpl;
    @Inject
    GroupDynamicCommentListBeanGreenDaoImpl mGroupDynamicCommentListBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
//    @Inject
//    GroupDynamicLikeListBeanDao mGroupDynamicLikeListBeanDao;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    CommentRepository mCommentRepository;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    public SharePolicy mSharePolicy;

    private boolean mIsNeedDynamicListRefresh = false;

    @Inject
    public GroupDynamicDetailPresenter(GroupDynamicDetailContract.Repository repository,
                                       GroupDynamicDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null) {
            return;
        }
        // 更新点赞列表
        if (!isLoadMore) {
            getDynamicDigList(mRootView.getCurrentDynamic().getGroup_id(), mRootView.getCurrentDynamic().getId(), maxId);
        }
        // 更新评论列表
        mRepository.getGroupDynamicCommentList(mRootView.getCurrentDynamic().getGroup_id(), mRootView.getCurrentDynamic().getId(), maxId)
                .subscribe(new BaseSubscribeForV2<List<GroupDynamicCommentListBean>>() {
                    @Override
                    protected void onSuccess(List<GroupDynamicCommentListBean> data) {
                        if (!isLoadMore) { // 刷新时，把自己还未发送成功的评论加载到前面
                            List<GroupDynamicCommentListBean> myComments = mGroupDynamicCommentListBeanGreenDao
                                    .getMySendingComment(mRootView.getCurrentDynamic().getGroup_id());
                            if (!myComments.isEmpty()) {
                                for (int i = 0; i < myComments.size(); i++) {
                                    myComments.get(i).setCommentUser(mUserInfoBeanGreenDao
                                            .getSingleDataFromCache(myComments.get(i).getUser_id
                                                    ()));
                                    if (myComments.get(i).getReply_to_user_id() != 0) {
                                        myComments.get(i).setReplyUser(mUserInfoBeanGreenDao
                                                .getSingleDataFromCache(myComments.get(i)
                                                        .getReply_to_user_id()));
                                    }
                                }
                                myComments.addAll(data);
                                data.clear();
                                data.addAll(myComments);
                            }
                        }
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
    }

    @Override
    public List<GroupDynamicCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null || AppApplication.getmCurrentLoginAuth() ==
                null) {
            return new ArrayList<>();
        }
        // 从数据库获取关注状态，如果没有从服务器获取
        FollowFansBean followFansBean = mFollowFansBeanGreenDao.getFollowState(AppApplication
                .getmCurrentLoginAuth().getUser_id(), mRootView.getCurrentDynamic().getUser_id());
        if (followFansBean != null) {
            mRootView.upDateFollowFansState(followFansBean.getFollowState());
        }
        // 从数据库获取评论列表
        return null/*mDynamicCommentBeanGreenDao.getLocalComments(mRootView.getCurrentDynamic()
                .getFeed_mark())*/;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupDynamicCommentListBean> data, boolean isLoadMore) {
        if (data == null) {
            return false;
        }
//        mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(mRootView.getCurrentDynamic()
//                .getFeed_mark());// 删除本条动态的本地评论
//        mDynamicCommentBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void getCurrentDynamicDetail(long group_id, long dynamic_id) {
        Subscription subscription = mRepository.getGroupDynamicDetail(group_id, dynamic_id)
                .subscribe(new BaseSubscribeForV2<GroupDynamicListBean>() {
                    @Override
                    protected void onSuccess(GroupDynamicListBean data) {
//                        data.setTop(topFlag);
                        mRootView.initDynamicDetail(data);
                        // 存入数据库
                        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        LogUtils.e(message);
                        handleDynamicHasBeDeleted(code, dynamic_id);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getDetailAll(long group_id, long dynamic_id, Long max_id, String user_ids) {
        Subscription subscription = Observable.zip(mRepository.getGroupDynamicDigList(group_id, dynamic_id, max_id)
                , mRepository.getUserFollowState(user_ids)
                , mRepository.getGroupDynamicCommentList(group_id, dynamic_id, max_id)
                , (followFansBeen, listBaseJson2, listBaseJson3) -> {
                    GroupDynamicListBean dynamicBean = new GroupDynamicListBean();
                    if (listBaseJson2.isStatus()) {
                        dynamicBean.setMGroupDynamicLikeListBeanList(followFansBeen);
                        mFollowFansBeanGreenDao.insertOrReplace(listBaseJson2.getData().get(0));
                        // 保存关注状态
                        List<GroupDynamicCommentListBean> data = listBaseJson3;
                        // 取出本地未发送成功的评论
                        List<GroupDynamicCommentListBean> myComments = mGroupDynamicCommentListBeanGreenDao
                                .getMySendingComment(mRootView.getCurrentDynamic().getGroup_id());
                        if (!myComments.isEmpty()) {
                            for (int i = 0; i < myComments.size(); i++) {
                                myComments.get(i).setCommentUser(mUserInfoBeanGreenDao
                                        .getSingleDataFromCache(myComments.get(i).getUser_id()));
                                if (myComments.get(i).getReply_to_user_id() != 0) {
                                    myComments.get(i).setReplyUser(mUserInfoBeanGreenDao
                                            .getSingleDataFromCache(myComments.get(i)
                                                    .getReply_to_user_id()));
                                }
                            }
                            myComments.addAll(data);
                            data.clear();
                            data.addAll(myComments);
                        }
                        dynamicBean.setCommentslist(data);

                    } else {
//                                dynamicBean.setStatus(listBaseJson2.isStatus());
//                                dynamicBean.setMessage(listBaseJson2.getMessage());
//                                dynamicBean.setCode(listBaseJson2.getCode());
                    }

                    return dynamicBean;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<GroupDynamicListBean>() {
                    @Override
                    protected void onSuccess(GroupDynamicListBean data) {
                        mRootView.getCurrentDynamic().setCommentslist(data.getCommentslist());
                        mRootView.getCurrentDynamic().setMGroupDynamicLikeListBeanList(data.getMGroupDynamicLikeListBeanList());
                        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getCurrentDynamic());
                        mRootView.allDataReady();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        LogUtils.i(message);
                        handleDynamicHasBeDeleted(code, dynamic_id);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 处理动态被删除了
     */
    private void handleDynamicHasBeDeleted(int code, Long dynamic_id) {
        if (code == ErrorCodeConfig.DYNAMIC_HAS_BE_DELETED) {
            mGroupDynamicListBeanGreenDaoimpl.deleteSingleCache(dynamic_id);
            mRootView.dynamicHasBeDeleted();
        } else {
            mRootView.loadAllError();
        }
    }

    @Override
    public void getDynamicDigList(long group_id, long dynamic_id, long max_id) {
        Subscription subscription = mRepository.getGroupDynamicDigList(group_id, dynamic_id, max_id)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(ApiConfig.DEFAULT_MAX_RETRY_COUNT, 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<FollowFansBean>>() {
                    @Override
                    protected void onSuccess(List<FollowFansBean> data) {
                        mRootView.setDigHeadIcon(data);
                        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getCurrentDynamic());
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleLike(boolean isLiked, long group_id, long dynamic_id, final GroupDynamicListBean
            dynamicToolBean) {
        mIsNeedDynamicListRefresh = true;
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        // 更新UI
        mRootView.setLike(isLiked);
        mRootView.getCurrentDynamic().setDiggs(isLiked ? mRootView.getCurrentDynamic()
                .getDiggs() + 1 : mRootView.getCurrentDynamic().getDiggs() - 1);
        mRootView.getCurrentDynamic().setIs_digg(isLiked ? 1:0);
        List<FollowFansBean> digUsers = mRootView.getCurrentDynamic().getMGroupDynamicLikeListBeanList();
        if (digUsers == null){
            digUsers = new ArrayList<>();
        }
        if (!isLiked) {// 取消喜欢，修改修换的用户信息
            int digUserSize = digUsers.size();
            for (int i = 0; i < digUserSize; i++) {
                if (digUsers.get(i).getTargetUserId() == AppApplication.getmCurrentLoginAuth()
                        .getUser_id()) {
                    digUsers.remove(i);
                    break;
                }
            }
        } else {// 喜欢
            FollowFansBean myFollowFansBean = new FollowFansBean();
            long user_id = AppApplication.getmCurrentLoginAuth().getUser_id();
            UserInfoBean mineUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(user_id);
            myFollowFansBean.setTargetUserInfo(mineUserInfo);
            myFollowFansBean.setTargetUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
            myFollowFansBean.setOriginUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
            myFollowFansBean.setOrigintargetUser("");
            digUsers.add(0, myFollowFansBean);// 把数据加到第一个
        }
        mRootView.getCurrentDynamic().setMGroupDynamicLikeListBeanList(digUsers);
        mRootView.updateCommentCountAndDig();
        // 更新数据库
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(dynamicToolBean);
        // 通知服务器
        mRepository.handleLike(isLiked, group_id, dynamic_id);
    }

    @Override
    public void handleCollect(GroupDynamicListBean dynamicBean) {
        // 收藏
        // 修改数据
        boolean is_collection = dynamicBean.getIs_collection() == 1;// 旧状态
        // 新状态
        dynamicBean.setIs_collection(is_collection ? 0 : 1);
        dynamicBean.setCollections(is_collection ? dynamicBean.getCollections() - 1 : dynamicBean.getCollections() + 1);
        boolean newCollectState = !is_collection;
        // 更新UI
        mRootView.setCollect(newCollectState);
        // 更新数据库
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(dynamicBean);
        // 通知服务器
        mRepository.handleCollect(is_collection, dynamicBean.getGroup_id(), dynamicBean.getId());
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_GROUP_DYNAMIC);
    }

    @Override
    public void shareDynamic(GroupDynamicListBean dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getContent()) ? mContext
                .getString(R.string
                        .share_dynamic) : dynamicBean.getContent());
        if (bitmap != null) {
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory
                    .decodeResource(mContext.getResources(), R.mipmap.icon_256)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean.getId()
                == null ? "" : dynamicBean.getId()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleFollowUser(FollowFansBean followFansBean) {
        BackgroundRequestTaskBean backgroundRequestTaskBean = null;
        if (followFansBean.getOrigin_follow_status() == FollowFansBean.UNFOLLOWED_STATE) {
            // 当前未关注，进行关注
            followFansBean.setOrigin_follow_status(FollowFansBean.IFOLLOWED_STATE);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.POST);
            backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_FOLLOW_USER);
        } else {
            // 已关注，取消关注
            followFansBean.setOrigin_follow_status(FollowFansBean.UNFOLLOWED_STATE);
            // 进行后台任务请求
            backgroundRequestTaskBean = new BackgroundRequestTaskBean();
            backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.DELETE);
            backgroundRequestTaskBean.setPath(ApiConfig.APP_PATH_CANCEL_FOLLOW_USER);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", followFansBean.getTargetUserId() + "");
        backgroundRequestTaskBean.setParams(hashMap);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        mFollowFansBeanGreenDao.insertOrReplace(followFansBean);
        mRootView.upDateFollowFansState(followFansBean.getFollowState());
    }

    @Override
    public void getUserFollowState(String user_ids) {
        FollowFansBean followFansBean = mFollowFansBeanGreenDao.getFollowState(AppApplication
                .getmCurrentLoginAuth().getUser_id(), mRootView.getCurrentDynamic().getUser_id());
        if (followFansBean != null) {
            mRootView.initFollowState(followFansBean);
        }
    }

    @Override
    public void deleteCommentV2(long comment_id, int commentPosition) {
        mIsNeedDynamicListRefresh = true;
        mRootView.getCurrentDynamic().setComments(mRootView.getCurrentDynamic()
                .getComments() - 1);
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getCurrentDynamic());
        mGroupDynamicCommentListBeanGreenDao.deleteSingleCache(mRootView.getCurrentDynamic().getCommentslist()
                .get(commentPosition));
        mRootView.getListDatas().remove(commentPosition);
        if (mRootView.getListDatas().isEmpty()) {
            GroupDynamicCommentListBean emptyData = new GroupDynamicCommentListBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.deleteGroupComment(mRootView.getCurrentDynamic().getGroup_id(), mRootView.getCurrentDynamic().getId(), comment_id);
    }

    /**
     * check current dynamic is has been deleted
     *
     * @param user_id   the dynamic is belong to
     * @param feed_mark the dynamic's feed_mark
     */
    @Override
    public boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark) {
//        if (user_id == AppApplication.getmCurrentLoginAuth().getUser_id() &&
//                mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(feed_mark) == null) { //
//            // 检查当前动态是否已经被删除了
//            return true;
//        }
        return false;
    }

    @Override
    public void sendCommentV2(long replyToUserId, String commentContent) {
        mIsNeedDynamicListRefresh = true;
        // 生成一条评论
        GroupDynamicCommentListBean creatComment = new GroupDynamicCommentListBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setContent(commentContent);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setFeed_id(mRootView.getCurrentDynamic().getId().intValue());
        creatComment.setReply_to_user_id(replyToUserId);
        if (replyToUserId == 0) { //当回复动态的时候
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mGroupDynamicCommentListBeanGreenDao.insertOrReplace(creatComment);
//         处理评论数
        mRootView.getCurrentDynamic().setComments(mRootView.getCurrentDynamic()
                .getComments() + 1);
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getCurrentDynamic());
        if (mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas()
                .get(0).getContent())) {
            mRootView.getListDatas().clear();
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.sendGroupComment(commentContent,
                (long) mRootView.getCurrentDynamic().getGroup_id(),
                mRootView.getCurrentDynamic().getId(),
                replyToUserId,
                creatComment.getComment_mark());
    }

    /**
     * 处理发送动态数据,动态发布成功回调
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(GroupDynamicListBean dynamicCommentBean) {
        LogUtils.d(TAG, "dynamic send success dynamicCommentBean = " + dynamicCommentBean
                .toString());
        Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(dynamicCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
//                    for (int i = 0; i < size; i++) {
//                        if (mRootView.getListDatas().get(i).getFeed_mark().equals
//                                (dynamicCommentBean1.getFeed_mark())) {
//                            dynamicPosition = i;
//                            mRootView.getListDatas().get(i).setState(dynamicCommentBean1.getState
//                                    ());
//                            mRootView.getListDatas().get(i).setComment_id
//                                    (dynamicCommentBean1.getComment_id());
//                            mRootView.getListDatas().get(i).setComment_mark
//                                    (dynamicCommentBean1.getComment_mark());
//                            break;
//                        }
//                    }
                    return dynamicPosition;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData(); // 加上 header
                    }

                }, throwable -> throwable.printStackTrace());

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_DYNAMIC)
    public void updateDynamic(Bundle data) {
        GroupDynamicListBean dynamicBean = data.getParcelable(DYNAMIC_DETAIL_DATA);
        mRootView.updateDynamic(dynamicBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 清除占位图数据
        if (mRootView.getListDatas() != null && mRootView.getListDatas().size() == 1 && TextUtils
                .isEmpty(mRootView.getListDatas().get(0).getContent())) {
            mRootView.getListDatas().clear();
        }
        Bundle bundle = mRootView.getArgumentsBundle();
        if (bundle != null && bundle.containsKey(DYNAMIC_DETAIL_DATA)) {
            if (mRootView.getListDatas() == null || mRootView.getListDatas().isEmpty()) {
                return;// 你说起气不气，这里这个个更新导致的bug不静心真的发现不了啊
            }
            mRootView.getCurrentDynamic().setCommentslist(mRootView.getListDatas());
            bundle.putParcelable(DYNAMIC_DETAIL_DATA, mRootView.getCurrentDynamic());
            bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, mIsNeedDynamicListRefresh);
            EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
        }
    }

    @Override
    public List<SystemConfigBean.Advert> getAdvert() {
        List<SystemConfigBean.Advert> imageAdvert = new ArrayList<>();
        for (SystemConfigBean.Advert advert : mSystemRepository.getBootstrappersInfoFromLocal()
                .getAdverts()) {
            if (advert.getImageAdvert() != null) {
                imageAdvert.add(advert);
            }
        }
        return imageAdvert;
    }

    @Override
    public void checkNote(int note) {

    }

    @Override
    public void payNote(final int imagePosition, int note, boolean isImage) {
        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataByUserId((long) AppApplication.getmCurrentLoginAuth().getUser_id());
        double balance = 0;
        if (walletBean != null) {
            balance = walletBean.getBalance();
        }
        mCommentRepository.paykNote(note)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(new Func1<BaseJsonV2<String>, Observable<BaseJsonV2<String>>>() {
                    @Override
                    public Observable<BaseJsonV2<String>> call(BaseJsonV2<String>
                                                                       stringBaseJsonV2) {
                        if (isImage) {
                            return Observable.just(stringBaseJsonV2);
                        }
                        return mRepository.getDynamicDetailBeanV2(mRootView.getCurrentDynamic()
                                .getId())
                                .flatMap(new Func1<DynamicDetailBeanV2,
                                        Observable<BaseJsonV2<String>>>() {
                                    @Override
                                    public Observable<BaseJsonV2<String>> call
                                            (DynamicDetailBeanV2 detailBeanV2) {
                                        stringBaseJsonV2.setData(detailBeanV2.getFeed_content());
                                        return Observable.just(stringBaseJsonV2);
                                    }
                                });
                    }
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.hideCenterLoading();
//                        if (isImage) {
//                            mRootView.getCurrentDynamic().getImages().get(imagePosition).setPaid
//                                    (true);
//                        } else {
//                            mRootView.getCurrentDynamic().getPaid_node().setPaid(true);
//                            mRootView.getCurrentDynamic().setFeed_content(data.getData());
//                        }

//                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
                        Bundle bundle = new Bundle();
//                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mRootView.getCurrentDynamic();
//                        if (mRootView.getCurrentDynamic().getComments().get(0).getComment_mark()
//                                == null) {
//                            dynamicDetailBeanV2.getComments().remove(0);
//                        }
//                        bundle.putParcelable(DYNAMIC_DETAIL_DATA, dynamicDetailBeanV2);
                        bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string
                                .transaction_success));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string
                                .transaction_fail));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.hideCenterLoading();
                    }
                });
    }

    public void setNeedDynamicListRefresh(boolean needDynamicListRefresh) {
        mIsNeedDynamicListRefresh = needDynamicListRefresh;
    }

    @Override
    public void onStart(Share share) {
    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }
}
