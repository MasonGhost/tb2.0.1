package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.github.tamir7.contacts.Contact;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.LocationRecommentActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.LocationRecommentFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contacts.ContactsFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOneActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneContainerFragment extends TSFragment {
    private static final int REQUST_CODE_LOCATION = 8200;

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;


    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    private AMapLocationClient mLocationClient;


    public static FindSomeOneContainerFragment newInstance(Bundle bundle) {
        FindSomeOneContainerFragment findSomeOneContainerFragment = new FindSomeOneContainerFragment();
        findSomeOneContainerFragment.setArguments(bundle);
        return findSomeOneContainerFragment;

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
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
        mRxPermissions.request(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
//                        initLocation();
                    } else {
                        mTvToolbarRight.setText(getString(R.string.emptyStr));
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
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationClient = new AMapLocationClient(getContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
        //以下为后者的举例：

        AMapLocationListener mAMapLocationListener = aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    LogUtils.d("1 = " + aMapLocation.getAddress());
                    LogUtils.d("2 = " + aMapLocation.getCity());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtils.d("AmapError" + "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

        };
        mLocationClient.setLocationListener(mAMapLocationListener);

    }

    @Override
    protected void initData() {
        ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager()
                , FindSomeOneContainerViewPagerFragment.initFragment(getActivity().getIntent().getExtras())
                , R.id.fragment_container);


    }

    private void initListener() {
    }


    @OnClick({R.id.tv_toolbar_center, R.id.tv_toolbar_right_two, R.id.tv_toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_center:
                startActivity(new Intent(getActivity(), SearchSomeOneActivity.class));

                break;
            case R.id.tv_toolbar_right_two:
                mRxPermissions.request(android.Manifest.permission.READ_CONTACTS)
                        .subscribe(aBoolean -> {
                            if(aBoolean){
                                ContactsFragment.startToEditTagActivity(getActivity());
                            }else {
                              showSnackErrorMessage(getString(R.string.contacts_permission_tip));
                            }

                        });


                break;
            case R.id.tv_toolbar_right:
                Intent intent = new Intent(getActivity(), LocationRecommentActivity.class);
                startActivityForResult(intent, REQUST_CODE_LOCATION);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.getExtras() != null) {
            LocationBean locationBean = data.getExtras().getParcelable(LocationSearchFragment.BUNDLE_DATA);
            try {
                String locationStr = LocationBean.getlocation(locationBean);
                String[] result = locationStr.split("，");
                mTvToolbarRight.setText(result[result.length - 1]);
            }catch (Exception e){
                mTvToolbarRight.setText(locationBean.getName());
            }

        }

    }
}
