package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessageCommentPresenter extends BasePresenter<MessageCommentContract.Repository, MessageCommentContract.View> implements MessageCommentContract.Presenter {

    @Inject
    CommentedBeanGreenDaoImpl mCommentedBeanGreenDao;

    @Inject
    public MessageCommentPresenter(MessageCommentContract.Repository repository, MessageCommentContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription commentSub = mRepository.getMyComments(maxId.intValue())
                .subscribe(new BaseSubscribe<List<CommentedBean>>() {
                    @Override
                    protected void onSuccess(List<CommentedBean> data) {
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
    public List<CommentedBean> requestCacheData(Long maxId, boolean isLoadMore) {
        if (isLoadMore) {
            return new ArrayList<>();
        }
        return mCommentedBeanGreenDao.getMultiDataFromCache();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CommentedBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            mCommentedBeanGreenDao.clearTable();
        }
        mCommentedBeanGreenDao.saveMultiData(data);
        return true;
    }
}
