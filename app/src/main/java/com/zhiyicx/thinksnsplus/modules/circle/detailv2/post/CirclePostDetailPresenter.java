package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.POST_LIST_COLLECT_UPDATE;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailPresenter extends AppBasePresenter<CirclePostDetailContract.Repository, CirclePostDetailContract.View>
        implements CirclePostDetailContract.Presenter {

    @Inject
    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    CirclePostListBeanGreenDaoImpl mCirclePostListBeanGreenDao;
    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public CirclePostDetailPresenter(CirclePostDetailContract.Repository repository, CirclePostDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (!isLoadMore) {
            Subscription subscription = Observable.zip(mRepository.getPostComments(mRootView.getPostId(), 0, maxId.intValue()),
                    mRepository.getPostDetail(mRootView.getCircleId(), mRootView.getPostId()),
                    mRepository.getPostRewardList(mRootView.getPostId(), 0, maxId.intValue()),
                    mRepository.getPostDigList(mRootView.getPostId(), 0, maxId.intValue()),
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

                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });

            addSubscrebe(subscription);
        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void sendComment(long replyToUserId, String commentContent) {
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
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().add(0, creatComment);
        mRootView.getCurrentePost().setComments_count(mRootView.getCurrentePost().getComments_count() + 1);
        mRootView.refreshData();

        mCirclePostCommentBeanGreenDao.insertOrReplace(creatComment);
        mRepository.sendPostComment(commentContent,
                mRootView.getCurrentePost().getId(),
                replyToUserId,
                creatComment.getComment_mark());
    }

    @Override
    public void deleteComment(CirclePostCommentBean data) {
        CirclePostListBean circlePostListBean = mRootView.getCurrentePost();
        circlePostListBean.setComments_count(circlePostListBean.getComments_count() - 1);
        mCirclePostCommentBeanGreenDao.deleteSingleCache(data);
        mRootView.getCurrentePost().getComments().remove(data);
        mRootView.refreshData();
        mRepository.deletePostComment(circlePostListBean.getId(), data.getId());
    }

    @Override
    public List<RealAdvertListBean> getAdvert() {
        return null;
    }

    @Override
    public void handleLike(boolean isLiked, long id) {
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

        mRepository.dealLike(isLiked, id);
    }

    @Override
    public void shareInfo(Bitmap bitmap) {

    }

    @Override
    public void handleCollect(boolean isUnCollected, long id) {
        mRootView.setCollect(isUnCollected);
        mRootView.getCurrentePost().setCollected(isUnCollected);
        mRootView.setCollect(isUnCollected);
        mCirclePostListBeanGreenDao.updateSingleData(mRootView.getCurrentePost());
        EventBus.getDefault().post(mRootView.getCurrentePost(), POST_LIST_COLLECT_UPDATE);
        mRepository.dealCollect(isUnCollected, id);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostCommentBean> data, boolean isLoadMore) {
        return false;
    }
}
