package com.zhiyicx.zhibolibrary.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.FooterView1Holder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by jess on 2015/11/27.
 */
public abstract class MuiltListAdapter<T> extends RecyclerView.Adapter<ZBLBaseHolder<T>> {
    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public static final int TYPE_ITEM_TIP = -1;
    public static final int TYPE_ITEM_TEXT = 1;
    public static final int TYPE_ITEM_FLLOW = 2;
    public static final int TYPE_FOOTER1 = 3;

//    private boolean mShowFooter = false;

    public MuiltListAdapter(List<T> infos) {
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
            case TYPE_ITEM_TEXT:
                View view;
                ZBLBaseHolder<T> mHolder;
                view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
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

            case TYPE_ITEM_FLLOW:
                View view2;
                ZBLBaseHolder<T> mHolder2;
                view2 = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
                mHolder2 = getHolder(view2, viewType);
                mHolder2.setOnItemClickListener(new ZBLBaseHolder.OnViewClickListener() {//设置Item点击事件
                    @Override
                    public void onViewClick(View view, int position) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, mInfos.get(position));
                        }
                    }
                });

                return mHolder2;

            case TYPE_ITEM_TIP:
                View view3;
                ZBLBaseHolder<T> mHolder3;
                view3 = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.zb_recycle_item_live_chat_tip, parent, false);
                mHolder3 = getHolder(view3, viewType);
                mHolder3.setOnItemClickListener(new ZBLBaseHolder.OnViewClickListener() {//设置Item点击事件
                    @Override
                    public void onViewClick(View view, int position) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, mInfos.get(position));
                        }
                    }
                });
                return mHolder3;


            case TYPE_FOOTER1:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.zb_footer_linear, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        AutoUtils.getPercentHeightSize(70)));
                return new FooterView1Holder(view);


        }

        return null;
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
//        int begin = mShowFooter ? 1 : 0;
//        if (!mShowFooter) {
//            begin = 0;
//        }
//        if (mInfos == null) {
//            return begin;
//        }
//        return mInfos.size() + begin;
        return mInfos.size();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
//        if (mShowFooter) {
//            return TYPE_FOOTER1;
//        }
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER1;
//        }
//        else {
        switch (((UserMessage) mInfos.get(position)).msg.type) {
            case MessageType.MESSAGE_TYPE_TEXT:

                return TYPE_ITEM_TEXT;
            case MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE:
                if (((UserMessage) mInfos.get(position)).msg.ext.customID == MessageType.MESSAGE_CUSTOM_ID_FLLOW)
                    return TYPE_ITEM_FLLOW;
                else {
                    return TYPE_ITEM_TIP;
                }
            default:

                return TYPE_ITEM_TIP;
        }


//        }
    }

//    public void isShowFooter(boolean showFooter) {
//        this.mShowFooter = showFooter;
//    }
//
//    public boolean isShowFooter() {
//        return this.mShowFooter;
//    }

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
