package com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QuestionCommentPresenter extends AppBasePresenter<QuestionCommentContract.Repository, QuestionCommentContract.View>
        implements QuestionCommentContract.Presenter{

    @Inject
    public QuestionCommentPresenter(QuestionCommentContract.Repository repository, QuestionCommentContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getQuestionCommentList(mRootView.getCurrentQuestion().getId(), maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<QuestionCommentBean>>() {

                    @Override
                    protected void onSuccess(List<QuestionCommentBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<QuestionCommentBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QuestionCommentBean> data, boolean isLoadMore) {
        return false;
    }
}
