package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicEmptyItem extends EmptyItem<DynamicDetailBeanV2> {

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item != null && item.getFeed_mark() == null;
    }
}
