package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import com.amap.api.services.core.LatLonPoint;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOneContract;

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

public class FindSomeOneContainerPresenter extends AppBasePresenter<FindSomeOneContainerContract.View>
        implements FindSomeOneContainerContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public FindSomeOneContainerPresenter(FindSomeOneContainerContract.View rootView) {
        super(rootView);
    }


    @Override
    public void updateUseLocation(LatLonPoint latLonPoint) {
        if (isLogin()) {
            Subscription subscribe = mUserInfoRepository
                    .updateUserLocation(latLonPoint.getLongitude(), latLonPoint.getLatitude())
                    .subscribe(new BaseSubscribeForV2<Object>() {
                        @Override
                        protected void onSuccess(Object data) {

                        }
                    });
            addSubscrebe(subscribe);
        }
    }
}
