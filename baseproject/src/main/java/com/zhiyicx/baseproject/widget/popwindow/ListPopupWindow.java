package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/02/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ListPopupWindow extends PopupWindow {

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha;
    private List mDatas;
    private String mTitle;
    private String mCancle;
    private OnItemListener mItemClickListener;
    private int mWidth;
    private int mHeight;
    private int mItemLayout;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private CommonAdapter mAdapter;

    private ListPopupWindow() {

    }

    private ListPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mDatas = builder.mDatas;
        this.mTitle = builder.mTitle;
        this.mCancle = builder.mCancle;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mItemLayout = builder.mItemLayout;
        this.mItemClickListener = builder.mItemClickListener;
        this.mAdapter = builder.mAdapter;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.ppw_for_list, null);
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id
                .tv_pop_list_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mAdapter);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        setWindowAlpha(mAlpha);
        showAtLocation(mParentView == null ? mContentView : mParentView, Gravity.BOTTOM, 0, 0);
    }

    public void hide() {
        dismiss();
    }

    public static ListPopupWindow.Builder Builder() {
        return new ListPopupWindow.Builder();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private boolean mIsOutsideTouch = true;
        private boolean mIsFocus = true;
        private float mAlpha;
        private int mWidth = 0;
        private int mHeight = 0;
        private int mItemLayout = 0;
        private List mDatas;
        private String mTitle;
        private CommonAdapter mAdapter;
        private String mCancle;
        private OnItemListener mItemClickListener;

        private Builder() {
        }

        public ListPopupWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public ListPopupWindow.Builder adapter(CommonAdapter adapter) {
            this.mAdapter = adapter;
            return this;
        }

        public ListPopupWindow.Builder itemLayout(int itemLayout) {
            this.mItemLayout = itemLayout;
            return this;
        }

        public ListPopupWindow.Builder width(int width) {
            this.mWidth = width;
            return this;
        }

        public ListPopupWindow.Builder height(int height) {
            this.mHeight = height;
            return this;
        }

        public ListPopupWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public ListPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public ListPopupWindow.Builder iFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public ListPopupWindow.Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public ListPopupWindow.Builder cancle(String cancle) {
            this.mCancle = cancle;
            return this;
        }

        public ListPopupWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public ListPopupWindow.Builder data(List datas) {
            this.mDatas = datas;
            return this;
        }

        public ListPopupWindow.Builder itemListener(OnItemListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
            return this;
        }

        public ListPopupWindow build() {
            return new ListPopupWindow(this);
        }
    }

    public interface OnItemListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }

}
