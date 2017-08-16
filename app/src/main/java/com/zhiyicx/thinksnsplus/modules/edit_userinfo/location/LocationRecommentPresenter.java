package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserTagBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IUploadRepository;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoContract;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class LocationRecommentPresenter extends BasePresenter<LocationRecommentContract.Repository,
        LocationRecommentContract.View> implements LocationRecommentContract.Presenter {


    @Inject
    SystemRepository mSystemRepository;
    @Inject
    public LocationRecommentPresenter(LocationRecommentContract.Repository repository, LocationRecommentContract.View
            rootView) {
        super(repository, rootView);

    }

    @Override
    public void getHotCity() {
        Subscription subscribe = mSystemRepository.getHoCity()
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
                        mRootView.updateHotCity(data);
                    }
                });
        addSubscrebe(subscribe);
    }

}
