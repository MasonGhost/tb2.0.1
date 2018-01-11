package com.zhiyicx.thinksnsplus.modules.chat.location;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/10
 * @contact email:648129313@qq.com
 */
public class SendLocationFragment extends TSFragment<SendLocationContract.Presenter> implements SendLocationContract.View, AMap.OnMyLocationChangeListener {

    public static final String BUNDLE_LOCATION_LATITUDE = "latitude";
    public static final String BUNDLE_LOCATION_LONGITUDE = "longitude";
    public static final String BUNDLE_LOCATION_ADDRESS = "address";
    @BindView(R.id.mapView)
    MapView mMapView;

    private AMap aMap;

    private double mLatitude;
    private double mLongitude;
    private String mAddress;

    private boolean mIsSend;

    public SendLocationFragment instance(Bundle bundle) {
        SendLocationFragment fragment = new SendLocationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(BUNDLE_LOCATION_LATITUDE)) {
            mLatitude = bundle.getDouble(BUNDLE_LOCATION_LATITUDE);
        }
        if (bundle.containsKey(BUNDLE_LOCATION_LONGITUDE)) {
            mLongitude = bundle.getDouble(BUNDLE_LOCATION_LONGITUDE);
        }
        if (bundle.containsKey(BUNDLE_LOCATION_ADDRESS)) {
            mAddress = bundle.getString(BUNDLE_LOCATION_ADDRESS);
        }
        if (mLatitude != 0 && mLongitude != 0 && !TextUtils.isEmpty(mAddress)) {
            setRightText("");
            mIsSend = false;
        } else {
            mIsSend = true;
        }
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (mIsSend) {
            aMap.setOnMyLocationChangeListener(this);
            MyLocationStyle myLocationStyle;
            /*初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);*/
            /*连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。*/
            myLocationStyle = new MyLocationStyle();
            /*设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。*/
            myLocationStyle.interval(2000);
            // 自定义定位蓝点图标
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                    fromResource(R.mipmap.find_ico_location2));
            // 自定义精度范围的圆形边框颜色
            myLocationStyle.strokeColor(getResources().getColor(R.color.themeColor));
            //自定义精度范围的圆形边框宽度
            myLocationStyle.strokeWidth(5);
            // 设置圆形的填充颜色
            myLocationStyle.radiusFillColor(getResources().getColor(R.color.general_for_bg_light_alpha_0_5));
            // 将自定义的 myLocationStyle 对象添加到地图上
            aMap.setMyLocationStyle(myLocationStyle);
            /*设置默认定位按钮是否显示，非必需设置。*/
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            /*设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。*/
            aMap.setMyLocationEnabled(true);
        } else {
            // 添加已有的定位点
            MarkerOptions markerOption = new MarkerOptions();
            LatLng latLng = new LatLng(mLatitude, mLongitude);
//            markerOption.position(latLng);
//            markerOption.title(mAddress).snippet("");
//            markerOption.draggable(false);//设置Marker可拖动
//            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                    .decodeResource(getResources(), R.mipmap.find_ico_location2)));
//            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//            markerOption.setFlat(true);//设置marker平贴地图效果
            aMap.addMarker(new MarkerOptions().position(latLng).title(mAddress).snippet(""));
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_send_location;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_location_info);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.chat_send_location);
    }

    @Override
    protected void setRightClick() {
        Intent intent = getActivity().getIntent();
        intent.putExtra("latitude", mLatitude);
        intent.putExtra("longitude", mLongitude);
        intent.putExtra("address", mAddress);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getActivity().getIntent();
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null){
            mMapView.onResume();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null){
            mMapView.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null){
            mMapView.onDestroy();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        LogUtils.d("Cathy", location == null ? "定位失败" : location.getExtras());
        if (location != null) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            if (location.getExtras() != null) {
                mAddress = location.getExtras().getString("Address").trim();
            }
        } else {
            showSnackErrorMessage("定位失败");
        }
    }
}
