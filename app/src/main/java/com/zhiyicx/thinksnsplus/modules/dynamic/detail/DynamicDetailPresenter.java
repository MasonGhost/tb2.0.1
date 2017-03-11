package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailContract.Repository,
        DynamicDetailContract.View> implements DynamicDetailContract.Presenter {

    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public DynamicDetailPresenter(DynamicDetailContract.Repository repository, DynamicDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null) {
            return;
        }
        // 更新点赞列表
        if (!isLoadMore) {
            getDynamicDigList(mRootView.getCurrentDynamic().getFeed_id(), maxId);
        }
        // 更新评论列表
        mRepository.getDynamicCommentList(mRootView.getCurrentDynamic().getFeed_mark(), mRootView.getCurrentDynamic().getFeed_id(), maxId)
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
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
    }

    @Override
    public List<DynamicCommentBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null) {
            return new ArrayList<>();
        }
        // 从数据库获取点赞列表
        // 从数据库获取关注状态，如果没有从服务器获取
        // 从数据库获取评论列表
        return mDynamicCommentBeanGreenDao.getLocalComments(mRootView.getCurrentDynamic().getFeed_mark());
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicCommentBean> data) {
        if (data == null) {
            return false;
        }
        mDynamicCommentBeanGreenDao.insertOrReplace(data);
        return true;
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
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleLike(boolean isLiked, final Long feed_id, final DynamicToolBean dynamicToolBean) {
        // 更新UI
        mRootView.setLike(isLiked);
        // 更新数据库
        mDynamicToolBeanGreenDao.insertOrReplace(dynamicToolBean);
        // 通知服务器
        mRepository.handleLike(isLiked, feed_id);
    }

    @Override
    public void handleCollect(boolean isCollected, Long feed_id, DynamicToolBean dynamicToolBean) {
        // 更新UI
        mRootView.setCollect(isCollected);
        // 更新数据库
        mDynamicToolBeanGreenDao.insertOrReplace(dynamicToolBean);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", feed_id);
        // 后台处理
        if (isCollected) {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST, params);
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        }
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_COLLECT_FORMAT, feed_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void shareDynamic() {
        ShareContent shareContent = new ShareContent();
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
        Subscription subscription = mRepository.getUserFollowState(user_ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<List<FollowFansBean>>() {
                    @Override
                    protected void onSuccess(List<FollowFansBean> data) {
                        mRootView.initFollowState(data.get(0));
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void deleteComment(long comment_id, int commentPositon) {
        mRootView.getCurrentDynamic().getTool().setFeed_comment_count(mRootView.getCurrentDynamic().getTool().getFeed_comment_count() - 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic().getTool());
        mDynamicCommentBeanGreenDao.deleteSingleCache(mRootView.getCurrentDynamic().getComments().get(commentPositon));
        mRootView.getDatas().remove(commentPositon);
        mRootView.refresh(commentPositon);
        mRepository.deleteComment(mRootView.getCurrentDynamic().getFeed_id(), comment_id);
    }

    /**
     * send a commment
     *
     * @param replyToUserId  comment  to who
     * @param commentContent comment content
     */
    @Override
    public void sendComment(long replyToUserId, String commentContent) {
        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getCurrentDynamic().getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        System.out.println("creatComment ---------------> = " + creatComment.getReply_to_user_id());
        if (replyToUserId == 0) { //当回复动态的时候
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {

            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.millis2String(System.currentTimeMillis()));
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        mRootView.getCurrentDynamic().getTool().setFeed_comment_count(mRootView.getCurrentDynamic().getTool().getFeed_comment_count() + 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic().getTool());
        mRootView.getDatas().add(0, creatComment);
        mRootView.refresh();

        mRepository.sendComment(commentContent, mRootView.getCurrentDynamic().getFeed_id(), replyToUserId, creatComment.getComment_mark());

    }


}
