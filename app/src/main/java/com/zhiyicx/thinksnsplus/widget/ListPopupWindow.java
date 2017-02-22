package com.zhiyicx.thinksnsplus.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/02/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ListPopupWindow<D> extends PopupWindow {
    public static final float POPUPWINDOW_ALPHA = 0.8f;

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha;
    private List<D> mDatas;
    private String mTitle;
    private String mCancle;
    private OnItemListener mItemClickListener;
    private int mWidth;
    private int mHeight;
    private int mItemLayout;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private CommonAdapter<D> mAdapter;

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
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(mWidth > 0 ? mWidth : LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(mHeight > 0 ? mHeight : LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.ppw_for_list, null);
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id
                .tv_pop_list_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
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

    /**
     * 显示popWindow :相对于控件
     */
    @TargetApi(19)
    public void showPopAsDropDown(View parent, int offX, int offY, int gravity) {
        setWindowAlpha(mAlpha);
        // 设置popwindow显示位置
        try {
            showAsDropDown(parent, offX, offY, gravity);
        } catch (Throwable t) {
            showAsDropDown(parent, offX, offY);
        }
    }

    public static <D> ListPopupWindow.Builder Builder() {
        return new ListPopupWindow.Builder();
    }

    public static final class Builder<D> {
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

        private <D> Builder() {
        }

        public ListPopupWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public <D> ListPopupWindow.Builder adapter(CommonAdapter<D> adapter) {
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

        public <D> ListPopupWindow.Builder data(List<D> datas) {
            this.mDatas = datas;
            if (mAdapter != null) {
                mAdapter.getDatas().clear();
                mAdapter.notifyDataSetChanged();
                mAdapter.getDatas().addAll(datas);
                mAdapter.notifyDataSetChanged();
            }
            return this;
        }

        public ListPopupWindow.Builder itemListener(OnItemListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
            return this;
        }

        public <D> ListPopupWindow build() {
            return new ListPopupWindow<D>(this);
        }
    }

    public interface OnItemListener<D> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        void convertView(ViewHolder holder, D o, int position);
    }

}
