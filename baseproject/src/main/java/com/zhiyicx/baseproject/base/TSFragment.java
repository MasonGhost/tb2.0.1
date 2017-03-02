package com.zhiyicx.baseproject.base;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSFragment<P extends IBasePresenter> extends BaseFragment<P> {
    private static final int DEFAULT_TOOLBAR = R.layout.toolbar_custom; // 默认的toolbar
    private static final int DEFAULT_TOOLBAR_BACKGROUD_COLOR = R.color.white;// 默认的toolbar背景色
    private static final int DEFAULT_DIVIDER_COLOR = R.color.general_for_line;// 默认的toolbar下方分割线颜色
    private static final int DEFAULT_TOOLBAR_LEFT_IMG = R.mipmap.topbar_back;// 默认的toolbar左边的图片，一般是返回键
    protected TextView mToolbarLeft;
    protected TextView mToolbarRight;
    protected TextView mToolbarCenter;
    private boolean mIscUseSatusbar = false;// 内容是否需要占用状态栏

    @Override
    protected View getContentView() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (showToolbar()) {// 在需要显示toolbar时，进行添加
            View toolBarContainer = mLayoutInflater.inflate(getToolBarLayoutId(), null);
            initDefaultToolBar(toolBarContainer);
            linearLayout.addView(toolBarContainer);
        }
        if (showToolBarDivider()) {// 在需要显示分割线时，进行添加
            View divider = new View(getContext());
            divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.divider_line)));
            divider.setBackgroundColor(ContextCompat.getColor(getContext(), setToolBarDividerColor()));
            linearLayout.addView(divider);
        }
        View bodyContainer = mLayoutInflater.inflate(getBodyLayoutId(), null);
        bodyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (setUseSatusbar()) {
            //顶上去
            StatusBarUtils.transparencyBar(getActivity());
            linearLayout.setFitsSystemWindows(false);
        } else {
            //不顶上去
            setToolBarTextColor();
            StatusBarUtils.setStatusBarColor(getActivity(), setToolBarBackgroud());
            linearLayout.setFitsSystemWindows(true);
        }
        linearLayout.addView(bodyContainer);
        return linearLayout;
    }

    /**
     * 状态栏是否可用
     *
     * @return 默认不可用
     */
    protected boolean setUseSatusbar() {
        return false;
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

        // 如果标题为空，就隐藏它
        mToolbarCenter.setVisibility(TextUtils.isEmpty(setCenterTitle()) ? View.GONE : View.VISIBLE);
        mToolbarCenter.setText(setCenterTitle());
        mToolbarLeft.setVisibility(TextUtils.isEmpty(setLeftTitle()) && setLeftImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarLeft.setText(setLeftTitle());
        mToolbarRight.setVisibility(TextUtils.isEmpty(setRightTitle()) && setRightImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarRight.setText(setRightTitle());
        mToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), setLeftImg()), null, null, null);
        mToolbarRight.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), setRightImg()), null, null, null);
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

    /**
     * 根据toolbar的背景设置它的文字颜色
     */
    protected void setToolBarTextColor() {
        // 如果toolbar背景是白色的，就将文字颜色设置成黑色
        if (showToolbar() && ContextCompat.getColor(getContext(), setToolBarBackgroud()) == Color.WHITE) {
            mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
            mToolbarRight.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_text_color));

            mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
            StatusBarUtils.statusBarLightMode(getActivity());
        }
    }

    /**
     * 添加返回按键的监听方法，在它所依附的activity中调用
     *
     * @param keyCode
     * @param event
     * @return false 表示down事件未处理，会继续传递，交给up处理，知道结束或true停止
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
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
}
