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

public class GroupDynamicListItemForSevenImage extends GroupDynamicListBaseItem {
    private static final int IMAGE_COUNTS = 7;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 3;// 当前列数
    public GroupDynamicListItemForSevenImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_seven_image;
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
    public void convert(ViewHolder holder, final GroupDynamicListBean dynamicBean, GroupDynamicListBean lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0,2); // 数字 0 代表 image 当前的位置， 2 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder, holder.getView(R.id.siv_1), dynamicBean, 1,1);
        initImageView(holder,holder.getView(R.id.siv_2), dynamicBean, 2,1);
        initImageView(holder,holder.getView(R.id.siv_3), dynamicBean, 3,2);
        initImageView(holder, holder.getView(R.id.siv_4), dynamicBean, 4,2);
        initImageView(holder, holder.getView(R.id.siv_5), dynamicBean, 5,1);
        initImageView(holder, holder.getView(R.id.siv_6), dynamicBean, 6,1);
    }
}

