package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.POST_LIST_COLLECT_UPDATE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.POST_LIST_DELETE_UPDATE;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailPresenter extends AppBasePresenter<CirclePostDetailContract.View>
        implements CirclePostDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    CirclePostListBeanGreenDaoImpl mCirclePostListBeanGreenDao;
    @Inject
    public SharePolicy mSharePolicy;
    @Inject
    BaseCircleRepository mBaseCircleRepository;
    @Inject
    AllAdvertListBeanGreenDaoImpl mAdvertListBeanGreenDao;

    private boolean mIsAllDataReady = false;
    private boolean mIsNeedDynamicListRefresh = false;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public CirclePostDetailPresenter(CirclePostDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        Subscription subscription = Observable.zip(mBaseCircleRepository.getPostComments(mRootView.getPostId(), 0, maxId.intValue()),
                mBaseCircleRepository.getPostDetail(mRootView.getCircleId(), mRootView.getPostId()),
                mBaseCircleRepository.getPostRewardList(mRootView.getPostId(), TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue(), null, null),
                mBaseCircleRepository.getPostDigList(mRootView.getPostId(), TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue()),
                (circlePostCommentBeans, circlePostDetailBean, postRewardList, postDigListBeans) -> {
                    circlePostDetailBean.setComments(circlePostCommentBeans);
                    circlePostDetailBean.setDigList(postDigListBeans);
                    Observable.empty()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new rx.Subscriber<Object>() {
                                @Override
                                public void onCompleted() {
                                    mRootView.updateReWardsView(new RewardsCountBean(circlePostDetailBean.getReward_number(),
                                            "" + PayConfig.realCurrency2GameCurrency(circlePostDetailBean.getReward_amount(), getRatio()),
                                            getGoldName()), postRewardList);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Object o) {

                                }
                            });
                    return circlePostDetailBean;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<CirclePostListBean>() {
                    @Override
                    protected void onSuccess(CirclePostListBean data) {
                        mRootView.allDataReady(data);
                        mIsAllDataReady = true;
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        if (code == ErrorCodeConfig.DATA_HAS_BE_DELETED) {
                            mCirclePostCommentBeanGreenDao.deleteCommentsByPostId(mRootView.getPostId());
                            EventBus.getDefault().post(mCirclePostListBeanGreenDao.getSingleDataFromCache(mRootView.getPostId()),
                                    POST_LIST_DELETE_UPDATE);
                            mRootView.postHasBeDeleted();
                            return;
                        }
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.loadAllError();
                    }
                });

        addSubscrebe(subscription);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void stickTopPost(Long postId, int day) {
        Subscription subscribe = mBaseCircleRepository.stickTopPost(postId, day)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.getCurrentePost().setPinned(true);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.post_top_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.post_top_failed));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.post_top_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void handleFollowUser(UserInfoBean userInfoBean) {
        mBaseCircleRepository.handleFollow(userInfoBean);
        mRootView.upDateFollowFansState(userInfoBean);
    }

    @Override
    public void undoTopPost(Long postId) {
        Subscription subscribe = mBaseCircleRepository.undoTopPost(postId)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.getCurrentePost().setPinned(false);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.dismissSnackBar();
                    }
                });
        addSubscrebe(subscribe);
    }

    /**
     * 更新打赏信息
     */
    @Override
    public void updateRewardData() {

        Subscription subscription = Observable.zip(mBaseCircleRepository.getPostDetail(mRootView.getCircleId(), mRootView.getPostId())
                , mBaseCircleRepository.getPostRewardList(mRootView.getPostId(), TSListFragment.DEFAULT_PAGE_SIZE, null, null, null)
                , (currenPost, rewardsListBeens) -> {
                    Observable.empty()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new rx.Subscriber<Object>() {
                                @Override
                                public void onCompleted() {
                                    mRootView.updateReWardsView(new RewardsCountBean(currenPost.getReward_number(),
                                            "" + PayConfig.realCurrency2GameCurrency(currenPost.getReward_amount(), getRatio()),
                                            getGoldName()), rewardsListBeens);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Object o) {

                                }
                            });
                    return currenPost;

                }).subscribe(new BaseSubscribeForV2<Object>() {
            @Override
            protected void onSuccess(Object data) {

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 数据加载完毕就更新动态列表
        if (!mIsAllDataReady) {
            return;
        }

        // 清除占位图数据
        if (mRootView.getListDatas() != null && mRootView.getListDatas().size() == 1 && TextUtils
                .isEmpty(mRootView.getListDatas().get(0).getContent())) {
            mRootView.getListDatas().clear();
        }
        Bundle bundle = mRootView.getArgumentsBundle();
        if (bundle != null && bundle.containsKey(CirclePostDetailFragment.POST)) {
            mRootView.getCurrentePost().setComments(mRootView.getListDatas());
            bundle.putParcelable(CirclePostDetailFragment.POST_DATA, mRootView.getCurrentePost());
            bundle.putBoolean(CirclePostDetailFragment.POST_LIST_NEED_REFRESH, mIsNeedDynamicListRefresh);
            EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_CIRCLE_POST);
        }
    }

    @Override
    public void sendComment(long replyToUserId, String commentContent) {
        mIsNeedDynamicListRefresh = true;
        CirclePostCommentBean creatComment = new CirclePostCommentBean();
        creatComment.setState(CirclePostCommentBean.SEND_ING);
        creatComment.setContent(commentContent);
        creatComment.setId(-1L);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setCircle_id((int) mRootView.getCurrentePost().getGroup_id());
        creatComment.setPost_id(mRootView.getCurrentePost().getId().intValue());
        creatComment.setReply_to_user_id(replyToUserId);
        //当回复帖子的时候
        if (replyToUserId == 0) {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        if (mRootView.getListDatas().get(0).getContent() == null) {
            // 去掉占位图
            mRootView.getListDatas().remove(0);
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.getCurrentePost().setComments_count(mRootView.getCurrentePost().getComments_count() + 1);
        mRootView.updateCommentView(mRootView.getCurrentePost());
        mRootView.refreshData();

        mCirclePostCommentBeanGreenDao.insertOrReplace(creatComment);
        mBaseCircleRepository.sendPostComment(commentContent,
                mRootView.getCurrentePost().getId(),
                replyToUserId,
                creatComment.getComment_mark());
    }

    @Override
    public void deleteComment(CirclePostCommentBean data) {
        mIsNeedDynamicListRefresh = true;
        CirclePostListBean circlePostListBean = mRootView.getCurrentePost();
        circlePostListBean.setComments_count(circlePostListBean.getComments_count() - 1);
        mCirclePostCommentBeanGreenDao.deleteSingleCache(data);
        mRootView.getCurrentePost().getComments().remove(data);
        mRootView.getListDatas().remove(data);
        mRootView.refreshData();
        mRootView.updateCommentView(mRootView.getCurrentePost());
        mBaseCircleRepository.deletePostComment(circlePostListBean.getId(), data.getId());
    }

    @Override
    public List<RealAdvertListBean> getAdvert() {
        AllAdverListBean adverBean = mAdvertListBeanGreenDao.getCircleDetailAdvert();
        if (adverBean == null) {
            return null;
        } else {
            return adverBean.getMRealAdvertListBeen();
        }
    }

    @Override
    public void handleLike(boolean isLiked, long id) {
        mIsNeedDynamicListRefresh = true;
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao
                .getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
        PostDigListBean digListBean = new PostDigListBean();
        digListBean.setUser_id(userInfoBean.getUser_id());
        digListBean.setId(System.currentTimeMillis());
        digListBean.setDiggUserInfo(userInfoBean);
        if (mRootView.getCurrentePost().getDigList() == null) {
            mRootView.getCurrentePost().setDigList(new ArrayList<>());
        }
        if (isLiked) {
            mRootView.getCurrentePost().getDigList().add(0, digListBean); // 放到第一个
            mRootView.getCurrentePost().setLikes_count(mRootView.getCurrentePost().getLikes_count() + 1);
        } else {
            for (PostDigListBean infoDigListBean : mRootView.getCurrentePost().getDigList()) {
                if (infoDigListBean.getUser_id().equals(userInfoBean.getUser_id())) {
                    mRootView.getCurrentePost().getDigList().remove(infoDigListBean);
                    mRootView.getCurrentePost().setLikes_count(mRootView.getCurrentePost().getLikes_count() - 1);
                    break;
                }
            }
        }
        mRootView.getCurrentePost().setLiked(isLiked);
        mRootView.setDigg(isLiked);

        mBaseCircleRepository.dealLike(isLiked, id);
    }

    @Override
    public void shareInfo(Bitmap shareBitMap) {
        CirclePostListBean circlePostListBean = mRootView.getCurrentePost();
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(circlePostListBean.getSummary()) ? mContext.getString(R.string
                .share_dynamic) : circlePostListBean.getSummary());
        if (shareBitMap != null) {
            shareContent.setBitmap(shareBitMap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        }
        shareContent.setUrl(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_GROUP);
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleCollect(boolean isUnCollected, long id) {
        mIsNeedDynamicListRefresh = true;
        mRootView.setCollect(isUnCollected);
        mRootView.getCurrentePost().setCollected(isUnCollected);
        mCirclePostListBeanGreenDao.updateSingleData(mRootView.getCurrentePost());
        EventBus.getDefault().post(mRootView.getCurrentePost(), POST_LIST_COLLECT_UPDATE);
        mBaseCircleRepository.dealCollect(isUnCollected, id);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostCommentBean> data, boolean isLoadMore) {
        return false;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_CIRCLE_POST)
    public void handleSendComment(CirclePostCommentBean postCommentBean) {
        Subscription subscribe = Observable.just(postCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(postCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getComment_mark().equals
                                (postCommentBean1.getComment_mark())) {
                            dynamicPosition = i;
                            mRootView.getListDatas().get(i).setState(postCommentBean1.getState
                                    ());
                            mRootView.getListDatas().get(i).setId(
                                    (postCommentBean1.getId()));
                            mRootView.getListDatas().get(i).setComment_mark
                                    (postCommentBean1.getComment_mark());
                            break;
                        }
                    }
                    return dynamicPosition;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(subscribe);
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
