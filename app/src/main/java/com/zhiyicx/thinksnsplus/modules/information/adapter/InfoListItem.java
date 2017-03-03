package com.zhiyicx.thinksnsplus.modules.information.adapter;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class InfoListItem implements ItemViewDelegate<BaseListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_info;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int
            position) {

    }
}