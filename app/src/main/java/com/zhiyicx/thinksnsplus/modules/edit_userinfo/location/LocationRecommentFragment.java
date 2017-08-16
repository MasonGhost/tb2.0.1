package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search.LocationSearchFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * @Describe 热门地区推荐
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class LocationRecommentFragment extends TSFragment<LocationRecommentContract.Presenter> implements
        LocationRecommentContract.View {

    private static final int DEFAULT_COLUMN = 4;
    private static final int REQUST_CODE_AREA = 8110;
    @BindView(R.id.tv_current_location)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.iv_animation)
    ImageView mIvAnimation;
    @BindView(R.id.rv_hot_city)
    RecyclerView mRvHotCity;

    // 声明 AMapLocationClientOption 对象
    public AMapLocationClientOption mLocationOption = null;
    // 声明定位回调监听器
    private AMapLocationClient mLocationClient;
    protected AnimationDrawable mAnimationDrawable;

    private List<LocationBean> mHotCitys = new ArrayList<>();
    private CommonAdapter mUnSubscribeAdapter;

    public static LocationRecommentFragment newInstance(Bundle bundle) {
        LocationRecommentFragment findSomeOneContainerFragment = new LocationRecommentFragment();
        findSomeOneContainerFragment.setArguments(bundle);
        return findSomeOneContainerFragment;

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_location_recomment;
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
        mAnimationDrawable = (AnimationDrawable) mIvAnimation.getDrawable();


        mRvHotCity.setLayoutManager(new GridLayoutManager(getActivity(),
                DEFAULT_COLUMN));
        mRxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        initLocation();
                    } else {
                        mTvCurrentLocation.setText(getString(R.string.empty));
                    }
                });
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
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationClient = new AMapLocationClient(getContext());
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

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
            handleAnimation(false);
        };
        mLocationClient.setLocationListener(mAMapLocationListener);
    }

    @Override
    protected void initData() {
        mPresenter.getHotCity();
    }

    @Override
    public void updateHotCity(List<LocationBean> data) {
        mHotCitys.addAll(data);
        mRvHotCity.setAdapter(initUnsubscribeAdapter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @OnClick({R.id.tv_toolbar_center, R.id.tv_cancel, R.id.iv_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_center:
                Intent intent = new Intent(getActivity(), LocationSearchActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(LocationSearchFragment.BUNDLE_LOCATION_STRING, mTvCity.getText().toString().trim());
//                intent.putExtras(bundle);
                startActivityForResult(intent, REQUST_CODE_AREA);
                break;
            case R.id.tv_cancel:
                getActivity().finish();
                break;
            case R.id.iv_location:
                LogUtils.d("test");
                startLocation();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUST_CODE_AREA && data != null && data.getExtras() != null) {
            LocationBean locationBean = data.getExtras().getParcelable(LocationSearchFragment.BUNDLE_DATA);
            if (locationBean != null) {
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            }

        }
    }

    private CommonAdapter initUnsubscribeAdapter() {
        mUnSubscribeAdapter = new CommonAdapter<LocationBean>(getActivity(),
                R.layout.item_info_channel, mHotCitys) {
            @Override
            protected void convert(ViewHolder holder, LocationBean data,
                                   int position) {
                holder.setText(R.id.item_info_channel, data.getName());
            }

            @Override
            protected void setListener(ViewGroup parent, final ViewHolder viewHolder, int viewType) {
                RxView.clicks(viewHolder.itemView)
                        .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .compose(bindToLifecycle())
                        .subscribe(o -> {
                            if (mOnItemClickListener != null) {
                                int position = viewHolder.getAdapterPosition();
                                mOnItemClickListener.onItemClick(viewHolder.itemView, viewHolder, position);
                            }
                        });

                viewHolder.itemView.setOnLongClickListener(v -> {
                    if (mOnItemClickListener != null) {
                        int position = viewHolder.getAdapterPosition();
                        return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                    }
                    return true;
                });
            }
        };

        mUnSubscribeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                LocationBean bean = mHotCitys.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(LocationSearchFragment.BUNDLE_DATA, bean);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });

        return mUnSubscribeAdapter;
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
        if (mAnimationDrawable == null)
            throw new IllegalArgumentException("load animation not be null");
        if (status) {
            if (!mAnimationDrawable.isRunning()) {
                mIvLocation.setVisibility(View.GONE);
                mIvAnimation.setVisibility(View.VISIBLE);
                mAnimationDrawable.start();
            }
        } else {
            if (mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
                mIvAnimation.setVisibility(View.GONE);
                mIvLocation.setVisibility(View.VISIBLE);
            }
        }
    }

}
