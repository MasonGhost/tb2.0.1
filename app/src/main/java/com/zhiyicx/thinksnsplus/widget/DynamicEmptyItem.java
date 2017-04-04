package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicEmptyItem extends EmptyItem<DynamicBean> {

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return item != null && item.getFeed_mark() == null;
    }
}
