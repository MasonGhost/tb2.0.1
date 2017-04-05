package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListBaseItem;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class DynamicListItemForZeroImage extends PersonalCenterDynamicListBaseItem {
    public DynamicListItemForZeroImage(Context context) {
        super(context);
    }


    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 0 || super.isForViewType(item, position);
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        // 对于0张图的动态，可能因为内容太少，而高度不够，导致分割线在左侧时间上，修改分割线的布局依赖,
        // 让分割线在时间的下方
        View timeContainer = holder.getView(R.id.ll_time_container);
        View contentView = holder.getView(R.id.tv_content);
        View dividerLine = holder.getView(R.id.v_line);
        LogUtils.i("zero_image" + "--timeContainer   " + timeContainer.getBottom() + "--contentView   " + contentView.getBottom());
        // 时间底部大于内容的底部，应该让分割线在时间的下面
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dividerLine.getLayoutParams();
        if (timeContainer.getBottom() > contentView.getBottom()) {
            layoutParams.addRule(RelativeLayout.BELOW, R.id.ll_time_container);
        }
    }
}
