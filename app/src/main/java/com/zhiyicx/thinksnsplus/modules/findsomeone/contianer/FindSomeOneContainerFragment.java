package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.LocationRecommentActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contacts.ContactsFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOneActivity;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerViewPagerFragment.PAGE_POSITION_NEARBY;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneContainerFragment extends TSFragment<FindSomeOneContainerContract.Presenter> implements FindSomeOneContainerContract.View,
        AMapLocationListener {
    private static final int REQUST_CODE_LOCATION = 8200;

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;


    /**
     * 声明AMapLocationClientOption对象
     */
    public AMapLocationClientOption mLocationOption = null;
    /**
     * 声明定位回调监听器
     */
    private AMapLocationClient mLocationClient;

    private boolean mIscationed = false;

    private FindSomeOneContainerViewPagerFragment mFindSomeOneContainerViewPagerFragment;

    public static FindSomeOneContainerFragment newInstance(Bundle bundle) {
        FindSomeOneContainerFragment findSomeOneContainerFragment = new FindSomeOneContainerFragment();
        findSomeOneContainerFragment.setArguments(bundle);
        return findSomeOneContainerFragment;

    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mTvToolbarRight;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find_some_container;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mFindSomeOneContainerViewPagerFragment = FindSomeOneContainerViewPagerFragment.initFragment(getActivity().getIntent().getExtras());

        ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager()
                , mFindSomeOneContainerViewPagerFragment
                , R.id.fragment_container);

        mRxPermissions.request(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean && !mIscationed) {
                        initLocation();
                    } else {
//                        mTvToolbarRight.setText(getString(R.string.choose_city));
                    }
                });

    }

    private void initLocation() {

        //初始化定位

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setOnceLocation(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationClient = new AMapLocationClient(getContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
        mLocationClient.setLocationListener(this);

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_toolbar_left, R.id.tv_toolbar_center, R.id.tv_toolbar_right_two, R.id.tv_toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_left:
                getActivity().finish();
                break;

            case R.id.tv_toolbar_center:
                startActivity(new Intent(getActivity(), SearchSomeOneActivity.class));

                break;
            case R.id.tv_toolbar_right_two:
                mRxPermissions.request(android.Manifest.permission.READ_CONTACTS)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                ContactsFragment.startToEditTagActivity(getActivity(), null, null);
                            } else {
                                showSnackErrorMessage(getString(R.string.contacts_permission_tip));
                            }

                        });


                break;
            case R.id.tv_toolbar_right:
                Intent intent = new Intent(getActivity(), LocationRecommentActivity.class);
                getActivity().startActivityForResult(intent, REQUST_CODE_LOCATION);
                mFindSomeOneContainerViewPagerFragment.setCurrentItem(PAGE_POSITION_NEARBY, false);
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mIscationed = true;
            String locationStr = "";
            if (data.getExtras() != null) {
                LocationBean locationBean = data.getExtras().getParcelable(LocationSearchFragment.BUNDLE_DATA);

                locationStr = LocationBean.getlocation(locationBean);
                if (locationStr.contains("，")) {
                    String[] result = locationStr.split("，");
                    if (result.length > 2) {
                        mTvToolbarRight.setText(result[result.length - 1]);
                    } else {
                        mTvToolbarRight.setText(result[result.length - 2]);
                    }
                } else {
                    try {
                        locationStr = locationBean.getName();
                        String[] result = locationStr.split(" ");
                        if (result.length > 2) {
                            mTvToolbarRight.setText(result[result.length - 1]);
                        } else {
                            mTvToolbarRight.setText(result[result.length - 2]);
                        }
                    } catch (Exception e1) {
                        locationStr = locationBean.getName();
                        mTvToolbarRight.setText(locationStr);
                    }
                }

            }
            if (TextUtils.isEmpty(locationStr)) {
                mTvToolbarRight.setText(getString(R.string.choose_city));
                mIscationed = false;
            }
            EventBus.getDefault().post(locationStr, EventBusTagConfig.EVENT_NEARBY_LOCATION_UPDATE);
        }

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
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mIscationed = true;
                //可在其中解析amapLocation获取相应内容。
                LogUtils.d("1 = " + aMapLocation.getAddress());
                LogUtils.d("2 = " + aMapLocation.getCity());
                LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                mTvToolbarRight.setText(aMapLocation.getCity());
                EventBus.getDefault().post(latLonPoint, EventBusTagConfig.EVENT_NEARBY_LOCATION);
                mPresenter.updateUseLocation(latLonPoint);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtils.d("AmapError" + "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }

    }
}
