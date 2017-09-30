package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListItemForFiveImage extends PersonalCenterDynamicListBaseItem {
    public PersonalCenterDynamicListItemForFiveImage(Context context) {
        super(context);
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_five_image;
    }
    private static final int IMAGE_COUNTS = 5;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 3;// 当前列数

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public  int getCurrenCloums() {
        return CURREN_CLOUMS;
    }


    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0,2); // 数字 0 代表 image 当前的位置， 2 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder, holder.getView(R.id.siv_1), dynamicBean, 1,1);
        initImageView(holder, holder.getView(R.id.siv_2), dynamicBean, 2,1);
        initImageView(holder, holder.getView(R.id.siv_3), dynamicBean, 3,2);
        initImageView(holder, holder.getView(R.id.siv_4), dynamicBean, 4,2);
    }

}
