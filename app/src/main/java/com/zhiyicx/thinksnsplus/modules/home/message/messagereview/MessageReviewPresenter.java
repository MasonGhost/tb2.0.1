package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.source.local.TopDynamicCommentBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MessageReviewPresenter extends AppBasePresenter<MessageReviewContract.Repository,
        MessageReviewContract.View> implements MessageReviewContract.Presenter {

    @Inject
    TopDynamicCommentBeanGreenDaoImpl mTopDynamicCommentBeanGreenDao;

    @Inject
    public MessageReviewPresenter(MessageReviewContract.Repository repository,
                                  MessageReviewContract.View rootView) {
        super(repository, rootView);
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
        return null;
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
}
