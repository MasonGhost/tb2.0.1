package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenter;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeQuestionDetailPresenter extends MarkdownPresenter<EditeQuestionDetailContract.View>
        implements EditeQuestionDetailContract.Presenter {

    @Inject
    BaseQARepository mBaseQARepository;
    @Inject
    AnswerDraftBeanGreenDaoImpl mAnswerDraftBeanGreenDao;

    @Inject
    public EditeQuestionDetailPresenter(EditeQuestionDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void publishAnswer(Long questionId, String body,String text_body, int anonymity) {
        Subscription subscribe = mBaseQARepository.publishAnswer(questionId, body,text_body, anonymity)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.publish_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<AnswerInfoBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<AnswerInfoBean> data) {
                        data.getData().setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
                        data.getData().setUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
                        EventBus.getDefault().post(data.getData(), EventBusTagConfig.EVENT_PUBLISH_ANSWER);
                        mRootView.showSnackMessage(mContext.getString(R.string.publish_success), Prompt.DONE);
                        mRootView.publishSuccess(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.publish_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void updateAnswer(Long answerId, String body,String text_body,  int anonymity) {
        Subscription subscribe = mBaseQARepository.updateAnswer(answerId, body, text_body,anonymity)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.update_ing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        EventBus.getDefault().post(0L, EventBusTagConfig.EVENT_UPDATE_ANSWER_OR_QUESTION);
                        mRootView.showSnackMessage(mContext.getString(R.string.update_success), Prompt.DONE);
                        mRootView.updateSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.update_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void updateQuestion(Long questionId, String body, int anonymity) {
        Subscription subscribe = mBaseQARepository.updateQuestion(questionId, body, anonymity)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.update_ing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        EventBus.getDefault().post(0L, EventBusTagConfig.EVENT_UPDATE_ANSWER_OR_QUESTION);
                        mRootView.showSnackMessage(mContext.getString(R.string.update_success), Prompt.DONE);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.update_failed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void deleteAnswer(AnswerDraftBean answer) {
        mAnswerDraftBeanGreenDao.deleteSingleCache(answer);
    }

    @Override
    public void saveAnswer(AnswerDraftBean answerDraftBean) {
        mAnswerDraftBeanGreenDao.saveSingleData(answerDraftBean);
    }
}
