package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/18
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class QASearchListPresenter extends AppBasePresenter<QASearchListContract.Repository,
        QASearchListContract.View> implements QASearchListContract.Presenter {


    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public QASearchListPresenter(QASearchListContract.Repository repository,
                                 QASearchListContract.View rootView) {
        super(repository, rootView);

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription all = mBaseQARepository.getQAQuestion(mRootView.getSearchInput(), maxId, "all")
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(all);

    }

    @Override
    public List<QAListInfoBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return true;
    }


}
