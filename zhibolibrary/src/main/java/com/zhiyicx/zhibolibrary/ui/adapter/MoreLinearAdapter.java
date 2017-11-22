package com.zhiyicx.zhibolibrary.ui.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.FooterView1Holder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by jess on 2015/11/27.
 */
public abstract class MoreLinearAdapter<T> extends RecyclerView.Adapter<ZBLBaseHolder<T>> {
    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private ZBLBaseHolder<T> mHolder;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER1 = 1;

    private boolean mShowFooter = false;

    public MoreLinearAdapter(List<T> infos) {
        super();
        this.mInfos = infos;
    }

    /**
     * 创建Hodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ZBLBaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
            mHolder = getHolder(view);
            mHolder.setOnItemClickListener(new ZBLBaseHolder.OnViewClickListener() {//设置Item点击事件
                @Override
                public void onViewClick(View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, mInfos.get(position));
                    }
                }
            });

            return mHolder;
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.zb_footer_linear, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    AutoUtils.getPercentHeightSize(140)));
            ((AnimationDrawable) ((ImageView) view.findViewById(R.id.tv_load_more)).getDrawable()).start();
            return new FooterView1Holder(view);
        }

    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ZBLBaseHolder<T> holder, int position) {
        if (holder instanceof FooterView1Holder || position >= mInfos.size()) {
            return;
        }
        holder.setData(mInfos.get(position));
    }

    /**
     * 数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (!mShowFooter) {
            begin = 0;
        }
        if (mInfos == null) {
            return begin;
        }
        return mInfos.size() + begin;
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter) {
            return TYPE_ITEM;
        }

        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER1;
        }
        else {
            return TYPE_ITEM;
        }
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    /**
     * 获得item的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    /**
     * 子类实现提供holder
     *
     * @param v
     * @return
     */
    public abstract ZBLBaseHolder<T> getHolder(View v);


    /**
     * 提供Item的布局
     *
     * @return
     */
    public abstract int getLayoutId();


    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, T data);
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
