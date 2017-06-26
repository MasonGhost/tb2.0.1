package com.zhiyicx.baseproject.base;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.dialog.LoadingDialog;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSFragment<P extends IBasePresenter> extends BaseFragment<P> implements WindowUtils.OnWindowDismisslistener {
    private static final int DEFAULT_TOOLBAR = R.layout.toolbar_custom; // 默认的toolbar
    private static final int DEFAULT_TOOLBAR_BACKGROUD_COLOR = R.color.white;// 默认的toolbar背景色
    private static final int DEFAULT_DIVIDER_COLOR = R.color.general_for_line;// 默认的toolbar下方分割线颜色
    private static final int DEFAULT_TOOLBAR_LEFT_IMG = R.mipmap.topbar_back;// 默认的toolbar左边的图片，一般是返回键

    protected TextView mToolbarLeft;
    protected View mDriver;
    protected TextView mToolbarRight;
    protected TextView mToolbarCenter;
    protected View mStatusPlaceholderView;
    private View mCenterLoadingView; // 加载
    private ImageView mIvRefresh; // 头部左边的刷新控件

    private boolean mIscUseSatusbar = false;// 内容是否需要占用状态栏
    protected ViewGroup mSnackRootView;
    private boolean mIsNeedClick = true;// 缺省图是否需要点击
    private boolean rightViewHadTranslated = false;// 右上角的按钮因为音乐播放悬浮显示，是否已经偏左移动
    private boolean isFirstIn = true;// 是否是第一次进入页面
    private Subscription mViewTreeSubscription = null;// View 树监听订阅器
    private LoadingDialog mCenterLoadingDialog;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected View getContentView() {

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (setUseSatusbar() && setUseStatusView()) { // 是否添加和状态栏等高的占位 View
            mStatusPlaceholderView = new View(getContext());
            mStatusPlaceholderView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.getStatuBarHeight(getContext())));
            if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0 && ContextCompat.getColor(getContext(), setToolBarBackgroud()) == Color.WHITE) {
                mStatusPlaceholderView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.themeColor));
            } else {
                mStatusPlaceholderView.setBackgroundColor(ContextCompat.getColor(getContext(), setToolBarBackgroud()));
            }
            linearLayout.addView(mStatusPlaceholderView);
        }
        if (showToolbar()) {// 在需要显示toolbar时，进行添加
            View toolBarContainer = mLayoutInflater.inflate(getToolBarLayoutId(), null);
            initDefaultToolBar(toolBarContainer);
            linearLayout.addView(toolBarContainer);
        }
        if (showToolBarDivider()) {// 在需要显示分割线时，进行添加
            mDriver = new View(getContext());
            mDriver.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.divider_line)));
            mDriver.setBackgroundColor(ContextCompat.getColor(getContext(), setToolBarDividerColor()));
            linearLayout.addView(mDriver);
        }
        if (setUseSatusbar()) {
            // 状态栏顶上去
            StatusBarUtils.transparencyBar(getActivity());
            linearLayout.setFitsSystemWindows(false);
        } else {
            // 状态栏不顶上去
            StatusBarUtils.setStatusBarColor(getActivity(), setToolBarBackgroud());
            linearLayout.setFitsSystemWindows(true);
        }
        setToolBarTextColor();
        // 是否设置状态栏文字图标灰色，对 小米、魅族、Android 6.0 及以上系统有效
        if (setStatusbarGrey()) {
            StatusBarUtils.statusBarLightMode(getActivity());
        }
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 内容区域
        final View bodyContainer = mLayoutInflater.inflate(getBodyLayoutId(), null);
        bodyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(bodyContainer);
        // 加载动画
        if (setUseCenterLoading()) {
            mCenterLoadingView = mLayoutInflater.inflate(R.layout.view_center_loading, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (!showToolbar()) {
                params.setMargins(0, getstatusbarAndToolbarHeight(), 0, 0);
            }
            mCenterLoadingView.setLayoutParams(params);
            if (setUseCenterLoadingAnimation()) {
                ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).start();
            }
            RxView.clicks(mCenterLoadingView.findViewById(R.id.iv_center_holder))
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .compose(this.<Void>bindToLifecycle())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (mIsNeedClick) {
                                setLoadingViewHolderClick();
                            }
                        }
                    });

            frameLayout.addView(mCenterLoadingView);
        }
        linearLayout.addView(frameLayout);
        mSnackRootView = (ViewGroup) getActivity().findViewById(android.R.id.content).getRootView();
        if (needCenterLoadingDialog()){
            mCenterLoadingDialog=new LoadingDialog(getActivity());
        }
        return linearLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        musicWindowsStatus(WindowUtils.getIsShown());
        if (!this.getClass().getSimpleName().equals("InfoListFragment")) {
            WindowUtils.setWindowDismisslistener(this);
        }
    }


    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSnackMessage(String message, Prompt prompt) {
        TSnackbar.make(mSnackRootView, message, TSnackbar.LENGTH_SHORT)
                .setPromptThemBackground(prompt)
                .show();
    }

    @Override
    public void showSnackSuccessMessage(String message) {
        showSnackMessage(message, Prompt.SUCCESS);
    }

    @Override
    public void showSnackErrorMessage(String message) {
        showSnackMessage(message, Prompt.ERROR);
    }

    @Override
    public void showSnackWarningMessage(String message) {
        showSnackMessage(message, Prompt.WARNING);
    }

    @Override
    public void showSnackLoadingMessage(String message) {
        TSnackbar.make(mSnackRootView, message, TSnackbar.LENGTH_INDEFINITE)
                .setPromptThemBackground(Prompt.SUCCESS)
                .addIconProgressLoading(0, true, false)
                .show();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onDismiss() {
        View view = getLeftViewOfMusicWindow();
        if (view != null && WindowUtils.getIsPause()) {
            view.setTranslationX(0);
        }
        if (WindowUtils.getIsPause()) {
            WindowUtils.setWindowDismisslistener(null);
        }
    }

    /**
     * 关闭加载动画
     */
    protected void closeLoadingView() {
        if (mCenterLoadingView == null) {
            return;
        }
        if (mCenterLoadingView.getVisibility() == View.VISIBLE) {
            ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).stop();
            mCenterLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 开启加载动画
     */
    protected void showLoadingView() {
        if (mCenterLoadingView == null) {
            return;
        }
        if (mCenterLoadingView.getVisibility() == View.GONE) {
            mCenterLoadingView.findViewById(R.id
                    .iv_center_load).setVisibility(View.VISIBLE);
            ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id
                    .iv_center_load)).getDrawable()).start();
            mCenterLoadingView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载失败，占位图点击事件
     */
    protected void setLoadingViewHolderClick() {
        if (mCenterLoadingView == null) {
            return;
        }
        mCenterLoadingView.setVisibility(View.VISIBLE);
        mCenterLoadingView.findViewById(R.id.iv_center_load).setVisibility(View.VISIBLE);
        mCenterLoadingView.findViewById(R.id.iv_center_holder).setVisibility(View.GONE);
        ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).start();

    }

    /**
     * 显示加载失败
     */
    protected void showLoadViewLoadError() {
        showErrorImage();
        mIsNeedClick = true;
    }

    /**
     * 显示加载失败
     */
    protected void showLoadViewLoadErrorDisableClick() {
        showErrorImage();
        mIsNeedClick = false;
    }

    protected void showLoadViewLoadErrorDisableClick(boolean isNeedClick) {
        showErrorImage();
        mIsNeedClick = isNeedClick;
    }

    private void showErrorImage() {
        if (mCenterLoadingView == null) {
            return;
        }
        mCenterLoadingView.setVisibility(View.VISIBLE);
        ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).stop();
        mCenterLoadingView.findViewById(R.id.iv_center_load).setVisibility(View.GONE);
        mCenterLoadingView.findViewById(R.id.iv_center_holder).setVisibility(View.VISIBLE);
    }


    /**
     * 登录提示框
     */
    @Override
    public void showLoginPop() {
        goLogin();
    }

    /**
     * 设置加载失败占位图
     *
     * @param resId
     */
    protected void setLoadViewHolderImag(@DrawableRes int resId) {
        if (mCenterLoadingView == null) {
            return;
        }
        ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_holder)).setImageResource(resId);
    }

    /**
     * 获取状态栏和操作栏的高度
     *
     * @return
     */
    protected int getstatusbarAndToolbarHeight() {
        return DeviceUtils.getStatuBarHeight(getContext()) + getResources().getDimensionPixelOffset(R.dimen.toolbar_height) + getResources().getDimensionPixelOffset(R.dimen.divider_line);
    }

    @Override
    public void showCenterLoading(String msg) {
        mCenterLoadingDialog.showStateIng(msg);
    }

    @Override
    public void hideCenterLoading() {
        mCenterLoadingDialog.onDestroy();
    }

    /**
     * 是否开启中心加载布局
     *
     * @return
     */
    protected boolean setUseCenterLoading() {
        return false;
    }

    protected boolean setUseCenterLoadingAnimation() {
        return true;
    }

    /**
     * 状态栏默认为灰色
     * 支持小米、魅族以及 6.0 以上机型
     *
     * @return
     */
    protected boolean setStatusbarGrey() {
        return true;
    }

    /**
     * 状态栏是否可用
     *
     * @return 默认不可用
     */
    protected boolean setUseSatusbar() {
        if (!this.getActivity().getClass().getSimpleName().contains("HomeActivity")) {
            mIscUseSatusbar = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        }
        return mIscUseSatusbar;
    }

    /**
     * 设置是否需要添加和状态栏等高的占位 view
     *
     * @return
     */
    protected boolean setUseStatusView() {
        boolean userStatusView = false;
        if (!this.getActivity().getClass().getSimpleName().contains("HomeActivity")) {
            userStatusView = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        }
        return userStatusView;
    }


    /**
     * 是否显示toolbar,默认显示
     */
    protected boolean showToolbar() {
        return true;
    }

    /**
     * 获取toolbar的布局文件,如果需要返回自定义的toolbar布局，重写该方法；否则默认返回缺省的toolbar
     */
    protected int getToolBarLayoutId() {
        return DEFAULT_TOOLBAR;
    }

    protected int setToolBarBackgroud() {
        return DEFAULT_TOOLBAR_BACKGROUD_COLOR;
    }

    /**
     * 是否显示分割线,默认显示
     */
    protected boolean showToolBarDivider() {
        return false;
    }

    /**
     * 音乐悬浮窗是否正在显示
     */
