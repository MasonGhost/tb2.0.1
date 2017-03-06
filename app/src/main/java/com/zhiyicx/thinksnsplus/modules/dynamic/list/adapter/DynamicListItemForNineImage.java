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

public class DynamicListItemForNineImage extends DynamicListBaseItem {
    private static final int IMAGE_COUNTS = 9;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 3;// 当前列数
    public DynamicListItemForNineImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_nine_image;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public  int getCurrenCloums() {
        return CURREN_CLOUMS;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_0), dynamicBean, 0,1); // 数字 0 代表 image 当前的位置， 1 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder,(ImageView) holder.getView(R.id.siv_1), dynamicBean, 1,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_2), dynamicBean, 2,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_3), dynamicBean, 3,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_4), dynamicBean, 4,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_5), dynamicBean, 5,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_6), dynamicBean, 6,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_7), dynamicBean, 7,1);
        initImageView(holder,(ImageView) holder.getView(R.id.siv_8), dynamicBean, 8,1);
    }

}

