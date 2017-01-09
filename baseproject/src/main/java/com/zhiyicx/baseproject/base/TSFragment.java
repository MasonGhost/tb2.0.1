package com.zhiyicx.baseproject.base;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.StatusBarUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSFragment<P> extends BaseFragment<P> {
    private static final int DEFAULT_TOOLBAR = R.layout.toolbar_custom;
    private static final int DEFAULT_TOOLBAR_BACKGROUD_COLOR = R.color.white;
    private static final int DEFAULT_DIVIDER_COLOR = R.color.general_for_line;
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
        if (mIscUseSatusbar) {
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
//        mToolbar = (Toolbar) toolBarContainer.findViewById(R.id.toolbar);
        toolBarContainer.setBackgroundResource(setToolBarBackgroud());
        mToolbarLeft = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_left);
        mToolbarRight = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_right);
        mToolbarCenter = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_center);

        // 如果标题为空，就隐藏它
        mToolbarCenter.setVisibility(TextUtils.isEmpty(setCenterTitle()) ? View.GONE : View.VISIBLE);
        mToolbarLeft.setVisibility(TextUtils.isEmpty(setLeftTitle()) ? View.GONE : View.VISIBLE);
        mToolbarRight.setVisibility(TextUtils.isEmpty(setRightTitle()) ? View.GONE : View.VISIBLE);
        mToolbarCenter.setText(setCenterTitle());
        mToolbarLeft.setText(setLeftTitle());
        mToolbarRight.setText(setRightTitle());

        mToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLeftClick();
            }
        });

        mToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        if (showToolbar() && getResources().getColor(setToolBarBackgroud()) == Color.WHITE) {
            mToolbarCenter.setTextColor(getResources().getColor(R.color.important_for_content));
            mToolbarRight.setTextColor(getResources().getColor(R.color.important_for_content));
            mToolbarLeft.setTextColor(getResources().getColor(R.color.important_for_content));
        }
    }
}
