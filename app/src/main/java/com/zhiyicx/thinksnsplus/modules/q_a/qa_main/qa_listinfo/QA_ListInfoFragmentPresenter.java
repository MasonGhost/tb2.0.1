package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.QAListInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragmentPresenter extends AppBasePresenter<QA_ListInfoConstact.View> implements QA_ListInfoConstact.Presenter {

    @Inject
    QAListInfoBeanGreenDaoImpl mQAListInfoBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;
    @Inject
    BaseQARepository mBaseQARepository;

    private SystemConfigBean mSystemConfigBean;

    @Inject
    public QA_ListInfoFragmentPresenter(QA_ListInfoConstact.View rootView) {
        super(rootView);
    }

    @Override
    public boolean isTourist() {
        return false;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        Subscription subscribe = mBaseQARepository.getQAQuestion(subject, maxId, type)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        mQAListInfoBeanGreenDao.saveMultiData(data);
        return true;
    }

    @Override
    public void payForOnlook(long answer_id, int position) {
        Subscription subscription = handleWalletBlance((long) getSystemConfig().getOnlookQuestion())
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
    public SystemConfigBean getSystemConfig() {
        if (mSystemConfigBean == null) {
            mSystemConfigBean = mSystemRepository.getBootstrappersInfoFromLocal();
        }
        return mSystemConfigBean;
    }
}
