package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe 个人中心显示动态数量的item
 * @date 2017/3/8
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicCountItem implements ItemViewDelegate<DynamicBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_count;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return item.getFeed() == null;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {
        holder.setText(R.id.tv_dynamic_count, holder.getConvertView().getContext().getString(R.string.dynamic_count, dynamicBean.getMyDyanamicListCount()));
    }
}
