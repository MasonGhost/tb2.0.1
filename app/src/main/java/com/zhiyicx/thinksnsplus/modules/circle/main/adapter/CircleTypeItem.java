package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/11/14/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleTypeItem implements ItemViewDelegate<GroupInfoBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.view_circle_type;
    }

    @Override
    public boolean isForViewType(GroupInfoBean item, int position) {
        return item.getId() < 0;
    }

    @Override
    public void convert(ViewHolder holder, GroupInfoBean groupInfoBean, GroupInfoBean lastT, int position, int itemCounts) {

    }
}
