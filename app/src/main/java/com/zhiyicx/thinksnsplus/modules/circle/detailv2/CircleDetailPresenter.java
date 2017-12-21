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
import com.zhiyicx.common.base.BaseJsonV2;
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
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleJoinedBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleSearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CircleSearchBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleFragment;
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

import static com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa.QASearchListPresenter.DEFAULT_FIRST_SHOW_HISTORY_SIZE;

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
    CircleSearchBeanGreenDaoImpl mCircleSearchBeanGreenDao;
    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;

    private Subscription mSearchSub;

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
                Subscription subscribe = Observable.zip(mRepository.getCircleInfo(mRootView.getCircleId()), mRepository.getPostListFromCircle
                                (mRootView.getCircleId(), maxId, mRootView.getType()),
                        CircleZipBean::new)
                        .map(circleZipBean -> {
                            List<CirclePostListBean> data = circleZipBean.getCirclePostListBeanList();
                            for (int i = 0; i < data.size(); i++) {
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
                                mCircleInfoGreenDao.insertOrReplace(data.getCircleInfo());
                            }

                            @Override
                            protected void onFailure(String message, int code) {
                                super.onFailure(message, code);
                                mRootView.showSnackErrorMessage(message);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                super.onException(throwable);
                                mRootView.onResponseError(throwable,isLoadMore);
                            }
                        });
                addSubscrebe(subscribe);

            }

        } else {
            switch (mRootView.getCircleMinePostType()) {
                case PUBLISH:
                case HAD_PINNED:
                case WAIT_PINNED_AUDIT:
                    Subscription subscribe = mRepository.getMinePostList(TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue(), mRootView
                            .getCircleMinePostType().value)
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
                    break;
                case SEARCH:
                    if (mSearchSub != null && !mSearchSub.isUnsubscribed()) {
                        mSearchSub.unsubscribe();
                    }
                    final String searchContent = mRootView.getSearchInput();
                    if (TextUtils.isEmpty(searchContent)) {// 无搜索内容
                        mRootView.hideRefreshState(isLoadMore);
                        return;
                    }
                    mSearchSub = mRepository.getAllePostList(TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue(), searchContent, mRootView
                            .getCircleId())
                            .subscribe(new BaseSubscribeForV2<List<CirclePostListBean>>() {
                                @Override
                                protected void onSuccess(List<CirclePostListBean> data) {
                                    // 历史记录存入数据库
                                    saveSearhDatq(searchContent);
                                    mRootView.onNetResponseSuccess(data, isLoadMore);
                                }

                                @Override
                                protected void onFailure(String message, int code) {
                                    super.onFailure(message, code);
                                    mRootView.showMessage(message);
                                }

                                @Override
                                protected void onException(Throwable throwable) {
                                    mRootView.onResponseError(throwable, isLoadMore);
                                }
                            });
                    addSubscrebe(mSearchSub);

                    break;
                default:

            }

        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);

//        if (mRootView.getCircleId() == null) {
//            mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
//            return;
//        }
//        List<CirclePostListBean> data = mCirclePostListBeanGreenDao.getDataWithComments(mRootView.getCircleId());
//        mRootView.onCacheResponseSuccess(data, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostListBean> data, boolean isLoadMore) {
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
        shareContent.setUrl(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_GROUP);
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void shareCircle(CircleInfo CircleInfo, Bitmap shareBitMap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(CircleInfo.getName()) ? mContext.getString(R.string
                .share_dynamic) : CircleInfo.getName());
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
    public void handleViewCount(Long postId, int position) {
        if (postId == null || postId == 0) {
            return;
        }
        mRootView.getListDatas().get(position).setViews_count(mRootView.getListDatas().get(position).getViews_count() + 1);
        mCirclePostListBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(position));
        mRootView.refreshData(position);
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

    @Override
    public void dealCircleJoinOrExit(CircleInfo circleInfo) {
        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.reviewing_circle));
            return;
        }
        boolean isJoined = circleInfo.getJoined() != null;

        mRepository.dealCircleJoinOrExit(circleInfo)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                        if (isJoined) {
                            circleInfo.setJoined(null);
                            circleInfo.setUsers_count(circleInfo.getUsers_count() - 1);
                        } else {
                            if (CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode())) {
                                return;
                            }
                            circleInfo.setJoined(new CircleJoinedBean(CircleMembers.MEMBER));
                            circleInfo.setUsers_count(circleInfo.getUsers_count() + 1);
                        }
                        mRootView.getCircleInfo().setJoined(new CircleJoinedBean(CircleMembers.MEMBER));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
    }


    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_CIRCLE_POST)
    public void handleSendComment(CirclePostCommentBean circlePostCommentBean) {
        Subscription subscription = Observable.just(circlePostCommentBean)
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

        addSubscrebe(subscription);

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

    /**
     * 存搜索记录
     *
     * @param searchContent
     */
    private void saveSearhDatq(String searchContent) {
        CircleSearchHistoryBean cricleSearchHistoryBean = new CircleSearchHistoryBean(searchContent, CircleSearchHistoryBean.TYPE_CIRCLE);
        mCircleSearchBeanGreenDao.saveHistoryDataByType(cricleSearchHistoryBean, CircleSearchHistoryBean.TYPE_CIRCLE);
    }


    @Override
    public List<CircleSearchHistoryBean> getFirstShowHistory() {
        return mCircleSearchBeanGreenDao.getFristShowData(DEFAULT_FIRST_SHOW_HISTORY_SIZE, QASearchHistoryBean.TYPE_QA);
    }

    @Override
    public void cleaerAllSearchHistory() {
        mCircleSearchBeanGreenDao.clearAllQASearchHistory();
    }

    @Override
    public List<CircleSearchHistoryBean> getAllSearchHistory() {
        return mCircleSearchBeanGreenDao.getCircleSearchHistory();
    }

    @Override
    public void deleteSearchHistory(CircleSearchHistoryBean qaSearchHistoryBean) {
        mCircleSearchBeanGreenDao.deleteSingleCache(qaSearchHistoryBean);
    }
}
