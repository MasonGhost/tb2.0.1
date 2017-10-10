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

public class PersonalCenterDynamicListItemForTwoImage extends PersonalCenterDynamicListBaseItem {
    public PersonalCenterDynamicListItemForTwoImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_two_image;
    }
    private static final int IMAGE_COUNTS = 2;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 2; // 当前列数

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    protected  int getCurrenCloums() {
        return CURREN_CLOUMS;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0,1); // 数字 0 代表 image 当前的位置， 1 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder, holder.getView(R.id.siv_1), dynamicBean, 1,1);
    }

}
