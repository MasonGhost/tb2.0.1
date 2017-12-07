package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Jliuer
 * @Date 2017/11/30/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostEmptyItem extends EmptyItem<CirclePostListBean> {

    public CirclePostEmptyItem() {
    }

    @Override
    public boolean isForViewType(CirclePostListBean item, int position) {
        return item != null && item.getId() == null;
    }

    @Override
    public void convert(ViewHolder holder, CirclePostListBean baseListBean, CirclePostListBean lastT, int position, int itemCounts) {
        super.convert(holder, baseListBean, lastT, position, itemCounts);
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setPadding(0, emptyView.getPaddingBottom() * 3, 0, emptyView.getPaddingBottom());
    }
}
