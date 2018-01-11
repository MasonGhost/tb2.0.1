package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageReviewRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_CIRCLE_MEMBER;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_DYNAMIC_COMMENT;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_NEWS_COMMENT;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_POST;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_POST_COMMENT;


/**
 * @Author Jliuer
 * @Date 2017/7/5/20:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MessageReviewPresenter extends AppBasePresenter<
        MessageReviewContract.View> implements MessageReviewContract.Presenter {

    public static final int DEFAULT_MAX_REQUEST_UNREAD_NUM = 100;

    @Inject
    MessageRepository mMessageRepository;
    @Inject
    MessageReviewRepository mMessageReviewRepository;

    @Inject
    public MessageReviewPresenter(
            MessageReviewContract.View rootView) {
        super(rootView);
    }

    public void test() {
        mMessageRepository.getNotificationList(null, ApiConfig.NOTIFICATION_TYPE_ALL, DEFAULT_MAX_REQUEST_UNREAD_NUM, 0);
    }


    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Observable observable = null;
        switch (mRootView.getType()) {
            case TOP_DYNAMIC_COMMENT:
                observable = mMessageReviewRepository.getDynamicReviewComment(maxId.intValue());
                break;
            case TOP_NEWS_COMMENT:
                observable = mMessageReviewRepository.getNewsReviewComment(maxId.intValue());
                break;
            case TOP_POST_COMMENT:
                observable = mMessageReviewRepository.getPostReviewComment(maxId.intValue());
                break;
            case TOP_CIRCLE_MEMBER:
                observable = mMessageReviewRepository.getCircleJoinRequest(maxId.intValue());
                break;
            case TOP_POST:
                observable = mMessageReviewRepository.getPostReview(mRootView.getSourceId(), maxId.intValue());
                break;
            default:
                observable = mMessageReviewRepository.getDynamicReviewComment(maxId.intValue());
                break;
        }

        Subscription commentSub = observable.subscribe(new BaseSubscribeForV2() {
            @Override
            protected void onSuccess(Object data) {
                List<BaseListBean> result = (List<BaseListBean>) data;
                mRootView.onNetResponseSuccess(result, isLoadMore);
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

        addSubscrebe(commentSub);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

        mRootView.onCacheResponseSuccess(null, isLoadMore);

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean
            isLoadMore) {

        return true;
    }

    /**
     * 同意置顶
     *
     * @param feedId
     * @param commentId
     * @param pinnedId
     * @param result
     * @param position
     */
    @Override
    public void approvedTopComment(Long feedId, int commentId, int pinnedId, BaseListBean result, int position) {
        Observable<BaseJsonV2> observable = null;
        switch (mRootView.getType()) {
            case TOP_DYNAMIC_COMMENT:
                observable = mMessageReviewRepository.approvedTopComment(feedId, commentId, pinnedId);
                break;
            case TOP_NEWS_COMMENT:
                observable = mMessageReviewRepository.approvedNewsTopComment(feedId, commentId, pinnedId);
                break;
            case TOP_POST_COMMENT:
                observable = mMessageReviewRepository.approvedPostTopComment(commentId);
                break;
            case TOP_CIRCLE_MEMBER:
                observable = mMessageReviewRepository.approvedCircleJoin(feedId, commentId);
                break;
            case TOP_POST:
                observable = mMessageReviewRepository.approvedPostTop(feedId);
                break;
            default:
        }
        if (observable == null) {
            return;
        }
        Subscription subscription = observable
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.getListDatas().set(position, result);
                        mRootView.refreshData(position);
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

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.dismissSnackBar();
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 拒绝置顶
     *
     * @param pinned_id
     * @param result
     * @param position
     */
    @Override
    public void refuseTopComment(int pinned_id, BaseListBean result, int position) {

        Observable<BaseJsonV2> observable = null;
        switch (mRootView.getType()) {
            case TOP_DYNAMIC_COMMENT:
                observable = mMessageReviewRepository.refuseTopComment(pinned_id);
                break;
            case TOP_NEWS_COMMENT:
                TopNewsCommentListBean data = (TopNewsCommentListBean) result;
                observable = mMessageReviewRepository.refuseNewsTopComment(data.getNews().getId(), data.getComment().getId(), pinned_id);
                break;
            case TOP_POST_COMMENT:
                observable = mMessageReviewRepository.refusePostTopComment(pinned_id);
                break;
            case TOP_CIRCLE_MEMBER:
                observable = mMessageReviewRepository.refuseCircleJoin(result);
                break;
            case TOP_POST:
                observable = mMessageReviewRepository.refusePostTop((long) pinned_id);
                break;
            default:
        }
        if (observable == null) {
            return;
        }
        Subscription subscription = observable
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.getListDatas().set(position, result);
                        mRootView.refreshData(position);
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

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.dismissSnackBar();
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void deleteTopComment(Long feed_id, int comment_id) {
        Subscription subscription = mMessageReviewRepository.deleteTopComment(feed_id, comment_id).subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
            @Override
            protected void onSuccess(BaseJsonV2 data) {

            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
        addSubscrebe(subscription);
    }
}
