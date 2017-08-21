package com.zhiyicx.thinksnsplus.modules.q_a.search.list.topic;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa.QASearchListContract;

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
public class QATopicSearchListPresenter extends AppBasePresenter<QATopicSearchListContract.Repository,
        QATopicSearchListContract.View> implements QATopicSearchListContract.Presenter {


    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public QATopicSearchListPresenter(QATopicSearchListContract.Repository repository,
                                      QATopicSearchListContract.View rootView) {
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
//                        mRootView.onNetResponseSuccess(data, isLoadMore);
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
    public List<QATopicBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QATopicBean> data, boolean isLoadMore) {
        return true;
    }


}
