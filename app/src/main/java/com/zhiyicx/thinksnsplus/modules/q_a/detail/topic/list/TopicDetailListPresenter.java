package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import android.os.Bundle;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class TopicDetailListPresenter extends AppBasePresenter<TopicDetailListContract.View>
        implements TopicDetailListContract.Presenter {


    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public TopicDetailListPresenter(TopicDetailListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseQARepository.getQAQuestionByTopic(String.valueOf(mRootView.getTopicId()),
                "", maxId, mRootView.getCurrentType())
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {

                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void payForOnlook(long answer_id, int position) {
        Subscription subscription = handleWalletBlance((long) getSystemConfigBean().getOnlookQuestion())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(o -> mBaseQARepository.payForOnlook(answer_id))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<AnswerInfoBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<AnswerInfoBean> data) {
                        mRootView.getListDatas().get(position).setAnswer(data.getData());
                        mRootView.refreshData(position);
                        mRootView.showSnackMessage("成功", Prompt.DONE);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

        mRootView.onCacheResponseSuccess(null, isLoadMore);

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE)
    public void updateList(Bundle bundle) {
        if (bundle != null) {
            QAListInfoBean qaListInfoBean = (QAListInfoBean) bundle.
                    getSerializable(EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
            if (qaListInfoBean != null) {
                for (int i = 0; i < mRootView.getListDatas().size(); i++) {
                    if (qaListInfoBean.getId().equals(mRootView.getListDatas().get(i).getId())) {
                        mRootView.getListDatas().remove(i);
                        mRootView.refreshData();
                        EventBus.getDefault().post("success", EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
                        break;
                    }
                }
            }
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_ONLOOK_ANSWER)
    public void updateQusetion(AnswerInfoBean answerInfoBean) {
        Observable.from(mRootView.getListDatas())
                .forEach(listInfoBean -> {
                    int position = -1;
                    if (listInfoBean.getId().intValue() == answerInfoBean.getQuestion().getId().intValue()
                            && listInfoBean.getAnswer().getId().intValue() == answerInfoBean.getId().intValue()) {
                        position = mRootView.getListDatas().indexOf(listInfoBean);
                        listInfoBean.setAnswer(answerInfoBean);
                        mRootView.refreshData(position);
                    }
                });
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
