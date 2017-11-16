package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/11/14/13:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListItem implements ItemViewDelegate<GroupInfoBean> {
    @Override
    public int getItemViewLayoutId() {
        return 0;
    }

    @Override
    public boolean isForViewType(GroupInfoBean item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, GroupInfoBean groupInfoBean, GroupInfoBean lastT, int position, int itemCounts) {

    }
}
