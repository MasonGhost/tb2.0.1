package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/05/19/11:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ChannelAdvertHeader implements MultiItemTypeAdapter.OnItemClickListener {

    private View mRootView;
    private LinearLayout mAdvertContainer;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;

    public ChannelAdvertHeader(final Context context, final List<String> urls) {
        this.mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.advert_channel, null);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_advert_channel);

        mRootView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        GridLayoutManager layoutManager = new GridLayoutManager(context, urls.size() <= 3 ? urls.size() : 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new TGridDecoration(10, 10, false));
        CommonAdapter adapter = new CommonAdapter<String>(context, R.layout.item_advert, urls) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setImageResource(R.id.im_item_advert, R.mipmap.icon_256);
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClik(view, holder, position);
        }

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            return mOnItemClickListener.onItemLongClick(view, holder, position);
        }
        return false;
    }

    public View getRootView() {
        return mRootView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClik(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }
}
