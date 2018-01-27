package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
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
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseRewardRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

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

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class DynamicDetailPresenter extends AppBasePresenter<
        DynamicDetailContract.View> implements DynamicDetailContract.Presenter,
        OnShareCallbackListener {

    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;

    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    @Inject
    BaseRewardRepository mRewardRepository;

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;
    private boolean mIsNeedDynamicListRefresh = false;
    private boolean mIsAllDataReady = false;


    @Inject
    public DynamicDetailPresenter(DynamicDetailContract.View rootView) {
        super( rootView);
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
        Subscription subscribe = mBaseDynamicRepository.getDynamicCommentListV2(mRootView.getCurrentDynamic().getFeed_mark(), mRootView
                .getCurrentDynamic().getId(), maxId)
                .subscribe(new BaseSubscribeForV2<List<DynamicCommentBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicCommentBean> data) {
                        if (!isLoadMore) { // 刷新时，把自己还未发送成功的评论加载到前面
                            List<DynamicCommentBean> myComments = mDynamicCommentBeanGreenDao
                                    .getMySendingComment(mRootView.getCurrentDynamic()
                                            .getFeed_mark());
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
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        if (mRootView.getCurrentDynamic() == null || AppApplication.getmCurrentLoginAuth() ==
                null) {
            mRootView.onCacheResponseSuccess(new ArrayList<>(),isLoadMore);
        }else {

            // 从数据库获取评论列表
            mRootView.onCacheResponseSuccess( mDynamicCommentBeanGreenDao.getLocalComments(mRootView.getCurrentDynamic()
                    .getFeed_mark()),isLoadMore);
        }
    }

    @Override
    public boolean insertOrUpdateData(List<DynamicCommentBean> data, boolean isLoadMore) {
        if (data == null) {
            return false;
        }
        mDynamicCommentBeanGreenDao.deleteCacheByFeedMark(mRootView.getCurrentDynamic()
                .getFeed_mark());// 删除本条动态的本地评论
        mDynamicCommentBeanGreenDao.insertOrReplace(data);
        return true;
    }

    /**
     * 获取当前动态数据
     *
     * @param feed_id
     * @param topFlag
     */
    @Override
    public void getCurrentDynamicDetail(final long feed_id, int topFlag) {
        Subscription subscription = mBaseDynamicRepository.getDynamicDetailBeanV2(feed_id)
                .subscribe(new BaseSubscribeForV2<DynamicDetailBeanV2>() {
                    @Override
                    protected void onSuccess(DynamicDetailBeanV2 data) {
                        data.setTop(topFlag);
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

    /**
     * 更新打赏数据
     *
     * @param feed_id
     */
    @Override
    public void updateRewardData(Long feed_id) {
        Subscription subscription = Observable.zip(mBaseDynamicRepository.getDynamicDetailBeanV2(feed_id)
                , mRewardRepository.rewardDynamicList(feed_id, TSListFragment.DEFAULT_PAGE_SIZE, null, null, null)
                , (currenDynamic, rewardsListBeens) -> {
                    mRootView.setRewardListBeans(rewardsListBeens);
                    mRootView.getCurrentDynamic().setReward(currenDynamic.getReward());
                    mDynamicDetailBeanV2GreenDao.insertOrReplace(currenDynamic);
                    mDynamicCommentBeanGreenDao.insertOrReplace(currenDynamic.getComments());
                    return currenDynamic;

                }).subscribe(new BaseSubscribeForV2<Object>() {
            @Override
            protected void onSuccess(Object data) {
                mRootView.updateReward();
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void allDataReady() {
        mIsAllDataReady = true;
    }

    /**
     * 获取动态详情所有数据
     *
     * @param feed_id
     * @param max_id
     * @param user_ids
     * @param topFlag
     */
    @Override
    public void getDetailAll(final Long feed_id, Long max_id, final String user_ids, final int
            topFlag) {
        Subscription subscription = Observable.zip(mBaseDynamicRepository.getDynamicDigListV2(feed_id, max_id)
                , mBaseDynamicRepository.getDynamicCommentListV2(mRootView.getCurrentDynamic().getFeed_mark(), feed_id, max_id)
                , mRewardRepository.rewardDynamicList(feed_id, TSListFragment.DEFAULT_PAGE_SIZE, null, null, null)
                , (mDynamicDigs, listBaseJson3, rewardsListBeens) -> {
                    DynamicDetailBeanV2 dynamicBean = new DynamicDetailBeanV2();
                    dynamicBean.setDigUserInfoList(mDynamicDigs);
                    List<DynamicCommentBean> data = listBaseJson3;
                    // 取出本地为发送成功的评论
                    List<DynamicCommentBean> myComments = mDynamicCommentBeanGreenDao
                            .getMySendingComment(mRootView.getCurrentDynamic().getFeed_mark());
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
                    dynamicBean.setComments(data);

                    mRootView.setRewardListBeans(rewardsListBeens);
                    return dynamicBean;
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
        if (code == ErrorCodeConfig.DATA_HAS_BE_DELETED) {
            mDynamicDetailBeanV2GreenDao.deleteDynamicByFeedId(feed_id);
            mRootView.dynamicHasBeDeleted();
        } else {
            mRootView.loadAllError();
        }
    }

    @Override
    public void getDynamicDigList(Long feed_id, Long max_id) {
        Subscription subscription = mBaseDynamicRepository.getDynamicDigListV2(feed_id, max_id)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(ApiConfig.DEFAULT_MAX_RETRY_COUNT, 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<DynamicDigListBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicDigListBean> data) {
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
    public void handleLike(boolean isLiked, final Long feed_id, final DynamicDetailBeanV2
            dynamicToolBean) {
        mIsNeedDynamicListRefresh = true;
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        // 更新UI
        mRootView.setLike(isLiked);
        mRootView.getCurrentDynamic().setFeed_digg_count(isLiked ? mRootView.getCurrentDynamic()
                .getFeed_digg_count() + 1 : mRootView.getCurrentDynamic().getFeed_digg_count() - 1);
        mRootView.getCurrentDynamic().setHas_digg(isLiked);
        if (!isLiked) {// 取消喜欢，修改修换的用户信息
            List<DynamicDigListBean> digUsers = mRootView.getCurrentDynamic().getDigUserInfoList();
            int digUserSize = digUsers.size();
            for (int i = 0; i < digUserSize; i++) {
                if (digUsers.get(i).getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                    digUsers.remove(i);
                    break;
                }
            }
        } else {// 喜欢
            UserInfoBean mineUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
            DynamicDigListBean dynamicDigListBean = new DynamicDigListBean();
            dynamicDigListBean.setUser_id(mineUserInfo.getUser_id());
            dynamicDigListBean.setId(System.currentTimeMillis());
            dynamicDigListBean.setDiggUserInfo(mineUserInfo);
            mRootView.getCurrentDynamic().getDigUserInfoList().add(0, dynamicDigListBean);// 把数据加到第一个
        }


        mRootView.updateCommentCountAndDig();

        // 更新数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicToolBean);
        // 通知服务器
        mBaseDynamicRepository.handleLike(isLiked, feed_id);
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
            backgroundRequestTaskBean = new BackgroundRequestTaskBean
                    (BackgroundTaskRequestMethodConfig.POST_V2, params);
            backgroundRequestTaskBean.setPath(String.format(ApiConfig
                            .APP_PATH_HANDLE_COLLECT_V2_FORMAT,
                    dynamicBean.getId()));
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean
                    (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
            backgroundRequestTaskBean.setPath(String.format(ApiConfig
                            .APP_PATH_HANDLE_UNCOLLECT_V2_FORMAT,
                    dynamicBean.getId()));
        }
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_DYNAMIC);
    }

    @Override
    public void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getFeed_content()) ? mContext
                .getString(R.string.share_dynamic) : dynamicBean.getFeed_content());
        if (bitmap != null) {
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory
                    .decodeResource(mContext.getResources(), R.mipmap.icon)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_DOMAIN+ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean.getId()
                == null ? "" : dynamicBean.getId()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleFollowUser(UserInfoBean userInfoBean) {
        mUserInfoRepository.handleFollow(userInfoBean);
        mRootView.upDateFollowFansState(userInfoBean);
    }

    @Override
    public void deleteCommentV2(long comment_id, int commentPosition) {
        mIsNeedDynamicListRefresh = true;
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic()
                .getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        mDynamicCommentBeanGreenDao.deleteSingleCache(mRootView.getListDatas()
                .get(commentPosition));
        mRootView.getListDatas().remove(commentPosition);
        if (mRootView.getListDatas().isEmpty()) {
            DynamicCommentBean emptyData = new DynamicCommentBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mBaseDynamicRepository.deleteCommentV2(mRootView.getCurrentDynamic().getId(), comment_id);
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
        if (user_id == AppApplication.getMyUserIdWithdefault() &&
                mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(feed_mark) == null) { //
            // 检查当前动态是否已经被删除了
            return true;
        }
        return false;
    }

    @Override
    public void sendCommentV2(long replyToUserId, String commentContent) {
        mIsNeedDynamicListRefresh = true;
        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getCurrentDynamic().getFeed_mark());
        String comment_mark = AppApplication.getMyUserIdWithdefault() + "" + System
                .currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        //当回复动态的时候
        if (replyToUserId == 0) {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getMyUserIdWithdefault());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        mRootView.getCurrentDynamic().setFeed_comment_count(mRootView.getCurrentDynamic()
                .getFeed_comment_count() + 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
        if (mRootView.getListDatas().size() == 1 && TextUtils.isEmpty(mRootView.getListDatas()
                .get(0).getComment_content())) {
            mRootView.getListDatas().clear();
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.refreshData();
        mRootView.updateCommentCountAndDig();
        mBaseDynamicRepository.sendCommentV2(commentContent, mRootView.getCurrentDynamic().getId(),
                replyToUserId, creatComment.getComment_mark());
    }

    @Override
    public void reSendComment(DynamicCommentBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mBaseDynamicRepository.sendCommentV2(commentBean.getComment_content(), feed_id, commentBean.getReply_to_user_id(),
                commentBean.getComment_mark());
        mRootView.refreshData();
    }

    /**
     * 处理发送动态数据,动态发布成功回调
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {

        Subscription subscribe = Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(dynamicCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getFeed_mark().equals
                                (dynamicCommentBean1.getFeed_mark())) {
                            dynamicPosition = i;
                            mRootView.getListDatas().get(i).setState(dynamicCommentBean1.getState
                                    ());
                            mRootView.getListDatas().get(i).setComment_id
                                    (dynamicCommentBean1.getComment_id());
                            mRootView.getListDatas().get(i).setComment_mark
                                    (dynamicCommentBean1.getComment_mark());
                            break;
                        }
                    }
                    return dynamicPosition;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData(); // 加上 header
                    }

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(subscribe);

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_DYNAMIC)
    public void updateDynamic(Bundle data) {
        DynamicDetailBeanV2 dynamicBean = data.getParcelable(DYNAMIC_DETAIL_DATA);
        mRootView.updateDynamic(dynamicBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mIsAllDataReady) { // 数据加载完毕就更新动态列表
            return;
        }

        // 清除占位图数据
        if (mRootView.getListDatas() != null && mRootView.getListDatas().size() == 1 && TextUtils
                .isEmpty(mRootView.getListDatas().get(0).getComment_content())) {
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
    public List<RealAdvertListBean> getAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getDynamicDetailAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getDynamicDetailAdvert().getMRealAdvertListBeen();
    }

    @Override
    public void checkNote(int note) {

    }

    @Override
    public void payNote(final int imagePosition, int note, boolean isImage) {

        double amount = mRootView.getCurrentDynamic().getImages().get(imagePosition).getAmount();

        Subscription subscribe = handleIntegrationBlance((long) amount)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(o -> mCommentRepository.paykNote(note))
                .flatMap(stringBaseJsonV2 -> {
                    if (isImage) {
                        return Observable.just(stringBaseJsonV2);
                    }
                    return mBaseDynamicRepository.getDynamicDetailBeanV2(mRootView.getCurrentDynamic().getId())
                            .flatMap(detailBeanV2 -> {
                                stringBaseJsonV2.setData(detailBeanV2.getFeed_content());
                                return Observable.just(stringBaseJsonV2);
                            });
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.hideCenterLoading();
                        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getMyUserIdWithdefault());
                        walletBean.setBalance(walletBean.getBalance() - amount);
                        mWalletBeanGreenDao.insertOrReplace(walletBean);
                        if (isImage) {
                            mRootView.getCurrentDynamic().getImages().get(imagePosition).setPaid
                                    (true);
                        } else {
                            mRootView.getCurrentDynamic().getPaid_node().setPaid(true);
                            mRootView.getCurrentDynamic().setFeed_content(data.getData());
                        }

                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getCurrentDynamic());
                        Bundle bundle = new Bundle();
                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mRootView.getCurrentDynamic();
                        if (mRootView.getCurrentDynamic().getComments().get(0).getComment_mark()
                                == null) {
                            dynamicDetailBeanV2.getComments().remove(0);
                        }
                        bundle.putParcelable(DYNAMIC_DETAIL_DATA, dynamicDetailBeanV2);
                        bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string
                                .transaction_success));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isIntegrationBalanceCheck(e)) {
                            return;
                        }
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
        addSubscrebe(subscribe);

    }

    @Override
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
