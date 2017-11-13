package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;

/**
 * @Author Jliuer
 * @Date 2017/07/19/10:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class GroupDynamicEmptyItem extends EmptyItem<GroupDynamicListBean> {

    public GroupDynamicEmptyItem() {
    }

    @Override
    public boolean isForViewType(GroupDynamicListBean item, int position) {
        return item != null && item.getId() == null;
    }
}
