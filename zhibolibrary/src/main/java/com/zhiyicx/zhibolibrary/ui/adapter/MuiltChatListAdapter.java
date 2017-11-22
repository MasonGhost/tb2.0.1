package com.zhiyicx.zhibolibrary.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.FooterView1Holder;

import java.util.List;

/**
 * Created by jess on 2015/11/27.
 */
public abstract class MuiltChatListAdapter<T> extends RecyclerView.Adapter<ZBLBaseHolder<T>> {
    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public static final int TYPE_ITEM_OTHER = 0;
    public static final int TYPE_ITEM_ME = 1;

    public MuiltChatListAdapter(List<T> infos) {
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

        switch (viewType) {

            case TYPE_ITEM_ME:
                View view2;
                ZBLBaseHolder<T> mHolder2;
                view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.zb_recycle_item_chat_message_ohter, parent, false);
                mHolder2 = getHolder(view2, viewType);
                mHolder2.setOnItemClickListener(new ZBLBaseHolder.OnViewClickListener() {//设置Item点击事件
                    @Override
                    public void onViewClick(View view, int position) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, mInfos.get(position));
                        }
                    }
                });
            case TYPE_ITEM_OTHER:
            default:
                View view;
                ZBLBaseHolder<T> mHolder;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zb_recycle_item_chat_message_me, parent, false);
                mHolder = getHolder(view, viewType);
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

        return mInfos.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (((UserMessage) mInfos.get(position)).msg.uid != 0)
            return TYPE_ITEM_OTHER;
        else
            return TYPE_ITEM_ME;

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
    public abstract ZBLBaseHolder<T> getHolder(View v, int viewType);


    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, T data);
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
