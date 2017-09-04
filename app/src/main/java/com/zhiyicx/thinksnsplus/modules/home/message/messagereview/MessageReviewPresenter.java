package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.source.local.TopDynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Func1;

import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_COMMENTS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_DIGGS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_PINNED_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_REPLY_COMMENTS;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_NEWS_PINNED_COMMENT;

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
    TopDynamicCommentBeanGreenDaoImpl mTopDynamicCommentBeanGreenDao;
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
        Subscription commentSub = mRepository.getReviewComment(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<TopDynamicCommentBean>>() {
                    @Override
                    protected void onSuccess(List<TopDynamicCommentBean> data) {
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
        addSubscrebe(commentSub);

    }

    @Override
    public List<TopDynamicCommentBean> requestCacheData(Long maxId, boolean isLoadMore) {
        if (isLoadMore) {
            return new ArrayList<>();
        }
        return mTopDynamicCommentBeanGreenDao.getMultiDataFromCache();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<TopDynamicCommentBean> data, boolean
            isLoadMore) {
        if (!isLoadMore) {
            mTopDynamicCommentBeanGreenDao.clearTable();
        }
        mTopDynamicCommentBeanGreenDao.saveMultiData(data);
        return true;
    }

    @Override
    public void approvedTopComment(Long feed_id, int comment_id, int pinned_id) {
        Subscription subscription = mRepository.approvedTopComment(feed_id, comment_id, pinned_id)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
                    @Override
                    protected void onSuccess(BaseJsonV2 data) {
                        mRootView.getCurrentComment().getComment().setPinned(true);
                        mRootView.getCurrentComment().setExpires_at(TimeUtils.getCurrenZeroTimeStr());
                        mRootView.getCurrentComment().setState(TopDynamicCommentBean.TOP_SUCCESS);
                        mTopDynamicCommentBeanGreenDao.insertOrReplace(mRootView.getCurrentComment());
                        mRootView.refreshData(mRootView.getListDatas().indexOf(mRootView.getCurrentComment()));
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
    public void refuseTopComment(int pinned_id) {
        Subscription subscription = mRepository.refuseTopComment(pinned_id).subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
            @Override
            protected void onSuccess(BaseJsonV2 data) {
                mRootView.getCurrentComment().setState(TopDynamicCommentBean.TOP_REFUSE);
                mRootView.getCurrentComment().setExpires_at(TimeUtils.getCurrenZeroTimeStr());
                mTopDynamicCommentBeanGreenDao.insertOrReplace(mRootView.getCurrentComment());
                mRootView.refreshData(mRootView.getListDatas().indexOf(mRootView.getCurrentComment()));
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
