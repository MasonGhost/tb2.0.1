package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

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
public class InfoContainerPresenter extends AppBasePresenter<InfoMainContract.Reppsitory
        , InfoMainContract.InfoContainerView> implements InfoMainContract.InfoContainerPresenter {

    @Inject
    InfoTypeBeanGreenDaoImpl mInfoTypeBeanGreenDao;

    @Inject
    public InfoContainerPresenter(InfoMainContract.Reppsitory repository,
                                  InfoMainContract.InfoContainerView rootContainerView) {
        super(repository, rootContainerView);
    }

    @Override
    public void getInfoType() {
        Observable.just(mInfoTypeBeanGreenDao)
                .map(infoTypeBeanGreenDao -> infoTypeBeanGreenDao.getSingleDataFromCache(1L))
                .filter(infoTypeBean -> infoTypeBean != null).subscribe(infoTypeBean -> mRootView.setInfoType(infoTypeBean));

        Subscription subscription = mRepository.getInfoType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<InfoTypeBean>() {
                    @Override
                    protected void onSuccess(InfoTypeBean infoTypeBean) {
                        mInfoTypeBeanGreenDao.updateSingleData(infoTypeBean);
                        mRootView.setInfoType(infoTypeBean);
                    }
                });
        addSubscrebe(subscription);
    }

}
