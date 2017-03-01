package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 动态列表 五张图的时候的 item
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListItemForOneImage extends DynamicListBaseItem {


    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量

    public DynamicListItemForOneImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_one_image;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        /**
         * 一张图时候，需要对宽高做限制
         */
        int with;
        int height;
        if (dynamicBean.getFeed().getStorages().get(0).getWidth() > mImageContainerWith) {
            with = mImageContainerWith;
        } else {
            with = (int) dynamicBean.getFeed().getStorages().get(0).getWidth();
        }
        if (dynamicBean.getFeed().getStorages().get(0).getHeight() > mImageMaxHeight) {
            height = mImageMaxHeight;
        } else {
            height = (int) dynamicBean.getFeed().getStorages().get(0).getHeight();
        }
        holder.getView(R.id.siv_0).setLayoutParams(new LinearLayout.LayoutParams(with, height));
        int bate = (int) ((with / dynamicBean.getFeed().getStorages().get(0).getWidth())*100);
        System.out.println("bate = " + bate+"--------->posioton"+position);
        initImageView((ImageView) holder.getView(R.id.siv_0), dynamicBean, 0);
    }

}

