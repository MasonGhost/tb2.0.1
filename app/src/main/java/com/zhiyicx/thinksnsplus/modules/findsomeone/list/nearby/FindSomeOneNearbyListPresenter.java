package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenter;
import com.zhiyicx.thinksnsplus.utils.LocationUtils;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_PAGE_SIZE;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FindSomeOneNearbyListPresenter extends AppBasePresenter<FindSomeOneNearbyListContract.View> implements FindSomeOneNearbyListContract
        .Presenter, GeocodeSearch.OnGeocodeSearchListener {

    public static final int DEFAULT_NEARBY_RADIUS = 50000; // 搜索范围，单位 M
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;


    @Inject
    UserInfoRepository mUserInfoRepository;

    LatLonPoint mLatLonPoint;

    private boolean mIsConverLocation;

    @Inject
    public FindSomeOneNearbyListPresenter(FindSomeOneNearbyListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long max_Id, boolean isLoadMore) {
        if (mIsConverLocation) { // 正在转换经纬度就不请求
            return;
        }

        if (mLatLonPoint == null) {
            mRootView.onNetResponseSuccess(new ArrayList<>(), isLoadMore);

        } else {
            Subscription subscribe = mUserInfoRepository.getNearbyData(mLatLonPoint.getLongitude(), mLatLonPoint.getLatitude()
                    , DEFAULT_NEARBY_RADIUS, DEFAULT_PAGE_SIZE, isLoadMore ? mRootView.getPage() : TSListFragment
                            .DEFAULT_PAGE)
                    .subscribe(new BaseSubscribeForV2<List<NearbyBean>>() {
                        @Override
                        protected void onSuccess(List<NearbyBean> data) {
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
            addSubscrebe(subscribe);
        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<NearbyBean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void followUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);

    }

    @Override
    public void cancleFollowUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);
    }

    /**
     * 地区选择更新
     *
     * @param location
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_NEARBY_LOCATION_UPDATE)
    public void updateNearByData(String location) {
        if (TextUtils.isEmpty(location) || location.equals(mContext.getString(R.string.choose_city))) {
            mRootView.onNetResponseSuccess(new ArrayList<>(), false);
            return;
        }
        mIsConverLocation = true;
        mRootView.showLoading();
        LocationUtils.getLatlon(location, mContext, this);

    }

    /**
     * 地区定位
     *
     * @param latLonPoint
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_NEARBY_LOCATION)
    public void location(LatLonPoint latLonPoint) {
        mRootView.showLoading();
        mLatLonPoint = latLonPoint;
        requestNetData(0L, false);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        try {
            mIsConverLocation = false;
            if (i == 1000) {
                LatLonPoint latLonPoint;
                if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
                        geocodeResult.getGeocodeAddressList().size() > 0) {
                    GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                    latLonPoint = geocodeAddress.getLatLonPoint();
                    if (mLatLonPoint == null || !mLatLonPoint.equals(latLonPoint)) {
                        mLatLonPoint = latLonPoint;

                        requestNetData(0L, false);
                    } else {
                        mRootView.hideLoading();
                    }
                }
            } else {
                LogUtils.e("地址名出错");
                mRootView.onNetResponseSuccess(new ArrayList<>(), false);
            }
        } catch (Exception e) {

        }

    }
}
