package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListBaseItem extends DynamicListBaseItem {
    public PersonalCenterDynamicListBaseItem(Context context) {
        super(context);
    }

    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position, itemCounts);
        //////这儿的时间处理放在了DynamicListBaseItem中，否则还要在每个ImageItem中重写//////
        ////////////////////////////添加个人中心时间处理/////////////////////////
        if (TextUtils.isEmpty(dynamicBean.getUserCenterFriendlyTimeDonw())) {
            holder.setText(R.id.tv_time_up, dynamicBean.getUserCenterFriendlyTimeUp());
            holder.getView(R.id.tv_time_down).setVisibility(View.GONE);

        } else {
            holder.setText(R.id.tv_time_up, dynamicBean.getUserCenterFriendlyTimeUp());
            holder.setText(R.id.tv_time_down, dynamicBean.getUserCenterFriendlyTimeDonw());
            holder.getView(R.id.tv_time_down).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_zero_image;
    }

}
