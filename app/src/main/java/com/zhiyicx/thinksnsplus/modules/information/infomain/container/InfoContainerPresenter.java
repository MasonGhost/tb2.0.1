package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoMainRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoContainerPresenter extends BasePresenter<InfoMainContract.Reppsitory
        , InfoMainContract.InfoContainerView> implements InfoMainContract.InfoContainerPresenter {

    @Inject
    InfoMainRepository mInfoMainRepository;

    @Inject
    InfoTypeBeanGreenDaoImpl mInfoTypeBeanGreenDao;

    @Inject
    public InfoContainerPresenter(InfoMainContract.Reppsitory repository,
                                  InfoMainContract.InfoContainerView rootContainerView) {
        super(repository, rootContainerView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    public InfoContainerPresenter() {
    }

    @Override
    public void getInfoType() {
        Observable.just(mInfoTypeBeanGreenDao)
                .map(new Func1<InfoTypeBeanGreenDaoImpl, InfoTypeBean>() {
                    @Override
                    public InfoTypeBean call(InfoTypeBeanGreenDaoImpl infoTypeBeanGreenDao) {
                        return infoTypeBeanGreenDao.getSingleDataFromCache(1L);
                    }
                })
                .filter(new Func1<InfoTypeBean, Boolean>() {
                    @Override
                    public Boolean call(InfoTypeBean infoTypeBean) {
                        return infoTypeBean!=null;
                    }
                }).subscribe(new Action1<InfoTypeBean>() {
                    @Override
                    public void call(InfoTypeBean infoTypeBean) {
                        mRootView.setInfoType(infoTypeBean);
                    }
                });

        Subscription subscription = mInfoMainRepository.getInfoType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<InfoTypeBean>() {
                    @Override
                    protected void onSuccess(InfoTypeBean data) {
                        mInfoTypeBeanGreenDao.insertOrReplace(data);
                        mRootView.setInfoType(data);
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
