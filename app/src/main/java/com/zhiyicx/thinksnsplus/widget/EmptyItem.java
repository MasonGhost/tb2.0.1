package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class EmptyItem implements ItemViewDelegate<BaseListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment_empty;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item.getMaxId() == null;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position) {
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setNeedTextTip(false);
        emptyView.setErrorType(EmptyView.STATE_NODATA);
    }


}
