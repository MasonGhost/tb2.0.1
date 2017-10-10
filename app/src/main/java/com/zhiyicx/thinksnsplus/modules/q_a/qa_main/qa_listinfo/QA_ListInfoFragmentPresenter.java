package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.QAListInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.jetbrains.annotations.NotNull;

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
public class QA_ListInfoFragmentPresenter extends AppBasePresenter<QA_ListInfoConstact.Repository, QA_ListInfoConstact.View> implements QA_ListInfoConstact.Presenter {

    @Inject
    QAListInfoBeanGreenDaoImpl mQAListInfoBeanGreenDao;

    @Inject
    SystemRepository mSystemRepository;

    private SystemConfigBean mSystemConfigBean;

    @Inject
    public QA_ListInfoFragmentPresenter(QA_ListInfoConstact.Repository repository, QA_ListInfoConstact.View rootView) {
        super(repository, rootView);
    }

    @Override
    public boolean istourist() {
        return false;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        Subscription subscribe = mRepository.getQAQuestion(subject, maxId, type)
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
    public List<QAListInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
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
                .flatMap(new Func1<Object, Observable<BaseJsonV2<AnswerInfoBean>>>() {
                    @Override
                    public Observable<BaseJsonV2<AnswerInfoBean>> call(Object o) {
                        return mRepository.payForOnlook(answer_id);
                    }
                })
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
    public SystemConfigBean getSystemConfig() {
        if (mSystemConfigBean == null) {
            mSystemConfigBean = mSystemRepository.getBootstrappersInfoFromLocal();
        }
        return mSystemConfigBean;
    }
}
