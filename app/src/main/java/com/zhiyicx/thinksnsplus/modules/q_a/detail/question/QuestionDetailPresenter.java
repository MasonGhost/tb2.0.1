package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QuestionDetailPresenter extends AppBasePresenter<QuestionDetailContract.Repository, QuestionDetailContract.View>
        implements QuestionDetailContract.Presenter {

    @Inject
    public QuestionDetailPresenter(QuestionDetailContract.Repository repository, QuestionDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (mRootView.getCurrentQuestion().getTopics() == null || mRootView.getCurrentQuestion().getTopics().size() == 0){
            getQuestionDetail(mRootView.getCurrentQuestion().getId() + "");
        } else {
            Subscription subscription = mRepository.getAnswerList(mRootView.getCurrentQuestion().getId() + "",
                    mRootView.getCurrentOrderType(), mRootView.getRealSize())
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribeForV2<List<AnswerInfoBean>>() {

                        @Override
                        protected void onSuccess(List<AnswerInfoBean> data) {
                            mRootView.hideCenterLoading();
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                        }
                    });
            addSubscrebe(subscription);
        }

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
        Subscription subscription = Observable.zip(mRepository.getQuestionDetail(questionId),
                mRepository.getAnswerList(questionId, mRootView.getCurrentOrderType(), 0),
                (qaListInfoBean, answerInfoBeanList) -> {
                    List<AnswerInfoBean> totalList = new ArrayList<>();
                    if (qaListInfoBean.getInvitation_answers() != null){
                        totalList.addAll(qaListInfoBean.getInvitation_answers());
                    }
                    if (qaListInfoBean.getAdoption_answers() != null){
                        totalList.addAll(qaListInfoBean.getAdoption_answers());
                    }
                    totalList.addAll(answerInfoBeanList);
                    qaListInfoBean.setAnswerInfoBeanList(totalList);
                    return qaListInfoBean;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<QAListInfoBean>() {
                    @Override
                    protected void onSuccess(QAListInfoBean data) {
                        mRootView.hideCenterLoading();
                        mRootView.setQuestionDetail(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleFollowState(String questionId, boolean isFollowed) {
        mRootView.getCurrentQuestion().setWatched(isFollowed);
        mRootView.updateFollowState();
        mRepository.handleQuestionFollowState(questionId, isFollowed);
    }
}
