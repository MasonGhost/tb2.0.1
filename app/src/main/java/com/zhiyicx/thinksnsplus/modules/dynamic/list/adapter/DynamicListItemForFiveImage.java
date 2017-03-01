package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListItemForFiveImage extends DynamicListBaseItem {
    private static final int IMAGE_COUNTS = 5;// 动态列表图片数量


    public DynamicListItemForFiveImage(Context context) {
        super(context);

    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_five_image;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        initImageView((ImageView) holder.getView(R.id.siv_0), dynamicBean, 0);
        initImageView((ImageView) holder.getView(R.id.siv_1), dynamicBean, 1);
        initImageView((ImageView) holder.getView(R.id.siv_2), dynamicBean, 2);
        initImageView((ImageView) holder.getView(R.id.siv_3), dynamicBean, 3);
        initImageView((ImageView) holder.getView(R.id.siv_4), dynamicBean, 4);
    }


}

