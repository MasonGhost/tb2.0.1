package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.common.widget.NoPullRecycleView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * @Describe sinple text  nopull recycleview
 * @Author Jungle68
 * @Date 2017/3/6
 * @Contact master.jungle68@gmail.com
 */

public abstract class SimpleTextNoPullRecycleView<T> extends NoPullRecycleView implements MultiItemTypeAdapter.OnItemClickListener {

    private OnIitemClickListener mOnIitemClickListener;

    private OnIitemLongClickListener mOnIitemLongClickListener;

    private MultiItemTypeAdapter<T> mAdapter;

    public SimpleTextNoPullRecycleView(Context context) {
        super(context);
        init(null, -1);
    }

    public SimpleTextNoPullRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, -1);
    }

    public SimpleTextNoPullRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(@Nullable AttributeSet attrs, int defStyle) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
        addItemDecoration(new LinearDecoration(0, getResources().getDimensionPixelSize(R.dimen.spacing_mid_small), 0, 0));
    }

    public void setOnIitemClickListener(OnIitemClickListener onIitemClickListener) {
        mOnIitemClickListener = onIitemClickListener;
    }

    public void setOnIitemLongClickListener(OnIitemLongClickListener onIitemLongClickListener) {
        mOnIitemLongClickListener = onIitemLongClickListener;
    }

    /**
     * set data
     *
     * @param data
     */
    public void setData(List<T> data) {
        initData(data);
    }

    /**
     * refresh
     */
    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * refresh curren item
     *
     * @param position
     */
    public void refresh(int position) {
        mAdapter.notifyItemChanged(position);
    }

    private void initData(final List<T> data) {

        mAdapter = new CommonAdapter<T>(getContext(), R.layout.item_simple_text_comment, data) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, T t, final int position) {
                holder.setText(R.id.tv_simple_text_comment, setShowText(t, position));

                // Add the links and make the links clickable
                LinkBuilder.on((TextView) holder.getView(R.id.tv_simple_text_comment))
                        .addLinks(setLiknks(t, position))
                        .build();
            }
        };
        mAdapter.setOnItemClickListener(this);
        setAdapter(mAdapter);
    }

    protected abstract String setShowText(T t, int position);

    protected abstract List<Link> setLiknks(T t, int position);


    @Override
    public void onItemClick(View view, ViewHolder holder, int position) {
        if (mOnIitemClickListener != null) {
            mOnIitemClickListener.onItemClick(view, position);
        }
    }

    @Override
    public boolean onItemLongClick(View view, ViewHolder holder, int position) {
        if (mOnIitemLongClickListener != null) {
            mOnIitemLongClickListener.onItemLongClick(view, position);
        }
        return false;
    }

    public interface OnIitemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnIitemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
