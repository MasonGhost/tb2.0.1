package com.zhiyicx.thinksnsplus.modules.usertag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridAdapter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergej Kravcenko on 4/24/2017.
 * Copyright (c) 2017 Sergej Kravcenko
 */

public class TagClassAdapter extends StickyHeaderGridAdapter {
    private List<TagCategoryBean> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    TagClassAdapter(List<TagCategoryBean> categoryBeanList) {

        if (categoryBeanList == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = categoryBeanList;
        }

    }

    @Override
    public int getSectionCount() {
        return mDatas.size();
    }

    @Override
    public int getSectionItemCount(int section) {
        return mDatas.get(section).getTags().size();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_user_tag, parent, false);
        return new MyHeaderViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_tag_class, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int categoryPosition) {
        final MyHeaderViewHolder holder = (MyHeaderViewHolder) viewHolder;
        holder.labelView.setText(mDatas.get(categoryPosition).getName());
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, final int categoryPosition, final int tagPosition) {
        final MyItemViewHolder holder = (MyItemViewHolder) viewHolder;
        final String label = mDatas.get(categoryPosition).getTags().get(tagPosition).getTagName();
        holder.labelView.setText(label);
        holder.labelView.setOnClickListener(v -> {
            final int section1 = getAdapterPositionSection(holder.getAdapterPosition());
            final int offset = getItemSectionOffset(section1, holder.getAdapterPosition());
//            mDatas.get(section1).remove(offset);
//            notifySectionItemRemoved(section1, offset);
            LogUtils.d("TagClassAdapter","categoryPosition : "+categoryPosition+"-----"+"tagPosition : "+tagPosition+"-----"+"section1 : "+section1+"offset : "+offset);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(categoryPosition,tagPosition);
            }
        });
    }

    public static class MyHeaderViewHolder extends HeaderViewHolder {
        TextView labelView;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            labelView = (TextView) itemView.findViewById(R.id.label);
        }
    }

    public static class MyItemViewHolder extends ItemViewHolder {
        TextView labelView;

        MyItemViewHolder(View itemView) {
            super(itemView);
            labelView = (TextView) itemView.findViewById(R.id.label);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int categoryPosition, int tagPosition);
    }
}
