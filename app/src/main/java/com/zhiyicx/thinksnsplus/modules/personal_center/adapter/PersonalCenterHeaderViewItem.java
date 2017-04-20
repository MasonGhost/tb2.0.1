package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewScrollChangeEvent;
import com.jakewharton.rxbinding.widget.RxAdapter;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.ZoomView;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import rx.functions.Action1;

/**
 * @author LiuChao
 * @describe 个人中心头部
 * @date 2017/3/8
 * @contact email:450127106@qq.com
 */

public class PersonalCenterHeaderViewItem {
    private static final String TAG = "PersonalCenterHeaderVie";
    /**********************************
     * headerView控件
     ********************************/
    private FrameLayout fl_cover_contaner;// 封面图的容器
    private ImageView iv_background_cover;// 封面
    private ImageView iv_head_icon;// 用户头像
    private TextView tv_user_name;// 用户名
    private TextView tv_user_intro;// 用户简介
    private TextView tv_user_follow;// 用户关注数量
    private TextView tv_user_fans;// 用户粉丝数量
    private LinearLayout ll_dynamic_count_container;// 动态数量的容器
    private TextView tv_dynamic_count;// 动态数量

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private int mDistanceY;// recylerView 滑动距离的累计
    private View mToolBarContainer;// 需要变换透明度的标题栏
    private View mToolBar;
    private ImageView back;
    private ImageView more;
    private TextView userName;
    private View bootomDivider;// 底部的分割线

    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;
    private ImageLoader mImageLoader;

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

    private View headerView;
    private int userNameFirstY = 0;

