package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoSearchPresenter extends BasePresenter<SearchContract.Repository, SearchContract
        .View>
        implements SearchContract.Presenter {

    @Inject
    public InfoSearchPresenter(SearchContract.Repository repository,
                               SearchContract.View rootView) {
        super(repository, rootView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    public InfoSearchPresenter() {

    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = mRepository.searchInfoList(mRootView.getKeyWords(), maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<List<InfoListBean.ListBean>>() {
                    @Override
                    protected void onSuccess(List<InfoListBean.ListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<InfoListBean.ListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoListBean.ListBean> data) {
        return false;
    }

}
