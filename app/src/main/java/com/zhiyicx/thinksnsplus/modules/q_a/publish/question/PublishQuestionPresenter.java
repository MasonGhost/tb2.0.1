package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.QAListInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class PublishQuestionPresenter extends AppBasePresenter<PublishQuestionContract.Repository, PublishQuestionContract.View>
        implements PublishQuestionContract.Presenter {

    @Inject
    QAListInfoBeanGreenDaoImpl mQAListInfoBeanGreenDao;

    private Subscription searchSub;

    @Inject
    public PublishQuestionPresenter(PublishQuestionContract.Repository repository, PublishQuestionContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mRepository.getDraftQuestion(qestion_mark);
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        mRepository.saveQuestion(qestion);
    }

    @Override
    public void deleteQuestion(QAPublishBean qestion) {
        mRepository.deleteQuestion(qestion);
    }

    @Override
    public void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        if (searchSub != null && !searchSub.isUnsubscribed()) {
            searchSub.unsubscribe();
        }
        searchSub = mRepository.getQAQuestion(subject, maxId, type)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(searchSub);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return false;
    }
}