    public PersonalCenterHeaderViewItem(Activity activity, PhotoSelectorImpl photoSelector, RecyclerView recyclerView, HeaderAndFooterWrapper headerAndFooterWrapper, View mToolBarContainer) {
        mActivity = activity;
        this.mPhotoSelector = photoSelector;
        mRecyclerView = recyclerView;
        mHeaderAndFooterWrapper = headerAndFooterWrapper;
        this.mToolBarContainer = mToolBarContainer;
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mToolBar = mToolBarContainer.findViewById(R.id.rl_toolbar_container);
        back = (ImageView) mToolBarContainer.findViewById(R.id.iv_back);
        more = (ImageView) mToolBarContainer.findViewById(R.id.iv_more);
        userName = (TextView) mToolBarContainer.findViewById(R.id.tv_user_name);
        int translationY = mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
        userName.setY(translationY);
        bootomDivider = mToolBarContainer.findViewById(R.id.v_horizontal_line);
        // 设置初始透明度为0
        setViewColorWithAlpha(userName, TITLE_RGB, 255);
        setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, 0);
        //setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, 0);
        setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, 0);

    }

    public void initHeaderView(boolean isSetScrollListener) {
        headerView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_personal_center_header, null);
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
                int needDistanceY = userNameFirstY - mToolBarContainer.getHeight() - userNamePadding;
                LogUtils.i(TAG + " mToolBarContainer.getHeight() " + mToolBarContainer.getHeight() + " needDistanceY " + needDistanceY + " mDistanceY " + mDistanceY);
                // toolbar文字移动到toolbar中间，这期间的最大滑动距离
                int maxDistance = needDistanceY + mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height);
                if (mDistanceY >= needDistanceY && mDistanceY <= maxDistance) {
                    userName.setTranslationY(maxDistance - mDistanceY);
                } else if (mDistanceY > maxDistance) {
                    userName.setTranslationY(0);
                } else {
                    userName.setTranslationY(mActivity.getResources().getDimensionPixelSize(R.dimen.toolbar_height));
                }
                //当滑动的距离 <= needDistanceY高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
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
                        setViewColorWithAlpha(userName, TITLE_RGB, 0);// 用户名不可见
                    } else {
                        setToolbarIconColor(Color.argb((int) alpha, TOOLBAR_BLACK_ICON[0],
                                TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
                        setViewColorWithAlpha(userName, TITLE_RGB, (int) alpha);
                    }
                    // 尝试设置状态栏文字成白色
                    //StatusBarUtils.statusBarDarkMode(mActivity);
                } else {
                    //如果不是完全不透明状态的bug，将标题栏的颜色设置为完全不透明状态
                    setViewColorWithAlpha(userName, TITLE_RGB, 255);
                    setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, 255);
                    //setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, 255);
                    setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, 255);

                    setToolbarIconColor(Color.argb(255, TOOLBAR_BLACK_ICON[0],
                            TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
                    // 尝试设置状态栏文字成黑色
                    //StatusBarUtils.statusBarLightMode(mActivity);
                }
                // 有可能到顶部了，仍然有一点白色透明背景，强制设置成完全透明
                if (headerTop >= 0) {
                    setViewColorWithAlpha(userName, TITLE_RGB, 0);
                    setViewColorWithAlpha(mToolBarContainer, STATUS_RGB, 0);
                    // setViewColorWithAlpha(mToolBar, TOOLBAR_RGB, 0);
                    setViewColorWithAlpha(bootomDivider, TOOLBAR_DIVIDER_RGB, 0);
                    setToolbarIconColor(Color.argb(255, TOOLBAR_WHITE_ICON[0]
                            , TOOLBAR_WHITE_ICON[1], TOOLBAR_WHITE_ICON[2]));
                    // 尝试设置状态栏文字成白色
                   // StatusBarUtils.statusBarDarkMode(mActivity);
                }
            }
        });
    }

    public void setToolbarIconColor(int argb) {
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(argb, PorterDuff.Mode.SRC_ATOP);
        back.setColorFilter(colorFilter);// 纯黑色
        more.setColorFilter(colorFilter);// 纯黑色
    }

    public void initHeaderViewData(final UserInfoBean userInfoBean) {
        // 显示头像
        mImageLoader.loadImage(mActivity, GlideImageConfig.builder()
                .url(ImageUtils.imagePathConvert(userInfoBean.getAvatar(), ImageZipConfig.IMAGE_70_ZIP))
                .placeholder(R.mipmap.pic_default_portrait2)
                .errorPic(R.mipmap.pic_default_portrait2)
                .imagerView(iv_head_icon)
                .transformation(new GlideCircleBorderTransform(mActivity, mActivity.getResources().getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(mActivity, R.color.white)))
                .build());
        // 设置用户名
        tv_user_name.setText(userInfoBean.getName());
        tv_user_name.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                tv_user_name.getLocationOnScreen(location);
                userNameFirstY = location[1];
                LogUtils.i(TAG + "tv_user_name " + userNameFirstY);
            }
        });
        // 标题栏的用户名
        userName.setText(userInfoBean.getName());
        // 设置简介
        tv_user_intro.setText(userInfoBean.getIntro());

        // 设置关注人数
        String followContent = "关注 " + "<" + ConvertUtils.numberConvert(Integer.parseInt((TextUtils.isEmpty(userInfoBean.getFollowing_count()) ? "0" : userInfoBean.getFollowing_count()))) + ">";
        CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(mActivity, R.color.themeColor))
                .outerColor(ContextCompat.getColor(mActivity, R.color.normal_for_assist_text))
                .format();
        tv_user_follow.setText(followString);

        // 设置粉丝人数
        String fansContent = "粉丝 " + "<" + ConvertUtils.numberConvert(Integer.parseInt((TextUtils.isEmpty(userInfoBean.getFollowed_count()) ? "0" : userInfoBean.getFollowed_count()))) + ">";
        CharSequence fansString = ColorPhrase.from(fansContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(mActivity, R.color.themeColor))
                .outerColor(ContextCompat.getColor(mActivity, R.color.normal_for_assist_text))
                .format();
        tv_user_fans.setText(fansString);

        // 设置动态数量
        String dynamicCountString = userInfoBean.getFeeds_count();
        int dynamicCountInt;
        if (!TextUtils.isEmpty(dynamicCountString)) {
            dynamicCountInt = Integer.parseInt(dynamicCountString);
        } else {
            dynamicCountInt = 0;
        }
        upDateDynamicNums(dynamicCountInt);
        // 设置封面
        setUserCover(userInfoBean.getCover());
        // 设置封面切换
        iv_background_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthBean authBean = AppApplication.getmCurrentLoginAuth();
                // 如果进入的是自己的个人中心，才允许修改背景封面
                if (authBean != null && authBean.getUser_id() == userInfoBean.getUser_id()) {
                    initPhotoPopupWindow();
                    mPhotoPopupWindow.show();
                }
            }
        });
        // 点击头像
        iv_head_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 跳转到粉丝列表
        tv_user_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleFans = new Bundle();
                bundleFans.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
                bundleFans.putLong(FollowFansListFragment.PAGE_DATA, userInfoBean.getUser_id());
                Intent itFans = new Intent(mActivity, FollowFansListActivity.class);
                itFans.putExtras(bundleFans);
                mActivity.startActivity(itFans);
            }
        });

        // 跳转到关注列表
        tv_user_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleFollow = new Bundle();
                bundleFollow.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FOLLOW_FRAGMENT_PAGE);
                bundleFollow.putLong(FollowFansListFragment.PAGE_DATA, userInfoBean.getUser_id());
                Intent itFollow = new Intent(mActivity, FollowFansListActivity.class);
                itFollow.putExtras(bundleFollow);
                mActivity.startActivity(itFollow);
            }
        });
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initHeaderViewUI(View headerView) {
        ViewGroup.LayoutParams headerLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(headerLayoutParams);
        fl_cover_contaner = (FrameLayout) headerView.findViewById(R.id.fl_cover_contaner);
        iv_background_cover = (ImageView) headerView.findViewById(R.id.iv_background_cover);
        iv_head_icon = (ImageView) headerView.findViewById(R.id.iv_head_icon);
        tv_user_name = (TextView) headerView.findViewById(R.id.tv_user_name);
        tv_user_intro = (TextView) headerView.findViewById(R.id.tv_user_intro);
        tv_user_follow = (TextView) headerView.findViewById(R.id.tv_user_follow);
        tv_user_fans = (TextView) headerView.findViewById(R.id.tv_user_fans);
        ll_dynamic_count_container = (LinearLayout) headerView.findViewById(R.id.ll_dynamic_count_container);
        tv_dynamic_count = (TextView) headerView.findViewById(R.id.tv_dynamic_count);
        // 高度为屏幕宽度一半加上20dp
        int width = UIUtils.getWindowWidth(mActivity);
        int height = UIUtils.getWindowWidth(mActivity) / 2 + mActivity.getResources().getDimensionPixelSize(R.dimen.spacing_large);
        LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        fl_cover_contaner.setLayoutParams(containerLayoutParams);
        // 添加头部放缩
        new ZoomView(fl_cover_contaner, mActivity, mRecyclerView, width, height).initZoom();

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

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(mActivity.getString(R.string.choose_from_photo))
                .item2Str(mActivity.getString(R.string.choose_from_camera))
                .bottomStr(mActivity.getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(mActivity)
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        // 选择相册，单张
                        mPhotoSelector.getPhotoListFromSelector(1, null);
                        mPhotoPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {
                        // 选择相机，拍照
                        mPhotoSelector.getPhotoFromCamera(null);
                        mPhotoPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mPhotoPopupWindow.hide();
                    }
                }).build();
    }

    /**
     * 设置用户的封面
     */
    private void setUserCover(String coverImage) {
        // 设置封面
        mImageLoader.loadImage(mActivity, GlideImageConfig.builder()
                .placeholder(R.mipmap.default_pic_personal)
                .errorPic(R.mipmap.default_pic_personal)
                .url(ImageUtils.imagePathConvert(coverImage, 100))// 显示原图
                .imagerView(iv_background_cover)
                .build());
    }

    /**
     * 更新封面
     *
     * @param coverImage
     */
    public void upDateUserCover(String coverImage) {
        setUserCover(coverImage);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 更新动态数量
     *
     * @param dynamicCountInt
     */
    public void upDateDynamicNums(int dynamicCountInt) {
        if (dynamicCountInt <= 0) {
            ll_dynamic_count_container.setVisibility(View.GONE);
        } else {
            ll_dynamic_count_container.setVisibility(View.VISIBLE);
            tv_dynamic_count.setText(mActivity.getString(R.string.dynamic_count, String.valueOf(dynamicCountInt)));
        }
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

}
