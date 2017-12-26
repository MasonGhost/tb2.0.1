package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoRecommendBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoRecommendBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_LIST_DELETE;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoListPresenter extends AppBasePresenter<InfoMainContract.InfoListView> implements InfoMainContract.InfoListPresenter {

    @Inject
    InfoListBeanGreenDaoImpl mInfoListBeanGreenDao;

    @Inject
    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    @Inject
    InfoRecommendBeanGreenDaoImpl mInfoRecommendBeanGreenDao;

    @Inject
    AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;

    @Inject
    BaseInfoRepository mBaseInfoRepository;

    @Inject
    public InfoListPresenter(InfoMainContract.InfoListView rootInfoListView) {
        super(rootInfoListView);
    }

    @Override
    public List<RealAdvertListBean> getBannerAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getInfoBannerAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getInfoBannerAdvert().getMRealAdvertListBeen();
    }

    @Override
    public List<RealAdvertListBean> getListAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getInfoListAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getInfoListAdvert().getMRealAdvertListBeen();
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        String typeString = mRootView.getInfoType();
        final long type = Long.parseLong(typeString);
        Subscription subscription = mBaseInfoRepository.getInfoListV2(mRootView.getInfoType().equals("-1") ? "" : mRootView.getInfoType()
                , "", maxId, mRootView.getPage(), mRootView.isRecommend())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<InfoListDataBean>>() {
                    @Override
                    protected void onSuccess(List<InfoListDataBean> data) {
                        List<BaseListBean> list = new ArrayList<>();
                        List<InfoRecommendBean> recommendList;
//                        try {
//                            recommendList = data.getRecommend();
//                        } catch (Exception e) {
//                            recommendList = data.getNetRecommend();
//                        }
//                        if (recommendList != null) {
//                            for (InfoRecommendBean recommendBean : recommendList) {
//                                recommendBean.setInfo_type(type);
//                            }
//                            list.addAll(recommendList);
//                            mInfoRecommendBeanGreenDao.saveMultiData(recommendList);
//                        }
                        for (InfoListDataBean listDataBean : data) {
                            listDataBean.setInfo_type(type);
                        }
                        list.addAll(data);
                        mInfoListDataBeanGreenDao.saveMultiData(data);
//                        data.setInfo_type(type);
//                        mInfoListBeanGreenDao.insertOrReplace(data);
                        mRootView.onNetResponseSuccess(list, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, final boolean isLoadMore) {
        String typeString = mRootView.getInfoType();
        final long type = Long.parseLong(typeString);
        Observable.just(mInfoListDataBeanGreenDao)
                .observeOn(Schedulers.io())
                .map(infoListDataBeanGreenDao -> infoListDataBeanGreenDao
                        .getInfoByType(type))
                .filter(infoListBean -> infoListBean != null)
                .map(data -> {
                    List<BaseListBean> localData = new ArrayList<>();

                    if (data != null) {
                        localData.addAll(data);
                    }
                    for (InfoListDataBean listDataBean : data) {
                        listDataBean.setInfo_type(type);
                    }
                    return localData;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    mRootView.onCacheResponseSuccess(result, isLoadMore);
                }, Throwable::printStackTrace);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getInfoList(String cateId, long maxId, long limit, final long page) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_UPDATE_LIST_DELETE)
    public void updateDeleteInfo(InfoListDataBean infoListDataBean) {
        for (BaseListBean listBean : mRootView.getListDatas()) {
            if (listBean instanceof InfoListDataBean && ((InfoListDataBean) listBean).getId() == infoListDataBean.getId()) {
                mRootView.getListDatas().remove(listBean);
                mRootView.refreshData();
                break;
            }
        }
    }
}
