package com.zhiyicx.thinksnsplus.modules.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class GalleryFragment extends TSFragment {
    @BindView(R.id.vp_photos)
    ViewPager mVpPhotos;
    @BindView(R.id.mi_indicator)
    MagicIndicator mMiIndicator;
    @BindView(R.id.tv_origin_photo)
    TextView mTvOriginPhoto;
    private GalleryPhotoAdapter mPagerAdapter;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {

        mPagerAdapter = new GalleryPhotoAdapter(getChildFragmentManager(), Arrays.asList(
                "http://cdn.duitang.com/uploads/item/201507/29/20150729112737_waACF.thumb.700_0.jpeg",
                "http://pic1.5442.com/2015/0604/07/05.jpg",
                "http://t-1.tuzhan.com/0f50dee8f485/c-2/l/2016/02/23/18/341ec1822a524c9eb30f5b615a1f8b3a.jpg",
                "http://pic1.5442.com/2015/0916/06/08.jpg",
                "http://pic1.win4000.com/wallpaper/a/565bdbdac0dc4.jpg",
                "http://pic1.5442.com/2015/0715/05/09.jpg"
        ));
        mVpPhotos.setAdapter(mPagerAdapter);

        CircleNavigator circleNavigator = new CircleNavigator(getContext());
        circleNavigator.setCircleCount(mPagerAdapter.getCount());
        circleNavigator.setCircleColor(Color.GRAY);
        circleNavigator.setFollowTouch(false);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mVpPhotos.setCurrentItem(index);
            }
        });
        mMiIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mMiIndicator, mVpPhotos);
    }

    @Override
    protected void initData() {

    }

    public static GalleryFragment initFragment(Bundle bundle) {
        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(bundle);
        return galleryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.tv_origin_photo)
    public void onClick() {
    }
}
