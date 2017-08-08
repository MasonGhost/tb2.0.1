package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeMoreCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class LocationSearchPresenter extends AppBasePresenter<LocationSearchContract.Repository, LocationSearchContract.View>
        implements LocationSearchContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public LocationSearchPresenter(LocationSearchContract.Repository repository, LocationSearchContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<LocationBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<LocationBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void searchLocation(String name) {
        mSystemRepository.searchLocation(name)
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

    }
}
