package com.zhiyicx.thinksnsplus.modules.information.adapter;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;

public abstract class InfoBannerItem implements ItemViewDelegate<BaseListBean> {


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_banner;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListBean.RecommendBean;
    }

}