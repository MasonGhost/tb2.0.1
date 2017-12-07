package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailPresenter extends AppBasePresenter<CircleDetailContract.Repository, CircleDetailContract.View>
        implements CircleDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    CirclePostListBeanGreenDaoImpl mCirclePostListBeanGreenDao;
    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public CircleDetailPresenter(CircleDetailContract.Repository repository, CircleDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        // 需要头信息
        if (mRootView.isNeedHeaderInfo()) {
            if (!isLoadMore) {
                Subscription subscribe = Observable.zip(mRepository.getCircleInfoDetail(mRootView.getCircleId()), mRepository.getPostListFromCircle
                                (mRootView.getCircleId(),
                                        maxId),
                        CircleZipBean::new)
                        .map(circleZipBean -> {
                            List<CirclePostListBean> data = circleZipBean.getCirclePostListBeanList();
                            for (int i = 0; i < data.size(); i++) { // 把自己发的评论加到评论列表的前面
                                List<CirclePostCommentBean> circlePostCommentBeans = mCirclePostCommentBeanGreenDao.getMySendingComment(data.get(i)
                                        .getMaxId().intValue());
                                if (!circlePostCommentBeans.isEmpty()) {
                                    circlePostCommentBeans.addAll(data.get(i).getComments());
                                    data.get(i).getComments().clear();
                                    data.get(i).getComments().addAll(circlePostCommentBeans);
                                }
                            }
                            return circleZipBean;
                        }).subscribe(new BaseSubscribeForV2<CircleZipBean>() {
                            @Override
                            protected void onSuccess(CircleZipBean data) {
                                mRootView.onNetResponseSuccess(data.getCirclePostListBeanList(), isLoadMore);
                                mRootView.allDataReady(data);
                            }

                            @Override
                            protected void onFailure(String message, int code) {
                                super.onFailure(message, code);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                super.onException(throwable);
                            }
                        });
                addSubscrebe(subscribe);

            }

        } else {
            Subscription subscribe = mRepository.getMinePostList(TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue(), mRootView
                    .getCircleMinePostType())
                    .subscribe(new BaseSubscribeForV2<List<CirclePostListBean>>() {
                        @Override
                        protected void onSuccess(List<CirclePostListBean> data) {
                            mRootView.onNetResponseSuccess(data, isLoadMore);

                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
            addSubscrebe(subscribe);


        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        try {
            mRootView.onCacheResponseSuccess(mCirclePostListBeanGreenDao.getDataWithComments(), isLoadMore);
        } catch (Exception e) {
            mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostListBean> data, boolean isLoadMore) {
        mCirclePostListBeanGreenDao.saveMultiData(data);
        return isLoadMore;
    }

    @Override
    public void reSendComment(CirclePostCommentBean commentBean, long feed_id) {

    }

    @Override
    public void deleteComment(CirclePostListBean circlePostListBean, int postPositon, Long commentId, int commentPosition) {
        mRootView.getListDatas().get(postPositon).setComments_count(circlePostListBean.getComments_count() - 1);
        mCirclePostListBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(postPositon));
        mCirclePostCommentBeanGreenDao.deleteSingleCache(circlePostListBean.getComments().get(commentPosition));
        mRootView.getListDatas().get(postPositon).getComments().remove(commentPosition);
        mRootView.refreshData(postPositon);
        mRepository.deletePostComment(circlePostListBean.getId(), commentId);
    }

    @Override
    public void sendComment(int mCurrentPostion, long replyToUserId, String commentContent) {
        CirclePostCommentBean creatComment = new CirclePostCommentBean();
        creatComment.setState(CirclePostCommentBean.SEND_ING);
        creatComment.setContent(commentContent);
        creatComment.setId(-1L);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setCircle_id((int) mRootView.getListDatas().get(mCurrentPostion).getGroup_id());
        creatComment.setPost_id(mRootView.getListDatas().get(mCurrentPostion).getId().intValue());
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
        List<CirclePostCommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getListDatas().get(mCurrentPostion).getComments());
        mRootView.getListDatas().get(mCurrentPostion).getComments().clear();
        mRootView.getListDatas().get(mCurrentPostion).getComments().addAll(commentBeanList);
        mRootView.getListDatas().get(mCurrentPostion).setComments_count(mRootView.getListDatas().get(mCurrentPostion).getComments_count() + 1);
        mRootView.refreshData();

        mCirclePostCommentBeanGreenDao.insertOrReplace(creatComment);
        mRepository.sendPostComment(commentContent,
                mRootView.getListDatas().get(mCurrentPostion).getId(),
                replyToUserId,
                creatComment.getComment_mark());
    }

    @Override
    public void deletePost(CirclePostListBean circlePostListBean, int position) {
        if (position == -1) {
            return;
        }
        mCirclePostListBeanGreenDao.deleteSingleCache(circlePostListBean);
        mRootView.getListDatas().remove(position);
        if (mRootView.getListDatas().isEmpty()) {
            mRootView.getListDatas().add(new CirclePostListBean());
        }
        mRootView.refreshData();
        if (circlePostListBean.getId() != null && circlePostListBean.getId() != 0) {
            mRepository.deletePost(circlePostListBean.getGroup_id(), circlePostListBean.getId());
        }
    }

    @Override
    public void sharePost(CirclePostListBean circlePostListBean, Bitmap shareBitMap) {
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
        shareContent.setUrl(String.format(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_GROUNP_DYNAMIC, circlePostListBean.getId()
                == null ? "" : circlePostListBean.getId()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleLike(boolean isLiked, Long postId, int dataPosition) {
        if (postId == 0) {
            return;
        }
        mCirclePostListBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(dataPosition));
        mRepository.dealLike(isLiked, postId);
    }

    @Override
    public void handleCollect(CirclePostListBean circlePostListBean) {
        // 修改数据-更新界面
        boolean is_collection = circlePostListBean.getCollected();// 旧状态
        // 更新数据库
        mCirclePostListBeanGreenDao.insertOrReplace(circlePostListBean);
        // 通知服务器
        mRepository.dealCollect(is_collection, circlePostListBean.getId());
    }

    @Override
    public int getCurrenPosiotnInDataList(Long id) {
        int position = -1;
        int size = mRootView.getListDatas().size();
        for (int i = 0; i < size; i++) {
            if (id.intValue() == mRootView.getListDatas().get(i).getId()) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_CIRCLE_POST)
    public void handleSendComment(CirclePostCommentBean circlePostCommentBean) {
        Observable.just(circlePostCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(circlePostCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int postPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getId().intValue() == circlePostCommentBean1.getPost_id()) {
                            postPosition = i;
                            break;
                        }
                    }
                    if (postPosition != -1) {// 如果列表有当前评论
                        int commentSize = mRootView.getListDatas().get(postPosition).getComments().size();
                        for (int i = 0; i < commentSize; i++) {
                            if (mRootView.getListDatas().get(postPosition).getComments().get(i).getPost_id() == circlePostCommentBean1.getPost_id()) {
                                mRootView.getListDatas().get(postPosition).getComments().get(i).setState(circlePostCommentBean1.getState());
                                mRootView.getListDatas().get(postPosition).getComments().get(i).setId(circlePostCommentBean1.getId());
                                mRootView.getListDatas().get(postPosition).getComments().get(i).setPost_id(circlePostCommentBean1.getPost_id());
                                break;
                            }
                        }
                    }
                    return postPosition;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, throwable -> throwable.printStackTrace());

    }

    @Subscriber(tag = EventBusTagConfig.POST_LIST_DELETE_UPDATE)
    public void deletePost(CirclePostListBean postListBean) {
        deletePost(postListBean, mRootView.getListDatas().indexOf(postListBean));
        LogUtils.d(EventBusTagConfig.POST_LIST_DELETE_UPDATE);
    }

    /**
     * 详情界面处理了数据
     * 处理更新动态数据
     *
     * @param data
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_POST)
    public void updatePost(Bundle data) {
        Subscription subscribe = Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .map(bundle -> {
                    boolean isNeedRefresh = bundle.getBoolean(CirclePostDetailFragment.POST_LIST_NEED_REFRESH);
                    CirclePostListBean postListBean = bundle.getParcelable(CirclePostDetailFragment.POST_DATA);
                    int position = mRootView.getListDatas().indexOf(postListBean);
                    // 如果列表有当前评论
                    if (position != -1) {
                        mRootView.getListDatas().set(position, postListBean);
                    }
                    return isNeedRefresh ? position : -1;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {

    }

    @Override
    public void onError(Share share, Throwable throwable) {

    }

    @Override
    public void onCancel(Share share) {

    }
}
