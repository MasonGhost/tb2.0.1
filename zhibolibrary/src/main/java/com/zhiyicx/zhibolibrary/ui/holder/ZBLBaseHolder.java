package com.zhiyicx.zhibolibrary.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.zhibolibrary.util.KnifeUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.simple.eventbus.EventBus;


/**
 * Created by jess on 2015/11/24.
 */
public abstract class ZBLBaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected OnViewClickListener mOnViewClickListener = null;
    protected final String TAG = this.getClass().getSimpleName();
    public ZBLBaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);//点击事件
        AutoUtils.autoSize(itemView);//适配
        KnifeUtil.bindTarget(this, itemView);//绑定
        EventBus.getDefault().register(this);//注册eventbus
    }


    public View getRootView() {
        return itemView;
    }


    /**
     * 设置数据
     * 刷新界面
     *
     * @param
     */
    public abstract void setData(T data);

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getPosition());
        }
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }
}
