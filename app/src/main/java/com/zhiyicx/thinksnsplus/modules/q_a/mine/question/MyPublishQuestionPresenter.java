package com.zhiyicx.thinksnsplus.modules.q_a.mine.question;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.QAListInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyPublishQuestionPresenter extends AppBasePresenter<MyPublishQuestionContract.Repository, MyPublishQuestionContract.View>
        implements MyPublishQuestionContract.Presenter {

    @Inject
    QAListInfoBeanGreenDaoImpl mQAListInfoBeanGreenDao;

    @Inject
    public MyPublishQuestionPresenter(MyPublishQuestionContract.Repository repository, MyPublishQuestionContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mRepository.getUserQAQustion(mRootView.getMyQuestionType(), maxId).subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
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
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        mQAListInfoBeanGreenDao.saveMultiData(data);
        return false;
    }
}
