package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerInfoListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.QAListInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SHARE_QA_QUESTION_DETAIL;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QuestionDetailPresenter extends AppBasePresenter<QuestionDetailContract.View>
        implements QuestionDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;
    @Inject
    AnswerInfoListBeanGreenDaoImpl mAnswerInfoListBeanGreenDao;
    @Inject
    QAListInfoBeanGreenDaoImpl mQAListInfoBeanGreenDao;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    BaseQARepository mBaseQARepository;

    private SystemConfigBean mSystemConfigBean;


    @Inject
    public QuestionDetailPresenter(QuestionDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public SystemConfigBean getSystemConfig() {
        if (mSystemConfigBean == null) {
            mSystemConfigBean = mSystemRepository.getBootstrappersInfoFromLocal();
        }
        return mSystemConfigBean;
    }

    @Override
    public void saveQuestion(QAPublishBean qaPublishBean) {
        mBaseQARepository.saveQuestion(qaPublishBean);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        getQuestionDetail(mRootView.getCurrentQuestion().getId() + "", maxId, isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<AnswerInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getQuestionDetail(String questionId, Long maxId, boolean isLoadMore) {
        Subscription subscription = Observable.zip(mBaseQARepository.getQuestionDetail(questionId),
                mBaseQARepository.getAnswerList(questionId, mRootView.getCurrentOrderType(), maxId.intValue()),
                (qaListInfoBean, answerInfoBeanList) -> {
                    qaListInfoBean.setAnswerInfoBeanList(dealAnswerList(maxId, qaListInfoBean, answerInfoBeanList));
                    mQAListInfoBeanGreenDao.insertOrReplace(qaListInfoBean);
                    return qaListInfoBean;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<QAListInfoBean>() {
                    @Override
                    protected void onSuccess(QAListInfoBean data) {
                        mRootView.setQuestionDetail(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);

                    }


                });
        addSubscrebe(subscription);
    }

    private List<AnswerInfoBean> dealAnswerList(long maxId, QAListInfoBean qaListInfoBean, List<AnswerInfoBean> list) {
        List<AnswerInfoBean> totalList = new ArrayList<>();
        if (qaListInfoBean.getInvitation_answers() != null && maxId == 0L) {
            totalList.addAll(qaListInfoBean.getInvitation_answers());
        }
        if (qaListInfoBean.getAdoption_answers() != null && maxId == 0L) {
            totalList.addAll(qaListInfoBean.getAdoption_answers());
        }
        totalList.addAll(list);
        return totalList;
    }

    @Override
    public void handleFollowState(String questionId, boolean isFollowed) {
        mRootView.getCurrentQuestion().setWatched(isFollowed);
        if (isFollowed) {
            mRootView.getCurrentQuestion().setWatchers_count(mRootView.getCurrentQuestion().getWatchers_count() + 1);
        } else {
            mRootView.getCurrentQuestion().setWatchers_count(mRootView.getCurrentQuestion().getWatchers_count() - 1);
        }
        mRootView.updateFollowState();
        mBaseQARepository.handleQuestionFollowState(questionId, isFollowed);
    }

    @Override
    public void shareQuestion(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, mRootView.getCurrentQuestion().getSubject()));
//        shareContent.setUrl(String.format(Locale.getDefault(), APP_PATH_SHARE_DEFAULT,
//                mRootView.getCurrentTopicBean().getId()));
        shareContent.setUrl(String.format(Locale.getDefault(), APP_PATH_SHARE_QA_QUESTION_DETAIL, mRootView.getCurrentQuestion().getId()));
        shareContent.setContent(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, mRootView.getCurrentQuestion().getBody()));

        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        } else {
            shareContent.setBitmap(bitmap);
        }
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void deleteQuestion(Long question_id) {
        mRootView.handleLoading(true, false, mContext.getString(R.string.info_deleting));
        Subscription subscription = mBaseQARepository.deleteQuestion(question_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EVENT_UPDATE_QUESTION_DELETE, mRootView.getCurrentQuestion());
                        EventBus.getDefault().post(bundle, EVENT_UPDATE_QUESTION_DELETE);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.qa_question_delete_success));
                        Subscription subscribe = Observable.timer(500, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> mRootView.deleteSuccess(), Throwable::printStackTrace);
                        addSubscrebe(subscribe);

                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        showErrorTip(throwable);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void applyForExcellent(Long question_id) {
        Subscription subscription = handleWalletBlance((long) getSystemConfig().getExcellentQuestion())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.apply_doing)))
                .flatMap(o -> mBaseQARepository.applyForExcellent(question_id))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.handleLoading(false, true, mContext.getString(R.string.apply_for_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.handleLoading(false, false, message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.handleLoading(false, false, throwable.getMessage());
                    }
                });


        addSubscrebe(subscription);
    }

    @Override
    public void handleAnswerLike(boolean isLiked, long answer_id, AnswerInfoBean answerInfoBean) {
        answerInfoBean.setLiked(isLiked);
        mAnswerInfoListBeanGreenDao.insertOrReplace(answerInfoBean);
        mBaseQARepository.handleAnswerLike(isLiked, answer_id);
    }

    @Override
    public void payForOnlook(long answer_id, int position) {

        Subscription subscription = handleWalletBlance((long) getSystemConfig().getOnlookQuestion())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.pay_alert_ing)))
                .flatMap(o -> mBaseQARepository.payForOnlook(answer_id))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<AnswerInfoBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<AnswerInfoBean> data) {
                        List<AnswerInfoBean> invitationAanswers = new ArrayList<>();
                        invitationAanswers.add(data.getData());
                        mRootView.getCurrentQuestion().setInvitation_answers(invitationAanswers);
                        mRootView.getListDatas().set(position, data.getData());
                        mRootView.refreshData(position);
                        EventBus.getDefault().post(data.getData(), EventBusTagConfig.EVENT_ONLOOK_ANSWER);
                        mRootView.showSnackMessage(mContext.getString(R.string.pay_alert_success), Prompt.DONE);
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
                        LogUtils.d("Cathy", "payForOnlook // " + throwable.toString());
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.pay_alert_failed));
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_ANSWER_LIST_LIKE)
    public void updateLike(Bundle bundle) {
        if (bundle != null) {
            AnswerInfoBean answerInfoBean = (AnswerInfoBean) bundle.
                    getSerializable(EventBusTagConfig.EVENT_UPDATE_ANSWER_LIST_LIKE);
            if (answerInfoBean != null) {
                for (AnswerInfoBean answerInfoBean1 : mRootView.getListDatas()) {
                    if (answerInfoBean.getId().equals(answerInfoBean1.getId())) {
                        mRootView.getListDatas().set(mRootView.getListDatas().indexOf(answerInfoBean1), answerInfoBean);
                        mRootView.refreshData();
                        break;
                    }
                }
            }
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_PUBLISH_ANSWER)
    public void publishAnswer(AnswerInfoBean data) {
        if (data != null) {
            if (mRootView.getListDatas().size() > 0 && mRootView.getListDatas().get(0).getUser() == null) {// 占位
                mRootView.getListDatas().remove(0);
            }
            mRootView.getListDatas().add(data);
            mRootView.refreshData();
            mRootView.getCurrentQuestion().setAnswers_count(mRootView.getCurrentQuestion().getAnswers_count() + 1);
            mRootView.getCurrentQuestion().setMy_answer(data);
            mRootView.updateAnswerCount();
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_ANSWER_OR_QUESTION)
    public void updateData(Long tag) {
        requestNetData(tag, false);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_ANSWER_LIST_DELETE)
    public void deleteAnswer(AnswerInfoBean answerInfoBean) {
        if (mRootView.getListDatas().remove(answerInfoBean)) {
            if (answerInfoBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                mRootView.getCurrentQuestion().setMy_answer(null);
            }
            mRootView.refreshData();
            mRootView.getCurrentQuestion().setAnswers_count(mRootView.getCurrentQuestion().getAnswers_count() - 1);
            mRootView.updateAnswerCount();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
