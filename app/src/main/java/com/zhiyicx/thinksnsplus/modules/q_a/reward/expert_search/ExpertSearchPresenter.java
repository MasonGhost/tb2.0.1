package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class ExpertSearchPresenter extends AppBasePresenter<ExpertSearchContract.Repository, ExpertSearchContract.View>
        implements ExpertSearchContract.Presenter {

    @Inject
    public ExpertSearchPresenter(ExpertSearchContract.Repository repository, ExpertSearchContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestNetData(Long maxId, int topic_id, boolean isLoadMore) {
        mRepository.getTopicExperts(maxId,topic_id).subscribe(new BaseSubscribeForV2<List<ExpertBean>>() {
            @Override
            protected void onSuccess(List<ExpertBean> data) {
                mRootView.onNetResponseSuccess(data,isLoadMore);
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
                mRootView.onResponseError(throwable,isLoadMore);
            }
        });
    }

    @Override
    public List<ExpertBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ExpertBean> data, boolean isLoadMore) {
        return false;
    }
}
