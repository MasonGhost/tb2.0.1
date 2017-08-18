package com.zhiyicx.thinksnsplus.modules.q_a.detail.question;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

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
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SHARE_DEFAULT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QuestionDetailPresenter extends AppBasePresenter<QuestionDetailContract.Repository, QuestionDetailContract.View>
        implements QuestionDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;

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

    @Override
    public void shareQuestion(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, mRootView.getCurrentQuestion().getSubject()));
//        shareContent.setUrl(String.format(Locale.getDefault(), APP_PATH_SHARE_DEFAULT,
//                mRootView.getCurrentTopicBean().getId()));
        shareContent.setUrl(APP_PATH_SHARE_DEFAULT);
        shareContent.setContent(RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, mRootView.getCurrentQuestion().getBody()));

        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256)));
        } else {
            shareContent.setBitmap(bitmap);
        }
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void deleteQuestion(Long question_id) {
        mRootView.handleLoading(true, false, mContext.getString(R.string.info_deleting));
        Subscription subscription = mRepository.deleteQuestion(question_id)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EVENT_UPDATE_QUESTION_DELETE, mRootView.getCurrentQuestion());
                        EventBus.getDefault().post(bundle, EVENT_UPDATE_QUESTION_DELETE);
                        mRootView.handleLoading(false, true, "");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.handleLoading(false, false, message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void applyForExcellent(Long question_id) {
        mRootView.handleLoading(true, false, mContext.getString(R.string.bill_doing));
        Subscription subscription = mRepository.applyForExcellent(question_id)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {

                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.handleLoading(false, true, data.getMessage().get(0));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.handleLoading(false, false, message);
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

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
