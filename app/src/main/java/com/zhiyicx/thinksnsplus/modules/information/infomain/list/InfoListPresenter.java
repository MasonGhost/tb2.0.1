package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoMainRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoListPresenter extends BasePresenter<InfoMainContract.Reppsitory
        , InfoMainContract.InfoListView> implements InfoMainContract.InfoListPresenter {

    @Inject
    InfoMainRepository mInfoMainRepository;

    @Inject
    public InfoListPresenter(InfoMainContract.Reppsitory repository,
                             InfoMainContract.InfoListView rootInfoListView) {
        super(repository, rootInfoListView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<InfoListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoListBean> data) {
        return false;
    }

    @Override
    public void getInfoList(String cate_id, long max_id, long limit, long page) {
        Subscription subscription = mInfoMainRepository.getInfoList(cate_id, max_id, limit, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<InfoListBean>() {
                    @Override
                    protected void onSuccess(InfoListBean data) {

                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscription);
    }
}
