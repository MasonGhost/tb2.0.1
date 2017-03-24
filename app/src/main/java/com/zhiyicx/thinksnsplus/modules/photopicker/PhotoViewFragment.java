package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_BACK_HERE;


/**
 * @author LiuChao
 * @describe
 * @date 2017/2/7
 * @contact email:450127106@qq.com
 */

public class PhotoViewFragment extends TSFragment {


    public final static String ARG_SELCTED_PATH = "ARG_SELECTED_PATHS";// 传进来的已经被选择的图片
    public final static String ARG_ALL_PATH = "ARG_ALL_PATHS";// 传进来的所有的图片路径
    public final static String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";

    @BindView(R.id.vp_photos)
    ViewPager mViewPager;
    @BindView(R.id.rb_select_photo)
    CheckBox mRbSelectPhoto;
    @BindView(R.id.bt_complete)
    TextView mBtComplete;
    @BindView(R.id.activity_photo_view)
    LinearLayout mActivityPhotoView;
    @BindView(R.id.rl_bottom_container)
    RelativeLayout mRlBottomContainer;
    @BindView(R.id.tv_toolbar_left)
    ImageView mTvToolbarLeft;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbarLayout;

    private ArrayList<String> seletedPaths;
    private ArrayList<String> allPaths;
    private SectionsPagerAdapter mPagerAdapter;

    public final static String ARG_MAX_COUNT = "MAX_COUNT";

    private int maxCount = 0;

    private boolean hasAnim = false;

    private final ColorMatrix colorizerMatrix = new ColorMatrix();

    private int currentItem = 0;// 点击第几张图片进入的预览界面

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photo_view;
    }

    @Override
    protected void initView(View rootView) {
        mViewPager.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hasAnim = currentItem == position;
                // 是否包含了已经选中的图片
                mRbSelectPhoto.setChecked(seletedPaths.contains(allPaths.get(position)));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化设置当前选择的数量
        //mBtComplete.setEnabled(seletedPaths.size() > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, seletedPaths.size(), maxCount));
        // 初始化选择checkbox
        mRbSelectPhoto.setChecked(seletedPaths.contains(allPaths.get(currentItem)));
        mRbSelectPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String path = allPaths.get(mViewPager.getCurrentItem());
                // 达到最大选择数量，添加新的图片，进行提示
                if (seletedPaths.size() >= maxCount && !seletedPaths.contains(path) && isChecked) {
                    ToastUtils.showToast(getString(R.string.choose_max_photos, maxCount));
                    mRbSelectPhoto.setChecked(false);
                    return;
                }
                if (isChecked) {
                    // 当前选择该图片，如果还没有添加过，就进行添加
                    if (!seletedPaths.contains(path)) {
                        seletedPaths.add(path);
                    }
                } else {
                    // 当前取消选择改图片，直接移除
                    seletedPaths.remove(path);
                }
                // 没有选择图片时，是否可以点击完成，应该可以点击，所以注释了下面的代码
                // mBtComplete.setEnabled(seletedPaths.size() > 0);
                // 重置当前的选择数量
                mBtComplete.setText(getString(R.string.album_selected_count, seletedPaths.size(), maxCount));
                // 通知图片列表进行刷新
                // 在 PhotoAlbumDetailsFragment 的 refreshDataAndUI() 方法中进行订阅
                // EventBus.getDefault().post(seletedPaths, EventBusTagConfig.EVENT_SELECTED_PHOTO_UPDATE);
            }
        });
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            seletedPaths = bundle.getStringArrayList(ARG_SELCTED_PATH);
            seletedPaths = (ArrayList<String>) seletedPaths.clone();// 克隆一份，防止改变数据源
            allPaths = bundle.getStringArrayList(ARG_ALL_PATH);
            currentItem = bundle.getInt(ARG_CURRENT_ITEM);
            rectList = bundle.getParcelableArrayList("rect");
            maxCount = bundle.getInt(ARG_MAX_COUNT);
        }
        mPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        allPaths.clear();
        seletedPaths.clear();
        allPaths = null;
        seletedPaths = null;
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
        }
    }

    @OnClick({R.id.tv_toolbar_left, R.id.bt_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_left:
                getActivity().onBackPressed();
                break;
            case R.id.bt_complete:
                setResult(false);
                break;
        }
    }

    public static PhotoViewFragment newInstance(List<String> selectedPaths, List<String> allPhotos, ArrayList<AnimationRectBean> animationRectBeen, int currentItem, int maxCount) {

        PhotoViewFragment f = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_SELCTED_PATH, (ArrayList<String>) selectedPaths);
        args.putStringArrayList(ARG_ALL_PATH, (ArrayList<String>) allPhotos);
        args.putInt(ARG_CURRENT_ITEM, currentItem);
        args.putInt(ARG_MAX_COUNT, maxCount);
        args.putParcelableArrayList("rect", animationRectBeen);
        f.setArguments(args);
        return f;
    }


    ////////////////////////////////缩放动画//////////////////////////////////
    private SparseArray<PhotoViewPictureContainerFragment> fragmentMap
            = new SparseArray<>();
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
            PhotoViewPictureContainerFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (currentItem == position) && !alreadyAnimateIn;
                fragment = PhotoViewPictureContainerFragment
                        .newInstance(allPaths.get(position), rectList.get(position), animateIn,
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
            return allPaths.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                fragmentMap.put(position, (PhotoViewPictureContainerFragment) object);
            }
        }

    }

    /////////////////////////////////处理转场缩放动画/////////////////////////////////////

    private ColorDrawable backgroundColor;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showBackgroundImmediately() {
        if (mRootView.getBackground() == null) {
            backgroundColor = new ColorDrawable(Color.WHITE);
            mViewPager.setBackground(backgroundColor);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ObjectAnimator showBackgroundAnimate() {
        backgroundColor = new ColorDrawable(Color.WHITE);
        // mViewPager.setBackground(backgroundColor);
        // ((PhotoViewActivity)getActivity()).getAppContentView(getActivity()).setBackground(backgroundColor);
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(backgroundColor, "alpha", 0, 255);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mViewPager.setBackground(backgroundColor);
            }
        });
        return bgAnim;
    }

    public void backPress() {
        LogUtils.i("PhotoVIew_toolbar_height" + mToolbarLayout.getHeight());
        PhotoViewPictureContainerFragment fragment = fragmentMap.get(mViewPager.getCurrentItem());
        if (fragment != null && fragment.canAnimateCloseActivity()) {
            backgroundColor = new ColorDrawable(Color.WHITE);
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(backgroundColor, "alpha", 0);
            bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mViewPager.setBackground(backgroundColor);
                }
            });
            bgAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setResult(true);
                    getActivity().overridePendingTransition(-1, -1);
                }
            });
            fragment.animationExit(bgAnim);
        } else {
            setResult(true);
            ((PhotoViewActivity) getActivity()).superBackpress();
        }
    }

    /**
     * 设置退出方法
     *
     * @param backToPhotoAlbum 如果回到图片列表页面，是否停留
     */
    private void setResult(boolean backToPhotoAlbum) {
        // 完成图片选择，处理图片返回结果
        Intent it = new Intent();
        it.putStringArrayListExtra("photos", seletedPaths);
        it.putExtra(EXTRA_BACK_HERE, backToPhotoAlbum);
        getActivity().setResult(Activity.RESULT_OK, it);
        getActivity().finish();
    }

}
