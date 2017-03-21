package com.zhiyicx.thinksnsplus.modules.gallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.indicator_expand.ScaleCircleNavigator;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class GalleryFragment extends TSFragment {
    public static final String BUNDLE_IMAGS = "imags";
    public static final String BUNDLE_IMAGS_POSITON = "imags_positon";
    private static final int MAX_OFF_SIZE = 9;
    @BindView(R.id.vp_photos)
    ViewPager mVpPhotos;
    @BindView(R.id.mi_indicator)
    MagicIndicator mMiIndicator;

    private SectionsPagerAdapter mPagerAdapter;
    private int currentItem = 0;// 点击第几张图片进入的预览界面
    private List<ImageBean> allImages;

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
        currentItem = getArguments().getInt(BUNDLE_IMAGS_POSITON);
        rectList = getArguments().getParcelableArrayList("rect");
        allImages = getArguments().<ImageBean>getParcelableArrayList(BUNDLE_IMAGS);
        mPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mVpPhotos.setAdapter(mPagerAdapter);
        mVpPhotos.setOffscreenPageLimit(MAX_OFF_SIZE);
        // 添加指示器
        ScaleCircleNavigator circleNavigator = new ScaleCircleNavigator(getContext());
        circleNavigator.setCircleCount(mPagerAdapter.getCount());
        circleNavigator.setMaxRadius(UIUtil.dip2px(getContext(), 2.5));
        circleNavigator.setMinRadius(UIUtil.dip2px(getContext(), 2.4));
        circleNavigator.setCircleSpacing(UIUtil.dip2px(getContext(), 5));
        circleNavigator.setNormalCircleColor(Color.argb(102, 99, 99, 99));
        circleNavigator.setSelectedCircleColor(Color.argb(255, 99, 99, 99));
        circleNavigator.setFollowTouch(false);
        circleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mVpPhotos.setCurrentItem(index);
            }
        });
        mMiIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mMiIndicator, mVpPhotos);
        mVpPhotos.setCurrentItem(currentItem);
    }

    @Override
    protected void initData() {

    }

    public static GalleryFragment initFragment(Bundle bundle) {
        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(bundle);
        return galleryFragment;
    }

    ////////////////////////////////缩放动画//////////////////////////////////
    private HashMap<Integer, GalleryPictureContainerFragment> fragmentMap
            = new HashMap<Integer, GalleryPictureContainerFragment>();
    private boolean alreadyAnimateIn = false;
    private ArrayList<AnimationRectBean> rectList;

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            GalleryPictureContainerFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (currentItem == position) && !alreadyAnimateIn;
                fragment = GalleryPictureContainerFragment
                        .newInstance(allImages.get(position), rectList.get(position), animateIn,
                                currentItem == position);
                alreadyAnimateIn = true;
                fragmentMap.put(position, fragment);
            }
            // PlaceholderFragment.newInstance(imageBeanList.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return allImages.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                fragmentMap.put(position, (GalleryPictureContainerFragment) object);
            }
        }

    }


    /////////////////////////////////处理转场缩放动画/////////////////////////////////////
    private ColorDrawable backgroundColor;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showBackgroundImmediately() {
        if (mRootView.getBackground() == null) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            mVpPhotos.setBackground(backgroundColor);
            // ((PhotoViewActivity)getActivity()).getAppContentView(getActivity()).setBackground(backgroundColor);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ObjectAnimator showBackgroundAnimate() {
        backgroundColor = new ColorDrawable(Color.BLACK);
        // mViewPager.setBackground(backgroundColor);
        // ((PhotoViewActivity)getActivity()).getAppContentView(getActivity()).setBackground(backgroundColor);
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(backgroundColor, "alpha", 0, 255);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mVpPhotos.setBackground(backgroundColor);
                //((PhotoViewActivity)getActivity()).getAppContentView(getActivity()).setBackground(backgroundColor);
            }
        });
        return bgAnim;
    }

    public void backPress() {

        GalleryPictureContainerFragment fragment = fragmentMap.get(mVpPhotos.getCurrentItem());
        if (fragment != null && fragment.canAnimateCloseActivity()) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(backgroundColor, "alpha", 0);
            bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mVpPhotos.setBackground(backgroundColor);
                    //((PhotoViewActivity)getActivity()).getAppContentView(getActivity()).setBackground(backgroundColor);
                }
            });
            bgAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    getActivity().finish();
                    getActivity().overridePendingTransition(-1, -1);
                }
            });
            fragment.animationExit(bgAnim);
        } else {
            ((GalleryActivity) getActivity()).superBackpress();
        }
    }


}
