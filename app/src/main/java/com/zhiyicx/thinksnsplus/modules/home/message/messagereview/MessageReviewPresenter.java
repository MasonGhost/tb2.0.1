package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_DYNAMIC_COMMENT;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_NEWS_COMMENT;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.TOP_POST_COMMENT;


/**
 * @Author Jliuer
 * @Date 2017/7/5/20:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MessageReviewPresenter extends AppBasePresenter<MessageReviewContract.Repository,
        MessageReviewContract.View> implements MessageReviewContract.Presenter {

    public static final int DEFAULT_MAX_REQUEST_UNREAD_NUM = 100;

    @Inject
    MessageRepository mMessageRepository;

    @Inject
    public MessageReviewPresenter(MessageReviewContract.Repository repository,
                                  MessageReviewContract.View rootView) {
        super(repository, rootView);
    }

    public void test() {
        mMessageRepository.getNotificationList(null, ApiConfig.NOTIFICATION_TYPE_ALL, DEFAULT_MAX_REQUEST_UNREAD_NUM, 0);
    }


    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Observable observable = null;
        switch (mRootView.getType()) {
            case TOP_DYNAMIC_COMMENT:
                observable = mRepository.getDynamicReviewComment(maxId.intValue());
                break;
            case TOP_NEWS_COMMENT:
                observable = mRepository.getNewsReviewComment(maxId.intValue());
                break;
            case TOP_POST_COMMENT:
//                observable = mRepository.getPostReviewComment(maxId.intValue());
                break;
            default:
                observable = mRepository.getDynamicReviewComment(maxId.intValue());
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

    @Override
    public void approvedTopComment(Long feedId, int commentId, int pinnedId, BaseListBean result, int position) {
        Observable observable = null;
        switch (mRootView.getType()) {
            case TOP_DYNAMIC_COMMENT:
                observable = mRepository.approvedTopComment(feedId, commentId, pinnedId);
                break;
            case TOP_NEWS_COMMENT:
                observable = mRepository.approvedNewsTopComment(feedId, commentId, pinnedId);
                break;
            case TOP_POST_COMMENT:
                observable = mRepository.approvedPostTopComment(commentId);
                break;
                default:
        }
        if(observable==null){
            return;
        }
        Subscription subscription = observable
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

                });
        addSubscrebe(subscription);
    }

    @Override
    public void refuseTopComment(int pinned_id, BaseListBean result, int position) {

        Observable observable = null;
        switch (mRootView.getType()) {
            case TOP_DYNAMIC_COMMENT:
                observable = mRepository.refuseTopComment(pinned_id);
                break;
            case TOP_NEWS_COMMENT:
                TopNewsCommentListBean data=(TopNewsCommentListBean)result;
                observable = mRepository.refuseNewsTopComment(data.getNews().getId(),data.getComment().getId(),pinned_id);
                break;
            case TOP_POST_COMMENT:
                observable = mRepository.refusePostTopComment(pinned_id);
                break;
                default:
        }
        if(observable==null){
            return;
        }
        Subscription subscription = observable
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
                });
        addSubscrebe(subscription);
    }

    @Override
    public void deleteTopComment(Long feed_id, int comment_id) {
        Subscription subscription = mRepository.deleteTopComment(feed_id, comment_id).subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
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
