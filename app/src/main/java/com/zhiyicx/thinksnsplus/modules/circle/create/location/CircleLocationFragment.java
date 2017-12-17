package com.zhiyicx.thinksnsplus.modules.circle.create.location;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/11/28/17:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleLocationFragment extends TSListFragment<CircleLocationContract.Presenter,
        LocationBean>
        implements CircleLocationContract.View,
        AMapLocationListener, PoiSearch.OnPoiSearchListener {

    public static final String BUNDLE_DATA = "DATA";

    @BindView(R.id.tv_cancel)
    TextView mTvSearchCancel;
    @BindView(R.id.tv_nolocation)
    TextView mTvNoLocation;
    @BindView(R.id.tv_current_location)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_animation)
    ImageView mIvAnimation;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;

    /**
     * 声明 AMapLocationClientOption 对象
     */
    public AMapLocationClientOption mLocationOption = null;
    /**
     * 声明定位回调监听器
     */
    private AMapLocationClient mLocationClient;

    protected AnimationDrawable mAnimationDrawable;
    private String mCurrentLocation = "";

    private List<PoiItem> mPoiItems = new ArrayList<>();

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mTvSearchCancel;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_cricle_location;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mAnimationDrawable = (AnimationDrawable) mIvAnimation.getDrawable();

        mRxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        initLocation();
                    } else {
                        handleAnimation(false);
                    }
                });
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new CommonAdapter<PoiItem>(getActivity(), R.layout.item_circle_location,
                mPoiItems) {
            @Override
            protected void convert(ViewHolder holder, PoiItem poiItem, int position) {
                holder.setText(R.id.tv_location_name, poiItem.getTitle());
                holder.setText(R.id.tv_location_address, poiItem.getSnippet());
                RxView.clicks(holder.itemView)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(BUNDLE_DATA, poiItem);
                            intent.putExtras(bundle);
                            getActivity().setResult(RESULT_OK, intent);
                            getActivity().finish();
                        });
            }
        };
        return mAdapter;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = aMapLocation.getLatitude();//获取纬度
                double longitude = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAddress();
                aMapLocation.getAccuracy();//获取精度信息

                // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                PoiSearch.Query query = new PoiSearch.Query("", "生活服务", "");
                query.setPageSize(20);
                PoiSearch search = new PoiSearch(getContext(), query);
                search.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude),
                        10000));
                search.setOnPoiSearchListener(this);
                search.searchPOIAsyn();
                mCurrentLocation = aMapLocation.getCountry() + " " + aMapLocation.getProvince() +
                        " " + aMapLocation.getCity();
                mTvCurrentLocation.setText(aMapLocation.getCity());

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogUtils.d("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                mTvCurrentLocation.setText(getString(R.string.wu));
            }
        }
        handleAnimation(false);
    }

    @Override
    protected void requestCacheData(Long maxId, boolean isLoadMore) {
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {
        PoiSearch.Query query = result.getQuery();
        ArrayList<PoiItem> pois = result.getPois();
        mPoiItems.clear();
        mPoiItems.addAll(pois);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        for (PoiItem poi : pois) {
            String name = poi.getCityName();
            String snippet = poi.getSnippet();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        getActivity().setResult(RESULT_OK, getActivity().getIntent());
        getActivity().finish();
    }

    private void initLocation() {
        initLocationOption();
        //启动定位
        startLocation();
    }

    private void initLocationOption() {
        //初始化定位

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)
        // 接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)
        // 接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationClient = new AMapLocationClient(getContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        mLocationClient.setLocationListener(this);
    }

    private void startLocation() {
        handleAnimation(true);
        mLocationClient.startLocation();
    }

    /**
     * 处理动画
     *
     * @param status true 开启动画，false 关闭动画
     */
    private void handleAnimation(boolean status) {
        if (mAnimationDrawable == null) {
            throw new IllegalArgumentException("load animation not be null");
        }
        if (status) {
            if (!mAnimationDrawable.isRunning()) {
                mIvLocation.setVisibility(View.GONE);
                mIvAnimation.setVisibility(View.VISIBLE);
                mAnimationDrawable.start();
            }
        } else {
            if (mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
                mIvLocation.setVisibility(View.VISIBLE);
                mIvAnimation.setVisibility(View.GONE);
            }
        }
    }
}
