package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicEmptyItem implements ItemViewDelegate<DynamicBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment_empty;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return item != null && item.getFeed_mark() == null;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean baseListBean, DynamicBean lastT, int position) {
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setNeedTextTip(false);
        emptyView.setErrorType(EmptyView.STATE_NODATA);
    }


}
