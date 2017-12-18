package com.zhiyicx.thinksnsplus.modules.circle.create.location;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/21/16:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleLocationPresenter extends AppBasePresenter<CircleLocationContract.Repository,CircleLocationContract.View>
        implements CircleLocationContract.Presenter {

    @Inject
    public CircleLocationPresenter(CircleLocationContract.Repository repository,
                                   CircleLocationContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<LocationBean> data, boolean isLoadMore) {
        return isLoadMore;
    }

    @Override
    public void searchLocation(String name) {
        mSystemRepository.searchLocation(name)
                .map(locationContainerBeen -> {
                    List<LocationBean> result = new ArrayList<>();

                    for (LocationContainerBean locationContainerBean : locationContainerBeen) {
                        if (locationContainerBean.getItems() == null || locationContainerBean
                                .getItems().isEmpty()) {
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
