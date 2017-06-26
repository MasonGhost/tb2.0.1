package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.common.utils.TimeUtils;
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
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        //////这儿的时间处理放在了DynamicListBaseItem中，否则还要在每个ImageItem中重写//////
        ////////////////////////////添加个人中心时间处理/////////////////////////
        TextView timeUp = holder.getView(R.id.tv_time_up);
        TextView timeDown = holder.getView(R.id.tv_time_down);
        String createdTime = dynamicBean.getCreated_at();
        String timeString = TimeUtils.getTimeFriendlyForUserHome(createdTime);
        if (timeString.equals("今,天") || timeString.equals("昨,天")) {
            timeString = timeString.replace(",", "\n");
            timeUp.setText(timeString);
            timeDown.setVisibility(View.GONE);
        } else {
            String[] dayAndMonth = timeString.split(",");
            timeUp.setText(dayAndMonth[0]);
            timeDown.setText(dayAndMonth[1]);
            timeDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_zero_image;
    }

}
