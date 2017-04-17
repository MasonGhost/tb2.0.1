package com.zhiyicx.thinksnsplus.modules.channel.detail.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideStokeTransform;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.ZoomView;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailContract;
import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailPresenter;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhiyicx.thinksnsplus.widget.ColorFilterTextView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import static com.zhiyicx.thinksnsplus.R.id.fl_cover_contaner;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ItemChannelDetailHeader implements ZoomView.ZoomTouchListenerForRefresh {
    private static final String TAG = "ItemChannelDetailHeader";
    /**********************************
     * headerView控件
     ********************************/
    private ImageView iv_channel_header_icon;// 频道头像
    private TextView tv_channel_name;//频道名称
    private TextView tv_channel_description;//频道简介
    private TextView tv_subscrib_count;//订阅数量
    private TextView tv_share_count;// 分享数量
    private LinearLayout fl_header_container;// 布局根结点

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private int mDistanceY;// recylerView 滑动距离的累计
    private View mToolBarContainer;// 需要变换透明度的标题栏
    private View mToolBar;
    private ImageView back;
    private ColorFilterTextView subscribBtn;
    private TextView channelName;
    private View bootomDivider;// 底部的分割线
    private ImageView refreshImage;// 刷新小菊花
    private ChannelDetailContract.Presenter mChannelDetailPresenter;
    private View headerView;
    private boolean isRefreshing = false;// 是否正在刷新


    /**
     * 标题文字的颜色:#333333
     **/
    public static int[] TITLE_RGB = {51, 51, 51};

    /**
     * 状态栏的颜色变化，也就是toolbar上半部分的底色:#ffffff
     **/
    public static int[] STATUS_RGB = {255, 255, 255};

    /**
     * toolbar的背景色：#ffffff
     **/
    public static int[] TOOLBAR_RGB = {255, 255, 255};

    /**
     * toolbar下方的分割线颜色：#dedede
     **/
    public static int[] TOOLBAR_DIVIDER_RGB = {222, 222, 222};

    /**
     * toolbar图标白色:滑到顶部的时候
     **/
    public static int[] TOOLBAR_WHITE_ICON = {255, 255, 255};
    /**
     * toolbar图标黑色：从顶部往下滑
     **/
    public static int[] TOOLBAR_BLACK_ICON = {51, 51, 51};
    /**
     * toolbar右边订阅图标白色：滑到顶部的时候
     */
    public static int[] TOOLBAR_RIGHT_WHITE = {255, 255, 255};
    /**
     * toolbar右边订阅图标主题色：从顶部往下滑动的时候
     */
    public static int[] TOOLBAR_RIGHT_BLUE = {89, 182, 215};
    private int channelNameFirstY = 0;

    public ItemChannelDetailHeader(Activity activity, RecyclerView recyclerView, HeaderAndFooterWrapper headerAndFooterWrapper, View mToolBarContainer, ChannelDetailContract.Presenter channelDetailPresenter) {
        mActivity = activity;
        mRecyclerView = recyclerView;
        mHeaderAndFooterWrapper = headerAndFooterWrapper;
        this.mToolBarContainer = mToolBarContainer;
        mToolBar = mToolBarContainer.findViewById(R.id.rl_toolbar_container);
        back = (ImageView) mToolBarContainer.findViewById(R.id.iv_back);
        subscribBtn = (ColorFilterTextView) mToolBarContainer.findViewById(R.id.iv_subscrib_btn);
        channelName = (TextView) mToolBarContainer.findViewById(R.id.tv_channel_name);
        int translationY = mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        channelName.setY(translationY);
        bootomDivider = mToolBarContainer.findViewById(R.id.v_horizontal_line);
        refreshImage = (ImageView) mToolBarContainer.findViewById(R.id.iv_refresh);
        this.mChannelDetailPresenter = channelDetailPresenter;
        // 设置初始透明度为0
        setViewColorWithAlpha(channelName, TITLE_RGB, 0);
        setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, 0);
        // setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, 0);
        setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, 0);

    }

    public void initHeaderView(boolean isSetScrollListener) {
        headerView = LayoutInflater.from(mActivity).inflate(R.layout.item_channel_detail_header, null);
        initHeaderViewUI(headerView);
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        // 设置recyclerview的滑动监听，用来处理toolbar透明渐变的效果
        if (isSetScrollListener) {
            setScrollListenter();
        }
    }

    public void setScrollListenter() {
        if (headerView == null) {
            throw new NullPointerException("header view not be null");
        }
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                int headerTop = headerView.getTop();
                //toolbar文字上边缘距离toolbar上边缘的距离
                int userNamePadding = (mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height) - mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_center_text_size)) / 2;
                // 滑动距离为多少时，toolbar完全不透明
                int needDistanceY = channelNameFirstY - mToolBarContainer.getHeight() - userNamePadding;
                LogUtils.i(TAG + " mToolBarContainer.getHeight() " + mToolBarContainer.getHeight() + " needDistanceY " + needDistanceY + " mDistanceY " + mDistanceY);
                // toolbar文字移动到toolbar中间，这期间的最大滑动距离
                int maxDistance = needDistanceY + mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
                if (mDistanceY >= needDistanceY && mDistanceY <= maxDistance) {
                    channelName.setTranslationY(maxDistance - mDistanceY);
                } else if (mDistanceY > maxDistance) {
                    channelName.setTranslationY(0);
                } else {
                    channelName.setTranslationY(mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height));
                }
                //当滑动的距离 <= headerView高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= needDistanceY) {
                    float scale = (float) mDistanceY / needDistanceY;
                    float alpha = scale * 255;
                    //setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, (int) alpha);
                    setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, (int) alpha);
                    setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, (int) alpha);
                    if (alpha == 0) {
                        // 设置ImageView的ColorFilter从而改变图标的颜色
                        setToolbarIconColor(Color.argb(255, TOOLBAR_WHITE_ICON[0],
                                TOOLBAR_WHITE_ICON[1], TOOLBAR_WHITE_ICON[2]));
                        setRightTextViewColor(Color.argb(255, TOOLBAR_RIGHT_WHITE[0],
                                TOOLBAR_RIGHT_WHITE[1], TOOLBAR_RIGHT_WHITE[2]));
                        setViewColorWithAlpha(channelName, TITLE_RGB, 0);// 用户名不可见
                    } else {
                        setToolbarIconColor(Color.argb((int) alpha, TOOLBAR_BLACK_ICON[0],
                                TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
                        setRightTextViewColor(Color.argb((int) alpha, TOOLBAR_RIGHT_BLUE[0],
                                TOOLBAR_RIGHT_BLUE[1], TOOLBAR_RIGHT_BLUE[2]));
                        setViewColorWithAlpha(channelName, TITLE_RGB, (int) alpha);
                    }
                    // 尝试设置状态栏文字成白色
                    StatusBarUtils.statusBarDarkMode(mActivity);
                } else {
                    //如果不是完全不透明状态的bug，将标题栏的颜色设置为完全不透明状态
                    setViewColorWithAlpha(channelName, TITLE_RGB, 255);
                    setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, 255);
                    //setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, 255);
                    setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, 255);

                    setToolbarIconColor(Color.argb(255, TOOLBAR_BLACK_ICON[0],
                            TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
                    setRightTextViewColor(Color.argb(255, TOOLBAR_RIGHT_BLUE[0],
                            TOOLBAR_RIGHT_BLUE[1], TOOLBAR_RIGHT_BLUE[2]));
                    // 尝试设置状态栏文字成黑色
                    StatusBarUtils.statusBarLightMode(mActivity);
                }
                // 有可能到顶部了，仍然有一点白色透明背景，强制设置成完全透明
                if (headerTop >= 0) {
                    setViewColorWithAlpha(channelName, TITLE_RGB, 0);
                    setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, 0);
                    // setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, 0);
                    setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, 0);
                    setToolbarIconColor(Color.argb(255, TOOLBAR_WHITE_ICON[0]
                            , TOOLBAR_WHITE_ICON[1], TOOLBAR_WHITE_ICON[2]));
                    setRightTextViewColor(Color.argb(255, TOOLBAR_RIGHT_WHITE[0],
                            TOOLBAR_RIGHT_WHITE[1], TOOLBAR_RIGHT_WHITE[2]));
                    // 尝试设置状态栏文字成白色
                    StatusBarUtils.statusBarDarkMode(mActivity);
                }
            }
        });
    }

    public void setToolbarIconColor(int argb) {
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(argb, PorterDuff.Mode.SRC_ATOP);
        back.setColorFilter(colorFilter);// 纯黑色
        //subscribBtn.setColorFilter(colorFilter);// 纯黑色
    }

    public void setRightTextViewColor(int argb) {
        Drawable[] drawables = subscribBtn.getCompoundDrawables();
        Drawable leftDrawable = drawables[0];
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(argb, PorterDuff.Mode.SRC_ATOP);
        leftDrawable.setColorFilter(colorFilter);
        subscribBtn.setColorFilter(colorFilter);
        Drawable drawable = subscribBtn.getBackground();
        drawable.setColorFilter(colorFilter);
    }

    public void initHeaderViewData(final ChannelSubscripBean channelSubscripBean) {
        ChannelInfoBean channelInfoBean = channelSubscripBean.getChannelInfoBean();
        // 显示头像
        ChannelInfoBean.ChannelCoverBean channelCoverBean = channelInfoBean.getCover();

        Glide.with(mActivity)
                .load(ImageUtils.imagePathConvert(channelCoverBean.getId() + "",
                        ImageZipConfig.IMAGE_70_ZIP))
                .asBitmap()
                .transform(new GlideStokeTransform(mActivity, 20))
                .placeholder(R.mipmap.icon_256)
                .error(R.mipmap.icon_256)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        Bitmap mBgBitmap = resource.copy(Bitmap.Config.RGB_565, false);
                        // 设置封面
                        iv_channel_header_icon.setImageBitmap(resource);
                        Palette mPalette = Palette.from(mBgBitmap).generate();
                        // 设置封面
                        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap
                                (mBgBitmap, mBgBitmap.getWidth(), mBgBitmap.getHeight()));
                        fl_header_container.setBackgroundDrawable(drawable);
                    }
                });

        // 设置频道名称
        tv_channel_name.setText(channelInfoBean.getTitle());
        tv_channel_name.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                tv_channel_name.getLocationOnScreen(location);
                channelNameFirstY = location[1];
                LogUtils.i(TAG + "tv_user_name " + channelNameFirstY);
            }
        });
        // 标题栏的频道名称
        channelName.setText(channelInfoBean.getTitle());
        // 设置简介
        tv_channel_description.setText(channelInfoBean.getDescription());

        // 设置订阅人数
        tv_subscrib_count.setText(mActivity.getString(R.string.channel_follow) + " " + ConvertUtils.numberConvert(channelInfoBean.getFollow_count()));
        // 设置分享人数
        tv_share_count.setText(mActivity.getString(R.string.channel_share) + " " + ConvertUtils.numberConvert(channelInfoBean.getFeed_count()));
        // 设置封面

        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initHeaderViewUI(View headerView) {
        ViewGroup.LayoutParams headerLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(headerLayoutParams);
        iv_channel_header_icon = (ImageView) headerView.findViewById(R.id.iv_channel_header_icon);
        tv_channel_name = (TextView) headerView.findViewById(R.id.tv_channel_name);
        tv_channel_description = (TextView) headerView.findViewById(R.id.tv_channel_description);
        tv_subscrib_count = (TextView) headerView.findViewById(R.id.tv_subscrib_count);
        tv_share_count = (TextView) headerView.findViewById(R.id.tv_share_count);
        fl_header_container = (LinearLayout) headerView.findViewById(R.id.fl_header_container);
        // 添加头部放缩
        fl_header_container.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.i("post run" + fl_header_container.getWidth() + "  " + fl_header_container.getHeight());
                // 通过post获取控件宽高后，创建缩放头部
                ZoomView zoomView = new ZoomView(fl_header_container, mActivity, mRecyclerView, fl_header_container.getWidth(), fl_header_container.getHeight());
                zoomView.initZoom();
                // 添加刷新监听
                zoomView.setZoomTouchListenerForRefresh(ItemChannelDetailHeader.this);
            }
        });
    }

    public void setViewColorWithAlpha(View v, int[] colorRGB, int alpha) {
        int color = Color.argb(alpha, colorRGB[0], colorRGB[1], colorRGB[2]);
        if (v instanceof TextView) {
            TextView textView = (TextView) v;
            textView.setTextColor(color);
            return;
        }
        v.setBackgroundColor(color);
    }


    @Override
    public void moving(int moveDistance) {

    }

    @Override
    public void refreshStart(int moveDistance) {
        // 在网络请求结束后，进行调用
        mChannelDetailPresenter.requestNetData(0l, false);
        refreshImage.setVisibility(View.VISIBLE);
        ((AnimationDrawable) refreshImage.getDrawable()).start();
        isRefreshing = true;
    }

    @Override
    public void refreshEnd() {
        ((AnimationDrawable) refreshImage.getDrawable()).stop();
        refreshImage.setVisibility(View.GONE);
        isRefreshing = false;
    }

    @Override
    public void canRefresh(int moveDistance, boolean canRefresh) {
        // 移动时，如果正在刷新或者可刷新，就显示刷新菊花
        if (canRefresh || isRefreshing) {
            refreshImage.setVisibility(View.VISIBLE);
        } else {
            refreshImage.setVisibility(View.GONE);
        }
    }
}