/*    protected void musicWindowsStatus(boolean isShow) {
        final View view = getLeftViewOfMusicWindow();
        if (isShow && !rightViewHadTranslated) {
            if (view.getVisibility() == View.VISIBLE) {
                // 向左移动一定距离
                int rightX = ConvertUtils.dp2px(getContext(), 44) * 3 / 4 + ConvertUtils.dp2px(getContext(), 15);
                view.setTranslationX(-rightX);
                rightViewHadTranslated = true;
            } else {
                view.setTranslationX(0);
                rightViewHadTranslated = false;
            }
        }
    }*/
    protected void musicWindowsStatus(final boolean isShow) {
        WindowUtils.changeToBlackIcon();
        final View view = getLeftViewOfMusicWindow();
        if (getLeftViewOfMusicWindow() != null) {
            mViewTreeSubscription = RxView.globalLayouts(getLeftViewOfMusicWindow())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (view != null && isShow) {
                                if (view.getVisibility() == View.VISIBLE) {
                                    // 向左移动一定距离
                                    int rightX = ConvertUtils.dp2px(getContext(), 44) * 3 / 4 + ConvertUtils.dp2px(getContext(), 15);
                                    view.setTranslationX(-rightX);
                                }
                            }
//                            if (mViewTreeSubscription != null) {
//                                mViewTreeSubscription.unsubscribe();
//                            }
                        }
                    });
        }
    }

    protected View getLeftViewOfMusicWindow() {
        return mToolbarRight;
    }

    protected boolean needCenterLoadingDialog(){
        return false;
    }
    /**
     * 是否显示分割线,默认显示
     */
    protected int setToolBarDividerColor() {
        return DEFAULT_DIVIDER_COLOR;
    }

    /**
     * 获取toolbar下方的布局文件
     */
    protected abstract int getBodyLayoutId();

    /**
     * 初始化toolbar布局,如果进行了自定义toolbar布局，就应该重写该方法
     */
    protected void initDefaultToolBar(View toolBarContainer) {
        toolBarContainer.setBackgroundResource(setToolBarBackgroud());
        mToolbarLeft = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_left);
        mToolbarRight = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_right);
        mToolbarCenter = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_center);
        mIvRefresh = (ImageView) toolBarContainer.findViewById(R.id.iv_refresh);
        // 如果标题为空，就隐藏它
        mToolbarCenter.setVisibility(TextUtils.isEmpty(setCenterTitle()) ? View.GONE : View.VISIBLE);
        mToolbarCenter.setText(setCenterTitle());
        mToolbarLeft.setVisibility(TextUtils.isEmpty(setLeftTitle()) && setLeftImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarLeft.setText(setLeftTitle());
        mToolbarRight.setVisibility(TextUtils.isEmpty(setRightTitle()) && setRightImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarRight.setText(setRightTitle());

        setToolBarLeftImage(setLeftImg());
        setToolBarRightImage(setRightImg());
        RxView.clicks(mToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setLeftClick();
                    }
                });
        RxView.clicks(mToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setRightClick();
                    }
                });
        RxView.clicks(mToolbarCenter)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setCenterClick();
                    }
                });
    }

    /**
     * set ToolBar left image
     *
     * @param resImg image resourse
     */
    protected void setToolBarLeftImage(int resImg) {
        mToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), resImg), null, null, null);
    }

    /**
     * set ToolBar left image
     *
     * @param resImg image resourse
     */
    protected void setToolBarRightImage(int resImg) {
        mToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), resImg), null);
    }

    /**
     * 设置中间的标题
     */
    protected String setCenterTitle() {
        return "";
    }

    /**
     * 设置左边的标题
     */
    protected String setLeftTitle() {
        return "";
    }

    /**
     * 显示右上角的加载动画
     */
    protected void showLeftTopLoading() {
        mIvRefresh.setVisibility(View.VISIBLE);
        ((AnimationDrawable) mIvRefresh.getDrawable()).start();
    }

    /**
     * 隐藏右上角的加载动画
     */
    protected void hideLeftTopLoading() {
        mIvRefresh.setVisibility(View.GONE);
        ((AnimationDrawable) mIvRefresh.getDrawable()).stop();
    }


    protected void setLeftTextColor(@ColorRes int resId) {
        mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    /**
     * 设置右边的标题
     */
    protected String setRightTitle() {
        return "";
    }

    /**
     * 设置左边的图片
     */
    protected int setLeftImg() {
        return DEFAULT_TOOLBAR_LEFT_IMG;
    }

    /**
     * 设置右边的图片
     */
    protected int setRightImg() {
        return 0;
    }

    /**
     * 设置左边的点击事件，默认为关闭activity，有必要重写该方法
     */
    protected void setLeftClick() {
        getActivity().finish();
    }

    /**
     * 设置右边的点击时间，有必要重写该方法
     */
    protected void setRightClick() {
    }

    protected void setCenterClick() {
    }

    /**
     * 根据toolbar的背景设置它的文字颜色
     */
    protected void setToolBarTextColor() {
        // 如果toolbar背景是白色的，就将文字颜色设置成黑色
        if (showToolbar() && ContextCompat.getColor(getContext(), setToolBarBackgroud()) == Color.WHITE) {
            mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
            mToolbarRight.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_text_color));

            mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));

        }
    }

    /**
     * 设置 title 的文字颜色
     *
     * @param resId color  resource id
     */
    protected void setCenterTextColor(@ColorRes int resId) {
        mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    /**
     * 设置右侧文字
     *
     * @param rightText 设置右侧文字 ，文字内容
     */
    protected void setRightText(String rightText) {
        changeText(mToolbarRight, rightText);
    }

    /**
     * 设置左侧文字内容
     *
     * @param leftText 左侧文字内容
     */
    protected void setLeftText(String leftText) {
        changeText(mToolbarLeft, leftText);
    }

    /**
     * 设置中间文字内容
     *
     * @param centerText 中间文字内容
     */
    protected void setCenterText(String centerText) {
        changeText(mToolbarCenter, centerText);
    }

    private void changeText(TextView view, String string) {
        if (TextUtils.isEmpty(string)) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(string);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置状态栏占位图背景色
     *
     * @param resId
     */
    public void setStatusPlaceholderViewBackgroundColor(int resId) {
        if (mStatusPlaceholderView != null) {
            mStatusPlaceholderView.setBackgroundColor(resId);
        }
    }

    /**
     * 登录跳转
     */
    private void goLogin() {
        //创建一个隐式的 Intent 对象，
        Intent intent = new Intent();
        intent.setAction("zhiyicx.intent.action.LOGIN");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("bundle_tourist_login", true);
        intent.setType("text/plain");
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    protected int getColor(int resId) {
        return getResources().getColor(resId);
    }
}
