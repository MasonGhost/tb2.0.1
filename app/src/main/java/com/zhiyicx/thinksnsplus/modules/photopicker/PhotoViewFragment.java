package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.adapter.PhotoPagerAdapter;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/7
 * @contact email:450127106@qq.com
 */

public class PhotoViewFragment extends TSFragment {
    @BindView(R.id.vp_photos)
    ViewPager mViewPager;

    public final static String ARG_SELCTED_PATH = "ARG_SELECTED_PATHS";// 传进来的已经被选择的图片
    public final static String ARG_ALL_PATH = "ARG_ALL_PATHS";// 传进来的所有的图片路径
    public final static String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    @BindView(R.id.rb_select_photo)
    CheckBox mRbSelectPhoto;
    @BindView(R.id.bt_complete)
    TextView mBtComplete;

    private ArrayList<String> seletedPaths;
    private ArrayList<String> allPaths;
    private PhotoPagerAdapter mPagerAdapter;

    public final static long ANIM_DURATION = 200L;

    public final static String ARG_THUMBNAIL_TOP = "THUMBNAIL_TOP";
    public final static String ARG_THUMBNAIL_LEFT = "THUMBNAIL_LEFT";
    public final static String ARG_THUMBNAIL_WIDTH = "THUMBNAIL_WIDTH";
    public final static String ARG_THUMBNAIL_HEIGHT = "THUMBNAIL_HEIGHT";
    public final static String ARG_HAS_ANIM = "HAS_ANIM";
    public final static String ARG_MAX_COUNT = "MAX_COUNT";

    private int thumbnailTop = 0;
    private int thumbnailLeft = 0;
    private int thumbnailWidth = 0;
    private int thumbnailHeight = 0;
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
                ToastUtils.showToast("页数--》position" + position + "currentItem-->" + currentItem + "---" + mViewPager.getCurrentItem());
                // 是否包含了已经选中的图片该图片
                mRbSelectPhoto.setChecked(seletedPaths.contains(mPagerAdapter.getPathAtPosition(position)));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 初始化设置当前选择的数量
        //mBtComplete.setEnabled(seletedPaths.size() > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, seletedPaths.size(), maxCount));
        // 初始化选择checkbox
        mRbSelectPhoto.setChecked(seletedPaths.contains(mPagerAdapter.getPathAtPosition(currentItem)));
        mRbSelectPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String path = mPagerAdapter.getPathAtPosition(mViewPager.getCurrentItem());
                // 达到最大选择数量，添加新的图片，进行提示
                if (seletedPaths.size() >= maxCount && !seletedPaths.contains(path) && isChecked) {
                    Toast.makeText(getActivity(), getString(me.iwf.photopicker.R.string.__picker_over_max_count_tips, maxCount),
                            LENGTH_LONG).show();
                    mRbSelectPhoto.setChecked(false);
                    return;
                }
                if (isChecked) {
                    if (!seletedPaths.contains(path)) {
                        seletedPaths.add(path);
                    }
                } else {
                    seletedPaths.remove(path);
                }

                // 重置当前的选择数量
                //mBtComplete.setEnabled(seletedPaths.size() > 0);
                mBtComplete.setText(getString(R.string.album_selected_count, seletedPaths.size(), maxCount));
                // 通知图片列表进行刷新
                // 在 PhotoAlbumDetailsFragment 的 refreshDataAndUI() 方法中进行订阅
                EventBus.getDefault().post(seletedPaths, EventBusTagConfig.EVENT_SELECTED_PHOTO_UPDATE);
            }
        });
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    public static PhotoViewFragment newInstance(List<String> selectedPaths, List<String> allPhotos, int currentItem, int maxCount) {

        PhotoViewFragment f = new PhotoViewFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_SELCTED_PATH, (ArrayList<String>) selectedPaths);
        args.putStringArrayList(ARG_ALL_PATH, (ArrayList<String>) allPhotos);
        args.putInt(ARG_CURRENT_ITEM, currentItem);
        args.putBoolean(ARG_HAS_ANIM, false);
        args.putInt(ARG_MAX_COUNT, maxCount);
        f.setArguments(args);

        return f;
    }


    public static PhotoViewFragment newInstance(List<String> selectedPaths, List<String> allPhotos, int currentItem, int[] screenLocation, int thumbnailWidth, int thumbnailHeight, int maxCount) {

        PhotoViewFragment f = newInstance(selectedPaths, allPhotos, currentItem, maxCount);

        f.getArguments().putInt(ARG_THUMBNAIL_LEFT, screenLocation[0]);
        f.getArguments().putInt(ARG_THUMBNAIL_TOP, screenLocation[1]);
        f.getArguments().putInt(ARG_THUMBNAIL_WIDTH, thumbnailWidth);
        f.getArguments().putInt(ARG_THUMBNAIL_HEIGHT, thumbnailHeight);
        f.getArguments().putBoolean(ARG_HAS_ANIM, true);

        return f;
    }

    public void setPhotos(List<String> paths, int currentItem) {
        this.allPaths.clear();
        this.allPaths.addAll(paths);
        this.currentItem = currentItem;

        mViewPager.setCurrentItem(currentItem);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            seletedPaths = bundle.getStringArrayList(ARG_SELCTED_PATH);
            seletedPaths = (ArrayList<String>) seletedPaths.clone();// 克隆一份，防止改变数据源
            allPaths = bundle.getStringArrayList(ARG_ALL_PATH);
            hasAnim = bundle.getBoolean(ARG_HAS_ANIM);
            currentItem = bundle.getInt(ARG_CURRENT_ITEM);
            thumbnailTop = bundle.getInt(ARG_THUMBNAIL_TOP);
            thumbnailLeft = bundle.getInt(ARG_THUMBNAIL_LEFT);
            thumbnailWidth = bundle.getInt(ARG_THUMBNAIL_WIDTH);
            thumbnailHeight = bundle.getInt(ARG_THUMBNAIL_HEIGHT);
            maxCount = bundle.getInt(ARG_MAX_COUNT);
        }
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), allPaths);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public ArrayList<String> getAllPaths() {
        return allPaths;
    }

    public ArrayList<String> getSeletedPaths() {
        return seletedPaths;
    }

    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
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

    @OnClick(R.id.bt_complete)
    public void onClick() {
        // 完成图片选择，处理图片返回结果
        Intent it = new Intent();
        it.putStringArrayListExtra("photos", seletedPaths);
        getActivity().setResult(Activity.RESULT_OK, it);
        getActivity().finish();

    }
}
