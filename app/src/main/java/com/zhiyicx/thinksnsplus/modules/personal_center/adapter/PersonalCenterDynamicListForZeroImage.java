package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListForZeroImage extends PersonalCenterDynamicListBaseItem {
    public PersonalCenterDynamicListForZeroImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return super.getItemViewLayoutId();
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 0 || (item.getFeed_mark() != null && (item.getFeed().getStorages() == null||item.getFeed().getStorages().isEmpty()));
    }


    @Override
    public void convert(ViewHolder holder, final DynamicBean dynamicBean, DynamicBean lastT, int position,int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
    }
}
