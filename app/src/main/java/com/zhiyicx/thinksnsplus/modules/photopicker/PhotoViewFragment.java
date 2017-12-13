package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.PhotoViewDataCacheBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll.PictureTollActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLL;
import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLL_TYPE;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsFragment.EXTRA_BACK_HERE;


/**
 * @author LiuChao
 * @describe
 * @date 2017/2/7
 * @contact email:450127106@qq.com
 */

public class PhotoViewFragment extends TSFragment {

    public final static int REQUEST_CODE = 100;

    public static PhotoViewDataCacheBean sPhotoViewDataCacheBean;

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

    private ArrayList<String> seletedPaths;
    private ArrayList<String> allPaths;
    private SectionsPagerAdapter mPagerAdapter;

    public final static String ARG_MAX_COUNT = "MAX_COUNT";
    public final static String RIGHTTITLE = "righttitle";
    public final static String OLDTOLL = "oldtoll";

    private int maxCount = 0;

    private boolean hasAnim = false;
    private boolean hasRightTitle = false;

    private final ColorMatrix colorizerMatrix = new ColorMatrix();

    private int currentItem = 0;// 点击第几张图片进入的预览界面

    private ArrayList<ImageBean> tolls;

    // 这两个用来记录图片选中的信息，因为要点击完成才能真正的处理图片是否选择
    private ArrayList<ImageBean> unCheckImage = new ArrayList<>();
    private ArrayList<String> unCheckImagePath = new ArrayList<>();

    private ArrayList<String> checkImagePath = new ArrayList<>();

    private ImageBean mImageBean;

    @Override
    protected String setRightTitle() {
        if (!hasRightTitle) {
            return "";
        }
        mToolbarRight.setTextColor(getColor(R.color.themeColor));
        return getString(R.string.toll_setting);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        Intent intent = new Intent(getActivity(), PictureTollActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(OLDTOLL, mImageBean);
        intent.putExtra(OLDTOLL, bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Toll toll = data.getBundleExtra(TOLL_TYPE).getParcelable(TOLL_TYPE);
            mImageBean.setToll(toll);
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photo_view;
    }

    @Override
    protected void initView(View rootView) {
        if (tolls.size() != 0) {
            mImageBean = tolls.get(currentItem);
        }

        mViewPager.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {// 越界处理(切换图片时切换图片收费信息，也许之前没有图片收费信息)
                    mImageBean = tolls.get(position);
                } catch (Exception e) {
                    mImageBean = null;
                }

                hasAnim = currentItem == position;
                // 是否包含了已经选中的图片
                mRbSelectPhoto.setChecked(checkImagePath.contains(allPaths.get(position)));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化设置当前选择的数量
        mBtComplete.setEnabled(seletedPaths.size() > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, seletedPaths.size(),
                maxCount));
        // 初始化选择checkbox
        if (!allPaths.isEmpty()) {
            mRbSelectPhoto.setChecked(seletedPaths.contains(allPaths.get(currentItem)));
        }
        mRbSelectPhoto.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
        mRbSelectPhoto.setOnCheckedChangeListener((buttonView, isChecked) -> {

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

                    checkImagePath.add(path);

                    seletedPaths.add(path);
                    tolls.add(mImageBean);
                    unCheckImagePath.remove(path);
                    unCheckImage.remove(mImageBean);
                } else {
                    checkImagePath.remove(path);
                    unCheckImage.remove(mImageBean);
                    unCheckImagePath.remove(path);
                    //tolls.remove(mImageBean);
                }
            } else {
                // 当前取消选择改图片，直接移除 old version
                // 当前取消选择改图片，不能直接移除，要点击完成才能去
//                seletedPaths.remove(path);
//                tolls.remove(mImageBean);
                if (!unCheckImagePath.contains(path)) {
                    unCheckImage.add(mImageBean);
                    unCheckImagePath.add(path);
                }
                checkImagePath.remove(path);

            }
            // 没有选择图片时，是否可以点击完成，应该可以点击，所以注释了下面的代码；需求改变，不需要点击了 #337
            mBtComplete.setEnabled(seletedPaths.size() > 0);
            // 重置当前的选择数量
            mBtComplete.setText(getString(R.string.album_selected_count, seletedPaths.size() - unCheckImagePath.size(),
                    maxCount));
            // 通知图片列表进行刷新
            // 在 PhotoAlbumDetailsFragment 的 refreshDataAndUI() 方法中进行订阅
            // EventBus.getDefault().post(seletedPaths, EventBusTagConfig
            // .EVENT_SELECTED_PHOTO_UPDATE);
        });
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        getActivity().onBackPressed();
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


