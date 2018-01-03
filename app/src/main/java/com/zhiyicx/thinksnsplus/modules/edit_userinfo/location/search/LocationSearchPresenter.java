package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class LocationSearchPresenter extends AppBasePresenter<LocationSearchContract.View>
        implements LocationSearchContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;
    private Subscription searchSub;

    @Inject
    public LocationSearchPresenter( LocationSearchContract.View rootView) {
        super( rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(),isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<LocationBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void searchLocation(String name) {
        if (searchSub!=null&&!searchSub.isUnsubscribed()) {
            searchSub.unsubscribe();
        }
        if(TextUtils.isEmpty(name)){
            mRootView.onNetResponseSuccess( new ArrayList<>(), false);
            return;
        }

       searchSub= mSystemRepository.searchLocation(name)
                .map(locationContainerBeen -> {
                    List<LocationBean> result = new ArrayList<>();

                    for (LocationContainerBean locationContainerBean : locationContainerBeen) {
                        if (locationContainerBean.getItems() == null || locationContainerBean.getItems().isEmpty()) {
                            result.add(locationContainerBean.getTree());
                        } else {
                            for (LocationBean locationBean : locationContainerBean.getItems()) {
                                locationBean.setParent(locationContainerBean.getTree());
                                result.add(locationBean);
                            }
                        }
                    }
                    return result;
                })
                .subscribe(new BaseSubscribeForV2<List<LocationBean>>() {
                    @Override
                    protected void onSuccess(List<LocationBean> data) {

                        mRootView.onNetResponseSuccess(data, false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, false);
                    }
                });
        addSubscrebe(searchSub);

    }
}
