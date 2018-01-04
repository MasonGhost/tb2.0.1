package com.zhiyicx.thinksnsplus.modules.circle.detail.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class GroupDynamicListItemForFourImage extends GroupDynamicListBaseItem {
    private static final int IMAGE_COUNTS = 4;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 2;// 当前列数

    public GroupDynamicListItemForFourImage(Context context) {
        super(context);

    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public int getCurrenCloums() {
        return CURREN_CLOUMS;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_four_image;
    }


    @Override
    public void convert(ViewHolder holder, final GroupDynamicListBean dynamicBean, GroupDynamicListBean lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0,1);// // 数字 0 代表 image 当前的位置， 1 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder, holder.getView(R.id.siv_1), dynamicBean, 1,1);
        initImageView(holder, holder.getView(R.id.siv_2), dynamicBean, 2,1);
        initImageView(holder, holder.getView(R.id.siv_3), dynamicBean, 3,1);
    }


}

