package com.zhiyicx.baseproject.widget.pictureviewer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.pictureviewer.core.ImageInfo;
import com.zhiyicx.baseproject.widget.pictureviewer.core.ParcelableSparseArray;
import com.zhiyicx.baseproject.widget.pictureviewer.core.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 图片浏览类
 * @Author Jungle68
 * @Date 2017/3/3
 * @Contact master.jungle68@gmail.com
 */

public class PictureViewer extends FrameLayout {
    private static final int DEFAULT_ANIMAE_TIME = 300;

    List<ImageBean> imgs = new ArrayList<>();
    ParcelableSparseArray<ImageInfo> mInfos = new ParcelableSparseArray<>();

    protected ViewPager mPager;
    protected View mBg;
    protected PhotoView mPhotoView;
    private ImageInfo mInfo;

    private AlphaAnimation in = new AlphaAnimation(0, 1);
    private AlphaAnimation out = new AlphaAnimation(1, 0);
    private PagerAdapter adapter;
    private Context mContext;

    public PictureViewer(Context context) {
        super(context);
        intit(context);
    }


    public PictureViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        intit(context);
    }

    public PictureViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intit(context);
    }

    private void intit(final Context context) {
        this.mContext = context;
        setVisibility(GONE);
        setBackgroundResource(android.R.color.transparent);
        LayoutInflater.from(context).inflate(R.layout.view_picture_viewer, this);
        in.setDuration(DEFAULT_ANIMAE_TIME);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPager.setVisibility(View.VISIBLE);
                mPhotoView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        out.setDuration(DEFAULT_ANIMAE_TIME);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPhotoView.setVisibility(View.VISIBLE);
                mPager.setVisibility(View.INVISIBLE);
                imgs.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBg = findViewById(R.id.bg);
        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imgs.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                PhotoView view = new PhotoView(container.getContext());
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(container.getContext()).load(imgs.get(position).getImgUrl()==null?String.format(ApiConfig.IMAGE_PATH, imgs.get(position).getStorage_id(), 20):imgs.get(position).getImgUrl())
                        .placeholder(R.drawable.shape_default_image)
                        .error(R.drawable.shape_default_image)
                        .into(view);
                container.addView(view);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInfo = mInfos.get(position);
                        close();
                    }
                });
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        mPager.setAdapter(adapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Glide.with(mContext)
                        .load(imgs.get(position).getImgUrl()==null?String.format(ApiConfig.IMAGE_PATH, imgs.get(position).getStorage_id(), 20):imgs.get(position).getImgUrl())
                        .placeholder(R.drawable.shape_default_image)
                        .error(R.drawable.shape_default_image)
                        .into(mPhotoView);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPhotoView = (PhotoView) findViewById(R.id.img);
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mPhotoView.enable();
    }

    /**
     * set data
     *
     * @param images
     * @param infos
     */
    public void setData(List<ImageBean> images, ParcelableSparseArray<ImageInfo> infos) {
        this.imgs.clear();
        this.imgs.addAll(images);
        adapter.notifyDataSetChanged();
        this.mInfos = infos;
    }

    /**
     * show anim
     *
     * @param potison image position
     */
    public void show(int potison) {
        mInfo = mInfos.get(potison);
        Glide.with(mContext)
                .load(imgs.get(potison).getImgUrl()==null?String.format(ApiConfig.IMAGE_PATH, imgs.get(potison).getStorage_id(), 20):imgs.get(potison).getImgUrl())
                .placeholder(R.drawable.shape_default_image)
                .override(mInfo.getWith(),mInfo.getHeight())
                .error(R.drawable.shape_default_image)
                .into(mPhotoView);
        mPager.setCurrentItem(potison);
        mBg.startAnimation(in);
        mBg.setVisibility(View.VISIBLE);
        setVisibility(VISIBLE);
        mPhotoView.animaFrom(mInfo);
    }

    /**
     * close anim
     */
    public void close() {
        if (getVisibility() == View.VISIBLE) {
            mBg.startAnimation(out);
            mPhotoView.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    setVisibility(View.GONE);
                }
            });
        }
    }
}
