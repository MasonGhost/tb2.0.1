package com.zhiyicx.thinksnsplus.widget;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AccountBean;
import com.zhiyicx.thinksnsplus.modules.login.AccountAdapterV2;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/27
 * @contact email:648129313@qq.com
 */

public class AccountPopWindow extends PopupWindow implements Filterable {

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private boolean mIsOutsideTouch = true;
    private boolean mIsFocus = true;
    private float mAlpha;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mItemLayout = 0;
    private List<AccountBean> mDatas;
    private AccountAdapterV2 mAdapter;
    private String mCancel;
    private OnItemListener mItemClickListener;
    private List<AccountBean> mOldData;

    public AccountPopWindow() {
    }

    public AccountPopWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mDatas = builder.mDatas;
        this.mOldData = builder.mDatas;
        this.mCancel = builder.mCancel;
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
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_account, null);
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id
                .rv_account);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
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
        setWindowAlpha(1.0f);
        showAsDropDown(mParentView == null ? mContentView : mParentView);
//        showAtLocation(mParentView == null ? mContentView : mParentView, Gravity.BOTTOM, 0, 0);
    }

    public void hide() {
        dismiss();
    }

    public CommonAdapter getAdapter() {
        return mAdapter;
    }

    public void dataChange(List datas) {
        this.mDatas = datas;
        this.mAdapter.notifyDataSetChanged();
    }

    public void dataChangeOne(int position) {
        this.mAdapter.notifyItemChanged(position);
    }

    public static AccountPopWindow.Builder Builder() {
        return new AccountPopWindow.Builder();
    }

    @Override
    public Filter getFilter() {
        return new MyFilter();
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
        private List<AccountBean> mDatas;
        private String mTitle;
        private AccountAdapterV2 mAdapter;
        private String mCancel;
        private OnItemListener mItemClickListener;

        private Builder() {
        }

        public AccountPopWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public AccountPopWindow.Builder adapter(AccountAdapterV2 adapter) {
            this.mAdapter = adapter;
            return this;
        }

        public AccountPopWindow.Builder itemLayout(int itemLayout) {
            this.mItemLayout = itemLayout;
            return this;
        }

        public AccountPopWindow.Builder width(int width) {
            this.mWidth = width;
            return this;
        }

        public AccountPopWindow.Builder height(int height) {
            this.mHeight = height;
            return this;
        }

        public AccountPopWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public AccountPopWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public AccountPopWindow.Builder iFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public AccountPopWindow.Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public AccountPopWindow.Builder cancel(String cancel) {
            this.mCancel = cancel;
            return this;
        }

        public AccountPopWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public AccountPopWindow.Builder data(List datas) {
            this.mDatas = datas;

            return this;
        }

        public AccountPopWindow.Builder itemListener(OnItemListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
            return this;
        }

        public AccountPopWindow build() {
            return new AccountPopWindow(this);
        }
    }

    public interface OnItemListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    private class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(null == constraint || 0 == constraint.length()) {
                constraint = "";
            }
            // 这里做一些简单的过滤
            String condition = String.valueOf(constraint).toLowerCase();
            List<AccountBean> temp = new ArrayList<>();
            for (AccountBean accountBean : mOldData) {
                if (accountBean.getAccountName().toLowerCase().contains(condition)) {
                    temp.add(accountBean);
                }
            }
            results.values = temp;
            results.count = temp.size();
            // 返回的results会在publishResult()函数中得到
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDatas = (ArrayList<AccountBean>) results.values;
            // 更新视图
            mAdapter.notifyDataSetChanged();
            setAccountPopHeight(mDatas.size());
        }
    }

    /**
     * 动态设置提示框高度
     * @param size 账号个数
     */
    private void setAccountPopHeight(int size){
        if (size > 3){
            mHeight = 150;
        } else {
            setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }
}
