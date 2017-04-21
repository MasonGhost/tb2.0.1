package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
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
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailContract.Repository,
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
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

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
        Subscription subscription = mRepository.getDynamicList(ApiConfig.DYNAMIC_TYPE_NEW, DEFAULT_PAGE_MAX_ID, 1, String.valueOf(feed_id), false)
                .subscribe(new BaseSubscribe<List<DynamicBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicBean> data) {
                        mRootView.initDynamicDetial(data.get(0));
                        mDynamicBeanGreenDao.insertOrReplace(data.get(0));
                        mDynamicCommentBeanGreenDao.insertOrReplace(data.get(0).getComments());
                        mDynamicToolBeanGreenDao.insertOrReplace(data.get(0).getTool());
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

    @Override
    public void getDetailAll(final Long feed_id, Long max_id, final String user_ids) {
        Subscription subscription = Observable.zip(mRepository.getDynamicDigList(feed_id, max_id)
                , mRepository.getUserFollowState(user_ids)
                , mRepository.getDynamicCommentList(mRootView.getCurrentDynamic().getFeed_mark(), mRootView.getCurrentDynamic().getFeed_id(), max_id)
                , new Func3<BaseJson<List<FollowFansBean>>, BaseJson<List<FollowFansBean>>, BaseJson<List<DynamicCommentBean>>, BaseJson<DynamicBean>>() {
                    @Override
                    public BaseJson<DynamicBean> call(BaseJson<List<FollowFansBean>> listBaseJson, BaseJson<List<FollowFansBean>> listBaseJson2, BaseJson<List<DynamicCommentBean>> listBaseJson3) {
                        BaseJson<DynamicBean> dynamicBean = new BaseJson<>();
                        dynamicBean.setData(new DynamicBean());
                        if (listBaseJson.isStatus()) {
                            if (listBaseJson2.isStatus()) {
                                if (listBaseJson3.isStatus()) {
                                    dynamicBean.setStatus(listBaseJson3.isStatus());
                                    dynamicBean.getData().setDigUserInfoList(listBaseJson.getData());
                                    mFollowFansBeanGreenDao.insertOrReplace(listBaseJson2.getData().get(0)); // 保存关注状态
                                    List<DynamicCommentBean> data = listBaseJson3.getData();
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
                                    dynamicBean.getData().setComments(data);
                                } else {
                                    dynamicBean.setStatus(listBaseJson3.isStatus());
                                    dynamicBean.setMessage(listBaseJson3.getMessage());
                                    dynamicBean.setCode(listBaseJson3.getCode());
                                }
                            } else {
                                dynamicBean.setStatus(listBaseJson2.isStatus());
                                dynamicBean.setMessage(listBaseJson2.getMessage());
                                dynamicBean.setCode(listBaseJson2.getCode());
                            }
                        } else {
                            dynamicBean.setStatus(listBaseJson.isStatus());
                            dynamicBean.setMessage(listBaseJson.getMessage());
                            dynamicBean.setCode(listBaseJson.getCode());
                        }
                        return dynamicBean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<DynamicBean>() {
                    @Override
                    protected void onSuccess(DynamicBean data) {
                        mRootView.getCurrentDynamic().setComments(data.getComments());
                        mRootView.getCurrentDynamic().setDigUserInfoList(data.getDigUserInfoList());
                        mDynamicBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic());
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
            mDynamicBeanGreenDao.deleteDynamicByFeedId(feed_id);
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
                        mDynamicBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic());
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
    public void handleLike(boolean isLiked, final Long feed_id, final DynamicToolBean dynamicToolBean) {
        mIsNeedDynamicListRefresh = true;
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        // 更新UI
        mRootView.setLike(isLiked);
        mRootView.getCurrentDynamic().getTool().setFeed_digg_count(isLiked ? mRootView.getCurrentDynamic().getTool().getFeed_digg_count() + 1 : mRootView.getCurrentDynamic().getTool().getFeed_digg_count() - 1);
        mRootView.getCurrentDynamic().getTool().setIs_digg_feed(isLiked ? STATUS_DIGG_FEED_CHECKED : STATUS_DIGG_FEED_UNCHECKED);
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
        mDynamicToolBeanGreenDao.insertOrReplace(dynamicToolBean);
        mDynamicBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        // 通知服务器
        mRepository.handleLike(isLiked, feed_id);
    }

    @Override
    public void handleCollect(DynamicBean dynamicBean) {
        // 收藏
        // 修改数据
        DynamicToolBean collectToolBean = dynamicBean.getTool();
        int is_collection = collectToolBean.getIs_collection_feed();// 旧状态
        // 新状态
        is_collection = is_collection == DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED
                ? DynamicToolBean.STATUS_COLLECT_FEED_CHECKED : DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED;
        collectToolBean.setIs_collection_feed(is_collection);
        boolean newCollectState = is_collection == DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED ? false : true;
        // 更新UI
        mRootView.setCollect(newCollectState);
        // 更新数据库
        mDynamicToolBeanGreenDao.insertOrReplace(collectToolBean);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", dynamicBean.getFeed_id());
        // 后台处理
        if (newCollectState) {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST, params);
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        }
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_COLLECT_FORMAT, dynamicBean.getFeed_id()));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_DYNAMIC);
    }

    @Override
    public void shareDynamic() {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(TextUtils.isEmpty(mRootView.getCurrentDynamic().getFeed().getTitle()) ? mContext.getString(R.string.share) : mRootView.getCurrentDynamic().getFeed().getTitle());
        shareContent.setContent(TextUtils.isEmpty(mRootView.getCurrentDynamic().getFeed().getContent()) ? mContext.getString(R.string.share_dynamic) : mRootView.getCurrentDynamic().getFeed().getContent());
        if (mRootView.getCurrentDynamic().getFeed().getStorages() != null && mRootView.getCurrentDynamic().getFeed().getStorages().size() > 0) {
            shareContent.setImage(ImageUtils.imagePathConvert(mRootView.getCurrentDynamic().getFeed().getStorages().get(0).getStorage_id() + "", 100));
        } else {
            shareContent.setBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, mRootView.getCurrentDynamic().getFeed_id() == null ? "" : mRootView.getCurrentDynamic().getFeed_id()));
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
        mRootView.getCurrentDynamic().getTool().setFeed_comment_count(mRootView.getCurrentDynamic().getTool().getFeed_comment_count() - 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic().getTool());
        mDynamicCommentBeanGreenDao.deleteSingleCache(mRootView.getCurrentDynamic().getComments().get(commentPositon));
        mRootView.getListDatas().remove(commentPositon);
        if (mRootView.getListDatas().isEmpty()) {
            DynamicCommentBean emptyData = new DynamicCommentBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.deleteComment(mRootView.getCurrentDynamic().getFeed_id(), comment_id);
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
        if (user_id == AppApplication.getmCurrentLoginAuth().getUser_id() && mDynamicBeanGreenDao.getDynamicByFeedMark(feed_mark) == null) { // 检查当前动态是否已经被删除了
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
        mRootView.getCurrentDynamic().getTool().setFeed_comment_count(mRootView.getCurrentDynamic().getTool().getFeed_comment_count() + 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getCurrentDynamic().getTool());
        if (mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas().get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mRepository.sendComment(commentContent, mRootView.getCurrentDynamic().getFeed_id(), replyToUserId, creatComment.getComment_mark());

    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {
        System.out.println("dynamicCommentBean = " + dynamicCommentBean.toString());
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
                        System.out.println("integer = " + integer);
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
