package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
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
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
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
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_PAGE_MAX_ID;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean.STATUS_DIGG_FEED_CHECKED;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class DynamicDetailPresenter extends AppBasePresenter<DynamicDetailContract.Repository,
        DynamicDetailContract.View> implements DynamicDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    CommentRepository mCommentRepository;

    @Inject
    public SharePolicy mSharePolicy;

    private boolean mIsNeedDynamicListRefresh = false;

    @Inject
    public DynamicDetailPresenter(DynamicDetailContract.Repository repository, DynamicDetailContract.View rootView) {
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
            getDynamicDigList(mRootView.getCurrentDynamic().getId(), maxId);
        }
        // 更新评论列表
        mRepository.getDynamicCommentList(mRootView.getCurrentDynamic().getFeed_mark(), mRootView
                .getCurrentDynamic().getId(), maxId)
                .subscribe(new BaseSubscribe<List<DynamicCommentBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicCommentBean> data) {
                        if (!isLoadMore) { // 刷新时，把自己还未发送成功的评论加载到前面
                            List<DynamicCommentBean> myComments = mDynamicCommentBeanGreenDao.getMySendingComment(mRootView.getCurrentDynamic().getFeed_mark());
                            if (!myComments.isEmpty()) {
                                for (int i = 0; i < myComments.size(); i++) {
                                    myComments.get(i).setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(myComments.get(i).getUser_id()));
                                    if (myComments.get(i).getReply_to_user_id() != 0) {
                                        myComments.get(i).setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(myComments.get(i).getReply_to_user_id()));
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
    public List<DynamicCommentBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null || AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        // 从数据库获取关注状态，如果没有从服务器获取
        FollowFansBean followFansBean = mFollowFansBeanGreenDao.getFollowState(AppApplication.getmCurrentLoginAuth().getUser_id(), mRootView.getCurrentDynamic().getUser_id());
        if (followFansBean != null) {
            mRootView.upDateFollowFansState(followFansBean.getFollowState());
        }
        // 从数据库获取评论列表
        return mDynamicCommentBeanGreenDao.getLocalComments(mRootView.getCurrentDynamic().getFeed_mark());
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicCommentBean> data, boolean isLoadMore) {
        if (data == null) {
            return false;
        }
        mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(mRootView.getCurrentDynamic().getFeed_mark());// 删除本条动态的本地评论
        mDynamicCommentBeanGreenDao.insertOrReplace(data);
        return true;
    }

    @Override
    public void getCurrentDynamic(final long feed_id) {
        Subscription subscription = mRepository.getDynamicListV2(ApiConfig.DYNAMIC_TYPE_NEW,
                DEFAULT_PAGE_MAX_ID, false)
                .subscribe(new BaseSubscribeForV2<List<DynamicDetailBeanV2>>() {
                    @Override
                    protected void onSuccess(List<DynamicDetailBeanV2> data) {
                        if (data.isEmpty()) {
                            onFailure("", ErrorCodeConfig.DYNAMIC_HAS_BE_DELETED);
                            return;
                        }
                        mRootView.initDynamicDetial(data.get(0));
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(data.get(0));
                        mDynamicCommentBeanGreenDao.insertOrReplace(data.get(0).getComments());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        LogUtils.e(message);
                        handleDynamicHasBeDeleted(code, feed_id);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getCurrentDynamicDetail(final long feed_id) {
        Subscription subscription = mRepository.getDynamicDetailBeanV2(feed_id)
                .subscribe(new BaseSubscribeForV2<DynamicDetailBeanV2>() {
                    @Override
                    protected void onSuccess(DynamicDetailBeanV2 data) {
                        mRootView.initDynamicDetial(data);
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(data);
                        mDynamicCommentBeanGreenDao.insertOrReplace(data.getComments());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        LogUtils.e(message);
                        handleDynamicHasBeDeleted(code, feed_id);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getDetailAll(final Long feed_id, Long max_id, final String user_ids) {
        Subscription subscription = Observable.zip(mRepository.getDynamicDigList(feed_id, max_id)
                , mRepository.getUserFollowState(user_ids)
                , mRepository.getDynamicCommentListV2(mRootView.getCurrentDynamic().getFeed_mark(),
                        mRootView.getCurrentDynamic().getId(), max_id)
                , new Func3<BaseJson<List<FollowFansBean>>, BaseJson<List<FollowFansBean>>, List<DynamicCommentBean>, DynamicDetailBeanV2>() {
                    @Override
                    public DynamicDetailBeanV2 call(BaseJson<List<FollowFansBean>> listBaseJson, BaseJson<List<FollowFansBean>> listBaseJson2, List<DynamicCommentBean> listBaseJson3) {
                        DynamicDetailBeanV2 dynamicBean = new DynamicDetailBeanV2();
                        if (listBaseJson.isStatus()) {
                            if (listBaseJson2.isStatus()) {
                                dynamicBean.setDigUserInfoList(listBaseJson.getData());
                                mFollowFansBeanGreenDao.insertOrReplace(listBaseJson2.getData().get(0)); // 保存关注状态
                                List<DynamicCommentBean> data = listBaseJson3;
                                // 取出本地为发送成功的评论
                                List<DynamicCommentBean> myComments = mDynamicCommentBeanGreenDao.getMySendingComment(mRootView.getCurrentDynamic().getFeed_mark());
                                if (!myComments.isEmpty()) {
                                    for (int i = 0; i < myComments.size(); i++) {
                                        myComments.get(i).setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(myComments.get(i).getUser_id()));
                                        if (myComments.get(i).getReply_to_user_id() != 0) {
                                            myComments.get(i).setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(myComments.get(i).getReply_to_user_id()));
                                        }
                                    }
                                    myComments.addAll(data);
                                    data.clear();
                                    data.addAll(myComments);
                                }
                                dynamicBean.setComments(data);

                            } else {
//                                dynamicBean.setStatus(listBaseJson2.isStatus());
//                                dynamicBean.setMessage(listBaseJson2.getMessage());
//                                dynamicBean.setCode(listBaseJson2.getCode());
                            }
                        } else {
//                            dynamicBean.setStatus(listBaseJson.isStatus());
//                            dynamicBean.setMessage(listBaseJson.getMessage());
//                            dynamicBean.setCode(listBaseJson.getCode());
                        }
                        return dynamicBean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<DynamicDetailBeanV2>() {
                    @Override
                    protected void onSuccess(DynamicDetailBeanV2 data) {
                        mRootView.getCurrentDynamic().setComments(data.getComments());
                        mRootView.getCurrentDynamic().setDigUserInfoList(data.getDigUserInfoList());
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
                        mRootView.allDataReady();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        LogUtils.i(message);
                        handleDynamicHasBeDeleted(code, feed_id);
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
     *
     * @param code
     * @param feed_id
     */
    private void handleDynamicHasBeDeleted(int code, Long feed_id) {
        if (code == ErrorCodeConfig.DYNAMIC_HAS_BE_DELETED) {
            mDynamicDetailBeanV2GreenDao.deleteDynamicByFeedId(feed_id);
            mRootView.dynamicHasBeDeleted();
        } else {
            mRootView.loadAllError();
        }
    }

    @Override
    public void getDynamicDigList(Long feed_id, Long max_id) {
        Subscription subscription = mRepository.getDynamicDigList(feed_id, max_id)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(ApiConfig.DEFAULT_MAX_RETRY_COUNT, 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<List<FollowFansBean>>() {
                    @Override
                    protected void onSuccess(List<FollowFansBean> data) {
                        mRootView.setDigHeadIcon(data);
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
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
    public void handleLike(boolean isLiked, final Long feed_id, final DynamicDetailBeanV2 dynamicToolBean) {
        mIsNeedDynamicListRefresh = true;
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        // 更新UI
        mRootView.setLike(isLiked);
        mRootView.getCurrentDynamic().setFeed_digg_count(isLiked ? mRootView.getCurrentDynamic().getFeed_digg_count() + 1 : mRootView.getCurrentDynamic().getFeed_digg_count() - 1);
        mRootView.getCurrentDynamic().setHas_digg(isLiked);
        if (!isLiked) {// 取消喜欢，修改修换的用户信息
            List<FollowFansBean> digUsers = mRootView.getCurrentDynamic().getDigUserInfoList();
            int digUserSize = digUsers.size();
            for (int i = 0; i < digUserSize; i++) {
                if (digUsers.get(i).getTargetUserId() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
                    digUsers.remove(i);
                    break;
                }
            }
        } else {// 喜欢
            FollowFansBean myFollowFansBean = new FollowFansBean();
            UserInfoBean mineUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id());
            myFollowFansBean.setTargetUserInfo(mineUserInfo);
            myFollowFansBean.setTargetUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
            myFollowFansBean.setOriginUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
            myFollowFansBean.setOrigintargetUser("");
            mRootView.getCurrentDynamic().getDigUserInfoList().add(0, myFollowFansBean);// 把数据加到第一个
        }
        mRootView.updateCommentCountAndDig();

        // 更新数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicToolBean);
        // 通知服务器
        mRepository.handleLike(isLiked, feed_id);
    }

    @Override
    public void handleCollect(DynamicDetailBeanV2 dynamicBean) {
        // 收藏
        // 修改数据
        boolean is_collection = dynamicBean.isHas_collect();// 旧状态
        // 新状态
        dynamicBean.setHas_collect(!is_collection);
        boolean newCollectState = !is_collection;
        // 更新UI
        mRootView.setCollect(newCollectState);
        // 更新数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", dynamicBean.getId());
        // 后台处理
        if (newCollectState) {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST, params);
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        }
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_COLLECT_FORMAT, dynamicBean.getId()));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_DYNAMIC);
    }

    @Override
    public void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getFeed_content()) ? mContext.getString(R.string
                .share_dynamic) : dynamicBean.getFeed_content());
        if (bitmap != null) {
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256)));
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
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        // 本地数据库和ui进行刷新
        mFollowFansBeanGreenDao.insertOrReplace(followFansBean);
        mRootView.upDateFollowFansState(followFansBean.getFollowState());
    }

    @Override
    public void getUserFollowState(String user_ids) {
        FollowFansBean followFansBean = mFollowFansBeanGreenDao.getFollowState(AppApplication.getmCurrentLoginAuth().getUser_id(), mRootView.getCurrentDynamic().getUser_id());
        if (followFansBean != null) {
            mRootView.initFollowState(followFansBean);
        }
    }

    @Override
    public void deleteComment(long comment_id, int commentPositon) {
        mIsNeedDynamicListRefresh = true;
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic().getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        mDynamicCommentBeanGreenDao.deleteSingleCache(mRootView.getCurrentDynamic().getComments().get(commentPositon));
        mRootView.getListDatas().remove(commentPositon);
        if (mRootView.getListDatas().isEmpty()) {
            DynamicCommentBean emptyData = new DynamicCommentBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.deleteComment(mRootView.getCurrentDynamic().getId(), comment_id);
    }

    @Override
    public void deleteCommentV2(long comment_id, int commentPosition) {
        mIsNeedDynamicListRefresh = true;
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic().getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        mDynamicCommentBeanGreenDao.deleteSingleCache(mRootView.getCurrentDynamic().getComments().get(commentPosition));
        mRootView.getListDatas().remove(commentPosition);
        if (mRootView.getListDatas().isEmpty()) {
            DynamicCommentBean emptyData = new DynamicCommentBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.deleteCommentV2(mRootView.getCurrentDynamic().getId(), comment_id);
    }

    /**
     * check current dynamic is has been deleted
     *
     * @param user_id   the dynamic is belong to
     * @param feed_mark the dynamic's feed_mark
     * @return
     */
    @Override
    public boolean checkCurrentDynamicIsDeleted(Long user_id, Long feed_mark) {
        if (user_id == AppApplication.getmCurrentLoginAuth().getUser_id() && mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(feed_mark) == null) { // 检查当前动态是否已经被删除了
            return true;
        }
        return false;
    }

    /**
     * send a commment
     *
     * @param replyToUserId  comment  to who
     * @param commentContent comment content
     */
    @Override
    public void sendComment(long replyToUserId, String commentContent) {
        mIsNeedDynamicListRefresh = true;
        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getCurrentDynamic().getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
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
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic().getFeed_comment_count() + 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        if (mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas().get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.sendComment(commentContent, mRootView.getCurrentDynamic().getId(),
                replyToUserId, creatComment.getComment_mark());

    }

    @Override
    public void sendCommentV2(long replyToUserId, String commentContent) {
        mIsNeedDynamicListRefresh = true;
        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getCurrentDynamic().getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
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
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic().getFeed_comment_count() + 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        if (mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas().get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.sendCommentV2(commentContent, mRootView.getCurrentDynamic().getId(),
                replyToUserId, creatComment.getComment_mark());
    }

    /**
     * 处理发送动态数据,动态发布成功回调
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {
        LogUtils.d(TAG, "dynamic send success dynamicCommentBean = " + dynamicCommentBean.toString());
        Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DynamicCommentBean, Integer>() {
                    @Override
                    public Integer call(DynamicCommentBean dynamicCommentBean) {
                        int size = mRootView.getListDatas().size();
                        int dynamicPosition = -1;
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicCommentBean.getFeed_mark())) {
                                dynamicPosition = i;
                                mRootView.getListDatas().get(i).setState(dynamicCommentBean.getState());
                                mRootView.getListDatas().get(i).setComment_id(dynamicCommentBean.getComment_id());
                                mRootView.getListDatas().get(i).setComment_mark(dynamicCommentBean.getComment_mark());
                                break;
                            }
                        }
                        return dynamicPosition;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer != -1) {
                            mRootView.refreshData(); // 加上 header
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 清除占位图数据
        if (mRootView.getListDatas() != null && mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas().get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        Bundle bundle = mRootView.getArgumentsBundle();
        if (bundle != null && bundle.containsKey(DYNAMIC_DETAIL_DATA)) {
            mRootView.getCurrentDynamic().setComments(mRootView.getListDatas());
            bundle.putParcelable(DYNAMIC_DETAIL_DATA, mRootView.getCurrentDynamic());
            bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, mIsNeedDynamicListRefresh);
            EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
        }
    }

    @Override
    public List<SystemConfigBean.Advert> getAdvert() {
        List<SystemConfigBean.Advert> imageAdvert = new ArrayList<>();
        for (SystemConfigBean.Advert advert : mSystemRepository.getBootstrappersInfoFromLocal().getAdverts()) {
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
    public void payNote(final int imagePosition, int note) {
        mCommentRepository.paykNote(note)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRootView.showCenterLoading(mContext.getString(R.string.transaction_doing));
                    }
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.hideCenterLoading();
                        mRootView.getCurrentDynamic().getImages().get(imagePosition).setPaid(true);
                        mRootView.reLaodImage();
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
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
