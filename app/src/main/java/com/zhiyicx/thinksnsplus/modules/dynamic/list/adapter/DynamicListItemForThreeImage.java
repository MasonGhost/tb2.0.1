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

public class DynamicListItemForThreeImage extends DynamicListBaseItem {
    private static final int IMAGE_COUNTS = 3;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 3;// 当前列数
    public DynamicListItemForThreeImage(Context context) {
        super(context);

    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_three_image;
    }

    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }
    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position,int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_0), dynamicBean, 0,1); // 数字 0 代表 image 当前的位置， 1 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder,(ImageView) holder.getView(R.id.siv_1), dynamicBean, 1,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_2), dynamicBean, 2,1);
    }



}

