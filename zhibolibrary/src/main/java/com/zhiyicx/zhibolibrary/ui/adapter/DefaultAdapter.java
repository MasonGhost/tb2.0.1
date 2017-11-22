package com.zhiyicx.zhibolibrary.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;

import java.util.List;

/**
 * Created by jess on 2015/11/27.
 */
public abstract class DefaultAdapter<T> extends RecyclerView.Adapter<ZBLBaseHolder<T>> {
    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private ZBLBaseHolder<T> mHolder;

    public DefaultAdapter(List<T> infos) {
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

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ZBLBaseHolder<T> holder, int position) {
        holder.setData(mInfos.get(position));
    }

    /**
     * 数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mInfos.size();
    }


    public List<T> getInfos() {
        return mInfos;
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