        if (sPhotoViewDataCacheBean != null) {
            seletedPaths = sPhotoViewDataCacheBean.getSelectedPhoto();
            checkImagePath.addAll(seletedPaths);
            seletedPaths = (ArrayList<String>) seletedPaths.clone();// 克隆一份，防止改变数据源
            allPaths = sPhotoViewDataCacheBean.getAllPhotos();
            currentItem = sPhotoViewDataCacheBean.getCurrentPosition();
            hasRightTitle = sPhotoViewDataCacheBean.isToll();
            rectList = sPhotoViewDataCacheBean.getAnimationRectBeanArrayList();
            maxCount = sPhotoViewDataCacheBean.getMaxCount();
            tolls = sPhotoViewDataCacheBean.getSelectedPhotos();
            removePlaceHolder(tolls);
            mPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        }
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
        if (sPhotoViewDataCacheBean != null) {
            sPhotoViewDataCacheBean = null;
        }
    }

    @OnClick({R.id.bt_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_complete:
                setResult(false);
                break;
            default:
        }
    }

    public static PhotoViewFragment newInstance() {
        PhotoViewFragment f = new PhotoViewFragment();
        return f;
    }


    ////////////////////////////////缩放动画//////////////////////////////////
    private boolean alreadyAnimateIn = false;
    private ArrayList<AnimationRectBean> rectList;
    private PhotoViewPictureContainerFragment mPhotoViewPictureContainerFragment;

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            boolean animateIn = (currentItem == position) && !alreadyAnimateIn;
            Fragment fragment = PhotoViewPictureContainerFragment
                    .newInstance(allPaths.get(position), rectList.get(position), animateIn,
                            currentItem == position);
            alreadyAnimateIn = true;
            return fragment;
        }

        @Override
        public int getCount() {
            return allPaths.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            // 获取到当前的fragment，保留引用，退出时使用
            mPhotoViewPictureContainerFragment = (PhotoViewPictureContainerFragment) object;
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
        // ((PhotoViewActivity)getActivity()).getAppContentView(getActivity()).setBackground
        // (backgroundColor);
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(backgroundColor, "alpha", 0, 255);
        bgAnim.addUpdateListener(animation -> mViewPager.setBackground(backgroundColor));
        return bgAnim;
    }

    public void backPress() {
        if (mPhotoViewPictureContainerFragment != null && mPhotoViewPictureContainerFragment
                .canAnimateCloseActivity()) {
            backgroundColor = new ColorDrawable(Color.WHITE);
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(backgroundColor, "alpha", 0);
            bgAnim.addUpdateListener(animation -> mViewPager.setBackground(backgroundColor));
            bgAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setResult(true);
                    getActivity().overridePendingTransition(-1, -1);
                }
            });
            mPhotoViewPictureContainerFragment.animationExit(bgAnim);
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
        if (!backToPhotoAlbum) {
            seletedPaths.removeAll(unCheckImagePath);
            tolls.removeAll(unCheckImage);
        }
        Intent it = new Intent();
        it.putStringArrayListExtra("photos", seletedPaths);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TOLL, tolls);
        it.putExtra(TOLL, bundle);
        it.putExtra(EXTRA_BACK_HERE, backToPhotoAlbum);
        getActivity().setResult(Activity.RESULT_OK, it);
        getActivity().finish();
    }

    public void removePlaceHolder(List<ImageBean> list) {
        if (list.isEmpty()) {
            return;
        }
        Iterator<ImageBean> iamgesIterator = list.iterator();
        while (iamgesIterator.hasNext()) {
            ImageBean data = iamgesIterator.next();
            if (data.getImgUrl() == null) {
                iamgesIterator.remove();
            }
        }
    }

}
