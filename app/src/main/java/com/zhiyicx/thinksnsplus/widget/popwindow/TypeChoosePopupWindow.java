package com.zhiyicx.thinksnsplus.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.common.utils.recycleviewdecoration.ShareDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;

import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Describe 用于 item 分类选择
 * @Author Jungle68
 * @Date 2017/9/15
 * @Contact master.jungle68@gmail.com
 */

public class TypeChoosePopupWindow extends PopupWindow {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    public static final int GRID = 2;

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha = 0.8f;
    private int mOritation = VERTICAL;
    private int mSpanCount;
    private int mItemSpacing;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private CommonAdapter mAdapter;

    private TypeChoosePopupWindow() {

    }

    private TypeChoosePopupWindow(TypeChoosePopupWindow.Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mAdapter = builder.mAdapter;
        this.mOritation = builder.mOritation;
        this.mSpanCount = builder.mSpanCount;
        this.mItemSpacing = builder.mItemSpacing;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
//        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.view_type_choose_popup_window, null);
        mContentView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 关闭硬件加速，使用自定义的阴影
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id
                .rv_popup_window);
        // 设置布局管理器
        switch (mOritation) {
            case VERTICAL:
                LinearLayoutManager verticalManager = new LinearLayoutManager(mActivity);
                verticalManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(verticalManager);
                recyclerView.addItemDecoration(new LinearDecoration(0, mItemSpacing, 0,0 ));
                break;
            case HORIZONTAL:
                LinearLayoutManager horizontalManager = new LinearLayoutManager(mActivity);
                horizontalManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(horizontalManager);
                recyclerView.addItemDecoration(new LinearDecoration(0, 0, mItemSpacing, 0));

                break;
            case GRID:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, mSpanCount);
                recyclerView.setLayoutManager(gridLayoutManager);

                recyclerView.addItemDecoration(new ShareDecoration(mItemSpacing));
                break;
            default:
        }

        recyclerView.setAdapter(mAdapter);
        setOnDismissListener(() -> setWindowAlpha(1.0f));
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        if (isShowing()) {
            dismiss();
            return;
        }
        setWindowAlpha(mAlpha);
        showAsDropDown(mParentView, -18, 10);
    }

    public static TypeChoosePopupWindow.Builder Builder() {
        return new TypeChoosePopupWindow.Builder();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private boolean mIsOutsideTouch = true;
        private boolean mIsFocus = true;
        private float mAlpha = POPUPWINDOW_ALPHA;
        private CommonAdapter mAdapter;
        private int mOritation = VERTICAL;
        private int mSpanCount;
        private int mItemSpacing = 1;

        private Builder() {
        }

        public TypeChoosePopupWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public TypeChoosePopupWindow.Builder adapter(CommonAdapter adapter) {
            this.mAdapter = adapter;
            return this;
        }

        public TypeChoosePopupWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public TypeChoosePopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public TypeChoosePopupWindow.Builder iFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public TypeChoosePopupWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public TypeChoosePopupWindow.Builder asVertical() {
            mOritation = VERTICAL;
            return this;
        }

        public TypeChoosePopupWindow.Builder asHorizontal() {
            mOritation = HORIZONTAL;
            return this;
        }

        public TypeChoosePopupWindow.Builder asGrid(int spanCount) {
            mOritation = GRID;
            this.mSpanCount = spanCount;
            return this;
        }

        public TypeChoosePopupWindow.Builder itemSpacing(int mItemSpacing) {
            this.mItemSpacing = mItemSpacing;
            return this;
        }


        public TypeChoosePopupWindow build() {
            return new TypeChoosePopupWindow(this);
        }
    }

}
