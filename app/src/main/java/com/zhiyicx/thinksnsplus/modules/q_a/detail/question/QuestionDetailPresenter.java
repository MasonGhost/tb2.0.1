package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QuestionDetailPresenter extends AppBasePresenter<QuestionDetailContract.Repository, QuestionDetailContract.View>
        implements QuestionDetailContract.Presenter{

    @Inject
    public QuestionDetailPresenter(QuestionDetailContract.Repository repository, QuestionDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<AnswerInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<AnswerInfoBean> data, boolean isLoadMore) {
        return false;
    }
    @Override
    public void getQuestionDetail(String questionId) {
        Subscription subscription = mRepository.getQuestionDetail(questionId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<QAListInfoBean>() {

                    @Override
                    protected void onSuccess(QAListInfoBean data) {
                        mRootView.setQuestionDetail(data);
                    }
                });
        addSubscrebe(subscription);
    }
}
