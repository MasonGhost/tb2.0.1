package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class LocationRecommentPresenter extends BasePresenter<LocationRecommentContract.View> implements LocationRecommentContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public LocationRecommentPresenter( LocationRecommentContract.View
            rootView) {
        super(rootView);

    }

    @Override
    public void getHotCity() {
        Subscription subscribe = mSystemRepository.getHoCity()
                .map(hotCitys -> {
                    List<LocationBean> result = new ArrayList<>();

                    for (String hotCity : hotCitys) {
                        LocationBean locationBean = new LocationBean();
                        locationBean.setName(hotCity);
                        result.add(locationBean);
                    }
                    return result;
                })
                .subscribe(new BaseSubscribeForV2<List<LocationBean>>() {
                    @Override
                    protected void onSuccess(List<LocationBean> data) {
                        mRootView.updateHotCity(data);
                    }
                });
        addSubscrebe(subscribe);
    }

}
